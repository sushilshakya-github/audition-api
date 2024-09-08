package com.audition.integration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;

import io.prometheus.client.exemplars.tracer.common.SpanContextSupplier;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuditionIntegrationClientTest {

	String POSTS_URL = "https://jsonplaceholder.typicode.com/posts";
	String COMMENTS_URL = "https://jsonplaceholder.typicode.com/comments";
	
	@Autowired
    private RestTemplate restTemplate;
	
	@MockBean
	private SpanContextSupplier spanContextSupplier;
	
	@Autowired
    private AuditionIntegrationClient auditionIntegrationClient;
   
    @Test
    @Order(1)
    public void callgetPostRestClientTest() throws Exception {
    	 // Given
        String mockResponse = "[\n"
        		+ "  {\n"
        		+ "    \"userId\": 1,\n"
        		+ "    \"id\": 1,\n"
        		+ "    \"title\": \"Java service\",\n"
        		+ "    \"body\": \"Bdy java professional\"\n"
        		+ "  },\n"
        		+ "  {\n"
        		+ "    \"userId\": 1,\n"
        		+ "    \"id\": 2,\n"
        		+ "    \"title\": \"qui est esse\",\n"
        		+ "    \"body\": \"est rerum tempore\"\n"
        		+ "  }\n"
        		+ "]";

        when(restTemplate.exchange(POSTS_URL, HttpMethod.GET, prepareRequest(), String.class).getBody()).thenReturn(mockResponse);
        
        List<AuditionPost> result = auditionIntegrationClient.getPosts();
        
        boolean isEmptyResult = result.isEmpty();
        assertFalse(isEmptyResult);
    }
    
    @Test
    @Order(2)
    public void callgetPostByIdTest() throws Exception {
    	 // Given
        String id = "1";
    	String mockResponse = "[\n"
        		+ "  {\n"
        		+ "    \"userId\": 1,\n"
        		+ "    \"id\": 1,\n"
        		+ "    \"title\": \"Java service\",\n"
        		+ "    \"body\": \"Bdy java professional\"\n"
        		+ "  },\n"
        		+ "  {\n"
        		+ "    \"userId\": 1,\n"
        		+ "    \"id\": 2,\n"
        		+ "    \"title\": \"qui est esse\",\n"
        		+ "    \"body\": \"est rerum tempore\"\n"
        		+ "  }\n"
        		+ "]";

        when(restTemplate.exchange(POSTS_URL+"/"+id, HttpMethod.GET, prepareRequest(), String.class).getBody()).thenReturn(mockResponse);
        
        AuditionPost result = auditionIntegrationClient.getPostById(id);
        
        assertNotNull(result);
    }
    
    @Test
    @Order(3)
    public void callgetCommentsTest() throws Exception {
    	 // Given
        String postId = "2";
    	String mockResponse = "[\n"
    			+ "  {\n"
    			+ "    \"postId\": 2,\n"
    			+ "    \"id\": 6,\n"
    			+ "    \"name\": \"et fugit eligendi deleniti quidem qui sint nihil autem\",\n"
    			+ "    \"email\": \"Presley.Mueller@myrl.com\",\n"
    			+ "    \"body\": \"doloribus at sed quis culpa deserunt consectetur qui praesentium\\naccusamus fugiat dicta\\nvoluptatem rerum ut voluptate autem\\nvoluptatem repellendus aspernatur dolorem in\"\n"
    			+ "  },\n"
    			+ "  {\n"
    			+ "    \"postId\": 2,\n"
    			+ "    \"id\": 7,\n"
    			+ "    \"name\": \"repellat consequatur praesentium vel minus molestias voluptatum\",\n"
    			+ "    \"email\": \"Dallas@ole.me\",\n"
    			+ "    \"body\": \"maiores sed dolores similique labore et inventore et\\nquasi temporibus esse sunt id et\\neos voluptatem aliquam\\naliquid ratione corporis molestiae mollitia quia et magnam dolor\"\n"
    			+ "  }\n"
    			+ "]";

        when(restTemplate.exchange(POSTS_URL+"/"+postId+"/comments", HttpMethod.GET, prepareRequest(), String.class).getBody()).thenReturn(mockResponse);
        
        List<AuditionComment> result = auditionIntegrationClient.getComments(postId);
        
        assertFalse(result.isEmpty());
    }
    
    @Test
    @Order(4)
    public void callgetCommentsByPostIdTest() throws Exception {
    	 // Given
        String postId = "2";
    	String mockResponse = "[\n"
    			+ "  {\n"
    			+ "    \"postId\": 2,\n"
    			+ "    \"id\": 6,\n"
    			+ "    \"name\": \"et fugit eligendi deleniti quidem qui sint nihil autem\",\n"
    			+ "    \"email\": \"Presley.Mueller@myrl.com\",\n"
    			+ "    \"body\": \"doloribus at sed quis culpa deserunt consectetur qui praesentium\\naccusamus fugiat dicta\\nvoluptatem rerum ut voluptate autem\\nvoluptatem repellendus aspernatur dolorem in\"\n"
    			+ "  },\n"
    			+ "  {\n"
    			+ "    \"postId\": 2,\n"
    			+ "    \"id\": 7,\n"
    			+ "    \"name\": \"repellat consequatur praesentium vel minus molestias voluptatum\",\n"
    			+ "    \"email\": \"Dallas@ole.me\",\n"
    			+ "    \"body\": \"maiores sed dolores similique labore et inventore et\\nquasi temporibus esse sunt id et\\neos voluptatem aliquam\\naliquid ratione corporis molestiae mollitia quia et magnam dolor\"\n"
    			+ "  }\n"
    			+ "]";

        when(restTemplate.exchange(COMMENTS_URL+"?postId="+postId, HttpMethod.GET, prepareRequest(), String.class).getBody()).thenReturn(mockResponse);
        
        List<AuditionComment> result = auditionIntegrationClient.getCommentByPostId(postId);
        
        assertFalse(result.isEmpty());
    }
    
    private HttpEntity<String> prepareRequest() {
 		HttpHeaders headers = new HttpHeaders();
         headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
         HttpEntity <String> entity = new HttpEntity<String>(headers);
 		return entity;
 	}
}