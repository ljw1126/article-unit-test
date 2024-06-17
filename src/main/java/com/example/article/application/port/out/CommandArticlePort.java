package com.example.article.application.port.out;

import com.example.article.domain.Article;

public interface CommandArticlePort {
    Article create(Article article);

    Article update(Article article);

    void delete(Long articleId);
}
