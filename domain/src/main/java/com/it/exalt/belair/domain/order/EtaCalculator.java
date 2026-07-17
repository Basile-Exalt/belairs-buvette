package com.it.exalt.belair.domain.order;

import java.util.Collection;

/**
 * Pure domain service that computes the estimated preparation time (in minutes) for an order.
 *
 * Rules (from FEATURES.md):
 * <ul>
 *   <li>Non-alcoholic drinks: 1 minute per distinct article type</li>
 *   <li>Normal alcoholic drinks: 2 minutes per distinct article type</li>
 *   <li>Premium alcoholic drinks: 3 minutes per distinct article type</li>
 *   <li>Mixed drink orders: sum of individual drink-type times</li>
 *   <li>Snacks: 2 minutes per distinct snack type</li>
 *   <li>Meals: 10 minutes per distinct meal type + longest single drink-type time in the order</li>
 *   <li>Meals and drinks can be prepared in parallel; ETA = max(drink+snack time, meal time)</li>
 * </ul>
 */
public class EtaCalculator {

    private static final int MINUTES_NON_ALCOHOLIC = 1;
    private static final int MINUTES_NORMAL_ALCOHOLIC = 2;
    private static final int MINUTES_PREMIUM_ALCOHOLIC = 3;
    private static final int MINUTES_SNACK = 2;
    private static final int MINUTES_MEAL = 10;

    /**
     * Computes the ETA for an order given the list of article categories it contains.
     * Each element in {@code categories} represents one article line from the order.
     * Multiple lines belonging to the same category each contribute to the count for that category.
     * For example, three non-alcoholic article lines produce an ETA of 3 minutes.
     *
     * @param categories one entry per article line in the order
     * @return estimated preparation time in minutes
     */
    public int compute(Collection<ArticleCategory> categories) {
        if (categories == null || categories.isEmpty()) {
            return 0;
        }

        long nonAlcoholicCount = countOf(categories, ArticleCategory.NON_ALCOHOLIC_DRINK);
        long normalAlcoholicCount = countOf(categories, ArticleCategory.NORMAL_ALCOHOLIC_DRINK);
        long premiumAlcoholicCount = countOf(categories, ArticleCategory.PREMIUM_ALCOHOLIC_DRINK);
        long snackCount = countOf(categories, ArticleCategory.SNACK);
        long mealCount = countOf(categories, ArticleCategory.MEAL);

        int drinkEta = (int) (nonAlcoholicCount * MINUTES_NON_ALCOHOLIC
                + normalAlcoholicCount * MINUTES_NORMAL_ALCOHOLIC
                + premiumAlcoholicCount * MINUTES_PREMIUM_ALCOHOLIC);

        int snackEta = (int) (snackCount * MINUTES_SNACK);

        int maxDrinkTypeTime = maxSingleDrinkTypeTime(premiumAlcoholicCount > 0,
                normalAlcoholicCount > 0, nonAlcoholicCount > 0);

        int mealEta = (int) (mealCount * MINUTES_MEAL) + maxDrinkTypeTime;

        // Meals and drinks can be prepared in parallel; take the longest
        return Math.max(drinkEta + snackEta, mealEta);
    }

    private long countOf(Collection<ArticleCategory> categories, ArticleCategory target) {
        return categories.stream().filter(target::equals).count();
    }

    private int maxSingleDrinkTypeTime(boolean hasPremium, boolean hasNormal, boolean hasNonAlcoholic) {
        if (hasPremium) return MINUTES_PREMIUM_ALCOHOLIC;
        if (hasNormal) return MINUTES_NORMAL_ALCOHOLIC;
        if (hasNonAlcoholic) return MINUTES_NON_ALCOHOLIC;
        return 0;
    }
}
