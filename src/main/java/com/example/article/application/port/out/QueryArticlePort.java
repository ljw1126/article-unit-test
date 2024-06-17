package com.example.article.application.port.out;

import com.example.article.domain.Article;

import java.util.Optional;

public interface QueryArticlePort {
    Optional<Article> getById(Long articleId);
}
