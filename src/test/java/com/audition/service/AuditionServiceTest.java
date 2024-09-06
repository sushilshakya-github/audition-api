package com.audition.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuditionServiceTest {

    @Mock
    private AuditionIntegrationClient auditionIntegrationClient;

    @InjectMocks
    private AuditionService auditionService;

    private AuditionPost auditionPost;


    @BeforeEach
    public void setup(){

    	auditionPost = AuditionPost.builder()
                .id(1)
                .userId(1)
                .title("IT Professional")
                .body("Assignment work service")
                .build();
    }

    @Test
    @Order(1)
    public void getPostById(){
        // precondition
        given(auditionIntegrationClient.getPostById("1")).willReturn(auditionPost);

        // action
        AuditionPost existingAuditionPost = auditionService.getPostById(auditionPost.getId()+"");

        // verify
        System.out.println(existingAuditionPost);
        assertThat(existingAuditionPost).isNotNull();

    }


    @Test
    @Order(2)
    public void getAllEmployee(){
    	AuditionPost auditionPostObj = AuditionPost.builder()
                .id(1)
                .userId(1)
                .title("Java Professional")
                .body("Web service test")
                .build();

        // precondition
        given(auditionIntegrationClient.getPosts()).willReturn(List.of(auditionPost, auditionPostObj));

        // action
        List<AuditionPost> auditionPosts = auditionService.getPosts();

        // verify
        System.out.println(auditionPosts);
        assertThat(auditionPosts).isNotNull();
        assertThat(auditionPosts.size()).isGreaterThan(1);
    }
  
}