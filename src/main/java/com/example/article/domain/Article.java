package com.example.article.domain;

import com.example.article.adaptor.port.in.dto.ArticleDto;

import java.time.LocalDateTime;
import java.util.Objects;


public class Article {
    private final Long id;
    private final String subject;
    private final String content;
    private final String username;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public Article(Long id, String subject, String content, String username, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.username = username;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static Article from(ArticleDto.CreateArticleRequest request, LocalDateTime createdAt) {
        return new Builder()
                .subject(request.getSubject())
                .content(request.getContent())
                .username(request.getUsername())
                .createdAt(createdAt)
                .build();
    }

    public Article update(ArticleDto.UpdateArticleRequest request, LocalDateTime modifiedAt) {
        return new Builder()
                .id(this.id)
                .subject(request.getSubject())
                .content(request.getContent())
                .username(this.username)
                .createdAt(this.createdAt)
                .modifiedAt(modifiedAt)
                .build();
    }

    public boolean verify(Article article) {
        return this.id.equals(article.getId()) && this.username.equals(article.getUsername());
    }

    public static class Builder {
        private Long id;
        private String subject;
        private String content;
        private String username;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder modifiedAt(LocalDateTime modifiedAt) {
            this.modifiedAt = modifiedAt;
            return this;
        }

        public Article build() {
            return new Article(this.id, this.subject, this.content, this.username, this.createdAt, this.modifiedAt);
        }
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

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Article article = (Article) other;
        return Objects.equals(getId(), article.getId()) && Objects.equals(getSubject(), article.getSubject()) && Objects.equals(getContent(), article.getContent()) && Objects.equals(getUsername(), article.getUsername()) && Objects.equals(getCreatedAt(), article.getCreatedAt()) && Objects.equals(getModifiedAt(), article.getModifiedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSubject(), getContent(), getUsername(), getCreatedAt(), getModifiedAt());
    }
}
