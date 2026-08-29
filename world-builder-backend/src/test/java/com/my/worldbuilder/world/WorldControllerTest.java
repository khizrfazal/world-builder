package com.my.worldbuilder.world;

import com.fasterxml.jackson.core.type.TypeReference;
import com.my.worldbuilder.IntegrationTest;
import com.my.worldbuilder.user.User;
import com.my.worldbuilder.world.dto.WorldRequest;
import com.my.worldbuilder.world.dto.WorldResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class WorldControllerTest extends IntegrationTest {

    private final String requestMapping = "/worlds";

    @Nested
    class GetWorldsTests {

        @Nested
        class GivenRequestToGetAllWorlds {

            @Test
            void thenReturnOnlyOwnedWorldsWith200StatusCode() throws Exception {
                worldRepository.save(World.builder()
                        .title("Game of Thrones")
                        .description("This show has a really bad ending")
                        .user(userRepository.findByUsername("testuser").orElseThrow())
                        .build());
                worldRepository.save(World.builder()
                        .title("Sonic")
                        .description("This world was full of speed and childhood memories")
                        .user(userRepository.findByUsername("testuser").orElseThrow())
                        .build());

                MvcResult mvcResult = getRequest(requestMapping);
                assertEquals(200, mvcResult.getResponse().getStatus());

                List<WorldResponse> body = objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(),
                        new TypeReference<>() {}
                );

                assertEquals(4, body.size());
                assertTrue(body.stream().anyMatch(w -> w.getTitle().equals("Murim")));
                assertTrue(body.stream().anyMatch(w -> w.getTitle().equals("Eldoria")));
                assertTrue(body.stream().anyMatch(w -> w.getTitle().equals("Game of Thrones")));
                assertTrue(body.stream().anyMatch(w -> w.getTitle().equals("Sonic")));
            }
        }
    }

    @Nested
    class GetWorldTests {

        @Nested
        class GivenValidWorldId {

            private UUID worldId;

            @BeforeEach
            void beforeEach() {
                worldId = worldRepository.findAll().getFirst().getId();
            }

            @Test
            void thenReturnWorldWith200StatusCode() throws Exception {
                MvcResult mvcResult = getRequest(requestMapping + "/" + worldId);
                assertEquals(200, mvcResult.getResponse().getStatus());

                WorldResponse body = objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(),
                        WorldResponse.class
                );
                assertEquals(worldId, body.getId());
                assertEquals("Murim", body.getTitle());
                assertEquals("Cultivation world", body.getDescription());
            }
        }

        @Nested
        class GivenWorldDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID worldId = UUID.randomUUID();

                MvcResult mvcResult = getRequest(requestMapping + "/" + worldId);
                assertEquals(404, mvcResult.getResponse().getStatus());
            }
        }

        @Nested
        class GivenWorldOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCode() throws Exception {
                User other = userRepository.findByUsername("otheruser").orElseThrow();

                World otherWorld = worldRepository.save(World.builder()
                        .title("Forbidden World")
                        .description("Should not be accessible")
                        .user(other)
                        .build());

                MvcResult mvcResult = getRequest(requestMapping + "/" + otherWorld.getId());
                assertEquals(403, mvcResult.getResponse().getStatus());
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

                MvcResult mvcResult = postRequest(requestMapping, request);
                assertEquals(201, mvcResult.getResponse().getStatus());

                UUID id = objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(),
                        UUID.class
                );

                World world = worldRepository.findById(id).orElseThrow();
                assertEquals("Avatar", world.getTitle());
                assertEquals("A world full of blue people", world.getDescription());

                User testUser = userRepository.findByUsername("testuser").orElseThrow();
                assertEquals(testUser.getId(), world.getUser().getId());
            }
        }

        @Nested
        class GivenInvalidRequestBody {

            @Test
            void thenReturn400WhenFieldsAreBlank() throws Exception {
                long before = worldRepository.count();
                WorldRequest request = new WorldRequest("", "");
                MvcResult mvcResult = postRequest(requestMapping, request);
                assertEquals(400, mvcResult.getResponse().getStatus());
                String response = mvcResult.getResponse().getContentAsString();
                assertTrue(response.contains("Title must not be blank"));
                assertTrue(response.contains("Description must not be blank"));
                assertEquals(before, worldRepository.count());
            }

        }
    }

    @Nested
    class UpdateWorldTests {

        @Nested
        class GivenValidRequestBody {

            @Test
            void thenUpdateWorldAndReturn204StatusCode() throws Exception {
                UUID id = worldRepository.findAll().getFirst().getId();
                WorldRequest request = new WorldRequest(
                        "Updated Title",
                        "Updated Description"
                );

                MvcResult mvcResult = putRequest(requestMapping + "/" + id, request);
                assertEquals(204, mvcResult.getResponse().getStatus());

                World updated = worldRepository.findById(id).orElseThrow();
                assertEquals("Updated Title", updated.getTitle());
                assertEquals("Updated Description", updated.getDescription());
            }
        }

        @Nested
        class GivenInvalidRequestBody {

            @Test
            void thenReturn400WhenFieldsAreBlank() throws Exception {
                long before = worldRepository.count();
                UUID id = worldRepository.findAll().getFirst().getId();
                WorldRequest request = new WorldRequest("", "");
                MvcResult mvcResult = putRequest(requestMapping + "/" + id, request);
                assertEquals(400, mvcResult.getResponse().getStatus());
                String response = mvcResult.getResponse().getContentAsString();
                assertTrue(response.contains("Title must not be blank"));
                assertTrue(response.contains("Description must not be blank"));
                assertEquals(before, worldRepository.count());
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

                MvcResult mvcResult = putRequest(requestMapping + "/" + id, request);
                assertEquals(404, mvcResult.getResponse().getStatus());
            }
        }

        @Nested
        class GivenWorldOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCode() throws Exception {
                User other = userRepository.findByUsername("otheruser").orElseThrow();

                World otherWorld = worldRepository.save(World.builder()
                        .title("Forbidden Update")
                        .description("Should not be updated")
                        .user(other)
                        .build());

                WorldRequest request = new WorldRequest("New Title", "New Desc");

                MvcResult mvcResult = putRequest(requestMapping + "/" + otherWorld.getId(), request);
                assertEquals(403, mvcResult.getResponse().getStatus());
            }
        }
    }

    @Nested
    class DeleteWorldTests {

        @Nested
        class GivenValidWorldId {

            @Test
            void thenDeleteWorldAndReturn204StatusCode() throws Exception {
                UUID id = worldRepository.findAll().getFirst().getId();

                MvcResult mvcResult = deleteRequest(requestMapping + "/" + id);
                assertEquals(204, mvcResult.getResponse().getStatus());
                assertFalse(worldRepository.findById(id).isPresent());
            }
        }

        @Nested
        class GivenWorldDoesNotExist {

            @Test
            void thenReturn404StatusCode() throws Exception {
                UUID id = UUID.randomUUID();

                MvcResult mvcResult = deleteRequest(requestMapping + "/" + id);
                assertEquals(404, mvcResult.getResponse().getStatus());
            }
        }

        @Nested
        class GivenWorldOwnedByAnotherUser {

            @Test
            void thenReturn403StatusCode() throws Exception {
                User other = userRepository.findByUsername("otheruser").orElseThrow();

                World otherWorld = worldRepository.save(World.builder()
                        .title("Forbidden Delete")
                        .description("Should not be deleted")
                        .user(other)
                        .build());

                MvcResult mvcResult = deleteRequest(requestMapping + "/" + otherWorld.getId());
                assertEquals(403, mvcResult.getResponse().getStatus());
            }
        }
    }
}