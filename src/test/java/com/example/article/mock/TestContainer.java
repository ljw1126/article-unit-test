package com.example.article.mock;

import com.example.article.adaptor.port.in.ArticleController;
import com.example.article.application.service.ArticleService;
import com.example.article.domain.Article;
import com.example.common.service.port.ClockHolder;
import com.example.domain.ArticleFixtures;

public class TestContainer {

    public final ArticleController articleController;

    public TestContainer(ClockHolder clockHolder) {
        FakeArticlePersistenceAdaptor articlePersistenceAdaptor = new FakeArticlePersistenceAdaptor();
        ArticleService articleService = new ArticleService(articlePersistenceAdaptor, articlePersistenceAdaptor, clockHolder);
        this.articleController = new ArticleController(articleService, articleService, articleService, articleService);

        Article article1 = ArticleFixtures.article();
        Article article2 = ArticleFixtures.article(2L);
        Article article3 = ArticleFixtures.article(3L);
        Article article4 = ArticleFixtures.article(4L);
        Article article5 = ArticleFixtures.article(5L);

        articlePersistenceAdaptor.create(article1);
        articlePersistenceAdaptor.create(article2);
        articlePersistenceAdaptor.create(article3);
        articlePersistenceAdaptor.create(article4);
        articlePersistenceAdaptor.create(article5);
    }
}
