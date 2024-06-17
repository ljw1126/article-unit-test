package com.example.article.adaptor.port.in.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class ArticleDto {
    public static class CreateArticleRequest {

        @NotNull
        private String subject;

        @NotNull
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

        @NotNull
        private String subject;

        @NotNull
        private String content;

        @NotEmpty
        private String usename;

        public UpdateArticleRequest() {
        }

        public UpdateArticleRequest(Long id, String subject, String content, String usename) {
            this.id = id;
            this.subject = subject;
            this.content = content;
            this.usename = usename;
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

        public String getUsename() {
            return usename;
        }
    }
}
