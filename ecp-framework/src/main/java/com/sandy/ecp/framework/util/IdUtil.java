/*
 * Copyright 2018 the original author or authors.
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

import java.util.UUID;

/**
 * ID工具类， 用于生产ID
 * @author Administrator
 *
 */
public final class IdUtil {

	private static final long serverId;
	
	// 可通过配置方式进行初始化
	static {
		serverId = 1;
	}
	
	private static IdGenerator idGenerator = new IdGenerator(serverId);
	
	private IdUtil() {
		super();
	}
	
	public static long getLongId() {
		return idGenerator.nextId();
	}
	
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String getUUID2() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static void main(String [] args) {
		System.out.println(IdUtil.getUUID2());
	}
	
}
