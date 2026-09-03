package com.my.worldbuilder.world;

import com.fasterxml.jackson.core.type.TypeReference;
import com.my.worldbuilder.IntegrationTest;
import com.my.worldbuilder.user.User;
import com.my.worldbuilder.world.dto.WorldRequest;
import com.my.worldbuilder.world.dto.WorldResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class WorldControllerTest extends IntegrationTest {

    private static final String REQUEST_MAPPING = "/worlds";

    private static final UUID MURIM_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Nested
    class GetWorldsTests {

        @Nested
        class GivenRequestToGetAllWorlds {

            @Test
            void thenReturnOnlyOwnedWorldsWith200StatusCode() throws Exception {
                User testUser =
                        userRepository.findByUsername("testuser").orElseThrow();

                worldRepository.save(
                        World.builder()
                                .title("Game of Thrones")
                                .description("This show has a really bad ending")
                                .user(testUser)
                                .build()
                );

                worldRepository.save(
                        World.builder()
                                .title("Sonic")
                                .description("This world was full of speed and childhood memories")
                                .user(testUser)
                                .build()
                );

                MvcResult result =
                        getRequest(REQUEST_MAPPING);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.OK.value());

                List<WorldResponse> body = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        new TypeReference<>() {}
                );

                assertThat(body)
                        .extracting(WorldResponse::getTitle)
                        .containsExactlyInAnyOrder(
                                "Murim",
                                "Eldoria",
                                "Game of Thrones",
                                "Sonic"
                        )
                        .doesNotContain("Other World");
            }
        }
    }

    @Nested
    class GetWorldTests {

        @Nested
        class GivenValidWorldId {

            @Test
            void thenReturnWorldWith200StatusCode() throws Exception {
                MvcResult result =
                        getRequest(REQUEST_MAPPING + "/" + MURIM_ID);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.OK.value());

                WorldResponse body = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        WorldResponse.class
                );

                assertThat(body.getId())
                        .isEqualTo(MURIM_ID);

                assertThat(body.getTitle())
                        .isEqualTo("Murim");

                assertThat(body.getDescription())
                        .isEqualTo("Cultivation world");
            }
        }

        @Nested
        class GivenWorldDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();

                MvcResult result =
                        getRequest(REQUEST_MAPPING + "/" + id);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenWorldOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCode() throws Exception {
                User otherUser =
                        userRepository.findByUsername("otheruser").orElseThrow();

                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Forbidden World")
                                .description("Should not be accessible")
                                .user(otherUser)
                                .build()
                );

                MvcResult result =
                        getRequest(
                                REQUEST_MAPPING + "/" + otherWorld.getId()
                        );

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.FORBIDDEN.value());
            }
        }
    }

    @Nested
    class CreateWorldTests {

        @Nested
        class GivenValidRequestBody {

            @Test
            void thenCreateWorldAndReturn201StatusCode() throws Exception {
                WorldRequest request = new WorldRequest(
                        "Avatar",
                        "A world full of blue people"
                );

                MvcResult result =
                        postRequest(REQUEST_MAPPING, request);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.CREATED.value());

                UUID id = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        UUID.class
                );

                World world =
                        worldRepository.findById(id).orElseThrow();

                assertThat(world.getTitle())
                        .isEqualTo("Avatar");

                assertThat(world.getDescription())
                        .isEqualTo("A world full of blue people");

                User testUser =
                        userRepository.findByUsername("testuser").orElseThrow();

                assertThat(world.getUser().getId())
                        .isEqualTo(testUser.getId());
            }
        }

        @Nested
        class GivenInvalidRequestBody {

            @Test
            void thenReturn400WhenFieldsAreBlank() throws Exception {
                long before = worldRepository.count();

                WorldRequest request =
                        new WorldRequest("", "");

                MvcResult result =
                        postRequest(REQUEST_MAPPING, request);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.BAD_REQUEST.value());

                String response =
                        result.getResponse().getContentAsString();

                assertThat(response)
                        .contains("Title must not be blank")
                        .contains("Description must not be blank");

                assertThat(worldRepository.count())
                        .isEqualTo(before);
            }
        }
    }

    @Nested
    class UpdateWorldTests {

        @Nested
        class GivenValidRequestBody {

            @Test
            void thenUpdateWorldAndReturn204StatusCode() throws Exception {
                WorldRequest request = new WorldRequest(
                        "Updated Title",
                        "Updated Description"
                );

                MvcResult result =
                        putRequest(REQUEST_MAPPING + "/" + MURIM_ID, request);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.NO_CONTENT.value());

                World updated =
                        worldRepository.findById(MURIM_ID).orElseThrow();

                assertThat(updated.getTitle())
                        .isEqualTo("Updated Title");

                assertThat(updated.getDescription())
                        .isEqualTo("Updated Description");
            }
        }

        @Nested
        class GivenInvalidRequestBody {

            @Test
            void thenReturn400AndDoNotModifyWorld() throws Exception {
                World before =
                        worldRepository.findById(MURIM_ID).orElseThrow();

                String originalTitle = before.getTitle();
                String originalDescription = before.getDescription();

                WorldRequest request =
                        new WorldRequest("", "");

                MvcResult result =
                        putRequest(REQUEST_MAPPING + "/" + MURIM_ID, request);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.BAD_REQUEST.value());

                World after =
                        worldRepository.findById(MURIM_ID).orElseThrow();

                assertThat(after.getTitle())
                        .isEqualTo(originalTitle);

                assertThat(after.getDescription())
                        .isEqualTo(originalDescription);
            }
        }

        @Nested
        class GivenWorldDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();

                WorldRequest request = new WorldRequest(
                        "Updated Title",
                        "Updated Description"
                );

                MvcResult result =
                        putRequest(REQUEST_MAPPING + "/" + id, request);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenWorldOwnedByAnotherUser {

            @Test
            void thenReturn403AndDoNotModifyWorld() throws Exception {
                User otherUser =
                        userRepository.findByUsername("otheruser").orElseThrow();

                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Original Title")
                                .description("Original Description")
                                .user(otherUser)
                                .build()
                );

                WorldRequest request =
                        new WorldRequest("New Title", "New Description");

                MvcResult result =
                        putRequest(
                                REQUEST_MAPPING + "/" + otherWorld.getId(),
                                request
                        );

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.FORBIDDEN.value());

                World unchanged =
                        worldRepository.findById(otherWorld.getId()).orElseThrow();

                assertThat(unchanged.getTitle())
                        .isEqualTo("Original Title");

                assertThat(unchanged.getDescription())
                        .isEqualTo("Original Description");
            }
        }
    }

    @Nested
    class DeleteWorldTests {

        @Nested
        class GivenValidWorldId {

            @Test
            void thenDeleteWorldAndReturn204StatusCode() throws Exception {
                User testUser =
                        userRepository.findByUsername("testuser").orElseThrow();

                World world = worldRepository.save(
                        World.builder()
                                .title("World To Delete")
                                .description("Should be deleted")
                                .user(testUser)
                                .build()
                );

                MvcResult result =
                        deleteRequest(REQUEST_MAPPING + "/" + world.getId());

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.NO_CONTENT.value());

                assertThat(worldRepository.findById(world.getId()))
                        .isEmpty();
            }
        }

        @Nested
        class GivenWorldDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();

                MvcResult result =
                        deleteRequest(REQUEST_MAPPING + "/" + id);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenWorldOwnedByAnotherUser {

            @Test
            void thenReturn403AndDoNotDeleteWorld() throws Exception {
                User otherUser =
                        userRepository.findByUsername("otheruser").orElseThrow();

                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Forbidden Delete World")
                                .description("Should not be deleted")
                                .user(otherUser)
                                .build()
                );

                MvcResult result =
                        deleteRequest(
                                REQUEST_MAPPING + "/" + otherWorld.getId()
                        );

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.FORBIDDEN.value());

                assertThat(
                        worldRepository.findById(otherWorld.getId())
                ).isPresent();
            }
        }
    }
}