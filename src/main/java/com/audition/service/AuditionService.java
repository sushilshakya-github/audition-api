package com.audition.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;

@Service
public class AuditionService {
	private static final Logger log = LoggerFactory.getLogger(AuditionService.class);

    @Autowired
    private AuditionIntegrationClient auditionIntegrationClient;


    public List<AuditionPost> getPosts() {
        return auditionIntegrationClient.getPosts();
    }

    public AuditionPost getPostById(final String postId) {
        return auditionIntegrationClient.getPostById(postId);
    }

    public List<AuditionComment> getPostComments(String postId) {
        return auditionIntegrationClient.getComments(postId);
    }

    public List<AuditionComment> getCommentbyPostId(final String postId) {
        log.info("getCommentbyPostId..");
    	return auditionIntegrationClient.getCommentByPostId(postId);
    }

}