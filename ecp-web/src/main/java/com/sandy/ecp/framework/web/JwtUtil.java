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
package com.sandy.ecp.framework.web;

import java.util.Map;

import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;

/**
 * JWT 签名工具类.
 * @author Sandy
 * @since 2024-12-24 09：09：09
 */
public class JwtUtil {

	public static Map<String,Object> map() {
		JwsHeader<?> jws = Jwts.jwsHeader();
		return jws;
	}
	
}
