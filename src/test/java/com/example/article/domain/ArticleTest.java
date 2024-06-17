package com.example.article.domain;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class ArticleTest {

    @Test
    void Article_생성한다() {
        LocalDateTime createdAt = LocalDateTime.now();
        var article = new Article.Builder()
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
        var article = new Article.Builder()
                .id(1L)
                .subject("subject")
                .content("content")
                .username("tester")
                .createdAt(createdAt)
                .build();

        LocalDateTime modifiedAt = LocalDateTime.now();
        Article updated = article.update("updated subject", "updated content", modifiedAt);

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
