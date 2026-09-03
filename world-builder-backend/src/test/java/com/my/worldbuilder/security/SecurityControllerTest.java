package com.my.worldbuilder.security;

import com.my.worldbuilder.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;

public class SecurityControllerTest extends IntegrationTest {

    @Nested
    class GivenUnauthenticatedRequest {

        @Test
        void thenReturn401WhenGettingWorlds() throws Exception {
            MvcResult result =
                    getUnauthenticatedRequest("/worlds");

            assertThat(result.getResponse().getStatus())
                    .isEqualTo(HttpStatus.UNAUTHORIZED.value());
        }
    }
}