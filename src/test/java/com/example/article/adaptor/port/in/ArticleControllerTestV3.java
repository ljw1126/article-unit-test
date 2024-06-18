package com.example.article.adaptor.port.in;

import com.example.article.application.port.in.CreateArticleUseCase;
import com.example.article.application.port.in.DeleteArticleUseCase;
import com.example.article.application.port.in.QueryArticleUseCase;
import com.example.article.application.port.in.UpdateArticleUseCase;
import com.example.article.domain.Article;
import com.example.common.exception.AccessDeniedException;
import com.example.common.exception.ResourceNotFoundException;
import com.example.domain.ArticleFixtures;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static com.example.article.adaptor.port.in.dto.ArticleDto.CreateArticleRequest;
import static com.example.article.adaptor.port.in.dto.ArticleDto.UpdateArticleRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
class ArticleControllerTestV3 {

    @MockBean
    private CreateArticleUseCase createArticleUseCase;

    @MockBean
    private UpdateArticleUseCase updateArticleUseCase;

    @MockBean
    private DeleteArticleUseCase deleteArticleUseCase;

    @MockBean
    private QueryArticleUseCase queryArticleUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("POST /article/create")
    class CreateArticle {
        @Test
        void create() throws Exception {
            CreateArticleRequest request = new CreateArticleRequest("subject", "content", "tester");

            Article article = ArticleFixtures.article();
            given(createArticleUseCase.create(any()))
                    .willReturn(article);

            mockMvc.perform(post("/article/create")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    ).andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L));
        }

        @ParameterizedTest(name = "{0}")
        @CsvSource(value = {
                "subject must not be null, ,content, user",
                "subject must not be blank, '',content, user",
                "content must not be null, subject, , user",
                "content must not be blank, subject, '', user",
                "username must not be null, subject, content, "
        })
        void 파라미터가_누락되면_BadRequest를_응답한다(String title, String subject, String content, String username) throws Exception {
            CreateArticleRequest request = new CreateArticleRequest(subject, content, username);

            mockMvc.perform(post("/article/create")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Put /article/update")
    class UpdateArticle {
        @Test
        void update() throws Exception {
            UpdateArticleRequest request = new UpdateArticleRequest(1L, "updated subject", "updated content", "tester");

            Article article = ArticleFixtures.article();
            LocalDateTime now = LocalDateTime.now();
            article = article.update(request, () -> now);

            given(updateArticleUseCase.update(any()))
                    .willReturn(article);

            mockMvc.perform(put("/article/update")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.subject").value("updated subject"))
                    .andExpect(jsonPath("$.content").value("updated content"))
                    .andExpect(jsonPath("$.username").value("tester"))
                    .andExpect(jsonPath("$.createdAt").exists());
        }

        @ParameterizedTest(name = "{0}")
        @CsvSource(value = {
                "articleId must not be null,,subject,content, user",
                "subject must not be null,1,,content, user",
                "subject must not be blank,1,'',content, user",
                "content must not be null,1, subject,, user",
                "content must not be blank,1,subject,'', user",
                "username must not be null,1,subject,content,"
        })
        void 파라미터가_누락되면_BadRequest를_응답한다(String title, Long articleId, String subject, String content, String username) throws Exception {
            UpdateArticleRequest request = new UpdateArticleRequest(articleId, subject, content, username);

            mockMvc.perform(put("/article/update")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isBadRequest());
        }

        @Test
        void 다른유저가_수정을하려하면_Forbbiend_예외_반환한다() throws Exception {
            UpdateArticleRequest request = new UpdateArticleRequest(1L, "update subject", "update content", "other user");

            given(updateArticleUseCase.update(any()))
                    .willThrow(new AccessDeniedException("다른 작성자는 수정 불가능"));

            mockMvc.perform(put("/article/update")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value("다른 작성자는 수정 불가능"));
        }
    }


    @Test
    void delete() throws Exception {
        willDoNothing().given(deleteArticleUseCase).delete(any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/article/delete/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Nested
    @DisplayName("GET /article/get/{articleId}")
    class GetArticle {
        @Test
        void getById() throws Exception {
            Article article = ArticleFixtures.article();
            given(queryArticleUseCase.getById(any()))
                    .willReturn(article);

            mockMvc.perform(get("/article/get/1"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void article이없으면_badRequest_응답한다() throws Exception {
            given(queryArticleUseCase.getById(any()))
                    .willThrow(new ResourceNotFoundException("article not exists"));

            mockMvc.perform(get("/article/get/{articleId}", 1L))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("article not exists"));
        }
    }

}
