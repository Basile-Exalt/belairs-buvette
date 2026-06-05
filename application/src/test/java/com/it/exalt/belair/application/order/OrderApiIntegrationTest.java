package com.it.exalt.belair.application.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class OrderApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateOrderAndReturn201_withCommandeId_and_statusPending() throws Exception {
        String payload = "{" +
                "\"festivalierId\":\"festivalier-42\"," +
                "\"articles\":[{\"id\":\"mojito\",\"quantite\":2}]" +
                "}";

        mockMvc.perform(post("/commandes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.commandeId").isNotEmpty())
                .andExpect(jsonPath("$.status").value("EN_ATTENTE"));
    }

    @Test
    void shouldReturn401_whenFestivalierNotAuthenticated() throws Exception {
        String payload = "{" +
                "\"festivalierId\":\"festivalier-42\"," +
                "\"articles\":[{\"id\":\"mojito\",\"quantite\":2}]" +
                "}";

        mockMvc.perform(post("/commandes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn400_whenRequestBodyIsInvalid() throws Exception {
        String invalidPayload = "{" +
                "\"festivalierId\":\"festivalier-42\"" +
                "}";

        mockMvc.perform(post("/commandes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest());
    }
}
