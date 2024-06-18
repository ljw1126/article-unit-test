package com.example.entity;

import com.example.article.adaptor.port.out.repository.entity.ArticleEntity;

import java.time.LocalDateTime;

public class ArticleEntityFixtures {
    private ArticleEntityFixtures() {
    }

    public static ArticleEntity entity() {
        return new ArticleEntity.Builder()
                .subject("new subject")
                .content("new content")
                .username("username")
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static ArticleEntity entity(Long id) {
        return new ArticleEntity.Builder()
                .id(id)
                .subject("subject" + id)
                .content("content" + id)
                .username("username")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
