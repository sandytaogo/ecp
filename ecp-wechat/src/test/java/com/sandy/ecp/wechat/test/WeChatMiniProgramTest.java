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

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.sandy.ecp.wechat.constant.WechatConstants;
import com.sandy.ecp.wechat.utils.HttpUtils;

/**
 * 微信小程序单元测试用例.
 * @author Sandy
 * @since 1.0.0 04th 12 2024
 */
public class WeChatMiniProgramTest {

	@BeforeClass
	public static void initBefore() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		// 创建SSLContextBuilder
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, new TrustSelfSignedStrategy()) // 信任所有自签名证书
                .build();
        sslContext.createSSLEngine();
		SSLConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
//		SSLContextBuilder ctx = SSLContextBuilder.create();
//      SSLContext sslctx = ctx.build();
//		sslSocketFactory = new SSLConnectionSocketFactory(sslctx);
//      sslFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		
		HttpUtils.setSslSocketFactory(sslSocketFactory);
	}
	
	@Test
	@Ignore
	public void authTest() throws IOException {
		String url = WechatConstants.MINI_PROGRAM_LOGIN_URL + "?appid=wwer&secret=dgdfgfd&grant_type=authorization_code&js_code=dsfds";
        String result = HttpUtils.doGet(url);
        System.out.println(result);
	}
	
}
