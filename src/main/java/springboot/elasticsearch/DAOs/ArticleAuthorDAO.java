package springboot.elasticsearch.DAOs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ArticleAuthorDAO {

    private final String INDEX = "android";
    private final String TYPE = "article";

    private RestHighLevelClient restHighLevelClient;

    private ObjectMapper objectMapper;

    public ArticleAuthorDAO(RestHighLevelClient restHighLevelClient, ObjectMapper objectMapper) {
        this.restHighLevelClient = restHighLevelClient;
        this.objectMapper = objectMapper;
    }

    public SearchHits getArticlesByAuthor(String author) {
        SearchRequest searchRequest = new SearchRequest(INDEX);
        searchRequest.types(TYPE);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders
                .matchQuery("author", author)
                .fuzziness(Fuzziness.AUTO))
                .size(10000);
        searchRequest.source(searchSourceBuilder);

        SearchHits hits = new SearchHits(new SearchHit[0], 0, 0);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            hits = searchResponse.getHits();

        } catch (IOException io) {
            io.getLocalizedMessage();
        }
        return hits;
    }

    public Map<String, Integer> getAuthorList() {
        SearchRequest searchRequest = new SearchRequest(INDEX);
        searchRequest.types(TYPE);

        String[] includeFields = new String[]{"author"};
        String[] excludeFields = new String[]{"id", "index", "score", "type", "content"};

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders
                .matchAllQuery())
                .size(10000);
        searchSourceBuilder.fetchSource(includeFields, excludeFields);
        searchRequest.source(searchSourceBuilder);

        Map<String, Integer> authorsArticleNumbers = new HashMap<>();

        SearchHits hits = new SearchHits(new SearchHit[0], 0, 0);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            hits = searchResponse.getHits();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (SearchHit hit : hits) {
            Map<String, Object> author = hit.getSourceAsMap();
            String key = (String) author.get("author");
            if (authorsArticleNumbers.containsKey(key)) {
                authorsArticleNumbers.put(key, authorsArticleNumbers.get(key) + 1);
            } else {
              authorsArticleNumbers.put(key, 1);
            }
        }

        return authorsArticleNumbers;
    }
}
