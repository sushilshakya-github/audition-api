package com.audition.web;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.audition.common.logging.AuditionLogger;
import com.audition.configuration.RestCallLoggingInterceptor;
import com.audition.model.AuditionComment;
import com.audition.model.AuditionPost;
import com.audition.service.AuditionService;

@WebMvcTest(AuditionController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuditionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditionService auditionService;

    @MockBean
	private RestCallLoggingInterceptor restCallLoggingInterceptor;
    
    @MockBean
    private AuditionLogger auditionLogger;

    private AuditionPost auditionPost;
    private AuditionComment auditionComment;

    @BeforeEach
    public void setup(){

    	auditionPost = AuditionPost.builder()
                .id(1)
                .userId(1)
                .title("IT")
                .body("Post Assignment work")
                .build();
    	
    	auditionComment = AuditionComment.builder()
                .id(11)
                .postId(3)
                .name("Alex")
                .email("alex@xyz.com")
                .body("Comments Assignment work")
                .build();
    }

    //Get Controller
    @Test
    @Order(1)
    public void getPostTest() throws Exception {
        // precondition
        List<AuditionPost> auditionPosts = new ArrayList<>();
        auditionPosts.add(auditionPost);
        given(auditionService.getPosts()).willReturn(auditionPosts);

        // action
        ResultActions response = mockMvc.perform(get("/posts?userId="+auditionPost.getUserId()));

        // verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(auditionPosts.size())));
    }

    //get by Id controller
    @Test
    @Order(2)
    public void getPostsByIdTest() throws Exception{
        // precondition
        given(auditionService.getPostById(auditionPost.getId()+"")).willReturn(auditionPost);

        // action
        ResultActions response = mockMvc.perform(get("/posts/{id}", auditionPost.getId()));

        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.userId", is(auditionPost.getUserId())))
                .andExpect(jsonPath("$.title", is(auditionPost.getTitle())))
                .andExpect(jsonPath("$.body", is(auditionPost.getBody())));

    }

    //Get post's Comments /posts/{postId}/comments
    @Test
    @Order(3)
    public void getPostComments() throws Exception {
        // precondition
        List<AuditionComment> auditionComments = new ArrayList<>();
        auditionComments.add(auditionComment);
        given(auditionService.getPostComments(auditionComment.getPostId()+"")).willReturn(auditionComments);

        // action
        ResultActions response = mockMvc.perform(get("/posts/"+auditionComment.getPostId()+"/comments"));

        // verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(auditionComments.size())));
    }

    //get comments by postId /comments?postId
    @Test
    @Order(4)
    public void getCommentByPostId() throws Exception{
        // precondition
    	List<AuditionComment> auditionComments = new ArrayList<>();
        auditionComments.add(auditionComment);
        given(auditionService.getCommentbyPostId(auditionComment.getPostId()+"")).willReturn(auditionComments);

        // action
        ResultActions response = mockMvc.perform(get("/comments?postId="+auditionComment.getPostId()));

        // verify
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].postId", is(auditionComment.getPostId())))
                .andExpect(jsonPath("$[0].email", is(auditionComment.getEmail())))
                .andExpect(jsonPath("$[0].body", is(auditionComment.getBody())));
    }
}