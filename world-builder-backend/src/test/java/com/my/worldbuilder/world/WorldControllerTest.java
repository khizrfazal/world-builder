package com.my.worldbuilder.world;

import com.fasterxml.jackson.core.type.TypeReference;
import com.my.worldbuilder.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorldControllerTest extends IntegrationTest {
    private final String requestMapping = "/worlds";

    @Nested
    class GetWorldTests {
        private MvcResult mvcResult;

        @Test
        void thenReturnAllWorlds() throws Exception {
            worldRepository.save(World.builder()
                    .title("Game of Thrones")
                    .description("This show has a really bad ending")
                    .build());
            worldRepository.save(World.builder()
                    .title("Sonic")
                    .description("This world was full of speed and childhood memories")
                    .build());

            mvcResult = getRequest(requestMapping);
            assertEquals(200, mvcResult.getResponse().getStatus());
            List<World> body =
                    objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
            assertEquals(4, body.size());
            assertEquals("Cultivation world", body.getFirst().getTitle());
            assertEquals("High fantasy realm", body.get(1).getDescription());
            assertEquals("Game of Thrones", body.get(2).getTitle());
            assertEquals("Sonic", body.get(3).getDescription());
        }
    }
}