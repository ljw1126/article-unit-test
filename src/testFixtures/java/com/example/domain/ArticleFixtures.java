package com.example.domain;


import com.example.article.domain.Article;

import java.time.LocalDateTime;

public class ArticleFixtures {

    private ArticleFixtures() {
    }

    public static Article article(Long id) {
        return new Article.Builder()
                .id(id)
                .subject("subject" + id)
                .content("content" + id)
                .username("tester" + id)
                .createdAt(LocalDateTime.parse("2024-01-01T10:20:30").plusDays(id))
                .build();
    }

    public static Article article() {
        return new Article.Builder()
                .id(1L)
                .subject("subject")
                .content("content")
                .username("tester")
                .createdAt(LocalDateTime.parse("2024-01-01T10:20:30"))
                .build();
    }
}
