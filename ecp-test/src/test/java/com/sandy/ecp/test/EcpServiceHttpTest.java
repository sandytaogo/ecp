package com.sandy.ecp.test;

import org.junit.gen5.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ActiveProfiles(profiles = {})
@Sql(scripts = "scripts/SystemUser.sql")
@AutoConfigureMockMvc
@RunWith(JUnitPlatform.class)
public class EcpServiceHttpTest {

	@Autowired
	private MockMvc mockMvc;
	
	@org.junit.Test
	public void test() {
		System.out.println("test");
	}
	
	@Test
	public void envTest() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("envTest");
		ResultActions resultActions = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk());
		resultActions.andReturn().getResponse().setCharacterEncoding("utf-8");
		resultActions.andDo(MockMvcResultHandlers.print());
	}
}

