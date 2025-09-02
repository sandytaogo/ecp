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

import com.sandy.ecp.framework.util.ConvertUtil;
import com.sandy.ecp.framework.web.security.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * JWT 签名工具类.
 * 
 * @author Sandy
 * @since 2024-12-24 09：09：09
 */
public class JwtUtil {

	public static String secret = "xinxinji.cn_default";

	public static Map<String, Object> map() {
		JwsHeader<?> jws = Jwts.jwsHeader();
		return jws;
	}

	/**
	 * 从数据声明生成令牌
	 *
	 * @param claims 数据声明
	 * @return 令牌
	 */
	public static String createToken(Map<String, Object> claims) {
		String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
		return token;
	}

	/**
	 * 从令牌中获取数据声明
	 *
	 * @param token 令牌
	 * @return 数据声明
	 */
	public static Claims parseToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	/**
	 * 根据令牌获取用户标识
	 * 
	 * @param token 令牌
	 * @return 用户ID
	 */
	public static String getUserKey(String token) {
		Claims claims = parseToken(token);
		return getValue(claims, SecurityConstants.USER_KEY);
	}

	/**
	 * 根据令牌获取用户标识
	 * 
	 * @param claims 身份信息
	 * @return 用户ID
	 */
	public static String getUserKey(Claims claims) {
		return getValue(claims, SecurityConstants.USER_KEY);
	}

	/**
	 * 根据令牌获取用户ID
	 * 
	 * @param token 令牌
	 * @return 用户ID
	 */
	public static String getUserId(String token) {
		Claims claims = parseToken(token);
		return getValue(claims, SecurityConstants.DETAILS_USER_ID);
	}

	/**
	 * 根据身份信息获取用户ID
	 * 
	 * @param claims 身份信息
	 * @return 用户ID
	 */
	public static String getUserId(Claims claims) {
		return getValue(claims, SecurityConstants.DETAILS_USER_ID);
	}

	/**
	 * 根据令牌获取用户名
	 * 
	 * @param token 令牌
	 * @return 用户名
	 */
	public static String getUserName(String token) {
		Claims claims = parseToken(token);
		return getValue(claims, SecurityConstants.DETAILS_USERNAME);
	}

	/**
	 * 根据身份信息获取用户名
	 * 
	 * @param claims 身份信息
	 * @return 用户名
	 */
	public static String getUserName(Claims claims) {
		return getValue(claims, SecurityConstants.DETAILS_USERNAME);
	}

	/**
	 * 根据身份信息获取键值
	 * 
	 * @param claims 身份信息
	 * @param key    键
	 * @return 值
	 */
	public static String getValue(Claims claims, String key) {
		return ConvertUtil.toString(claims.get(key), "");
	}

}
