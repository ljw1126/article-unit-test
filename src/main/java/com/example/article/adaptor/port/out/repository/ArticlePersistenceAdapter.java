package com.example.article.adaptor.port.out.repository;


import com.example.article.application.port.out.CommandArticlePort;
import com.example.article.application.port.out.QueryArticlePort;
import com.example.article.domain.Article;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ArticlePersistenceAdapter implements QueryArticlePort, CommandArticlePort {

    @Override
    public Article create(Article article) {
        return null;
    }

    @Override
    public Article update(Article article) {
        return null;
    }

    @Override
    public void delete(Long articleId) {

    }

    @Override
    public Optional<Article> getById(Long articleId) {
        return Optional.empty();
    }

}
