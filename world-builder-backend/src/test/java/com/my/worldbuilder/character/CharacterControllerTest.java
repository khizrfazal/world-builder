package com.my.worldbuilder.character;

import com.fasterxml.jackson.core.type.TypeReference;
import com.my.worldbuilder.IntegrationTest;
import com.my.worldbuilder.character.dto.CharacterRequest;
import com.my.worldbuilder.character.dto.CharacterResponse;
import com.my.worldbuilder.user.User;
import com.my.worldbuilder.world.World;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CharacterControllerTest extends IntegrationTest {

    private static final String REQUEST_MAPPING = "/worlds";

    private static final UUID MURIM_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    private static final UUID JIN_MU_WON_ID =
            UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    private static final UUID SEO_HAE_RANG_ID =
            UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");

    @Nested
    class GetCharactersTests {

        @Nested
        class GivenRequestToGetCharactersByWorld {

            @Test
            void thenReturnOnlyCharactersOwnedByUserWith200StatusCode() throws Exception {
                MvcResult result =
                        getRequest(REQUEST_MAPPING + "/" + MURIM_ID + "/characters");

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.OK.value());

                List<CharacterResponse> body = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        new TypeReference<>() {}
                );

                assertThat(body)
                        .hasSize(2)
                        .extracting(CharacterResponse::getName)
                        .containsExactlyInAnyOrder(
                                "Jin Mu-Won",
                                "Seo Hae-Rang"
                        );
            }
        }
    }

    @Nested
    class GetCharacterTests {

        @Nested
        class GivenValidCharacterId {

            @Test
            void thenReturnCharacterWith200StatusCode() throws Exception {
                MvcResult result =
                        getRequest("/characters/" + JIN_MU_WON_ID);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.OK.value());

                CharacterResponse body = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        CharacterResponse.class
                );

                assertThat(body.getId()).isEqualTo(JIN_MU_WON_ID);
                assertThat(body.getName()).isEqualTo("Jin Mu-Won");
                assertThat(body.getSummary())
                        .isEqualTo("Young master of the Northern Heavenly Sect");
            }
        }

        @Nested
        class GivenCharacterDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();

                MvcResult result =
                        getRequest("/characters/" + id);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenCharacterOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCode() throws Exception {
                User otherUser =
                        userRepository.findByUsername("otheruser").orElseThrow();

                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Forbidden Character World")
                                .description("Owned by another user")
                                .user(otherUser)
                                .build()
                );

                Character forbiddenCharacter = characterRepository.save(
                        Character.builder()
                                .name("Forbidden Hero")
                                .summary("Should not be accessible")
                                .world(otherWorld)
                                .build()
                );

                MvcResult result =
                        getRequest("/characters/" + forbiddenCharacter.getId());

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.FORBIDDEN.value());
            }
        }
    }

    @Nested
    class CreateCharacterTests {

        @Nested
        class GivenValidRequestBody {

            @Test
            void thenCreateCharacterAndReturn201StatusCode() throws Exception {
                CharacterRequest request = new CharacterRequest(
                        "New Hero",
                        "A brave new character"
                );

                MvcResult result = postRequest(
                        REQUEST_MAPPING + "/" + MURIM_ID + "/characters",
                        request
                );

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.CREATED.value());

                UUID id = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        UUID.class
                );

                Character character =
                        characterRepository.findById(id).orElseThrow();

                assertThat(character.getName())
                        .isEqualTo("New Hero");

                assertThat(character.getSummary())
                        .isEqualTo("A brave new character");

                assertThat(character.getWorld().getId())
                        .isEqualTo(MURIM_ID);
            }
        }

        @Nested
        class GivenInvalidRequestBody {

            @Test
            void thenReturn400WhenFieldsAreBlank() throws Exception {
                long before = characterRepository.count();

                CharacterRequest request =
                        new CharacterRequest("", "");

                MvcResult result = postRequest(
                        REQUEST_MAPPING + "/" + MURIM_ID + "/characters",
                        request
                );

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.BAD_REQUEST.value());

                assertThat(characterRepository.count())
                        .isEqualTo(before);
            }
        }
    }

    @Nested
    class UpdateCharacterTests {

        @Nested
        class GivenValidRequestBody {

            @Test
            void thenUpdateCharacterAndReturn204StatusCode() throws Exception {
                CharacterRequest request = new CharacterRequest(
                        "Updated Name",
                        "Updated Summary"
                );

                MvcResult result =
                        putRequest("/characters/" + SEO_HAE_RANG_ID, request);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.NO_CONTENT.value());

                Character updated =
                        characterRepository.findById(SEO_HAE_RANG_ID).orElseThrow();

                assertThat(updated.getName())
                        .isEqualTo("Updated Name");

                assertThat(updated.getSummary())
                        .isEqualTo("Updated Summary");
            }
        }

        @Nested
        class GivenInvalidRequestBody {

            @Test
            void thenReturn400AndDoNotModifyCharacter() throws Exception {
                Character before =
                        characterRepository.findById(SEO_HAE_RANG_ID).orElseThrow();

                String originalName = before.getName();
                String originalSummary = before.getSummary();

                CharacterRequest request =
                        new CharacterRequest("", "");

                MvcResult result =
                        putRequest("/characters/" + SEO_HAE_RANG_ID, request);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.BAD_REQUEST.value());

                Character after =
                        characterRepository.findById(SEO_HAE_RANG_ID).orElseThrow();

                assertThat(after.getName())
                        .isEqualTo(originalName);

                assertThat(after.getSummary())
                        .isEqualTo(originalSummary);
            }
        }

        @Nested
        class GivenCharacterDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();

                CharacterRequest request = new CharacterRequest(
                        "Updated Name",
                        "Updated Summary"
                );

                MvcResult result =
                        putRequest("/characters/" + id, request);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenCharacterOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCode() throws Exception {
                User otherUser =
                        userRepository.findByUsername("otheruser").orElseThrow();

                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Forbidden Update World")
                                .description("Owned by another user")
                                .user(otherUser)
                                .build()
                );

                Character forbiddenCharacter = characterRepository.save(
                        Character.builder()
                                .name("Original Name")
                                .summary("Original Summary")
                                .world(otherWorld)
                                .build()
                );

                CharacterRequest request =
                        new CharacterRequest("New Name", "New Summary");

                MvcResult result =
                        putRequest(
                                "/characters/" + forbiddenCharacter.getId(),
                                request
                        );

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.FORBIDDEN.value());

                Character unchanged =
                        characterRepository
                                .findById(forbiddenCharacter.getId())
                                .orElseThrow();

                assertThat(unchanged.getName())
                        .isEqualTo("Original Name");

                assertThat(unchanged.getSummary())
                        .isEqualTo("Original Summary");
            }
        }
    }

    @Nested
    class DeleteCharacterTests {

        @Nested
        class GivenValidCharacterId {

            @Test
            void thenDeleteCharacterAndReturn204StatusCode() throws Exception {
                Character character =
                        characterRepository.findById(SEO_HAE_RANG_ID).orElseThrow();

                MvcResult result =
                        deleteRequest("/characters/" + character.getId());

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.NO_CONTENT.value());

                assertThat(characterRepository.findById(character.getId()))
                        .isEmpty();
            }
        }

        @Nested
        class GivenCharacterDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();

                MvcResult result =
                        deleteRequest("/characters/" + id);

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenCharacterOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCode() throws Exception {
                User otherUser =
                        userRepository.findByUsername("otheruser").orElseThrow();

                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Forbidden Delete World")
                                .description("Owned by another user")
                                .user(otherUser)
                                .build()
                );

                Character forbiddenCharacter = characterRepository.save(
                        Character.builder()
                                .name("Forbidden Delete Character")
                                .summary("Should not be deleted")
                                .world(otherWorld)
                                .build()
                );

                MvcResult result =
                        deleteRequest("/characters/" + forbiddenCharacter.getId());

                assertThat(result.getResponse().getStatus())
                        .isEqualTo(HttpStatus.FORBIDDEN.value());

                assertThat(
                        characterRepository.findById(forbiddenCharacter.getId())
                ).isPresent();
            }
        }
    }
}