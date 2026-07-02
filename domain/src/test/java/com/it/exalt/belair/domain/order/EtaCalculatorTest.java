package com.it.exalt.belair.domain.order;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EtaCalculatorTest {

    private final EtaCalculator calculator = new EtaCalculator();

    // -------------------------------------------------------------------------
    // Non-alcoholic drinks only: 1 min per distinct type
    // -------------------------------------------------------------------------

    @Test
    void givenSingleNonAlcoholicDrink_whenComputingEta_thenOneMinute() {
        // Given one distinct non-alcoholic type
        // When computing ETA
        int eta = calculator.compute(List.of(ArticleCategory.NON_ALCOHOLIC_DRINK));
        // Then 1 minute
        assertThat(eta).isEqualTo(1);
    }

    @Test
    void givenThreeNonAlcoholicArticleLines_whenComputingEta_thenThreeMinutes() {
        // Given 3 article lines all belonging to the non-alcoholic drink category
        // (e.g. water, juice, soda — each is a separate article line)
        List<ArticleCategory> categories = List.of(
                ArticleCategory.NON_ALCOHOLIC_DRINK,
                ArticleCategory.NON_ALCOHOLIC_DRINK,
                ArticleCategory.NON_ALCOHOLIC_DRINK);
        // When computing ETA
        int eta = calculator.compute(categories);
        // Then 3 minutes (1 per article line in this category)
        assertThat(eta).isEqualTo(3);
    }

    // -------------------------------------------------------------------------
    // Normal alcoholic drinks: 2 min per distinct type
    // -------------------------------------------------------------------------

    @Test
    void givenOneNormalAlcoholicDrink_whenComputingEta_thenTwoMinutes() {
        int eta = calculator.compute(List.of(ArticleCategory.NORMAL_ALCOHOLIC_DRINK));
        assertThat(eta).isEqualTo(2);
    }

    @Test
    void givenTwoNormalAlcoholicDrinkTypes_whenComputingEta_thenFourMinutes() {
        int eta = calculator.compute(List.of(
                ArticleCategory.NORMAL_ALCOHOLIC_DRINK,
                ArticleCategory.NORMAL_ALCOHOLIC_DRINK));
        assertThat(eta).isEqualTo(4);
    }

    // -------------------------------------------------------------------------
    // Premium alcoholic drinks: 3 min per distinct type
    // -------------------------------------------------------------------------

    @Test
    void givenOnePremiumAlcoholicDrink_whenComputingEta_thenThreeMinutes() {
        int eta = calculator.compute(List.of(ArticleCategory.PREMIUM_ALCOHOLIC_DRINK));
        assertThat(eta).isEqualTo(3);
    }

    // -------------------------------------------------------------------------
    // Mixed drink orders: sum of all drink type times
    // -------------------------------------------------------------------------

    @Test
    void givenMixedDrinkOrder_whenComputingEta_thenSumOfAllDrinkTypeTimes() {
        // 1 non-alc (1 min) + 1 normal (2 min) + 1 premium (3 min) = 6 min
        List<ArticleCategory> categories = List.of(
                ArticleCategory.NON_ALCOHOLIC_DRINK,
                ArticleCategory.NORMAL_ALCOHOLIC_DRINK,
                ArticleCategory.PREMIUM_ALCOHOLIC_DRINK);
        int eta = calculator.compute(categories);
        assertThat(eta).isEqualTo(6);
    }

    // -------------------------------------------------------------------------
    // Snacks: 2 min per distinct snack type
    // -------------------------------------------------------------------------

    @Test
    void givenOneSnackType_whenComputingEta_thenTwoMinutes() {
        int eta = calculator.compute(List.of(ArticleCategory.SNACK));
        assertThat(eta).isEqualTo(2);
    }

    @Test
    void givenThreeSnackTypes_whenComputingEta_thenSixMinutes() {
        List<ArticleCategory> categories = List.of(
                ArticleCategory.SNACK, ArticleCategory.SNACK, ArticleCategory.SNACK);
        int eta = calculator.compute(categories);
        assertThat(eta).isEqualTo(6);
    }

    // -------------------------------------------------------------------------
    // Meals: 10 min per type + longest single drink type time
    // -------------------------------------------------------------------------

    @Test
    void givenOneMealAndNoDrinks_whenComputingEta_thenTenMinutes() {
        int eta = calculator.compute(List.of(ArticleCategory.MEAL));
        assertThat(eta).isEqualTo(10);
    }

    @Test
    void givenOneMealAndOneNormalDrink_whenComputingEta_thenTenPlusTwoMinutes() {
        // meal (10) + longest drink type = 2 (normal) = 12 min
        List<ArticleCategory> categories = List.of(
                ArticleCategory.MEAL,
                ArticleCategory.NORMAL_ALCOHOLIC_DRINK);
        int eta = calculator.compute(categories);
        assertThat(eta).isEqualTo(12);
    }

    @Test
    void givenOneMealAndMixedDrinks_whenComputingEta_thenMealDominatesWithLongestDrinkTime() {
        // meal (10) + longest drink type = 3 (premium) = 13 min
        // drink sum = 1 + 2 + 3 = 6 min
        // eta = max(13, 6) = 13
        List<ArticleCategory> categories = List.of(
                ArticleCategory.MEAL,
                ArticleCategory.NON_ALCOHOLIC_DRINK,
                ArticleCategory.NORMAL_ALCOHOLIC_DRINK,
                ArticleCategory.PREMIUM_ALCOHOLIC_DRINK);
        int eta = calculator.compute(categories);
        assertThat(eta).isEqualTo(13);
    }

    @Test
    void givenTwoMealTypesAndNormalDrink_whenComputingEta_thenTwentyTwoMinutes() {
        // 2 meals (20) + longest drink = 2 (normal) = 22 min
        List<ArticleCategory> categories = List.of(
                ArticleCategory.MEAL, ArticleCategory.MEAL,
                ArticleCategory.NORMAL_ALCOHOLIC_DRINK);
        int eta = calculator.compute(categories);
        assertThat(eta).isEqualTo(22);
    }

    // -------------------------------------------------------------------------
    // Meals and drinks prepared in parallel: max(drink+snack, meal)
    // -------------------------------------------------------------------------

    @Test
    void givenMealWithNormalDrinkAndSnack_whenComputingEta_thenMealEtaDominates() {
        // drink+snack: 2 (normal) + 2 (snack) = 4 min
        // meal: 10 + 2 (longest = normal) = 12 min
        // eta = max(4, 12) = 12
        List<ArticleCategory> categories = List.of(
                ArticleCategory.MEAL,
                ArticleCategory.NORMAL_ALCOHOLIC_DRINK,
                ArticleCategory.SNACK);
        int eta = calculator.compute(categories);
        assertThat(eta).isEqualTo(12);
    }

    @Test
    void givenEmptyOrder_whenComputingEta_thenZeroMinutes() {
        int eta = calculator.compute(List.of());
        assertThat(eta).isEqualTo(0);
    }
}
