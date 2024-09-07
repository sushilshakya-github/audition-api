package com.audition.web;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
    public void getPostsByIdTest() throws Exception {
        Integer id = 0;
    	// precondition
        given(auditionService.getPostById(auditionPost.getId()+"")).willReturn(auditionPost);
        assertNotNull(id);
        assertTrue(id.intValue() <= 0);
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
    public void getCommentByPostId() throws Exception {
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
    
   //get posts by userId /posts?filterParam
    @Test
    @Order(5)
    public void getPostNodataFoundTest() throws Exception {
        // precondition
    	 List<AuditionPost> auditionPosts = new ArrayList<>();
         //auditionPosts.add(auditionPost);
         given(auditionService.getPosts()).willReturn(auditionPosts);

         // action
         ResultActions response = mockMvc.perform(get("/posts?userId="+auditionPost.getUserId()));
         
         // verify the output
         response.andExpect(status().isOk())
                 .andDo(print())
                 .andExpect(jsonPath("$.title", containsStringIgnoringCase("Error")));
    }
    
  
    @Test
    @Order(6)
    public void filterParamNullTest() throws Exception {
        // precondition
    	 String filterParam = "2";
         assertNotNull(filterParam);
         assertFalse(filterParam.isEmpty());
         assertTrue(filterParam.matches(".*\\d.*"));
         
         ResultActions response = mockMvc.perform(get("/posts?filterParam="+filterParam));
         
         // verify the output
         response.andExpect(status().isOk())
                 .andDo(print());
        
    }
    
    @Test
    @Order(7)
    public void filterParamLengthTest() throws Exception {
        // precondition
    	 String filterParam = "eiku";
         assertNotNull(filterParam);
         assertTrue(filterParam.length() >=3);
         
         ResultActions response = mockMvc.perform(get("/posts?filterParam="+filterParam));
         
         // verify the output
         response.andExpect(status().isOk())
                 .andDo(print());
        
    }
    
    // Negative test cases
    
    @Test
    @Order(8)
    public void postIdNumericTest() throws Exception {
        // precondition
    	 String postId = "4";
         assertNotNull(postId);
         assertTrue(StringUtils.isNumeric(postId));
         
         ResultActions response = mockMvc.perform(get("/posts/"+postId+"/comments"));
         
         // verify the output
         response.andExpect(status().isOk())
                 .andDo(print());
        
    }
    
    @Test
    @Order(9)
    public void postIdNotNumericTest() throws Exception {
        // precondition
    	 String postId = "not num";
         assertNotNull(postId);
         assertFalse(StringUtils.isNumeric(postId));
         
         ResultActions response = mockMvc.perform(get("/posts/"+postId+"/comments"));
         response.andExpect(status().is4xxClientError()).andDo(print());
         
         ResultActions response2 = mockMvc.perform(get("/comments?postId="+postId));
         response2.andExpect(status().is4xxClientError()).andDo(print());
    }
    
    @Test
    @Order(10)
    public void commentListNotNullNotEmptyTest() throws Exception {
        // precondition
    	 String postId = "not num";
         assertNotNull(postId);
         assertFalse(postId.isEmpty());
         
         ResultActions response = mockMvc.perform(get("/posts/"+postId+"/comments"));
         
         // verify the output
         response.andExpect(status().is4xxClientError())
                 .andDo(print());
    }
    
    @Test
    @Order(11)
    public void postIdValueGreaterThanZeroTest() throws Exception {
    	 Integer id = 0;
         assertNotNull(id);
         assertTrue(id.intValue() <= 0);
         // action
         ResultActions response = mockMvc.perform(get("/posts/{id}", id));

        response.andExpect(status().is4xxClientError()).andDo(print());
    }
    
    @Test
    @Order(12)
    public void getPostIdNotNullShouldNumericTest() throws Exception {
		String postId = null;
		assertNull(postId);
		//assertTrue(StringUtils.isNumeric(postId));
		ResultActions response = mockMvc.perform(get("/posts/"+postId+"/comments"));

		response.andExpect(status().is4xxClientError()).andDo(print());
    }
}