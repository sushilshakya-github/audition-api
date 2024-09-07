package com.audition.web;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
@Validated
public class AuditionController {
	
	private static final Logger log = LoggerFactory.getLogger(AuditionController.class);

    @Autowired
    AuditionService auditionService;

    // TODO Add a query param that allows data filtering. The intent of the filter is at developers discretion.
    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPosts(@RequestParam(name ="filterParam", required = false) final String filterParam) {

        // TODO Add logic that filters response data based on the query param
    	List<AuditionPost> auditionPosts = null;
    	if(filterParam != null && !filterParam.isEmpty() && filterParam.matches(".*\\d.*")) {
    		auditionPosts = auditionService.getPosts().stream().filter(post -> post.getUserId() == Integer.valueOf(filterParam)).toList();
    	} else if(filterParam != null && filterParam.length() >= 3) {
    		auditionPosts = auditionService.getPosts().stream().filter(post -> post.getTitle().contains(filterParam)).toList();
    	} else {
    		auditionPosts = auditionService.getPosts();
    	}
    	
    	log.info("getting posts..");
    	if(auditionPosts != null && ! auditionPosts.isEmpty()) {
    		return auditionPosts;
    	} else {
    		throw new SystemException("No posts found for filter param: "+filterParam, 200);
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
    public @ResponseBody List<AuditionComment> getPostComments(@PathVariable String postId) {

        // TODO Add logic that filters response data based on the query param
    	List<AuditionComment> auditionComments = null;
    	if(!StringUtils.isNumeric(postId)) {
    		throw new SystemException("Invalid post Id: "+postId, 400);
    	} 
    	
    	auditionComments = auditionService.getPostComments(postId).stream().filter(post -> post.getPostId() == Integer.valueOf(postId)).toList();
    	
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
}
