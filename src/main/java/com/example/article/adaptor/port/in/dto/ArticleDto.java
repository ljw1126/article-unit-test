package com.example.article.adaptor.port.in.dto;

import com.example.article.domain.Article;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ArticleDto {
    public static class CreateArticleRequest {

        @NotBlank
        private String subject;

        @NotBlank
        private String content;

        @NotNull
        private String username;

        public CreateArticleRequest() {
        }

        public CreateArticleRequest(String subject, String content, String username) {
            this.subject = subject;
            this.content = content;
            this.username = username;
        }

        public String getSubject() {
            return subject;
        }

        public String getContent() {
            return content;
        }

        public String getUsername() {
            return username;
        }
    }

    public static class UpdateArticleRequest {
        @NotNull
        private Long id;

        @NotBlank
        private String subject;

        @NotBlank
        private String content;

        @NotEmpty
        private String username;

        public UpdateArticleRequest() {
        }

        public UpdateArticleRequest(Long id, String subject, String content, String username) {
            this.id = id;
            this.subject = subject;
            this.content = content;
            this.username = username;
        }

        public Long getId() {
            return id;
        }

        public String getSubject() {
            return subject;
        }

        public String getContent() {
            return content;
        }

        public String getUsername() {
            return username;
        }
    }

    public static class ArticleResponse {
        private final Long id;
        private final String subject;
        private final String content;
        private final String username;
        private final LocalDateTime createdAt;

        public ArticleResponse(Long id, String subject, String content, String username, LocalDateTime createdAt) {
            this.id = id;
            this.subject = subject;
            this.content = content;
            this.username = username;
            this.createdAt = createdAt;
        }

        public static ArticleResponse from(Article article) {
            return new ArticleResponse(
                    article.getId(),
                    article.getSubject(),
                    article.getContent(),
                    article.getUsername(),
                    article.getCreatedAt()
            );
        }

        public Long getId() {
            return id;
        }

        public String getSubject() {
            return subject;
        }

        public String getContent() {
            return content;
        }

        public String getUsername() {
            return username;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }
    }
}
