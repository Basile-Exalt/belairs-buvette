package com.it.exalt.belair.application.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import com.it.exalt.belair.domain.order.CreateOrderRequest;
import com.it.exalt.belair.domain.order.CreateOrderResponse;
import com.it.exalt.belair.domain.order.CreateOrderUseCase;
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

    public static CreateOrderResponse successResponse() {
        return new CreateOrderResponse(UUID.randomUUID().toString(), "EN_ATTENTE");
    }

    public static void mockSuccess(CreateOrderUseCase mock) {
        try {
            when(mock.create(any(CreateOrderRequest.class))).thenReturn(successResponse());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void mockUnauthorized(CreateOrderUseCase mock) {
        try {
                when(mock.create(any(CreateOrderRequest.class))).thenThrow(new UnauthorizedException("unauthorized"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void mockInvalidRequest(CreateOrderUseCase mock) {
        try {
            when(mock.create(any(CreateOrderRequest.class))).thenThrow(new com.it.exalt.belair.domain.order.InvalidOrderException("articles.empty"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
