package com.example.article.domain;

import com.example.domain.ArticleFixtures;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.example.article.adaptor.port.in.dto.ArticleDto.UpdateArticleRequest;

class ArticleTest {
    private static final LocalDateTime CREATED_AT = LocalDateTime.parse("2024-01-01T10:20:30");

    @Test
    void Article_생성한다() {
        Article article = ArticleFixtures.article();

        BDDAssertions.then(article)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("subject", "subject")
                .hasFieldOrPropertyWithValue("content", "content")
                .hasFieldOrPropertyWithValue("username", "tester")
                .hasFieldOrPropertyWithValue("createdAt", CREATED_AT);
    }

    @Test
    void Article_수정한다() {
        Article article = ArticleFixtures.article();
        UpdateArticleRequest request = new UpdateArticleRequest(1L, "updated subject", "updated content", "tester");

        LocalDateTime modifiedAt = LocalDateTime.now();
        Article updated = article.update(request, () -> modifiedAt);

        BDDAssertions.then(updated)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("subject", "updated subject")
                .hasFieldOrPropertyWithValue("content", "updated content")
                .hasFieldOrPropertyWithValue("username", "tester")
                .hasFieldOrPropertyWithValue("createdAt", CREATED_AT)
                .hasFieldOrPropertyWithValue("modifiedAt", modifiedAt);
    }

    @Test
    void Article_수정할때_username이다르면_예외를던진다() {
        Article article = ArticleFixtures.article();
        UpdateArticleRequest request = new UpdateArticleRequest(1L, "updated subject", "updated content", "other username");

        Assertions.assertThatThrownBy(() -> article.update(request, () -> null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
