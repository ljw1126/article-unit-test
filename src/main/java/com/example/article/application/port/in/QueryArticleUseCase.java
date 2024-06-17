package com.example.article.application.port.in;

import com.example.article.domain.Article;

public interface QueryArticleUseCase {
    Article getById(Long articleId);
}
