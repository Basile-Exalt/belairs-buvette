package com.it.exalt.belair.domain.order.dto;

import com.it.exalt.belair.domain.order.Article;
import java.util.List;

public record CreerCommandeRequest(String festivalgoerId, List<Article> articles) { }
