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
package com.sandy.ecp.wechat.enums;

/**
 * 腾讯微信 用户性别声明枚举.
 * @author Sandy
 * @since 1.0.0 04th 12 2024
 */
public enum WechatGender {
	
	UNKNOWN(1, "未知"),
	MALE(1, "男性"), 
	FEMALE(2, "女性");
	
	private int code;
	private String name;
	
	private WechatGender(int code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
}
