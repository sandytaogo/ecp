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

import com.sandy.ecp.wechat.config.WechatMiniProgramProperties;
import com.sandy.ecp.wechat.constant.WechatConstants;

public class MinProgramAuth {

	private WechatMiniProgramProperties wechatMiniProgramProperties;
	
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
	 */
	public Object auth(String code) {
		 // 微信appId
        String appId = wechatMiniProgramProperties.getAppId();
        // 微信 secret
        String appSecret = wechatMiniProgramProperties.getSecret();
        String url = WechatConstants.MINI_PROGRAM_LOGIN_URL + "?appid=" + appId + "&secret=" + appSecret + "&grant_type=authorization_code&js_code=" + code;
        return null;
	}
}
