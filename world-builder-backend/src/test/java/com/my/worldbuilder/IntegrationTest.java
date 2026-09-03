package com.my.worldbuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.worldbuilder.character.CharacterRepository;
import com.my.worldbuilder.common.exception.GlobalExceptionHandler;
import com.my.worldbuilder.user.User;
import com.my.worldbuilder.user.UserRepository;
import com.my.worldbuilder.world.WorldRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.oauth2.jwt.JwtClaimsSet.builder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
@Sql(
        scripts = {
                "classpath:sql/users_data.sql",
                "classpath:sql/worlds_data.sql",
                "classpath:sql/characters_data.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public abstract class IntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected WorldRepository worldRepository;

    @Autowired
    protected CharacterRepository characterRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected JwtEncoder jwtEncoder;

    protected User testUser() {
        return userRepository.findByUsername("testuser")
                .orElseThrow();
    }

    protected User otherUser() {
        return userRepository.findByUsername("otheruser")
                .orElseThrow();
    }

    protected String createJwtToken(User user) {
        var now = Instant.now();

        var claims = builder()
                .issuer("worldbuilder-test")
                .subject(user.getId().toString())
                .claim("username", user.getUsername())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .build();

        var headers = JwsHeader.with(() -> "RS256").build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(headers, claims))
                .getTokenValue();
    }

    protected MvcResult getRequest(String url) throws Exception {
        return mockMvc.perform(
                authenticated(get(url), testUser())
        ).andReturn();
    }

    protected MvcResult getUnauthenticatedRequest(String url) throws Exception {
        return mockMvc.perform(
                unauthenticated(get(url))
        ).andReturn();
    }

    protected MvcResult postRequest(String url, Object body) throws Exception {
        return mockMvc.perform(
                authenticated(post(url), testUser())
                        .content(objectMapper.writeValueAsString(body))
        ).andReturn();
    }

    protected MvcResult putRequest(String url, Object body) throws Exception {
        return mockMvc.perform(
                authenticated(put(url), testUser())
                        .content(objectMapper.writeValueAsString(body))
        ).andReturn();
    }

    protected MvcResult deleteRequest(String url) throws Exception {
        return mockMvc.perform(
                authenticated(delete(url), testUser())
        ).andReturn();
    }

    private MockHttpServletRequestBuilder authenticated(
            MockHttpServletRequestBuilder request,
            User user
    ) {
        return request
                .header("Authorization", "Bearer " + createJwtToken(user))
                .header("X-Correlation-Id", UUID.randomUUID().toString())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder unauthenticated(
            MockHttpServletRequestBuilder request
    ) {
        return request
                .header("X-Correlation-Id", UUID.randomUUID().toString())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON);
    }
}