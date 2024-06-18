package com.example.article.adaptor.port.out.repository.entity;

import com.example.article.domain.Article;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "article")
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String subject;

    @Column
    private String content;

    @Column
    private String username;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    // 지연전략, 프록시 생성시 필요
    public ArticleEntity() {
    }

    public ArticleEntity(Long id, String subject, String content, String username, LocalDateTime createdAt) {
        this(id, subject, content, username, createdAt, null);
    }

    public ArticleEntity(Long id, String subject, String content, String username, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.username = username;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
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

        public ArticleEntity build() {
            return new ArticleEntity(this.id, this.subject, this.content, this.username, this.createdAt, this.modifiedAt);
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

    public Article toDomain() {
        return new Article.Builder()
                .id(this.id)
                .subject(this.subject)
                .content(this.content)
                .username(this.username)
                .createdAt(this.createdAt)
                .modifiedAt(this.modifiedAt)
                .build();
    }

    public static ArticleEntity from(Article article) {
        return new ArticleEntity.Builder()
                .id(article.getId())
                .subject(article.getSubject())
                .content(article.getContent())
                .username(article.getUsername())
                .createdAt(article.getCreatedAt())
                .modifiedAt(article.getModifiedAt())
                .build();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        ArticleEntity that = (ArticleEntity) other;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getSubject(), that.getSubject()) && Objects.equals(getContent(), that.getContent()) && Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getCreatedAt(), that.getCreatedAt()) && Objects.equals(getModifiedAt(), that.getModifiedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSubject(), getContent(), getUsername(), getCreatedAt(), getModifiedAt());
    }
}
