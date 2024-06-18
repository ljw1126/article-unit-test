package com.example.article.adaptor.port.out.repository;

import com.example.article.adaptor.port.out.repository.entity.ArticleEntity;
import com.example.entity.ArticleEntityFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@SqlGroup(
        value = {
                @Sql(value = "/sql/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
                @Sql(value = "/sql/delete-all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        }
)
class ArticleRepositoryTestV2 {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void create() {
        ArticleEntity entity = ArticleEntityFixtures.entity();

        ArticleEntity result = articleRepository.save(entity);

        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("subject", "new subject")
                .hasFieldOrPropertyWithValue("content", "new content")
                .hasFieldOrPropertyWithValue("username", "username")
                .hasFieldOrProperty("createdAt")
                .hasFieldOrPropertyWithValue("modifiedAt", null);
    }

    @Test
    void update() {
        ArticleEntity entity = ArticleEntityFixtures.entity(1L);

        ArticleEntity result = articleRepository.save(entity);

        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("subject", "subject1")
                .hasFieldOrPropertyWithValue("content", "content1")
                .hasFieldOrPropertyWithValue("username", "username")
                .hasFieldOrProperty("createdAt")
                .hasFieldOrPropertyWithValue("modifiedAt", null);

    }

    @Test
    void delete() {
        articleRepository.deleteById(1L);

        Optional<ArticleEntity> result = articleRepository.findById(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void getById() {
        Optional<ArticleEntity> result = articleRepository.findById(1L);

        assertThat(result).isPresent();
        assertThat(result.get())
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("subject", "subject1")
                .hasFieldOrPropertyWithValue("content", "content1")
                .hasFieldOrPropertyWithValue("username", "username1")
                .hasFieldOrProperty("createdAt")
                .hasFieldOrPropertyWithValue("modifiedAt", null);
    }
}
