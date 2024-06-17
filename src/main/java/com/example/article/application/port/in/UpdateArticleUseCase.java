package com.example.article.application.port.in;

import com.example.article.adaptor.port.in.dto.ArticleDto;
import com.example.article.domain.Article;

public interface UpdateArticleUseCase {
    Article update(ArticleDto.UpdateArticleRequest request);
}
