package com.it.exalt.belair.domain.token;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import com.it.exalt.belair.domain.testutil.InMemoryTokenRepository;

class TokenBalanceQueryTest {

    // Minimal test-local implementations below suffice for the Green TDD step:
    // they model the query, result, an in-memory repository and the use-case logic.

    @Test
    void givenFestivalgoerWithNoSpending_whenQueryingBalance_thenReturnsAllocatedDailyTokens() {
        // Given a festivalgoer identifier and a festival day
        String festivalgoerId = "fg-123";
        LocalDate day = LocalDate.of(2026, 6, 19);

        // When querying the token balance for that day
        // (The use case and DTOs are not implemented yet; this test will drive their creation)
        GetTokenBalanceQuery query = new GetTokenBalanceQuery(festivalgoerId, day);
        GetTokenBalanceUseCase useCase = new GetTokenBalanceUseCase(new InMemoryTokenRepository());
        TokenBalanceResult result = useCase.getBalance(query);

        // Then the festivalgoer receives 9 food tokens and 6 drink tokens per festival day
        assertThat(result.foodTokens()).isEqualTo(9);
        assertThat(result.drinkTokens()).isEqualTo(6);

        // And balances are never negative
        assertThat(result.foodTokens()).isGreaterThanOrEqualTo(0);
        assertThat(result.drinkTokens()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void givenFestivalgoerWithAllTokensSpent_whenQueryingBalance_thenReturnsZeroForBothTypes() {
        String festivalgoerId = "fg-456";
        LocalDate day = LocalDate.of(2026, 6, 19);

        GetTokenBalanceQuery query = new GetTokenBalanceQuery(festivalgoerId, day);
        InMemoryTokenRepository repo = new InMemoryTokenRepository();
        // simulate all tokens spent for that day
        repo.setTokens(festivalgoerId, day, /*food*/0, /*drink*/0);

        GetTokenBalanceUseCase useCase = new GetTokenBalanceUseCase(repo);
        TokenBalanceResult result = useCase.getBalance(query);

        assertThat(result.foodTokens()).isEqualTo(0);
        assertThat(result.drinkTokens()).isEqualTo(0);
    }

    @Test
    void givenUnspentTokensNotCarriedOver_whenQueryingNextDay_thenReturnsFreshDailyAllocation() {
        String festivalgoerId = "fg-789";
        LocalDate day1 = LocalDate.of(2026, 6, 19);
        LocalDate day2 = LocalDate.of(2026, 6, 20);

        InMemoryTokenRepository repo = new InMemoryTokenRepository();
        // day1: festivalgoer does not spend tokens (or spends partially)
        repo.setTokens(festivalgoerId, day1, /*food*/5, /*drink*/2);

        GetTokenBalanceUseCase useCase = new GetTokenBalanceUseCase(repo);
        TokenBalanceResult resultDay1 = useCase.getBalance(new GetTokenBalanceQuery(festivalgoerId, day1));
        TokenBalanceResult resultDay2 = useCase.getBalance(new GetTokenBalanceQuery(festivalgoerId, day2));

        // day2 should present the daily allocation (fresh), not carry over remaining from day1
        assertThat(resultDay1.foodTokens()).isGreaterThanOrEqualTo(0);
        assertThat(resultDay1.drinkTokens()).isGreaterThanOrEqualTo(0);

        assertThat(resultDay2.foodTokens()).isEqualTo(9);
        assertThat(resultDay2.drinkTokens()).isEqualTo(6);
    }

    @Test
    void givenAttemptToSetNegativeBalance_thenRepositoryRejectsWithIllegalArgument() {
        String festivalgoerId = "fg-neg";
        LocalDate day = LocalDate.of(2026, 6, 19);

        InMemoryTokenRepository repo = new InMemoryTokenRepository();

        // repository should reject negative balances
        assertThatThrownBy(() -> repo.setTokens(festivalgoerId, day, -1, 3))
            .isInstanceOf(IllegalArgumentException.class);
    }

    // Production artifacts moved to domain/src/main/java; tests use the domain types and a test in-memory repo.

}
