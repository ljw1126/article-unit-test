package com.example.article.adaptor.port.out.repository;

import com.example.article.adaptor.port.out.repository.entity.ArticleEntity;
import com.example.entity.ArticleEntityFixtures;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        //articleRepository.save(new ArticleEntity("subject1", "content1", "username1", LocalDateTime.now()));
        //articleRepository.save(new ArticleEntity("subject2", "content2", "username2", LocalDateTime.now()));

        // id null 아닌 경우 예외 반환 (org.hibernate.PersistentObjectException: detached entity passed to persist)
        entityManager.persist(new ArticleEntity(null, "subject1", "content1", "username1", LocalDateTime.now()));
        entityManager.persist(new ArticleEntity(null, "subject2", "content2", "username2", LocalDateTime.now()));
    }

    @AfterEach
    void tearDown() {
        articleRepository.deleteAllInBatch();
        entityManager.getEntityManager().createNativeQuery("ALTER TABLE article ALTER COLUMN `id` RESTART WITH 1").executeUpdate();
    }

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
