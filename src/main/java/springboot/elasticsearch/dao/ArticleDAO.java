package springboot.elasticsearch.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;
import springboot.elasticsearch.model.Article;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Repository
public class ArticleDAO {

        private final String INDEX = "android";
        private final String TYPE = "article";

    private RestHighLevelClient restHighLevelClient;

    private ObjectMapper objectMapper;

    public ArticleDAO(RestHighLevelClient restHighLevelClient, ObjectMapper objectMapper) {
        this.restHighLevelClient = restHighLevelClient;
        this.objectMapper = objectMapper;
    }

    public Article insertArticle(Article article){
        article.setId(UUID.randomUUID().toString());
        Map<String, Object> dataMap = objectMapper.convertValue(article, Map.class);
        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, article.getId())
                .source(dataMap);
        try {
            IndexResponse response = restHighLevelClient.index(indexRequest);
        } catch(ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (java.io.IOException ex){
            ex.getLocalizedMessage();
        }
        return article;
    }

    public SearchHits getAllArticles() {
        SearchRequest searchRequest = new SearchRequest(INDEX);
        searchRequest.types(TYPE);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchHits allArticles = null;

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            allArticles = searchResponse.getHits();
        } catch (IOException e) {
            e.getLocalizedMessage();
        }
        return allArticles;
    }

    public SearchHits getArticlesByKeywords(String keywords) {
        SearchRequest searchRequest = new SearchRequest(INDEX);
        searchRequest.types(TYPE);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders
                .matchPhraseQuery("content", keywords))
                .size(10000);
        searchRequest.source(searchSourceBuilder);

        SearchHits matches = null;

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            matches = searchResponse.getHits();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return matches;
    }
}
