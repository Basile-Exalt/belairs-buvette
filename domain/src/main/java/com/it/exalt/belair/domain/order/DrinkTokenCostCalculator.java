package com.it.exalt.belair.domain.order;

public class DrinkTokenCostCalculator {

    public int tokensFor(DrinkCategory category, int quantity) {
        if (category == null || quantity <= 0) return 0;

        switch (category) {
            case NON_ALCOHOLIC:
                return 0;
            case ALCOHOLIC_NORMAL:
                return quantity * 1;
            case ALCOHOLIC_PREMIUM:
                return quantity * 2;
            default:
                return 0;
        }
    }
}
