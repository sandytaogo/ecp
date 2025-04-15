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
package com.sandy.ecp.wechat.test;

import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.Base64;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sandy.ecp.wechat.utils.WeChatAesUtil;

/**
 * 微信加密通信算法单元测试用例.
 * @author Sandy
 * @since 1.0.0 04th 12 2024
 */
public class WeChatAesTest {

	/*
	 * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
	 */
	private String secretKey = "0123456789abcdef";
	private String iv = "0123456789abcdef";
	
	@BeforeClass
	public static void staticBefore() {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	@Test
	public void aes128CbcTest() {
		String data = "test";
		try {
			byte [] encode = WeChatAesUtil.aes128CbcEncode(data, iv, secretKey);
			String base64code = Base64.getEncoder().encodeToString(encode);
			System.out.println("base64code=" + base64code);
			
			byte[] base64decode = Base64.getDecoder().decode(base64code);
			String content = WeChatAesUtil.aes128CbcDecode(base64decode, iv.getBytes(), secretKey.getBytes());
			System.out.println("decode=" + content);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
