package com.example.article.mock;

import com.example.article.application.port.out.CommandArticlePort;
import com.example.article.application.port.out.QueryArticlePort;
import com.example.article.domain.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeArticlePersistenceAdaptor implements CommandArticlePort, QueryArticlePort {
    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<Article> data = new ArrayList<>();

    @Override
    public Article create(Article article) {
        Article newArticle = new Article.Builder()
                .id(autoGeneratedId.incrementAndGet())
                .subject(article.getSubject())
                .content(article.getContent())
                .username(article.getUsername())
                .createdAt(article.getCreatedAt())
                .modifiedAt(article.getModifiedAt())
                .build();

        data.add(newArticle);
        return newArticle;
    }

    @Override
    public Article update(Article article) {
        data.removeIf(d -> d.verify(article));
        data.add(article);
        return article;
    }

    @Override
    public void delete(Long articleId) {
        Article article = getById(articleId).orElseThrow(NoSuchElementException::new);
        data.remove(article);
    }

    @Override
    public Optional<Article> getById(Long articleId) {
        return data.stream().filter(item -> item.getId().equals(articleId)).findFirst();
    }
}