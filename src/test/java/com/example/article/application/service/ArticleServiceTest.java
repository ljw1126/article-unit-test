package com.example.article.application.service;

import com.example.article.adaptor.port.in.dto.ArticleDto;
import com.example.article.domain.Article;
import com.example.article.mock.FakeArticlePersistenceAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static com.example.article.adaptor.port.in.dto.ArticleDto.CreateArticleRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleServiceTest {

    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        FakeArticlePersistenceAdapter fakeArticlePersistenceAdapter = new FakeArticlePersistenceAdapter();

        this.articleService = new ArticleService(fakeArticlePersistenceAdapter, fakeArticlePersistenceAdapter);

        Article article1 = new Article.Builder()
                .subject("subject1")
                .content("content1")
                .username("tester1")
                .createdAt(LocalDateTime.now())
                .build();

        Article article2 = new Article.Builder()
                .subject("subject2")
                .content("content2")
                .username("tester2")
                .createdAt(LocalDateTime.now())
                .build();

        fakeArticlePersistenceAdapter.create(article1);
        fakeArticlePersistenceAdapter.create(article2);
    }

    @Test
    void create() {
        CreateArticleRequest request = new CreateArticleRequest("subject", "content", "tester");

        Article result = this.articleService.create(request);

        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("subject", "subject")
                .hasFieldOrPropertyWithValue("content", "content")
                .hasFieldOrPropertyWithValue("username", "tester")
                .hasFieldOrProperty("createdAt");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidParameters")
    void create시_파라미터_누락한경우_예외를_던진다(String title, String subject, String content, String username) {
        CreateArticleRequest request = new CreateArticleRequest(subject, content, username);

        assertThatThrownBy(() -> this.articleService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    static Stream<Arguments> invalidParameters() {
        return Stream.of(
                Arguments.of("subject null", null, "content", "user"),
                Arguments.of("subject empty", "", "content", "user"),
                Arguments.of("content null", "subject", null, "user"),
                Arguments.of("content empty", "subject", "", "user"),
                Arguments.of("username null", "subject", "content", null)
        );
    }

    @Test
    void update() {
        Long articleId = 1L;
        ArticleDto.UpdateArticleRequest request
                = new ArticleDto.UpdateArticleRequest(articleId, "updated-subject", "updated-content", "tester1");

        Article result = this.articleService.update(request);

        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void update시_Article이없는경우_예외를던진다() {
        Long articleId = 99L;
        ArticleDto.UpdateArticleRequest request
                = new ArticleDto.UpdateArticleRequest(articleId, "updated-subject", "updated-content", "tester1");

        assertThatThrownBy(() -> this.articleService.update(request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void delete() {
        assertThatNoException()
                .isThrownBy(() -> articleService.delete(1L));
    }

    @Test
    void delete시_Article존재하지않으면_예외던진다() {
        assertThatThrownBy(() -> articleService.delete(99L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void getById() {
        Article article = articleService.getById(1L);
        assertThat(article)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("subject", "subject1")
                .hasFieldOrPropertyWithValue("content", "content1")
                .hasFieldOrPropertyWithValue("username", "tester1");
    }

    @Test
    void getById로_Article이없으면_예외를던진다() {
        assertThatThrownBy(() -> articleService.getById(99L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
