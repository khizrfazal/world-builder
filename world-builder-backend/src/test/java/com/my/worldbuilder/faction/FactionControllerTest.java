package com.my.worldbuilder.faction;

import com.fasterxml.jackson.core.type.TypeReference;
import com.my.worldbuilder.IntegrationTest;
import com.my.worldbuilder.faction.dto.FactionRequest;
import com.my.worldbuilder.faction.dto.FactionResponse;
import com.my.worldbuilder.user.User;
import com.my.worldbuilder.world.World;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FactionControllerTest extends IntegrationTest {

    private static final String REQUEST_MAPPING = "/worlds";

    private static final UUID MURIM_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    private static final UUID NORTHERN_HEAVENLY_SECT_ID =
            UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    private static final UUID BLACK_MOON_CLAN_ID =
            UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");

    private static final UUID FORBIDDEN_FACTION_ID =
            UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd");

    @Nested
    class GetFactionsTests {

        @Nested
        class GivenRequestToGetFactionsByWorld {

            @Test
            void thenReturnOnlyFactionsInWorldWith200StatusCode() throws Exception {
                MvcResult result = getRequest(REQUEST_MAPPING + "/" + MURIM_ID + "/factions");

                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

                List<FactionResponse> body = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        new TypeReference<>() {}
                );

                assertThat(body)
                        .hasSize(2)
                        .extracting(FactionResponse::getName)
                        .containsExactlyInAnyOrder(
                                "Northern Heavenly Sect",
                                "Black Moon Clan"
                        )
                        .doesNotContain("Forbidden Faction");
            }
        }
    }

    @Nested
    class GetFactionTests {

        @Nested
        class GivenValidFactionId {

            @Test
            void thenReturnFactionWith200StatusCode() throws Exception {
                MvcResult result = getRequest("/factions/" + NORTHERN_HEAVENLY_SECT_ID);

                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

                FactionResponse body = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        FactionResponse.class
                );

                assertThat(body.getId()).isEqualTo(NORTHERN_HEAVENLY_SECT_ID);
                assertThat(body.getName()).isEqualTo("Northern Heavenly Sect");
                assertThat(body.getDescription()).isEqualTo("A powerful martial arts sect in the Northern region");
                assertThat(body.getAlignment()).isEqualTo(Alignment.GOOD);
                assertThat(body.getType()).isEqualTo(FactionType.ORDER);
            }
        }

        @Nested
        class GivenFactionDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();
                MvcResult result = getRequest("/factions/" + id);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenFactionOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCode() throws Exception {
                MvcResult result = getRequest("/factions/" + FORBIDDEN_FACTION_ID);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
            }
        }
    }

    @Nested
    class CreateFactionTests {

        @Nested
        class GivenValidRequestBody {

            @Test
            void thenCreateFactionAndReturn201StatusCode() throws Exception {
                FactionRequest request = new FactionRequest(
                        "Iron Fist Clan",
                        "A powerful martial arts clan",
                        Alignment.GOOD,
                        FactionType.CLAN
                );

                MvcResult result = postRequest(REQUEST_MAPPING + "/" + MURIM_ID + "/factions", request);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
                UUID id = objectMapper.readValue(
                        result.getResponse().getContentAsString(),
                        UUID.class
                );

                Faction faction = factionRepository.findById(id).orElseThrow();
                assertThat(faction.getName()).isEqualTo("Iron Fist Clan");
                assertThat(faction.getDescription()).isEqualTo("A powerful martial arts clan");
                assertThat(faction.getAlignment()).isEqualTo(Alignment.GOOD);
                assertThat(faction.getType()).isEqualTo(FactionType.CLAN);
                assertThat(faction.getWorld().getId()).isEqualTo(MURIM_ID);
            }
        }

        @Nested
        class GivenInvalidRequestBody {

            @Test
            void thenReturn400WhenFieldsAreBlank() throws Exception {
                long before = factionRepository.count();

                FactionRequest request = new FactionRequest(
                        "",
                        "",
                        Alignment.GOOD,
                        FactionType.CLAN
                );
                MvcResult result = postRequest(REQUEST_MAPPING + "/" + MURIM_ID + "/factions", request);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                assertThat(result.getResponse().getContentAsString())
                        .contains("Name must not be blank")
                        .contains("Description must not be blank");

                assertThat(factionRepository.count()).isEqualTo(before);
            }

            @Test
            void thenReturn400WhenFieldsExceedMaximumLength() throws Exception {
                long before = factionRepository.count();

                FactionRequest request = new FactionRequest(
                        "A".repeat(151),
                        "A".repeat(1001),
                        Alignment.GOOD,
                        FactionType.CLAN
                );
                MvcResult result = postRequest(REQUEST_MAPPING + "/" + MURIM_ID + "/factions", request);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                assertThat(result.getResponse().getContentAsString())
                        .contains("Name must be less than 150 characters")
                        .contains("Description must be less than 1000 characters");
                assertThat(factionRepository.count()).isEqualTo(before);
            }

            @Test
            void thenReturn400WhenAlignmentOrTypeIsNull() throws Exception {
                long before = factionRepository.count();

                FactionRequest request = new FactionRequest(
                        "Valid Name",
                        "Valid description",
                        null,
                        null
                );

                MvcResult result = postRequest(REQUEST_MAPPING + "/" + MURIM_ID + "/factions", request);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                assertThat(result.getResponse().getContentAsString())
                        .contains("Alignment must not be null")
                        .contains("Type must not be null");
                assertThat(factionRepository.count()).isEqualTo(before);
            }
        }

        @Nested
        class GivenWorldOwnedByAnotherUser {

            @Test
            void thenReturn403AndDoNotCreateFaction() throws Exception {
                User otherUser = userRepository.findByUsername("otheruser").orElseThrow();
                World otherWorld = worldRepository.save(
                        World.builder()
                                .title("Another User's World")
                                .description("Not owned by testuser")
                                .user(otherUser)
                                .build()
                );
                FactionRequest request = new FactionRequest(
                        "Unauthorized Faction",
                        "Should not be created",
                        Alignment.EVIL,
                        FactionType.CLAN
                );
                long before = factionRepository.count();
                MvcResult result = postRequest(REQUEST_MAPPING + "/" + otherWorld.getId() + "/factions", request);

                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
                assertThat(factionRepository.count()).isEqualTo(before);
            }
        }
    }

    @Nested
    class UpdateFactionTests {

        @Nested
        class GivenValidRequestBody {

            @Test
            void thenUpdateFactionAndReturn204StatusCode() throws Exception {
                FactionRequest request = new FactionRequest(
                        "Updated Faction",
                        "Updated description",
                        Alignment.NEUTRAL,
                        FactionType.GUILD
                );

                MvcResult result = putRequest("/factions/" + BLACK_MOON_CLAN_ID, request);

                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
                Faction updated = factionRepository.findById(BLACK_MOON_CLAN_ID).orElseThrow();

                assertThat(updated.getName()).isEqualTo("Updated Faction");
                assertThat(updated.getDescription()).isEqualTo("Updated description");
                assertThat(updated.getAlignment()).isEqualTo(Alignment.NEUTRAL);
                assertThat(updated.getType()).isEqualTo(FactionType.GUILD);
                assertThat(updated.getWorld().getId()).isEqualTo(MURIM_ID);
            }
        }

        @Nested
        class GivenInvalidRequestBody {

            @Test
            void thenReturn400AndDoNotModifyFaction() throws Exception {
                Faction before = factionRepository.findById(BLACK_MOON_CLAN_ID)
                        .orElseThrow();

                String originalName = before.getName();
                String originalDescription = before.getDescription();
                Alignment originalAlignment = before.getAlignment();
                FactionType originalType = before.getType();

                FactionRequest request = new FactionRequest(
                        "",
                        "",
                        Alignment.GOOD,
                        FactionType.CLAN
                );

                MvcResult result = putRequest("/factions/" + BLACK_MOON_CLAN_ID, request);

                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

                Faction after = factionRepository.findById(BLACK_MOON_CLAN_ID).orElseThrow();

                assertThat(after.getName()).isEqualTo(originalName);
                assertThat(after.getDescription()).isEqualTo(originalDescription);
                assertThat(after.getAlignment()).isEqualTo(originalAlignment);
                assertThat(after.getType()).isEqualTo(originalType);
            }

            @Test
            void thenReturn400WhenFieldsExceedMaximumLength() throws Exception {
                Faction before = factionRepository.findById(BLACK_MOON_CLAN_ID)
                        .orElseThrow();

                String originalName = before.getName();
                String originalDescription = before.getDescription();

                FactionRequest request = new FactionRequest(
                        "A".repeat(151),
                        "A".repeat(1001),
                        Alignment.GOOD,
                        FactionType.CLAN
                );

                MvcResult result = putRequest("/factions/" + BLACK_MOON_CLAN_ID, request);

                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

                assertThat(result.getResponse().getContentAsString())
                        .contains("Name must be less than 150 characters")
                        .contains("Description must be less than 1000 characters");

                Faction after = factionRepository.findById(BLACK_MOON_CLAN_ID).orElseThrow();

                assertThat(after.getName()).isEqualTo(originalName);
                assertThat(after.getDescription()).isEqualTo(originalDescription);
            }
        }

        @Nested
        class GivenFactionDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();

                FactionRequest request = new FactionRequest(
                        "Updated Faction",
                        "Updated description",
                        Alignment.GOOD,
                        FactionType.CLAN
                );

                MvcResult result = putRequest("/factions/" + id, request);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenFactionOwnedByAnotherUser {

            @Test
            void thenReturn403AndDoNotModifyFaction() throws Exception {
                Faction before = factionRepository.findById(FORBIDDEN_FACTION_ID).orElseThrow();

                String originalName = before.getName();
                String originalDescription = before.getDescription();
                Alignment originalAlignment = before.getAlignment();
                FactionType originalType = before.getType();

                FactionRequest request = new FactionRequest(
                        "Unauthorized Update",
                        "Should not be updated",
                        Alignment.GOOD,
                        FactionType.ORDER
                );

                MvcResult result = putRequest("/factions/" + FORBIDDEN_FACTION_ID, request);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
                Faction after = factionRepository.findById(FORBIDDEN_FACTION_ID).orElseThrow();
                assertThat(after.getName()).isEqualTo(originalName);
                assertThat(after.getDescription()).isEqualTo(originalDescription);
                assertThat(after.getAlignment()).isEqualTo(originalAlignment);
                assertThat(after.getType()).isEqualTo(originalType);
            }
        }
    }

    @Nested
    class DeleteFactionTests {

        @Nested
        class GivenValidFactionId {

            @Test
            void thenDeleteFactionAndReturn204StatusCode() throws Exception {
                MvcResult result = deleteRequest("/factions/" + NORTHERN_HEAVENLY_SECT_ID);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
                assertThat(factionRepository.findById(NORTHERN_HEAVENLY_SECT_ID)).isEmpty();
            }
        }

        @Nested
        class GivenFactionDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();
                MvcResult result = deleteRequest("/factions/" + id);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
            }
        }

        @Nested
        class GivenFactionOwnedByAnotherUser {

            @Test
            void thenReturn403AndDoNotDeleteFaction() throws Exception {
                MvcResult result = deleteRequest("/factions/" + FORBIDDEN_FACTION_ID);
                assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
                assertThat(factionRepository.findById(FORBIDDEN_FACTION_ID)).isPresent();
            }
        }
    }
}