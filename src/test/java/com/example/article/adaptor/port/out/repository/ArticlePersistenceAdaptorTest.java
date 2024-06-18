package com.example.article.adaptor.port.out.repository;

import com.example.article.adaptor.port.out.repository.entity.ArticleEntity;
import com.example.article.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class ArticlePersistenceAdaptorTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticlePersistenceAdaptor adaptor;


    @Nested
    @DisplayName("Article을 생성한다")
    class CreateArticle {
        @Captor
        ArgumentCaptor<ArticleEntity> captor;

        private final Article article = new Article.Builder()
                .subject("subject")
                .content("content")
                .username("tester")
                .createdAt(LocalDateTime.now())
                .build();

        @Test
        void create() {
            ArticleEntity articleEntity = ArticleEntity.from(article);
            ReflectionTestUtils.setField(articleEntity, "id", 1L); // setter 없으므로

            given(articleRepository.save(any()))
                    .willReturn(articleEntity);

            Article result = adaptor.create(article);

            assertThat(result)
                    .hasFieldOrPropertyWithValue("id", 1L)
                    .hasFieldOrPropertyWithValue("subject", "subject")
                    .hasFieldOrPropertyWithValue("content", "content")
                    .hasFieldOrPropertyWithValue("username", "tester")
                    .hasFieldOrProperty("createdAt");
        }

        @Test
        void 저장했을때_ArgumentCapture검증한다() {
            ArticleEntity articleEntity = ArticleEntity.from(article);
            ReflectionTestUtils.setField(articleEntity, "id", 1L); // setter 없으므로

            given(articleRepository.save(any()))
                    .willReturn(articleEntity);

            adaptor.create(article);

            verify(articleRepository).save(captor.capture());
            assertThat(captor.getValue())
                    .hasFieldOrPropertyWithValue("id", null)
                    .hasFieldOrPropertyWithValue("subject", "subject")
                    .hasFieldOrPropertyWithValue("content", "content")
                    .hasFieldOrPropertyWithValue("username", "tester")
                    .hasFieldOrProperty("createdAt")
                    .hasFieldOrPropertyWithValue("modifiedAt", null);
        }
    }

    @Nested
    @DisplayName("Article을 수정한다")
    class UpdateArticle {
        @Captor
        ArgumentCaptor<ArticleEntity> captor;

        private final Article article = new Article.Builder()
                .id(1L)
                .subject("updated subject")
                .content("updated content")
                .username("tester")
                .createdAt(LocalDateTime.now())
                .build();

        @Test
        void update() {
            LocalDateTime modifiedAt = LocalDateTime.now();
            ReflectionTestUtils.setField(article, "modifiedAt", modifiedAt);

            ArticleEntity articleEntity = ArticleEntity.from(article);
            ReflectionTestUtils.setField(articleEntity, "modifiedAt", modifiedAt);

            given(articleRepository.save(any()))
                    .willReturn(articleEntity);

            Article updated = adaptor.update(article);

            verify(articleRepository).save(captor.capture()); // captor 입장에서 entity 생성시 modifiedAt이 없으므로 null
            assertThat(updated)
                    .hasFieldOrPropertyWithValue("id", 1L)
                    .hasFieldOrPropertyWithValue("subject", "updated subject")
                    .hasFieldOrPropertyWithValue("content", "updated content")
                    .hasFieldOrPropertyWithValue("username", "tester")
                    .hasFieldOrProperty("createdAt")
                    .hasFieldOrPropertyWithValue("modifiedAt", modifiedAt);
        }

        @Test
        void 수정시_arguments를검증한다() {
            LocalDateTime modifiedAt = LocalDateTime.now();
            ArticleEntity articleEntity = ArticleEntity.from(article);
            ReflectionTestUtils.setField(articleEntity, "modifiedAt", modifiedAt);

            given(articleRepository.save(any()))
                    .willReturn(articleEntity);

            adaptor.update(article);

            verify(articleRepository).save(captor.capture()); // captor 입장에서 entity 생성시 modifiedAt이 없으므로 null
            assertThat(captor.getValue())
                    .hasFieldOrPropertyWithValue("id", 1L)
                    .hasFieldOrPropertyWithValue("subject", "updated subject")
                    .hasFieldOrPropertyWithValue("content", "updated content")
                    .hasFieldOrPropertyWithValue("username", "tester")
                    .hasFieldOrProperty("createdAt")
                    .hasFieldOrPropertyWithValue("modifiedAt", null);
        }
    }


    @Test
    void delete_article을_삭제한다() {
        willDoNothing()
                .given(articleRepository).deleteById(any());

        adaptor.delete(1L);

        verify(articleRepository).deleteById(1L);
    }

    @Test
    void getById() {
        ArticleEntity articleEntity = new ArticleEntity.Builder()
                .id(1L)
                .subject("subject")
                .content("content")
                .username("tester")
                .createdAt(LocalDateTime.now())
                .build();
        given(articleRepository.findById(any()))
                .willReturn(Optional.of(articleEntity));

        Optional<Article> result = adaptor.getById(1L);

        assertThat(result).isPresent();
        assertThat(result.get())
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("subject", "subject")
                .hasFieldOrPropertyWithValue("content", "content")
                .hasFieldOrPropertyWithValue("username", "tester")
                .hasFieldOrProperty("createdAt");
    }
}
