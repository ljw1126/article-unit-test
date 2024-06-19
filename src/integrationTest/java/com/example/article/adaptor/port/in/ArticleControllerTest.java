package com.example.article.adaptor.port.in;

import com.example.article.adaptor.port.in.dto.ArticleDto;
import com.example.article.adaptor.port.out.repository.ArticleRepository;
import com.example.article.adaptor.port.out.repository.entity.ArticleEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Sql(value = "/sql/init-data.sql")
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleRepository articleRepository;


    @Nested
    @DisplayName("POST /article/create")
    class CreateArticle {
        @Test
        void create() throws Exception {
            ArticleDto.CreateArticleRequest request = new ArticleDto.CreateArticleRequest("new subject", "new content", "tester");

            mockMvc.perform(post("/article/create")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    ).andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(3L));

            Optional<ArticleEntity> result = articleRepository.findById(3L);
            assertThat(result).isPresent();
            assertThat(result.get())
                    .hasFieldOrPropertyWithValue("id", 3L)
                    .hasFieldOrPropertyWithValue("subject", "new subject")
                    .hasFieldOrPropertyWithValue("content", "new content")
                    .hasFieldOrPropertyWithValue("username", "tester")
                    .hasFieldOrProperty("createdAt");
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
            ArticleDto.CreateArticleRequest request = new ArticleDto.CreateArticleRequest(subject, content, username);

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
            ArticleDto.UpdateArticleRequest request = new ArticleDto.UpdateArticleRequest(1L, "updated subject", "updated content", "username1");

            mockMvc.perform(put("/article/update")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    ).andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.subject").value("updated subject"))
                    .andExpect(jsonPath("$.content").value("updated content"))
                    .andExpect(jsonPath("$.username").value("username1"))
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
            ArticleDto.UpdateArticleRequest request = new ArticleDto.UpdateArticleRequest(articleId, subject, content, username);

            mockMvc.perform(put("/article/update")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isBadRequest());
        }

        @Test
        void 다른유저가_수정을하려하면_Forbbiend_예외_반환한다() throws Exception {
            ArticleDto.UpdateArticleRequest request = new ArticleDto.UpdateArticleRequest(1L, "update subject", "update content", "other user");

            mockMvc.perform(put("/article/update")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.message").value("수정하실 수 없습니다"));
        }
    }


    @Nested
    @DisplayName("GET /article/get/{articleId}")
    class GetArticle {
        @Test
        void getById() throws Exception {
            mockMvc.perform(get("/article/get/{articleId}", 1L))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.id").value(1L),
                            jsonPath("$.subject").value("subject1"),
                            jsonPath("$.content").value("content1"),
                            jsonPath("$.username").value("username1")
                    );
        }

        @Test
        void article이없으면_badRequest_응답한다() throws Exception {
            mockMvc.perform(get("/article/get/{articleId}", 99L))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Article 에서 Id 99를 찾을 수 없습니다"));
        }
    }

    @Nested
    @DisplayName("DELETE /article/delete/{articleId}")
    class DeleteArticle {
        @Test
        void article을_하나_삭제한다() throws Exception {
            mockMvc.perform(delete("/article/delete/{articleId}", 1L))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            Optional<ArticleEntity> result = articleRepository.findById(1L);
            assertThat(result).isNotPresent();
        }
    }
}
