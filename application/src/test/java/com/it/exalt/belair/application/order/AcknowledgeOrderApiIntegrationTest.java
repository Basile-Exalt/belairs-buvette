package com.it.exalt.belair.application.order;

import com.it.exalt.belair.application.utils.TestUtils;
import com.it.exalt.belair.domain.order.dto.AccuserReceptionCommandeResult;
import com.it.exalt.belair.domain.order.exception.CommandeNonTrouveeException;
import com.it.exalt.belair.domain.order.usecase.AccuserReceptionCommandeUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AcknowledgeOrderApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccuserReceptionCommandeUseCase acknowledgeOrderUseCase;

    @BeforeEach
    void setUp() {
        reset(acknowledgeOrderUseCase);
    }

    @Test
    void givenExistingOrder_whenBartenderAcknowledges_thenReturns200WithEta() throws Exception {
        // Given the use case returns a successful result with 12 minutes ETA
        when(acknowledgeOrderUseCase.acknowledge(any()))
                .thenReturn(new AccuserReceptionCommandeResult("order-1", 12));

        // When the bartender calls the acknowledge endpoint
        MvcResult result = mockMvc.perform(
                        post("/commandes/order-1/accuser-reception"))
                .andExpect(status().isOk())
                .andReturn();

        // Then the response contains the order ID and ETA
        AccuserReceptionCommandeResult response = TestUtils.fromResult(result, AccuserReceptionCommandeResult.class);
        assertThat(response.commandeId()).isEqualTo("order-1");
        assertThat(response.etaMinutes()).isEqualTo(12);
    }

    @Test
    void givenNonExistentOrderId_whenBartenderAcknowledges_thenReturns404() throws Exception {
        // Given the use case throws CommandeNonTrouveeException
        when(acknowledgeOrderUseCase.acknowledge(any()))
                .thenThrow(new CommandeNonTrouveeException("COMMANDE_NON_TROUVEE"));

        // When
        mockMvc.perform(post("/commandes/unknown-id/accuser-reception"))
                .andExpect(status().isNotFound());
    }
}
