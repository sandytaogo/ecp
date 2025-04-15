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
package com.sandy.ecp.wechat.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

/**
 * 微信通信 HTTP协议工具类.
 * @author Sandy
 * @since 1.0.0 01th 12 2025
 */
public class HttpUtils {
	
	private static Log log = LogFactory.getLog(HttpUtils.class);
	
	private static LayeredConnectionSocketFactory sslSocketFactory = null;
	
	public static void setSslSocketFactory(LayeredConnectionSocketFactory sslSocketFactory) {
		HttpUtils.sslSocketFactory = sslSocketFactory;
	}
	
	public static String doGet(String url) throws IOException {
        HttpGet get = new HttpGet(url);
        HttpClientBuilder clientBuilder = HttpClients.custom();
        clientBuilder.setSSLSocketFactory(sslSocketFactory);
        StringBuilder sb = new StringBuilder();
        CloseableHttpResponse response = null;
        try {
        	CloseableHttpClient client = clientBuilder.build();
            response = client.execute(get);
            HttpEntity entity = response.getEntity();
            BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
            String line;
            while ((line = br.readLine()) != null) {
            	sb.append(line);
            }
            if (log.isDebugEnabled()) {
            	log.debug(String.format("doGet response=%s", sb.toString()));
            }
        } finally {
			if (response != null) {
				response.close();
			}
		}
		return sb.toString();
	}
	
}
