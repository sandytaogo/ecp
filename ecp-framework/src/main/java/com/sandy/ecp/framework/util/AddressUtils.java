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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Description: 
 * 
 * @author Sandy
 * @Date 2024年4月3日 下午3:41:06
 * @since 1.0.0
 */
public class AddressUtils {

	private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

	// IP地址查询
	public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

	// 未知地址
	public static final String UNKNOWN = "XX XX";

	public static String getRealAddressByIP(String ip) {
		// 内网不查询
		if (IpUtil.internalIp(ip)) {
			return "内网IP";
		}
		try {
			String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", "GBK");
			if (StringUtils.isEmpty(rspStr)) {
				log.error("获取地理位置异常 {}", ip);
				return UNKNOWN;
			}
			ObjectMapper om = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String, String> map = om.readValue(rspStr, HashMap.class);
			String region = map.get("pro");
			String city = map.get("city");
			return String.format("%s %s", region, city);
		} catch (Exception e) {
			log.error("获取地理位置异常 {}", e);
		}
		return UNKNOWN;
	}
}
