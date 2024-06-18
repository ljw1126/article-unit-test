package com.example.article.adaptor.port.out.repository;


import com.example.article.adaptor.port.out.repository.entity.ArticleEntity;
import com.example.article.application.port.out.CommandArticlePort;
import com.example.article.application.port.out.QueryArticlePort;
import com.example.article.domain.Article;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ArticlePersistenceAdaptor implements QueryArticlePort, CommandArticlePort {

    private final ArticleRepository articleRepository;

    public ArticlePersistenceAdaptor(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Article create(Article article) {
        ArticleEntity saved = articleRepository.save(ArticleEntity.from(article));
        return saved.toDomain();
    }

    @Override
    public Article update(Article article) {
        ArticleEntity updated = articleRepository.save(ArticleEntity.from(article));
        return updated.toDomain();
    }

    @Override
    public void delete(Long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Override
    public Optional<Article> getById(Long articleId) {
        Optional<ArticleEntity> data = articleRepository.findById(articleId);
        return data.map(ArticleEntity::toDomain);
    }
}
