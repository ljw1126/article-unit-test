package com.example.article.adaptor.port.in;

import com.example.article.application.port.in.CreateArticleUseCase;
import com.example.article.application.port.in.DeleteArticleUseCase;
import com.example.article.application.port.in.QueryArticleUseCase;
import com.example.article.application.port.in.UpdateArticleUseCase;
import com.example.article.domain.Article;
import com.example.common.api.dto.CommandResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.article.adaptor.port.in.dto.ArticleDto.ArticleResponse;
import static com.example.article.adaptor.port.in.dto.ArticleDto.CreateArticleRequest;
import static com.example.article.adaptor.port.in.dto.ArticleDto.UpdateArticleRequest;

@RestController
@RequestMapping("/article")
public class ArticleController {

    private final CreateArticleUseCase createArticleUseCase;
    private final UpdateArticleUseCase updateArticleUseCase;
    private final DeleteArticleUseCase deleteArticleUseCase;
    private final QueryArticleUseCase queryArticleUseCase;

    public ArticleController(CreateArticleUseCase createArticleUseCase, UpdateArticleUseCase updateArticleUseCase, DeleteArticleUseCase deleteArticleUseCase, QueryArticleUseCase queryArticleUseCase) {
        this.createArticleUseCase = createArticleUseCase;
        this.updateArticleUseCase = updateArticleUseCase;
        this.deleteArticleUseCase = deleteArticleUseCase;
        this.queryArticleUseCase = queryArticleUseCase;
    }

    @PostMapping("/create")
    public ResponseEntity<CommandResponse> create(@Valid @RequestBody CreateArticleRequest request) {
        Article article = createArticleUseCase.create(request);
        return ResponseEntity
                .status(201)
                .body(new CommandResponse(article.getId()));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<ArticleResponse> update(@Valid @RequestBody UpdateArticleRequest request) {
        Article updated = updateArticleUseCase.update(request);
        return ResponseEntity
                .status(200)
                .body(ArticleResponse.from(updated));
    }

    @DeleteMapping("/delete/{articleId}")
    public ResponseEntity<Void> delete(@PathVariable Long articleId) {
        deleteArticleUseCase.delete(articleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/get/{articleId}")
    public ResponseEntity<ArticleResponse> getById(@PathVariable Long articleId) {
        Article article = queryArticleUseCase.getById(articleId);
        return ResponseEntity
                .status(200)
                .body(ArticleResponse.from(article));
    }
}
