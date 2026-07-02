package com.it.exalt.belair.domain.testutil;

import com.it.exalt.belair.domain.order.ArticleCategory;
import com.it.exalt.belair.domain.order.ArticleCategoryPort;

import java.util.HashMap;
import java.util.Map;

public class InMemoryArticleCategoryAdapter implements ArticleCategoryPort {

    private final Map<String, ArticleCategory> categories = new HashMap<>();

    public void register(String articleId, ArticleCategory category) {
        categories.put(articleId, category);
    }

    @Override
    public ArticleCategory getCategory(String articleId) {
        ArticleCategory category = categories.get(articleId);
        if (category == null) {
            throw new IllegalArgumentException("Unknown article: " + articleId);
        }
        return category;
    }
}
