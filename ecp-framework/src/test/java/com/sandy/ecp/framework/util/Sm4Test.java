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

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.junit.Test;


/**
 * 国产加密测试.
 * @author Sandy
 * @since 1.0.0 28th 12 2018
 */
public class Sm4Test {

	@Test
	public void sipmeTest() throws UnsupportedEncodingException {
		// 密钥
		byte[] key = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef, (byte) 0xfe,
				(byte) 0xdc, (byte) 0xba, (byte) 0x98, 0x76, 0x54, 0x32, 0x10 };
		String plainText = "密码学测试！";
		byte[] enOut = Sm4.encode(plainText, key);
		System.out.println("加密结果：");
		System.out.println(Sm4.toHexString(enOut));

		byte[] deOut = Sm4.decode(enOut, key);
		System.out.println("解密结果(return byte[])：");
		System.out.println(Arrays.toString(deOut));

		String deOutStr = Sm4.decodeToString(enOut, key);
		System.out.println("解密结果(return String)：" + deOutStr);
	}
	
	
	@Test
	public void test() throws Exception {
		Sm4Util.generateKey();
	}
}
