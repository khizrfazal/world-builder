package com.my.worldbuilder.location;

import com.fasterxml.jackson.core.type.TypeReference;
import com.my.worldbuilder.IntegrationTest;
import com.my.worldbuilder.location.dto.LocationRequest;
import com.my.worldbuilder.location.dto.LocationResponse;
import com.my.worldbuilder.user.User;
import com.my.worldbuilder.world.World;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class LocationControllerTest extends IntegrationTest {

    private static final String REQUEST_MAPPING = "/worlds";

    private static final UUID MURIM_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    private static final UUID NORTHERN_HEAVENLY_SECT_ID =
            UUID.fromString("aaaaaaaa-1111-1111-1111-111111111111");

    private static final UUID BLACK_MOON_VALLEY_ID =
            UUID.fromString("bbbbbbbb-2222-2222-2222-222222222222");

    private static final UUID FORBIDDEN_RUINS_ID =
            UUID.fromString("dddddddd-4444-4444-4444-444444444444");

    @Nested
    class GetLocationsTests {

        @Nested
        class GivenRequestToGetLocationsByWorld {

            @Test
            void thenReturnLocationsInWorldWith200StatusCode() throws Exception {
                MvcResult result = getRequest(REQUEST_MAPPING + "/" + MURIM_ID + "/locations");
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                List<LocationResponse> body = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        new TypeReference<>() {}
                );
                assertThat(body)
                        .hasSize(2)
                        .extracting(LocationResponse::getId)
                        .containsExactlyInAnyOrder(
                                NORTHERN_HEAVENLY_SECT_ID,
                                BLACK_MOON_VALLEY_ID
                        );
            }
        }

        @Nested
        class GivenWorldOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCode() throws Exception {
                UUID otherWorldId = UUID.fromString("33333333-3333-3333-3333-333333333333");
                MvcResult result = getRequest(REQUEST_MAPPING + "/" + otherWorldId + "/locations");
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
            }
        }

        @Nested
        class GivenWorldDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();
                MvcResult result = getRequest(REQUEST_MAPPING + "/" + id + "/locations");
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }
    }

    @Nested
    class GetLocationTests {

        @Nested
        class GivenValidLocationId {

            @Test
            void thenReturnLocationWith200StatusCode() throws Exception {
                MvcResult result = getRequest("/locations/" + NORTHERN_HEAVENLY_SECT_ID);
                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.OK.value());
                LocationResponse body = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        LocationResponse.class
                );
                assertThat(body.getId()).isEqualTo(NORTHERN_HEAVENLY_SECT_ID);
                assertThat(body.getName()).isEqualTo("Northern Heavenly Sect");
                assertThat(body.getDescription()).isEqualTo("The headquarters of the Northern Heavenly Sect, hidden among the mountains.");
            }
        }

        @Nested
        class GivenLocationDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();
                MvcResult result = getRequest("/locations/" + id);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenLocationOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCode() throws Exception {
                MvcResult result = getRequest("/locations/" + FORBIDDEN_RUINS_ID);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
            }
        }
    }

    @Nested
    class CreateLocationTests {

        @Nested
        class GivenValidRequestBody {

            @Test
            void thenCreateLocationAndReturn201StatusCode() throws Exception {
                LocationRequest request = new LocationRequest(
                        "Jade Palace",
                        "A magnificent palace overlooking the eastern mountains."
                );
                MvcResult result = postRequest(REQUEST_MAPPING + "/" + MURIM_ID + "/locations", request);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());

                UUID id = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        UUID.class
                );
                Location location = locationRepository.findById(id).orElseThrow();
                assertThat(location.getName()).isEqualTo("Jade Palace");
                assertThat(location.getDescription()).isEqualTo("A magnificent palace overlooking the eastern mountains.");
                assertThat(location.getWorld().getId()).isEqualTo(MURIM_ID);
                assertThat(location.getCreatedAt()).isNotNull();
                assertThat(location.getUpdatedAt()).isNotNull();
            }
        }

        @Nested
        class GivenInvalidRequestBody {

            @Test
            void thenReturn400WhenFieldsAreBlank() throws Exception {
                long before = locationRepository.count();
                LocationRequest request = new LocationRequest("", "");
                MvcResult result = postRequest(
                        REQUEST_MAPPING + "/" + MURIM_ID + "/locations",
                        request
                );
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                assertThat(result.getResponse().getContentAsString())
                        .contains("Name must not be blank")
                        .contains("Description must not be blank");
                assertThat(locationRepository.count()).isEqualTo(before);
            }

            @Test
            void thenReturn400WhenFieldsExceedMaximumLength() throws Exception {
                long before = locationRepository.count();

                LocationRequest request = new LocationRequest(
                        "A".repeat(151),
                        "A".repeat(1001)
                );
                MvcResult result = postRequest(
                        REQUEST_MAPPING + "/" + MURIM_ID + "/locations",
                        request
                );
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                assertThat(result.getResponse().getContentAsString())
                        .contains("Name must be less than 150 characters")
                        .contains("Description must be less than 1000 characters");
                assertThat(locationRepository.count()).isEqualTo(before);
            }
        }

        @Nested
        class GivenWorldOwnedByAnotherUser {

            @Test
            void thenReturn403AndDoNotCreateLocation() throws Exception {
                User otherUser = userRepository.findByUsername("otheruser").orElseThrow();
                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Another User's World")
                                .description("Not owned by testuser")
                                .user(otherUser)
                                .build()
                );
                LocationRequest request = new LocationRequest(
                        "Unauthorized Location",
                        "Should not be created."
                );
                long before = locationRepository.count();
                MvcResult result = postRequest(
                        REQUEST_MAPPING + "/" + otherWorld.getId() + "/locations",
                        request
                );
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
                assertThat(locationRepository.count()).isEqualTo(before);
            }
        }

        @Nested
        class GivenWorldDoesNotExist {

            @Test
            void thenReturn404AndDoNotCreateLocation() throws Exception {
                UUID worldId = UUID.randomUUID();

                LocationRequest request = new LocationRequest(
                        "Unknown World Location",
                        "Should not be created."
                );

                long before = locationRepository.count();
                MvcResult result = postRequest(REQUEST_MAPPING + "/" + worldId + "/locations", request);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
                assertThat(locationRepository.count()).isEqualTo(before);
            }
        }
    }

    @Nested
    class UpdateLocationTests {

        @Nested
        class GivenValidRequestBody {

            @Test
            void thenUpdateLocationAndReturn204StatusCode() throws Exception {
                LocationRequest request = new LocationRequest(
                        "Updated Northern Sect",
                        "The sect has moved to a new mountain stronghold."
                );

                MvcResult result = putRequest("/locations/" + NORTHERN_HEAVENLY_SECT_ID, request);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.NO_CONTENT.value());

                Location updated = locationRepository
                        .findById(NORTHERN_HEAVENLY_SECT_ID)
                        .orElseThrow();

                assertThat(updated.getName()).isEqualTo("Updated Northern Sect");
                assertThat(updated.getDescription()).isEqualTo("The sect has moved to a new mountain stronghold.");
                assertThat(updated.getWorld().getId()).isEqualTo(MURIM_ID);
            }
        }

        @Nested
        class GivenInvalidRequestBody {

            @Test
            void thenReturn400AndDoNotModifyLocation() throws Exception {
                Location before = locationRepository
                        .findById(NORTHERN_HEAVENLY_SECT_ID)
                        .orElseThrow();

                String originalName = before.getName();
                String originalDescription = before.getDescription();

                LocationRequest request = new LocationRequest("", "");

                MvcResult result = putRequest("/locations/" + NORTHERN_HEAVENLY_SECT_ID, request);

                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

                Location after = locationRepository
                        .findById(NORTHERN_HEAVENLY_SECT_ID)
                        .orElseThrow();

                assertThat(after.getName()).isEqualTo(originalName);
                assertThat(after.getDescription()).isEqualTo(originalDescription);
            }

            @Test
            void thenReturn400WhenFieldsExceedMaximumLength() throws Exception {
                Location before = locationRepository
                        .findById(NORTHERN_HEAVENLY_SECT_ID)
                        .orElseThrow();

                String originalName = before.getName();
                String originalDescription = before.getDescription();

                LocationRequest request = new LocationRequest(
                        "A".repeat(151),
                        "A".repeat(1001)
                );

                MvcResult result = putRequest("/locations/" + NORTHERN_HEAVENLY_SECT_ID, request);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.BAD_REQUEST.value());

                Location after = locationRepository
                        .findById(NORTHERN_HEAVENLY_SECT_ID)
                        .orElseThrow();

                assertThat(after.getName()).isEqualTo(originalName);
                assertThat(after.getDescription()).isEqualTo(originalDescription);
            }
        }

        @Nested
        class GivenLocationDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();
                LocationRequest request = new LocationRequest("Updated Location", "Updated description.");
                MvcResult result = putRequest("/locations/" + id, request);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenLocationOwnedByAnotherUser {

            @Test
            void thenReturn403AndDoNotModifyLocation() throws Exception {
                Location before = locationRepository
                        .findById(FORBIDDEN_RUINS_ID)
                        .orElseThrow();

                String originalName = before.getName();
                String originalDescription = before.getDescription();

                LocationRequest request = new LocationRequest(
                        "Unauthorized Update",
                        "This update should not be allowed."
                );

                MvcResult result = putRequest(
                        "/locations/" + FORBIDDEN_RUINS_ID,
                        request
                );

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.FORBIDDEN.value());

                Location after = locationRepository
                        .findById(FORBIDDEN_RUINS_ID)
                        .orElseThrow();

                assertThat(after.getName()).isEqualTo(originalName);
                assertThat(after.getDescription()).isEqualTo(originalDescription);
            }
        }
    }

    @Nested
    class DeleteLocationTests {

        @Nested
        class GivenValidLocationId {

            @Test
            void thenDeleteLocationAndReturn204StatusCode() throws Exception {
                MvcResult result = deleteRequest("/locations/" + NORTHERN_HEAVENLY_SECT_ID);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
                assertThat(locationRepository.findById(NORTHERN_HEAVENLY_SECT_ID)).isEmpty();
            }
        }

        @Nested
        class GivenLocationDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();
                MvcResult result = deleteRequest("/locations/" + id);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenLocationOwnedByAnotherUser {

            @Test
            void thenReturn403AndDoNotDeleteLocation() throws Exception {
                MvcResult result = deleteRequest("/locations/" + FORBIDDEN_RUINS_ID);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
                assertThat(locationRepository.findById(FORBIDDEN_RUINS_ID)).isPresent();
            }
        }
    }
}