package springboot.elasticsearch.controller;

import org.elasticsearch.search.SearchHits;
import org.springframework.web.bind.annotation.*;
import springboot.elasticsearch.dao.ArticleAuthorDAO;
import springboot.elasticsearch.dao.ArticleDAO;
import springboot.elasticsearch.dao.ArticleIdDAO;
import springboot.elasticsearch.model.Article;

import java.util.Map;

@RestController
@RequestMapping("/android-articles")
public class ArticleController {

    private ArticleIdDAO articleIdDAO;
    private ArticleAuthorDAO articleAuthorDAO;
    private ArticleDAO articleDAO;

    public ArticleController(ArticleIdDAO articleIdDAO, ArticleAuthorDAO articleAuthorDAO, ArticleDAO articleDAO) {
        this.articleIdDAO = articleIdDAO;
        this.articleAuthorDAO = articleAuthorDAO;
        this.articleDAO = articleDAO;
    }

    @PostMapping
    public Article insertArticle(@RequestBody Article article) throws Exception {
        return articleDAO.insertArticle(article);
    }

    @GetMapping(path = "/")
    public SearchHits getAllArticles() {
        return articleDAO.getAllArticles();
    }

    @GetMapping(path = "/ids/{id}")
    public Map<String, Object> getArticleById(@PathVariable String id) {
        return articleIdDAO.getArticleById(id);
    }


    @PutMapping(path = "/ids/{id}")
    public Map<String, Object> updateArticleById(@RequestBody Article article, @PathVariable String id) {
        return articleIdDAO.updateArticleById(id, article);
    }

    @DeleteMapping(path = "/ids/{id}")
    public void deleteArticleById(@PathVariable String id) {
        articleIdDAO.deleteArticleById(id);
    }

    @GetMapping(path = "/authors/{author}")
    public SearchHits getArticlesByAuthor(@PathVariable String author) {
        return articleAuthorDAO.getArticleByAuthor(author);
    }

    @GetMapping(path = "/authors")
    public Map<String, Integer> getAuthorList() {
        return articleAuthorDAO.getAuthorList();
    }

    @GetMapping(path = "/search/{keywords}")
    public SearchHits getArticlesByKeywords(@PathVariable String keywords) {
        return articleDAO.getArticlesByKeywords(keywords);
    }
}
