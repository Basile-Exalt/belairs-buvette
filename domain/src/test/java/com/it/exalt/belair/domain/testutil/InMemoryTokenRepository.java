package com.it.exalt.belair.domain.testutil;

import com.it.exalt.belair.domain.token.TokenBalanceResult;
import com.it.exalt.belair.domain.token.TokenRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTokenRepository implements TokenRepository {
    private final Map<String, TokenBalanceResult> store = new HashMap<>();

    @Override
    public TokenBalanceResult findByFestivalgoerAndDay(String festivalgoerId, LocalDate day) {
        return store.get(key(festivalgoerId, day));
    }

    @Override
    public void setTokens(String festivalgoerId, LocalDate day, int food, int drink) {
        if (food < 0 || drink < 0) throw new IllegalArgumentException("Negative token balance not allowed");
        store.put(key(festivalgoerId, day), new TokenBalanceResult(food, drink));
    }

    private String key(String festivalgoerId, LocalDate day) {
        return festivalgoerId + "|" + day.toString();
    }
}
