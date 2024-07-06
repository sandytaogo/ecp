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
package com.sandy.ecp.framework.session;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.TestingAuthenticationProvider;

/**
 * ECP session test case.
 * @author Sandy
 * @date 2024-02-02 12:12:12
 */
public class EcpSessionTest {
	
	private TestingAuthenticationProvider provider = new TestingAuthenticationProvider();
	
	private ProviderManager manager = null;
	
	@Before
	public void before() {
		ArrayList<AuthenticationProvider> list = new ArrayList<AuthenticationProvider>();
		list.add(provider);
		manager = new ProviderManager(list);
	}

	@Test
	@org.junit.jupiter.api.Test
	public void userTest() {
		SessionVO session = new SessionVO();
		session.setId("abc");
		session.setAccount("admin");
		session.setUserId(123L);
		session.setNickName("张三");
		EcpAuthenticationToken token = new EcpAuthenticationToken(null, null, null);
		token.setAuthenticationManager(manager);
		token.setDetails(session);
		System.out.println(token.getDetails());
	}
	
	@Test
	@org.junit.jupiter.api.Test
	public void  serializableUser() throws IOException {
		SessionVO session = new SessionVO();
		session.setId("abc");
		session.setAccount("admin");
		session.setUserId(123L);
		session.setNickName("张三");
		session.setMobile("135.......");
		FileOutputStream out = new FileOutputStream(EcpSessionTest.class.getResource("/").getPath() + "/tmp");
//		BufferedOutputStream bos = new BufferedOutputStream(null);
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(session);
		System.out.println(oos);
	}
}
