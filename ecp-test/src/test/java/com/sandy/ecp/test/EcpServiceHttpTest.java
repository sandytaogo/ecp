/*
 * Copyright 2024-2030 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.test;

import java.nio.charset.StandardCharsets;

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
		resultActions.andReturn().getResponse().setCharacterEncoding(StandardCharsets.UTF_8.name());
		resultActions.andDo(MockMvcResultHandlers.print());
	}
}

