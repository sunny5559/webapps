package org.tfa.mtld.web.controller.ws;





import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.tfa.mtld.service.services.CriteriaService;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CriteriaControllerTest {

	
	@Configuration
	static class CriteriaControllerTestConfiguration{
		
		@Bean
		public CriteriaService criteriaService() {
		return Mockito.mock(CriteriaService.class);
		}
		
		@Bean
		public CriteriaController criteriaController() {
		return new CriteriaController();
		}
		
	}
	@Autowired
	private CriteriaController criteriaController;
	
	
	/*Below test case covers returned value,adding model attributes from SelectCriteria method.
	 * returned value should : selectCriteria
	 * model attributes added : criteriaBean and criteriaList.
	 *   */
	@Test
	public void testSelectCriteria() throws Exception {
		

			MockMvc mockMvc = MockMvcBuilders.standaloneSetup(
					this.criteriaController).build();

			
				mockMvc.perform(MockMvcRequestBuilders.get("/selectCriteria"))
						.andExpect(
								MockMvcResultMatchers.model().attributeExists(
										"criteriaBean"))
						.andExpect(
								MockMvcResultMatchers.model().attributeExists(
										"criteriaList"))
						.andExpect(
								MockMvcResultMatchers.view().name("criteriaList"));
		

		

	}
	

}
