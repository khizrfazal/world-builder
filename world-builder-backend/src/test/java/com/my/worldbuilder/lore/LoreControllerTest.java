package com.my.worldbuilder.lore;

import com.fasterxml.jackson.core.type.TypeReference;
import com.my.worldbuilder.IntegrationTest;
import com.my.worldbuilder.lore.dto.LoreRequest;
import com.my.worldbuilder.lore.dto.LoreResponse;
import com.my.worldbuilder.user.User;
import com.my.worldbuilder.world.World;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class LoreControllerTest extends IntegrationTest {

    private static final String REQUEST_MAPPING = "/worlds";
    private static final UUID MURIM_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID NORTHERN_SECT_LORE_ID = UUID.fromString("aaaaaaaa-1111-1111-1111-111111111111");
    private static final UUID FIVE_CLANS_LORE_ID = UUID.fromString("bbbbbbbb-2222-2222-2222-222222222222");

    @Nested
    class GetLoresTests {

        @Nested
        class GivenRequestToGetLoresByWorld {

            @Test
            void thenReturnOnlyLoresOwnedByUserWith200StatusCode() throws Exception {
                MvcResult result = getRequest(REQUEST_MAPPING + "/" + MURIM_ID + "/lore");
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                List<LoreResponse> body = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        new TypeReference<>() {}
                );
                assertThat(body)
                        .hasSize(2)
                        .extracting(LoreResponse::getTitle)
                        .containsExactlyInAnyOrder(
                                "The Northern Heavenly Sect",
                                "The Five Great Clans"
                        );
            }
        }

        @Nested
        class GivenWorldOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCode() throws Exception {
                User otherUser = userRepository.findByUsername("otheruser").orElseThrow();
                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Another User's World")
                                .description("Owned by another user")
                                .user(otherUser)
                                .build()
                );
                MvcResult result = getRequest(REQUEST_MAPPING + "/" + otherWorld.getId() + "/lore");
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
            }
        }

        @Nested
        class GivenWorldDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();
                MvcResult result = getRequest(REQUEST_MAPPING + "/" + id + "/lore");
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }
    }

    @Nested
    class GetLoreTests {

        @Nested
        class GivenValidLoreId {

            @Test
            void thenReturnLoreWith200StatusCode() throws Exception {
                MvcResult result = getRequest("/lore/" + NORTHERN_SECT_LORE_ID);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
                LoreResponse body = objectMapper.readValue(result.getResponse().getContentAsString(), LoreResponse.class);
                assertThat(body.getId()).isEqualTo(NORTHERN_SECT_LORE_ID);
                assertThat(body.getTitle()).isEqualTo("The Northern Heavenly Sect");
                assertThat(body.getBody()).isEqualTo("A powerful sect in the northern region of Murim.");
                assertThat(result.getResponse().getContentAsString()).doesNotContain("worldId");
            }
        }

        @Nested
        class GivenLoreDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();
                MvcResult result = getRequest("/lore/" + id);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenLoreOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCode() throws Exception {
                User otherUser = userRepository.findByUsername("otheruser").orElseThrow();
                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Forbidden Lore World")
                                .description("Owned by another user")
                                .user(otherUser)
                                .build()
                );
                LoreEntry forbiddenLore = loreRepository.save(
                        LoreEntry.builder()
                                .title("Forbidden Lore")
                                .body("Should not be accessible")
                                .world(otherWorld)
                                .build()
                );
                MvcResult result = getRequest("/lore/" + forbiddenLore.getId());
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
            }
        }
    }

    @Nested
    class CreateLoreTests {

        @Nested
        class GivenValidRequestBody {

            @Test
            void thenCreateLoreAndReturn201StatusCode() throws Exception {
                LoreRequest request = new LoreRequest(
                        "The Shadow Alliance",
                        "A secret alliance operating throughout Murim."
                );
                MvcResult result = postRequest(REQUEST_MAPPING + "/" + MURIM_ID + "/lore", request);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
                UUID id = objectMapper.readValue(result.getResponse().getContentAsString(), UUID.class);
                LoreEntry lore = loreRepository.findById(id).orElseThrow();
                assertThat(lore.getTitle()).isEqualTo("The Shadow Alliance");
                assertThat(lore.getBody()).isEqualTo("A secret alliance operating throughout Murim.");
                assertThat(lore.getWorld().getId()).isEqualTo(MURIM_ID);
            }
        }

        @Nested
        class GivenInvalidRequestBody {

            @Test
            void thenReturn400WhenTitleIsBlankAndDoNotCreateLore() throws Exception {
                long before = loreRepository.count();
                LoreRequest request = new LoreRequest("", "This lore entry has no title.");
                MvcResult result = postRequest(REQUEST_MAPPING + "/" + MURIM_ID + "/lore", request);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                assertThat(loreRepository.count()).isEqualTo(before);
            }
        }

        @Nested
        class GivenWorldOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCodeAndDoNotCreateLore() throws Exception {
                User otherUser = userRepository.findByUsername("otheruser").orElseThrow();
                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Another User's World")
                                .description("Not owned by testuser")
                                .user(otherUser)
                                .build()
                );
                LoreRequest request = new LoreRequest("Unauthorized Lore", "Should not be created.");
                long before = loreRepository.count();
                MvcResult result = postRequest(REQUEST_MAPPING + "/" + otherWorld.getId() + "/lore", request);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
                assertThat(loreRepository.count()).isEqualTo(before);
            }
        }
    }

    @Nested
    class UpdateLoreTests {

        @Nested
        class GivenValidRequestBody {

            @Test
            void thenUpdateLoreAndReturn204StatusCode() throws Exception {
                LoreRequest request = new LoreRequest(
                        "Updated Lore",
                        "The lore entry has been updated."
                );
                MvcResult result = putRequest("/lore/" + FIVE_CLANS_LORE_ID, request);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
                LoreEntry updated = loreRepository.findById(FIVE_CLANS_LORE_ID).orElseThrow();
                assertThat(updated.getTitle()).isEqualTo("Updated Lore");
                assertThat(updated.getBody()).isEqualTo("The lore entry has been updated.");
            }
        }

        @Nested
        class GivenInvalidRequestBody {

            @Test
            void thenReturn400AndDoNotModifyLore() throws Exception {
                LoreEntry before = loreRepository.findById(FIVE_CLANS_LORE_ID).orElseThrow();
                String originalTitle = before.getTitle();
                String originalBody = before.getBody();

                LoreRequest request = new LoreRequest(
                        "",
                        "This update should fail."
                );

                MvcResult result = putRequest("/lore/" + FIVE_CLANS_LORE_ID, request);

                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

                LoreEntry after = loreRepository.findById(FIVE_CLANS_LORE_ID).orElseThrow();

                assertThat(after.getTitle()).isEqualTo(originalTitle);
                assertThat(after.getBody()).isEqualTo(originalBody);
            }
        }

        @Nested
        class GivenLoreDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();

                LoreRequest request = new LoreRequest(
                        "Updated Lore",
                        "Updated body."
                );

                MvcResult result = putRequest("/lore/" + id, request);

                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenLoreOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCodeAndDoNotModifyLore() throws Exception {
                User otherUser = userRepository.findByUsername("otheruser").orElseThrow();

                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Forbidden Update World")
                                .description("Owned by another user")
                                .user(otherUser)
                                .build()
                );

                LoreEntry forbiddenLore = loreRepository.save(
                        LoreEntry.builder()
                                .title("Original Lore")
                                .body("Original body.")
                                .world(otherWorld)
                                .build()
                );

                LoreRequest request = new LoreRequest(
                        "New Title",
                        "New body."
                );

                MvcResult result = putRequest("/lore/" + forbiddenLore.getId(), request);

                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());

                LoreEntry unchanged = loreRepository.findById(forbiddenLore.getId()).orElseThrow();

                assertThat(unchanged.getTitle()).isEqualTo("Original Lore");
                assertThat(unchanged.getBody()).isEqualTo("Original body.");
            }
        }
    }

    @Nested
    class DeleteLoreTests {

        @Nested
        class GivenValidLoreId {

            @Test
            void thenDeleteLoreAndReturn204StatusCode() throws Exception {
                LoreEntry lore = loreRepository.findById(FIVE_CLANS_LORE_ID).orElseThrow();

                MvcResult result = deleteRequest("/lore/" + lore.getId());

                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
                assertThat(loreRepository.findById(lore.getId())).isEmpty();
            }
        }

        @Nested
        class GivenLoreDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();

                MvcResult result = deleteRequest("/lore/" + id);

                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenLoreOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCodeAndDoNotDeleteLore() throws Exception {
                User otherUser = userRepository.findByUsername("otheruser").orElseThrow();

                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Forbidden Delete World")
                                .description("Owned by another user")
                                .user(otherUser)
                                .build()
                );

                LoreEntry forbiddenLore = loreRepository.save(
                        LoreEntry.builder()
                                .title("Forbidden Delete Lore")
                                .body("Should not be deleted.")
                                .world(otherWorld)
                                .build()
                );

                MvcResult result = deleteRequest("/lore/" + forbiddenLore.getId());

                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
                assertThat(loreRepository.findById(forbiddenLore.getId())).isPresent();
            }
        }
    }
}