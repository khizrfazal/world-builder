package com.my.worldbuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.worldbuilder.character.CharacterRepository;
import com.my.worldbuilder.character.relationship.CharacterRelationshipRepository;
import com.my.worldbuilder.event.EventRepository;
import com.my.worldbuilder.event.character.EventCharacterRepository;
import com.my.worldbuilder.faction.FactionRepository;
import com.my.worldbuilder.faction.location.FactionLocationRepository;
import com.my.worldbuilder.location.LocationRepository;
import com.my.worldbuilder.location.character.CharacterLocationRepository;
import com.my.worldbuilder.lore.LoreRepository;
import com.my.worldbuilder.user.User;
import com.my.worldbuilder.user.UserRepository;
import com.my.worldbuilder.world.WorldRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.oauth2.jwt.JwtClaimsSet.builder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(
        scripts = {
                "classpath:sql/users_data.sql",
                "classpath:sql/worlds_data.sql"
        },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
public abstract class IntegrationTest {

    @Autowired protected EntityManager entityManager;
    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;

    @Autowired protected WorldRepository worldRepository;
    @Autowired protected CharacterRepository characterRepository;
    @Autowired protected CharacterRelationshipRepository characterRelationshipRepository;
    @Autowired protected EventRepository eventRepository;
    @Autowired protected EventCharacterRepository eventCharacterRepository;
    @Autowired protected FactionRepository factionRepository;
    @Autowired protected FactionLocationRepository factionLocationRepository;
    @Autowired protected LocationRepository locationRepository;
    @Autowired protected CharacterLocationRepository characterLocationRepository;
    @Autowired protected LoreRepository loreRepository;
    @Autowired protected UserRepository userRepository;

    @Autowired protected JwtEncoder jwtEncoder;

    private String jwtToken;

    @BeforeEach
    void setupAuth() {
        // Load the test user inserted via SQL
        User user = userRepository.findByUsername("testuser")
                .orElseThrow(() -> new IllegalStateException("Test user not found in SQL fixture"));

        // Build JWT claims
        var claims = builder()
                .issuer("worldbuilder-test")
                .subject(user.getId().toString())
                .claim("username", user.getUsername())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        // Build JWT header (Spring Security 6+)
        var headers = JwsHeader.with(() -> "RS256").build();

        // Encode JWT
        var params = JwtEncoderParameters.from(headers, claims);
        jwtToken = jwtEncoder.encode(params).getTokenValue();

    }

    protected MvcResult getRequest(String url) throws Exception {
        return mockMvc.perform(
                get(url)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Correlation-Id", UUID.randomUUID().toString())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
        ).andReturn();
    }

    protected MvcResult postRequest(String url, Object body) throws Exception {
        String json = objectMapper.writeValueAsString(body);
        return mockMvc.perform(
                post(url)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Correlation-Id", UUID.randomUUID().toString())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json)
        ).andReturn();
    }

    protected MvcResult putRequest(String url, Object body) throws Exception {
        String json = objectMapper.writeValueAsString(body);
        return mockMvc.perform(
                put(url)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Correlation-Id", UUID.randomUUID().toString())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json)
        ).andReturn();
    }

    protected MvcResult deleteRequest(String url) throws Exception {
        return mockMvc.perform(
                delete(url)
                        .header("Authorization", "Bearer " + jwtToken)
                        .header("X-Correlation-Id", UUID.randomUUID().toString())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
        ).andReturn();
    }
}