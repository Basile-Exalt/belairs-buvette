package com.it.exalt.belair.application.order;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import static org.junit.jupiter.api.Assertions.*;
import com.it.exalt.belair.domain.order.dto.CreerCommandeResponse;
import com.it.exalt.belair.application.utils.TestUtils;
import com.it.exalt.belair.domain.order.dto.CreerCommandeRequest;
import com.it.exalt.belair.domain.order.Article;
import com.it.exalt.belair.domain.order.usecase.CreerCommandeUseCase;
import java.util.List;
import java.util.Collections;
import static org.mockito.Mockito.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreerCommandeUseCase createOrderUseCase;

    @BeforeEach
    void setUp() {
        // Default mock returns EN_ATTENTE
        reset(createOrderUseCase);
    }

    @Test
    void shouldCreateOrderAndReturn201_withCommandeId_and_statusPending() throws Exception {
        Article article = new Article("mojito", 2);
        CreerCommandeRequest request = new CreerCommandeRequest("festivalier-42", List.of(article));

        // arrange
        TestUtils.mockSuccess(createOrderUseCase);

        MvcResult result = mockMvc.perform(post("/commandes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.toJson(request)))
                .andExpect(status().isCreated())
                .andReturn();

        CreerCommandeResponse resp = TestUtils.fromResult(result, CreerCommandeResponse.class);
        assertNotNull(resp.orderId());
        assertEquals("EN_ATTENTE", resp.status());
    }

    @Test
    void shouldReturn401_whenFestivalierNotAuthenticated() throws Exception {
        Article article = new Article("mojito", 2);
        CreerCommandeRequest request = new CreerCommandeRequest("festivalier-42", List.of(article));

        // Configure the @MockBean to throw UnauthorizedException for this test
        TestUtils.mockUnauthorized(createOrderUseCase);

        mockMvc.perform(post("/commandes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtils.toJson(request)))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn400_whenRequestBodyIsInvalid() throws Exception {
        CreerCommandeRequest request = new CreerCommandeRequest("festivalier-42", Collections.emptyList());

        TestUtils.mockInvalidRequest(createOrderUseCase);

        mockMvc.perform(post("/commandes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtils.toJson(request)))
            .andExpect(status().isBadRequest());
    }
}
