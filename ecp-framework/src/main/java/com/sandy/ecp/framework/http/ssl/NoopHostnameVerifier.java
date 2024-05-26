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
package com.sandy.ecp.framework.http.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * SSL 不验证传输.
 * @author Sandy
 * @date 2003-02-01 12:12:12
 * @since 1.0.0
 */
public class NoopHostnameVerifier implements HostnameVerifier {
	
	public static final NoopHostnameVerifier INSTANCE = new NoopHostnameVerifier();

	public boolean verify(String s, SSLSession sslSession) {
		return true;
	}

	public final String toString() {
		return "NO_OP";
	}
}
