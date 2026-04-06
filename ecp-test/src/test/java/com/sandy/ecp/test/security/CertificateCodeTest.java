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
package com.sandy.ecp.test.security;

import java.io.File;
import java.security.Key;
import java.security.KeyStore;
import java.security.Security;

import org.junit.jupiter.api.Test;

/**
 * Certificate Code 证书解码测试类.
 * @author Sandy
 * @since 2026-03-16 10:12:12
 */
public class CertificateCodeTest {
	
	private static String alias = "test";
	
	private static String password = "123456";
	
	private static String library = "C:\\windows\\system32\\opensc-pkcs11.dll";
	
	/**
	 * 读取u-key 证书.
	 */
	@Test
	public void readUkeyTest() {
		File filePath = new File("i:");
		File[] files = filePath.listFiles();
		for (File file : files) {
			System.out.println(String.format("directory=%s, isHidden=%s", file.isDirectory(), file.isHidden()));
			System.out.println(file.getPath());
		}
		System.out.println("finish...");
	}
	
	public static void main(String[] args) throws Exception {
		System.loadLibrary(library);
		String pin = "###";
		Security.addProvider(new sun.security.pkcs11.SunPKCS11("windows.cnf"));
		KeyStore keyStore = KeyStore.getInstance("PKCS11", "SunPKCS11-dnie");
		keyStore.load(null, pin.toCharArray());
		Key key = keyStore.getKey(alias, password.toCharArray());
		System.out.println(key);
	}
	
}
