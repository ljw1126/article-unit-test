package com.example.article.adaptor.port.in;

import com.example.article.mock.TestContainer;
import com.example.common.api.dto.CommandResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static com.example.article.adaptor.port.in.dto.ArticleDto.ArticleResponse;
import static com.example.article.adaptor.port.in.dto.ArticleDto.CreateArticleRequest;
import static com.example.article.adaptor.port.in.dto.ArticleDto.UpdateArticleRequest;
import static org.assertj.core.api.Assertions.assertThat;

class ArticleControllerTest {

    private static final LocalDateTime NOW = LocalDateTime.of(2024, 1, 1, 10, 0, 0);
    private TestContainer testContainer;

    @BeforeEach
    void setUp() {
        testContainer = new TestContainer(() -> NOW);
    }

    @Test
    void create() {
        ArticleController articleController = testContainer.articleController;
        CreateArticleRequest request = new CreateArticleRequest("subject", "content", "tester");

        ResponseEntity<CommandResponse> result = articleController.create(request);

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatusCode.valueOf(201));

        assertThat(result.getBody().getId()).isEqualTo(6L);
    }

    @ParameterizedTest(name = "{0} {1}")
    @MethodSource("invalidParameters")
    void create요청시_파라미터가_누락되면_안된다(String target, String msg, String subject, String content, String username) {
        CreateArticleRequest request = new CreateArticleRequest(subject, content, username);

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        BindingResult bindingResult = new BeanPropertyBindingResult(request, "createArticleRequest");
        validator.validate(request, bindingResult);

        assertThat(bindingResult.hasErrors()).isTrue();
        assertThat(bindingResult.getFieldError(target).getDefaultMessage()).isEqualTo(msg);
    }

    static Stream<Arguments> invalidParameters() {
        return Stream.of(
                Arguments.of("subject", "must not be blank", null, "content", "user"),
                Arguments.of("subject", "must not be blank", "", "content", "user"),
                Arguments.of("content", "must not be blank", "subject", null, "user"),
                Arguments.of("content", "must not be blank", "subject", "", "user"),
                Arguments.of("username", "must not be null", "subject", "content", null)
        );
    }

    @Test
    void update() {
        ArticleController articleController = testContainer.articleController;
        UpdateArticleRequest request = new UpdateArticleRequest(1L, "updated subject", "updated content", "tester");

        ResponseEntity<ArticleResponse> result = articleController.update(request);
        ArticleResponse body = result.getBody();

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatusCode.valueOf(200));

        assertThat(body)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("subject", "updated subject")
                .hasFieldOrPropertyWithValue("content", "updated content")
                .hasFieldOrPropertyWithValue("username", "tester")
                .hasFieldOrProperty("createdAt");
    }

    @Test
    void delete() {
        ArticleController articleController = testContainer.articleController;

        ResponseEntity<Void> result = articleController.delete(1L);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void getById() {
        ArticleController articleController = testContainer.articleController;
        ResponseEntity<ArticleResponse> result = articleController.getById(1L);
        ArticleResponse body = result.getBody();

        assertThat(result.getStatusCode())
                .isEqualTo(HttpStatusCode.valueOf(200));

        assertThat(body)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("subject", "subject")
                .hasFieldOrPropertyWithValue("content", "content")
                .hasFieldOrPropertyWithValue("username", "tester")
                .hasFieldOrProperty("createdAt");
    }
}
