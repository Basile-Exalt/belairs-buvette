package com.it.exalt.belair.application.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import com.it.exalt.belair.domain.order.dto.CreerCommandeRequest;
import com.it.exalt.belair.domain.order.dto.CreerCommandeResponse;
import com.it.exalt.belair.domain.order.usecase.CreerCommandeUseCase;
import com.it.exalt.belair.domain.order.exception.StockInsuffisantException;
import com.it.exalt.belair.application.UnauthorizedException;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public final class TestUtils {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    private TestUtils() {}

    public static byte[] toJson(Object o) throws Exception {
        return MAPPER.writeValueAsBytes(o);
    }

    public static <T> T fromResult(MvcResult result, Class<T> cls) throws Exception {
        return MAPPER.readValue(result.getResponse().getContentAsByteArray(), cls);
    }

    public static CreerCommandeResponse successResponse() {
        return new CreerCommandeResponse(UUID.randomUUID().toString(), "EN_ATTENTE");
    }

    public static void mockSuccess(CreerCommandeUseCase mock) {
        try {
            when(mock.create(any(CreerCommandeRequest.class))).thenReturn(successResponse());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void mockUnauthorized(CreerCommandeUseCase mock) {
        try {
                when(mock.create(any(CreerCommandeRequest.class))).thenThrow(new UnauthorizedException("unauthorized"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void mockInvalidRequest(CreerCommandeUseCase mock) {
        try {
            when(mock.create(any(CreerCommandeRequest.class))).thenThrow(new StockInsuffisantException("articles.empty"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
