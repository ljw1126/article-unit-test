package com.example.article.adaptor.port.out.repository;

import com.example.article.adaptor.port.out.repository.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
}
