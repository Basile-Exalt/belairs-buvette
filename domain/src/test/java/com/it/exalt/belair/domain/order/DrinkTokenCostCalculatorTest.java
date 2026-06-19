package com.it.exalt.belair.domain.order;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DrinkTokenCostCalculatorTest {

    @Test
    void shouldChargeZeroTokensForNonAlcoholic() {
        DrinkTokenCostCalculator calc = new DrinkTokenCostCalculator();
        int tokens = calc.tokensFor(DrinkCategory.NON_ALCOHOLIC, 1);
        assertEquals(0, tokens);
    }

    @Test
    void shouldChargeOneTokenForNormalAlcoholic() {
        DrinkTokenCostCalculator calc = new DrinkTokenCostCalculator();
        int tokens = calc.tokensFor(DrinkCategory.ALCOHOLIC_NORMAL, 1);
        assertEquals(1, tokens);
    }

    @Test
    void shouldChargeTwoTokensForPremiumAlcoholic() {
        DrinkTokenCostCalculator calc = new DrinkTokenCostCalculator();
        int tokens = calc.tokensFor(DrinkCategory.ALCOHOLIC_PREMIUM, 1);
        assertEquals(2, tokens);
    }

    @Test
    void shouldSumTokensForMultipleItems() {
        DrinkTokenCostCalculator calc = new DrinkTokenCostCalculator();
        int tokens = calc.tokensFor(DrinkCategory.ALCOHOLIC_NORMAL, 2) +
                     calc.tokensFor(DrinkCategory.ALCOHOLIC_PREMIUM, 1) +
                     calc.tokensFor(DrinkCategory.NON_ALCOHOLIC, 3);
        // 2 * 1 + 1 * 2 + 3 * 0 = 4
        assertEquals(4, tokens);
    }

    // Test-local implementations: minimal enum and calculator to make this test green.
    // These are intentionally inside the test class as a TDD "green" step.
    // Calculator and category extracted to production code under domain module.
}
