package com.it.exalt.belair.domain.token;

import java.time.LocalDate;

public interface TokenRepository {
    TokenBalanceResult findByFestivalgoerAndDay(String festivalgoerId, LocalDate day);
    void setTokens(String festivalgoerId, LocalDate day, int food, int drink);
}
