/*
 * Copyright 2020-2030 the original author or authors.
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
package com.sandy.ecp.framework.util;

import org.junit.Test;
/**  
 * Description: 
 * @author Sandy
 * @Date 2024年4月3日 下午6:44:23
 * @since 1.0.0
 */
public class IdTest {
	
	
	@Test
	public void utilTest() {
		System.out.println(IdUtil.getUUID());
	}
	
	@Test
	public void getLongIdTest() {
		System.out.println(IdUtil.getLongId());
	}
	@Test
	public void getUUID2Test() {
		System.out.println(IdUtil.getUUID2());
	}
	
}
