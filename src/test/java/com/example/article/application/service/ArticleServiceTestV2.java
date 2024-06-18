package com.example.article.application.service;

import com.example.article.adaptor.port.in.dto.ArticleDto;
import com.example.article.application.port.out.CommandArticlePort;
import com.example.article.application.port.out.QueryArticlePort;
import com.example.article.domain.Article;
import com.example.common.exception.AccessDeniedException;
import com.example.common.exception.ResourceNotFoundException;
import com.example.common.service.port.ClockHolder;
import com.example.domain.ArticleFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static com.example.article.adaptor.port.in.dto.ArticleDto.CreateArticleRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;


class ArticleServiceTestV2 {


    private ArticleService articleService;

    private CommandArticlePort commandArticlePort;

    private QueryArticlePort queryArticlePort;

    private ClockHolder clockHolder;

    @BeforeEach
    void setUp() {
        commandArticlePort = Mockito.mock(CommandArticlePort.class);
        queryArticlePort = Mockito.mock(QueryArticlePort.class);
        clockHolder = Mockito.mock(ClockHolder.class);

        articleService = new ArticleService(commandArticlePort, queryArticlePort, clockHolder);
    }

    @Nested
    @DisplayName("Article 생성")
    class CreateArticle {
        @Test
        void create() {
            Article article = ArticleFixtures.article();

            BDDMockito.given(commandArticlePort.create(any()))
                    .willReturn(article);

            CreateArticleRequest request = new CreateArticleRequest("subject", "content", "tester");
            Article result = articleService.create(request);

            assertThat(result)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("id", 1L)
                    .hasFieldOrPropertyWithValue("subject", "subject")
                    .hasFieldOrPropertyWithValue("content", "content")
                    .hasFieldOrPropertyWithValue("username", "tester")
                    .hasFieldOrProperty("createdAt");
            ;
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidParameters")
        void 파라미터_누락한경우_예외를_던진다(String title, String subject, String content, String username) {
            CreateArticleRequest request = new CreateArticleRequest(subject, content, username);

            assertThatThrownBy(() -> articleService.create(request))
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
    }

    @Nested
    @DisplayName("Article 수정")
    class UpdateArticle {
        @Test
        void update() {
            Article article = ArticleFixtures.article();
            given(queryArticlePort.getById(any()))
                    .willReturn(Optional.of(article));

            LocalDateTime modifiedAt = LocalDateTime.now();
            Article updatedArticle = new Article.Builder()
                    .id(article.getId())
                    .subject("updated subject")
                    .content("updated content")
                    .username(article.getUsername())
                    .createdAt(article.getCreatedAt())
                    .modifiedAt(modifiedAt)
                    .build();
            given(commandArticlePort.update(any()))
                    .willReturn(updatedArticle);

            ArticleDto.UpdateArticleRequest request
                    = new ArticleDto.UpdateArticleRequest(1L, "updated subject", "updated content", "tester");
            Article result = articleService.update(request);

            assertThat(result)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("id", 1L)
                    .hasFieldOrPropertyWithValue("subject", "updated subject")
                    .hasFieldOrPropertyWithValue("content", "updated content")
                    .hasFieldOrPropertyWithValue("username", "tester")
                    .hasFieldOrPropertyWithValue("createdAt", article.getCreatedAt())
                    .hasFieldOrPropertyWithValue("modifiedAt", modifiedAt);
            ;
        }

        @Test
        void Article이없는경우_예외를던진다() {
            given(queryArticlePort.getById(any()))
                    .willReturn(Optional.empty());

            ArticleDto.UpdateArticleRequest request
                    = new ArticleDto.UpdateArticleRequest(99L, "updated-subject", "updated-content", "tester1");

            assertThatThrownBy(() -> articleService.update(request))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void username다르면_예외를던진다() {
            Article article = ArticleFixtures.article();
            given(queryArticlePort.getById(any()))
                    .willReturn(Optional.of(article));

            ArticleDto.UpdateArticleRequest request
                    = new ArticleDto.UpdateArticleRequest(99L, "updated-subject", "updated-content", "other username");

            assertThatThrownBy(() -> articleService.update(request))
                    .isInstanceOf(AccessDeniedException.class);
        }
    }

    @Nested
    @DisplayName("Article 삭제")
    class DeleteArticle {
        @Test
        void delete() {
            willDoNothing()
                    .given(commandArticlePort).delete(any());

            articleService.delete(1L);

            verify(commandArticlePort).delete(1L);
        }
    }

    @Nested
    @DisplayName("Article 한 건 조회")
    class GetArticle {
        @Test
        void getById() {
            Article article = ArticleFixtures.article();

            given(queryArticlePort.getById(ArgumentMatchers.any()))
                    .willReturn(Optional.of(article));

            Article result = articleService.getById(1L);

            assertThat(result)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue("id", 1L)
                    .hasFieldOrPropertyWithValue("subject", "subject")
                    .hasFieldOrPropertyWithValue("content", "content")
                    .hasFieldOrPropertyWithValue("username", "tester")
                    .hasFieldOrProperty("createdAt");
        }

        @Test
        void Article이없으면_예외를던진다() {
            given(queryArticlePort.getById(any()))
                    .willReturn(Optional.empty());

            assertThatThrownBy(() -> articleService.getById(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
