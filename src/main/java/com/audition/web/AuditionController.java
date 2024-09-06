package com.audition.web;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.audition.common.exception.SystemException;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.audition.service.AuditionService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@Validated
public class AuditionController {

    @Autowired
    AuditionService auditionService;

    // TODO Add a query param that allows data filtering. The intent of the filter is at developers discretion.
    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @CircuitBreaker(name = "postsCircuitBreaker", fallbackMethod = "postsServiceFallback")
    public @ResponseBody List<AuditionPost> getPosts(@RequestParam("userId") final Integer userId) {

        // TODO Add logic that filters response data based on the query param
    	List<AuditionPost> auditionPosts = null;
    	if(userId != null && userId.intValue() > 0) {
    		auditionPosts = auditionService.getPosts().stream().filter(post -> post.getUserId() == userId).toList();
    	} else {
    		auditionPosts = auditionService.getPosts();
    	}
    	
    	if(auditionPosts != null && ! auditionPosts.isEmpty()) {
    		return auditionPosts;
    	} else {
    		throw new SystemException("No posts found for User Id: "+userId, 200);
    	}
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPostsById(@PathVariable final Integer id) {
    	// TODO Add input validation
    	if(id != null && id.intValue() <=0) {
    		throw new SystemException("Invalid Id: "+id, 400);
    	}
    	final AuditionPost auditionPosts = auditionService.getPostById(id.toString());

        return auditionPosts;
    }

    // TODO Add additional methods to return comments for each post. Hint: Check https://jsonplaceholder.typicode.com/
    
    @RequestMapping(value = "/posts/{postId}/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @CircuitBreaker(name = "commentsCircuitBreaker", fallbackMethod = "commentsServiceFallback")
    public @ResponseBody List<AuditionComment> getPostComments(@PathVariable String postId) {

        // TODO Add logic that filters response data based on the query param
    	if(!StringUtils.isNumeric(postId)) {
    		throw new SystemException("Invalid post Id: "+postId, 400);
    	}
    	List<AuditionComment> auditionComments = null;
    	if(postId != null && StringUtils.isNumeric(postId)) {
    		auditionComments = auditionService.getPostComments(postId).stream().filter(post -> post.getPostId() == Integer.valueOf(postId)).toList();
    	} else {
    		auditionComments = auditionService.getPostComments(postId);
    	}
    	
    	if(auditionComments != null && ! auditionComments.isEmpty()) {
    		return auditionComments;
    	} else {
    		throw new SystemException("No comments found for Post Id: "+postId, 200);
    	}

    }

    @RequestMapping(value = "/comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionComment> getCommentByPostId(@RequestParam("postId") final String postId) {

        // TODO Add input validation
    	if(!StringUtils.isNumeric(postId)) {
    		throw new SystemException("Invalid postId: "+postId, 400);
    	}
        final List<AuditionComment> auditionComment = auditionService.getCommentbyPostId(postId);
        
        return auditionComment;
    }
    
    public List<AuditionPost> postsServiceFallback(Integer userId, Throwable throwable) {
    	throw new SystemException("Posts service unavailable!", 200);
    }
    
    public List<AuditionPost> commentsServiceFallback(String postId, Throwable throwable) {
    	throw new SystemException("Comments service unavailable!", 200);
    }
}
