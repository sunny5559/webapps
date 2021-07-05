/**
 * 
 */
package org.tfa.mtld.web.controller.ws;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.tfa.mtld.service.services.CohortService;
import org.tfa.mtld.service.services.CohortUpdateService;


/**
 * @author vaibhav.poorey
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CohortControllerTest {
	
	
	
	@Configuration
	static class CohortControllerTestConfiguration{
		
		
		@Bean
		public CohortService cohortService() {
		return Mockito.mock(CohortService.class);
		}
		
		@Bean
		public CohortUpdateService cohortUpdateService(){
			return Mockito.mock(CohortUpdateService.class );
		}
		
		@Bean
		public CohortController cohortController() {
		return new CohortController();
		}
		
	}
	
	@Autowired
	private CohortController cohortController;
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
