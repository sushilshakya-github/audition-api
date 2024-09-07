package com.audition;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.audition.configuration.RestCallLoggingInterceptor;

@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class AuditionApplicationTests {

    // TODO implement unit test. Note that an applicant should create additional unit tests as required.
	@MockBean
	public RestCallLoggingInterceptor restCallLoggingInterceptor;
	
    @Test
    void contextLoads() {
    }
    
    @Test
    void main() {
    	
    }

}