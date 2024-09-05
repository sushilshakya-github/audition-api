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
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AuditionIntegrationClient {

	String POSTS_URL = "https://jsonplaceholder.typicode.com/posts";
	String COMMENTS_URL = "https://jsonplaceholder.typicode.com/comments";

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    private HttpEntity<String> prepareRequest() {
		HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity <String> entity = new HttpEntity<String>(headers);
		return entity;
	}
    
    public List<AuditionPost> getPosts() {
        // TODO make RestTemplate call to get Posts from https://jsonplaceholder.typicode.com/posts
    	HttpEntity<String> entity = prepareRequest();
        String jsonString = restTemplate.exchange(POSTS_URL, HttpMethod.GET, entity, String.class).getBody();
        List<AuditionPost> auditPosts = new ArrayList<>();
		try {
			auditPosts = Arrays.asList(objectMapper.readValue(jsonString, AuditionPost[].class));
		} catch (JsonProcessingException e) {
			throw new SystemException("Error while parsing json string");
		} catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("No posts found", 404);
            } else {
                // TODO Find a better way to handle the exception so that the original error message is not lost. Feel free to change this function.
                throw new SystemException("Unknown Error message", e);
            }
		}
        return auditPosts;

    }

    public AuditionPost getPostById(final String id) {
        // TODO get post by post ID call from https://jsonplaceholder.typicode.com/posts/
    	HttpEntity<String> entity = prepareRequest();
        
        AuditionPost auditionPost = null;
		try {
			String jsonString = restTemplate.exchange(POSTS_URL+"/"+id, HttpMethod.GET, entity, String.class).getBody();
			auditionPost = objectMapper.readValue(jsonString, AuditionPost.class);
		} catch (JsonProcessingException e) {
			throw new SystemException("Error while parsing json string");
		} catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Post with id " + id, "Resource Not Found", 404);
            } else {
                // TODO Find a better way to handle the exception so that the original error message is not lost. Feel free to change this function.
                throw new SystemException("Unknown Error message", e);
            }
        }
		return auditionPost;
    }

    // TODO Write a method GET comments for a post from https://jsonplaceholder.typicode.com/posts/{postId}/comments - the comments must be returned as part of the post.

    public List<AuditionComment> getComments(final String postId) {
    	HttpEntity<String> entity = prepareRequest();
        
    	String jsonString = restTemplate.exchange(POSTS_URL+"/"+postId+"/comments", HttpMethod.GET, entity, String.class).getBody();
        List<AuditionComment> auditionComments = new ArrayList<>();
		try {
			auditionComments = Arrays.asList(objectMapper.readValue(jsonString, AuditionComment[].class));
		} catch (JsonProcessingException e) {
			throw new SystemException("Error while parsing json string", 500);
		} catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("No comments found", 404);
            } else {
                // TODO Find a better way to handle the exception so that the original error message is not lost. Feel free to change this function.
                throw new SystemException("Unknown Error message", e);
            }
		}
        return auditionComments;

    }

    // TODO write a method. GET comments for a particular Post from https://jsonplaceholder.typicode.com/comments?postId={postId}.
    // The comments are a separate list that needs to be returned to the API consumers. Hint: this is not part of the AuditionPost pojo.
    
    public List<AuditionComment> getCommentByPostId(final String postId) {
    	HttpEntity<String> entity = prepareRequest();
        
    	List<AuditionComment> auditionComment = null;
		try {
			String jsonString = restTemplate.exchange(COMMENTS_URL+"?postId="+postId, HttpMethod.GET, entity, String.class).getBody();
			auditionComment = Arrays.asList(objectMapper.readValue(jsonString, AuditionComment[].class));
		} catch (JsonProcessingException e) {
			throw new SystemException("Error while parsing json string", 500, e);
		} catch (final HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SystemException("Cannot find a Comment for post id " + postId, "Resource Not Found", 404);
            } else {
                // TODO Find a better way to handle the exception so that the original error message is not lost. Feel free to change this function.
                throw new SystemException("Unknown Error message", e);
            }
        }
		return auditionComment;
    }
    
}
