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
package com.sandy.ecp.wechat.auth;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.ClientProtocolException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandy.ecp.wechat.config.WechatMiniProgramProperties;
import com.sandy.ecp.wechat.constant.WechatConstants;
import com.sandy.ecp.wechat.utils.HttpUtils;
import com.sandy.ecp.wechat.vo.WeChatAccessTokenVO;
import com.sandy.ecp.wechat.vo.WeChatJscode2SessionVO;

/**
 * 企业云平台微信小程序认证.
 * @author Sandy
 * @since 1.0.0 04th 12 2024
 */
public class MinProgramAuth {

	private WechatMiniProgramProperties wechatMiniProgramProperties;
	
	private ObjectMapper objectMapper;
	
	
	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	public void setWechatMiniProgramProperties(WechatMiniProgramProperties wechatMiniProgramProperties) {
		this.wechatMiniProgramProperties = wechatMiniProgramProperties;
	}
	
	/**
	 * 根据临时代号获取用户信息.
	 * @param code temporary authentication code.
	 * 属性	类型	必填	说明
     * appid	string	是	小程序 appId
     * secret	string	是	小程序 appSecret
     * js_code	string	是	登录时获取的 code，可通过wx.login获取
     * grant_type	string	是	授权类型，此处只需填写 authorization_code
     * 
	 * @return 换取 openId, sessionKey, unionId
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public WeChatJscode2SessionVO auth(String code) throws IOException {
		// 微信appId
        String appId = wechatMiniProgramProperties.getAppId();
        // 微信 secret
        String appSecret = wechatMiniProgramProperties.getSecret();
        String url = WechatConstants.MINI_PROGRAM_LOGIN_URL + "?appid=" + appId + "&secret=" + appSecret + "&grant_type=authorization_code&js_code=" + code;
        String result = HttpUtils.doGet(url);
        return objectMapper.readValue(result, WeChatJscode2SessionVO.class);
	}
	
	/**
	 * 接口调用凭证 /获取接口调用凭据
	 * @return
	 * @throws IOException
	 */
	public WeChatAccessTokenVO getAccessToken() throws IOException {
		// 微信appId
        String appId = wechatMiniProgramProperties.getAppId();
        // 微信 secret
        String appSecret = wechatMiniProgramProperties.getSecret();
        String url = WechatConstants.URL_GET_ACCESS_TOKEN.replace("APPID", appId).replace("APPSECRET", appSecret);
        String result = HttpUtils.doGet(url);
        return objectMapper.readValue(result, WeChatAccessTokenVO.class);
	}
	
}
