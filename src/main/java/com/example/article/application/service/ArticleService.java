package com.example.article.application.service;

import com.example.article.adaptor.port.in.dto.ArticleDto;
import com.example.article.application.port.in.CreateArticleUseCase;
import com.example.article.application.port.in.DeleteArticleUseCase;
import com.example.article.application.port.in.QueryArticleUseCase;
import com.example.article.application.port.in.UpdateArticleUseCase;
import com.example.article.application.port.out.CommandArticlePort;
import com.example.article.application.port.out.QueryArticlePort;
import com.example.article.domain.Article;
import com.example.common.service.port.ClockHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.NoSuchElementException;

@Service
@Transactional
public class ArticleService implements CreateArticleUseCase, DeleteArticleUseCase, QueryArticleUseCase, UpdateArticleUseCase {

    private final CommandArticlePort commandArticlePort;
    private final QueryArticlePort queryArticlePort;
    private final ClockHolder clockHolder;

    public ArticleService(CommandArticlePort commandArticlePort, QueryArticlePort queryArticlePort, ClockHolder clockHolder) {
        this.commandArticlePort = commandArticlePort;
        this.queryArticlePort = queryArticlePort;
        this.clockHolder = clockHolder;
    }

    @Override
    public Article create(ArticleDto.CreateArticleRequest request) {
        Assert.hasLength(request.getSubject(), "subject should not empty");
        Assert.hasLength(request.getContent(), "content should not empty");
        Assert.hasLength(request.getUsername(), "username should not empty");

        return commandArticlePort.create(Article.from(request, clockHolder));
    }

    @Override
    public Article update(ArticleDto.UpdateArticleRequest request) {
        Article article = queryArticlePort.getById(request.getId()).orElseThrow(NoSuchElementException::new);
        article = article.update(request, clockHolder);
        return commandArticlePort.update(article);
    }

    @Override
    public void delete(Long articleId) {
        commandArticlePort.delete(articleId);
    }

    @Override
    @Transactional(readOnly = true)
    public Article getById(Long articleId) {
        return queryArticlePort.getById(articleId)
                .orElseThrow(NoSuchElementException::new);
    }
}
