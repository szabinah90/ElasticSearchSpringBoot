package springboot.elasticsearch.controller;

import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springboot.elasticsearch.DAOs.ArticleAuthorDAO;
import springboot.elasticsearch.DAOs.ArticleDAO;
import springboot.elasticsearch.DAOs.ArticleIdDAO;
import springboot.elasticsearch.models.Article;
import springboot.elasticsearch.models.AuthorObject;
import springboot.elasticsearch.models.IdObject;
import springboot.elasticsearch.models.KeywordObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Controller
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

    @GetMapping(path = "/")
    public String home(Model model) {
        model.addAttribute("targetId", new IdObject());
        model.addAttribute("articlesByAuthor", new AuthorObject());
        model.addAttribute("articlesByKeyword", new KeywordObject());
        return "home";
    }

    @GetMapping(path = "/all")
    public String getAllArticles(Model model) {
        ArrayList<Article> articleList = new ArrayList<>();

        SearchHit[] searchHits = articleDAO.getAllArticles().getHits();
        for (SearchHit hit : searchHits) {
            Article temp = new Article();
            temp.setId(hit.getId());
            temp.setAuthor((String) hit.getSourceAsMap().get("author"));
            temp.setTitle((String) hit.getSourceAsMap().get("title"));
            temp.setUrl((String) hit.getSourceAsMap().get("url"));
            String dateString = hit.getSourceAsMap().get("date_published").toString();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = format.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            temp.setDate_published(date);
            temp.setContent((String) hit.getSourceAsMap().get("content"));

            articleList.add(temp);
        }

        model.addAttribute("articles", articleList);
        return "allArticles";
    }

    @GetMapping(path = "/ids/{id}")
    public String getArticleById(@ModelAttribute IdObject id, Model model) {
        Map<String, Object> response = articleIdDAO.getArticleById(id.getId());

        Article match = new Article();
        match.setId(id.getId());
        match.setAuthor((String) response.get("author"));
        match.setTitle((String) response.get("title"));
        match.setUrl((String) response.get("url"));
        String dateString = response.get("date_published").toString();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        match.setDate_published(date);
        match.setContent((String) response.get("content"));

        model.addAttribute("article", match);

        return "articleByID";
    }

    @GetMapping(path = "/authors/{author}")
    public String getArticlesByAuthor(@ModelAttribute AuthorObject author, Model model) {
        ArrayList<Article> matchList = new ArrayList<>();

        SearchHit[] searchHits = articleAuthorDAO.getArticlesByAuthor(author.getAuthor()).getHits();
        for (SearchHit hit : searchHits) {
            Article temp = new Article();
            temp.setId(hit.getId());
            temp.setAuthor((String) hit.getSourceAsMap().get("author"));
            temp.setTitle((String) hit.getSourceAsMap().get("title"));
            temp.setUrl((String) hit.getSourceAsMap().get("url"));
            String dateString = hit.getSourceAsMap().get("date_published").toString();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = format.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            temp.setDate_published(date);
            temp.setContent((String) hit.getSourceAsMap().get("content"));
            matchList.add(temp);
        }

        model.addAttribute("articles", matchList);

        return "articlesByAuthor";
    }

    @GetMapping(path = "/search/{keywords}")
    public String getArticlesByKeywords(@ModelAttribute KeywordObject keyword, Model model) {
        ArrayList<Article> matchList = new ArrayList<>();

        SearchHit[] searchHits = articleDAO.getArticlesByKeywords(keyword.getKeywords()).getHits();
        for (SearchHit hit : searchHits) {
            Article temp = new Article();
            temp.setId(hit.getId());
            temp.setAuthor((String) hit.getSourceAsMap().get("author"));
            temp.setTitle((String) hit.getSourceAsMap().get("title"));
            temp.setUrl((String) hit.getSourceAsMap().get("url"));
            String dateString = hit.getSourceAsMap().get("date_published").toString();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = format.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            temp.setDate_published(date);
            temp.setContent((String) hit.getSourceAsMap().get("content"));
            matchList.add(temp);
        }

        model.addAttribute("articles", matchList);

        return "articlesByKeywords";
    }

    @GetMapping(path = "/new")
    public String newArticleForm(Model article) {
        article.addAttribute("newArticle", new Article());
        return "newArticleForm";
    }

    @PostMapping(path = "/confirm")
    public String insertArticle(@Validated @ModelAttribute Article article, BindingResult bindingResult, Model model) throws Exception {
        articleDAO.insertArticle(article);
        return "created";
    }

    @PutMapping(path = "/ids/{id}")
    public Map<String, Object> updateArticleById(@RequestBody Article article, @PathVariable String id) {
        return articleIdDAO.updateArticleById(id, article);
    }

    @DeleteMapping(path = "/ids/{id}")
    public void deleteArticleById(@PathVariable String id) {
        articleIdDAO.deleteArticleById(id);
    }

    @GetMapping(path = "/authors")
    public Map<String, Integer> getAuthorList() {
        return articleAuthorDAO.getAuthorList();
    }

}
