package com.example.article.domain;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.example.article.adaptor.port.in.dto.ArticleDto.UpdateArticleRequest;

class ArticleTest {

    @Test
    void Article_생성한다() {
        LocalDateTime createdAt = LocalDateTime.now();
        Article article = new Article.Builder()
                .id(1L)
                .subject("subject")
                .content("content")
                .username("tester")
                .createdAt(createdAt)
                .build();

        BDDAssertions.then(article)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("subject", "subject")
                .hasFieldOrPropertyWithValue("content", "content")
                .hasFieldOrPropertyWithValue("username", "tester")
                .hasFieldOrPropertyWithValue("createdAt", createdAt);
    }

    @Test
    void Article_수정한다() {
        LocalDateTime createdAt = LocalDateTime.now();
        Article article = new Article.Builder()
                .id(1L)
                .subject("subject")
                .content("content")
                .username("tester")
                .createdAt(createdAt)
                .build();

        UpdateArticleRequest request = new UpdateArticleRequest(1L, "updated subject", "updated content", "tester");

        LocalDateTime modifiedAt = LocalDateTime.now();
        Article updated = article.update(request, () -> modifiedAt);

        BDDAssertions.then(updated)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("subject", "updated subject")
                .hasFieldOrPropertyWithValue("content", "updated content")
                .hasFieldOrPropertyWithValue("username", "tester")
                .hasFieldOrPropertyWithValue("createdAt", createdAt)
                .hasFieldOrPropertyWithValue("modifiedAt", modifiedAt);
    }
}
