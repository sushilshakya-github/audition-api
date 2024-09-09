package com.audition.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuditionService {

    @Autowired
    private AuditionIntegrationClient auditionIntegrationClient;
   
    public List<AuditionPost> getPosts() {
    	if (log.isInfoEnabled()) {
            log.info("service call getPosts");
        }
        return auditionIntegrationClient.getPosts();
    }

    public AuditionPost getPostById(final String postId) {
        return auditionIntegrationClient.getPostById(postId);
    }

    public List<AuditionComment> getPostComments(String postId) {
        return auditionIntegrationClient.getComments(postId);
    }

    public List<AuditionComment> getCommentbyPostId(final String postId) {
    	if (log.isInfoEnabled()) {
            log.info("service call getCommentbyPostId postId={}", postId);
        }
    	return auditionIntegrationClient.getCommentByPostId(postId);
    }

}