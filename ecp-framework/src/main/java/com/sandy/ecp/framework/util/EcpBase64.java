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
package com.sandy.ecp.framework.util;

import java.util.Base64;

/**
 * 企业云平台 base64 独特编码.
 * @author Sandy
 * @since 1.0.0 04th 12 2024
 */
public class EcpBase64 {
	
	public static String encodeUrlSafe(byte[] bytes, boolean padded) {
		String encodeString = Base64.getEncoder().encodeToString(bytes);// 此处使用BASE64做转码。
		// websafe base64
		encodeString = encodeString.replace("+", "-");
		encodeString = encodeString.replace("/", "_"); // nopadding base64
		if (!padded) {
			if (encodeString.endsWith("=")) {
				encodeString = encodeString.substring(0, encodeString.length() - 1);
				if (encodeString.endsWith("=")) {
					encodeString = encodeString.substring(0, encodeString.length() - 1);
				}
			}
		}
		return encodeString;
	}

	public static byte[] decodeUrlSafe(String content) { 
		// websafe base64
		content = content.replace("-", "+");
		content = content.replace("_", "/");
		return Base64.getDecoder().decode(content);
	}
}
