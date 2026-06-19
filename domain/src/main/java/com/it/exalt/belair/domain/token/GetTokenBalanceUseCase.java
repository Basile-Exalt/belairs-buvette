package com.it.exalt.belair.domain.token;

public class GetTokenBalanceUseCase {
    private static final int DAILY_FOOD = 9;
    private static final int DAILY_DRINK = 6;

    private final TokenRepository repo;

    public GetTokenBalanceUseCase(TokenRepository repo) {
        this.repo = repo;
    }

    public TokenBalanceResult getBalance(GetTokenBalanceQuery query) {
        TokenBalanceResult stored = repo.findByFestivalgoerAndDay(query.festivalgoerId(), query.day());
        if (stored != null) return stored;
        return new TokenBalanceResult(DAILY_FOOD, DAILY_DRINK);
    }
}
