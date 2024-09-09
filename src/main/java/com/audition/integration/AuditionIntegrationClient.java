package com.audition.integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuditionIntegrationClient {

	String POSTS_URL = "https://jsonplaceholder.typicode.com/posts";
	String COMMENTS_URL = "https://jsonplaceholder.typicode.com/comments";

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private AuditionLogger auditionLogger;

    private HttpEntity<String> prepareRequest() {
		HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity <String> entity = new HttpEntity<String>(headers);
		return entity;
	}
    
    @CircuitBreaker(name = "postsCircuitBreaker", fallbackMethod = "postsServiceFallback")
    public List<AuditionPost> getPosts() {
        // TODO make RestTemplate call to get Posts from https://jsonplaceholder.typicode.com/posts
    	auditionLogger.info(log, "calling getPosts() external service");
    	HttpEntity<String> entity = prepareRequest();
        String jsonString = restTemplate.exchange(POSTS_URL, HttpMethod.GET, entity, String.class).getBody();
        List<AuditionPost> auditPosts = new ArrayList<>();
		try {
			auditPosts = Arrays.asList(objectMapper.readValue(jsonString, AuditionPost[].class));
		} catch (JsonProcessingException e) {
			auditionLogger.logErrorWithException(log, "json parsing error in getPosts() message: {}", e);
			throw new SystemException("Error while parsing json string ", 500, e);
		} catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            	auditionLogger.logHttpStatusCodeError(log, "getPosts() not found error {}", e.getStatusCode().value());
            	throw new SystemException("HttpClientErrorException Not Found Error", e.getStatusCode().value(), e);
            } else {
                // TODO Find a better way to handle the exception so that the original error message is not lost. Feel free to change this function.
            	final ProblemDetail problemDetail = ProblemDetail.forStatus(e.getStatusCode());
                problemDetail.setTitle("HttpClientErrorException in getPosts()");
                problemDetail.setDetail(String.format("Error while calling URL {}", POSTS_URL));
            	auditionLogger.logStandardProblemDetail(log, problemDetail, e);
            	throw new SystemException("HttpClientErrorException Error", e);
            }
		}
        return auditPosts;

    }

    public AuditionPost getPostById(final String id) {
        // TODO get post by post ID call from https://jsonplaceholder.typicode.com/posts/
    	auditionLogger.info(log, "calling getPostById() external service for id:", id);
    	HttpEntity<String> entity = prepareRequest();
        
        AuditionPost auditionPost = null;
		try {
			String jsonString = restTemplate.exchange(POSTS_URL+"/"+id, HttpMethod.GET, entity, String.class).getBody();
			auditionPost = objectMapper.readValue(jsonString, AuditionPost.class);
		} catch (JsonProcessingException e) {
			auditionLogger.logErrorWithException(log, "json parsing error in getPostById() message: ", e);
			throw new SystemException("Error while parsing json string ", 500, e);
		} catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            	auditionLogger.logHttpStatusCodeError(log, "getPostById() not found error {}", e.getStatusCode().value());
                throw new SystemException("HttpClientErrorException Not found error", e.getStatusCode().value(), e);
            } else {
                // TODO Find a better way to handle the exception so that the original error message is not lost. Feel free to change this function.
            	final ProblemDetail problemDetail = ProblemDetail.forStatus(e.getStatusCode());
                problemDetail.setTitle("HttpClientErrorException in getPostById()");
                problemDetail.setDetail(String.format("Error while calling URL {}", POSTS_URL));
            	auditionLogger.logStandardProblemDetail(log, problemDetail, e);
            	throw new SystemException("HttpClientErrorException Error", e);
            }
        }
		return auditionPost;
    }

    // TODO Write a method GET comments for a post from https://jsonplaceholder.typicode.com/posts/{postId}/comments - the comments must be returned as part of the post.

    public List<AuditionComment> getComments(final String postId) {
    	auditionLogger.info(log, "calling getComments() external service for postId:", postId);
    	HttpEntity<String> entity = prepareRequest();
        
    	String jsonString = restTemplate.exchange(POSTS_URL+"/"+postId+"/comments", HttpMethod.GET, entity, String.class).getBody();
        List<AuditionComment> auditionComments = new ArrayList<>();
		try {
			auditionComments = Arrays.asList(objectMapper.readValue(jsonString, AuditionComment[].class));
		} catch (JsonProcessingException e) {
			auditionLogger.logErrorWithException(log, "json parsing error in getComments() message: ", e);
			throw new SystemException("Error while parsing json string", 500, e);
		} catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            	auditionLogger.logHttpStatusCodeError(log, "getComments() not found error {}", e.getStatusCode().value());
            	throw new SystemException("HttpClientErrorException Not found error", e.getStatusCode().value(), e);
            } else {
                // TODO Find a better way to handle the exception so that the original error message is not lost. Feel free to change this function.
            	final ProblemDetail problemDetail = ProblemDetail.forStatus(e.getStatusCode());
                problemDetail.setTitle("HttpClientErrorException in getComments()");
                problemDetail.setDetail(String.format("Error while calling URL {} {} /comments", POSTS_URL, postId));
            	auditionLogger.logStandardProblemDetail(log, problemDetail, e);
            	throw new SystemException("HttpClientErrorException Error", e);
            }
		}
        return auditionComments;

    }

    // TODO write a method. GET comments for a particular Post from https://jsonplaceholder.typicode.com/comments?postId={postId}.
    // The comments are a separate list that needs to be returned to the API consumers. Hint: this is not part of the AuditionPost pojo.
    
    public List<AuditionComment> getCommentByPostId(final String postId) {
    	auditionLogger.info(log, "calling getCommentByPostId() external service for postId:", postId);
    	HttpEntity<String> entity = prepareRequest();
        
    	List<AuditionComment> auditionComment = null;
		try {
			String jsonString = restTemplate.exchange(COMMENTS_URL+"?postId="+postId, HttpMethod.GET, entity, String.class).getBody();
			auditionComment = Arrays.asList(objectMapper.readValue(jsonString, AuditionComment[].class));
		} catch (JsonProcessingException e) {
			auditionLogger.logErrorWithException(log, "json parsing error in getCommentByPostId() message: ", e);
			throw new SystemException("Error while parsing json string", 500, e);
		} catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
            	auditionLogger.logHttpStatusCodeError(log, "getComments() not found error {}", e.getStatusCode().value());
            	throw new SystemException("HttpClientErrorException Not found error", e.getStatusCode().value(), e);
            } else {
                // TODO Find a better way to handle the exception so that the original error message is not lost. Feel free to change this function.
            	final ProblemDetail problemDetail = ProblemDetail.forStatus(e.getStatusCode());
                problemDetail.setTitle("HttpClientErrorException in getCommentByPostId()");
                problemDetail.setDetail(String.format("Error while calling URL {} ?postId={}", POSTS_URL, postId));
            	auditionLogger.logStandardProblemDetail(log, problemDetail, e);
            	throw new SystemException("HttpClientErrorException Error", e);
            }
        }
		return auditionComment;
    }
    
    public List<AuditionPost> postsServiceFallback(Throwable throwable) {
    	auditionLogger.warn(log, String.format("postsServiceFallback error {}", throwable.getMessage()));
    	throw new SystemException("Posts service unavailable!", 200);
    }
}
