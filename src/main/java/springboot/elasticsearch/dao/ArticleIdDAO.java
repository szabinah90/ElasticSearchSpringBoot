package springboot.elasticsearch.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Repository;
import springboot.elasticsearch.model.Article;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ArticleIdDAO {

    private final String INDEX = "android";
    private final String TYPE = "article";

    private RestHighLevelClient restHighLevelClient;

    private ObjectMapper objectMapper;

    public ArticleIdDAO(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
        this.objectMapper = objectMapper;
        this.restHighLevelClient = restHighLevelClient;
    }

    public Map<String, Object> getArticleById(String id){
        GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
        Map<String, Object> sourceAsMap = new HashMap<>();
        try {
            GetResponse getResponse = restHighLevelClient.get(getRequest);
            sourceAsMap = getResponse.getSourceAsMap();
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }

        return sourceAsMap;
    }

    public Map<String, Object> updateArticleById(String id, Article article){
        UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id)
                .fetchSource(true);    // Fetch Object after its update
        Map<String, Object> error = new HashMap<>();
        error.put("Error", "Unable to update article.");
        try {
            String articleJson = objectMapper.writeValueAsString(article);
            updateRequest.doc(articleJson, XContentType.JSON);
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
            Map<String, Object> sourceAsMap = updateResponse.getGetResult().sourceAsMap();
            return sourceAsMap;
        }catch (JsonProcessingException e){
            e.getMessage();
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
        return error;
    }

    public void deleteArticleById(String id) {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
        try {
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
    }
}
