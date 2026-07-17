package com.it.exalt.belair.infrastructure.order;

import com.it.exalt.belair.domain.order.ArticleCategory;
import com.it.exalt.belair.domain.order.ArticleCategoryPort;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * In-memory article category adapter.
 * Provides a hardcoded mapping of known article names to their categories.
 * This serves as a simple catalogue until a persistent article catalogue is introduced.
 */
@Component
public class InMemoryArticleCategoryAdapter implements ArticleCategoryPort {

    private static final Map<String, ArticleCategory> CATEGORIES = Map.ofEntries(
            Map.entry("Eau plate", ArticleCategory.NON_ALCOHOLIC_DRINK),
            Map.entry("Eau gazeuse", ArticleCategory.NON_ALCOHOLIC_DRINK),
            Map.entry("Jus d'orange", ArticleCategory.NON_ALCOHOLIC_DRINK),
            Map.entry("Coca-Cola", ArticleCategory.NON_ALCOHOLIC_DRINK),
            Map.entry("Limonade", ArticleCategory.NON_ALCOHOLIC_DRINK),
            Map.entry("Bière", ArticleCategory.NORMAL_ALCOHOLIC_DRINK),
            Map.entry("Bière Pale Ale", ArticleCategory.NORMAL_ALCOHOLIC_DRINK),
            Map.entry("Vin rouge", ArticleCategory.NORMAL_ALCOHOLIC_DRINK),
            Map.entry("Vin blanc", ArticleCategory.NORMAL_ALCOHOLIC_DRINK),
            Map.entry("Cidre", ArticleCategory.NORMAL_ALCOHOLIC_DRINK),
            Map.entry("Mojito", ArticleCategory.NORMAL_ALCOHOLIC_DRINK),
            Map.entry("Champagne", ArticleCategory.PREMIUM_ALCOHOLIC_DRINK),
            Map.entry("Whisky", ArticleCategory.PREMIUM_ALCOHOLIC_DRINK),
            Map.entry("Cocktail premium", ArticleCategory.PREMIUM_ALCOHOLIC_DRINK),
            Map.entry("Chips", ArticleCategory.SNACK),
            Map.entry("Cacahuetes", ArticleCategory.SNACK),
            Map.entry("Olives", ArticleCategory.SNACK),
            Map.entry("Burger", ArticleCategory.MEAL),
            Map.entry("Pizza", ArticleCategory.MEAL),
            Map.entry("Wrap", ArticleCategory.MEAL)
    );

    @Override
    public ArticleCategory getCategory(String articleId) {
        ArticleCategory category = CATEGORIES.get(articleId);
        if (category == null) {
            throw new IllegalArgumentException("Unknown article: " + articleId);
        }
        return category;
    }
}
