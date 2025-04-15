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
package com.sandy.ecp.wechat.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 微信访问令牌 model.<br>
 * 错误码	错误描述	解决方案<br>
 * -1	system error	系统繁忙，此时请开发者稍候再试<br>
 * 40001	invalid credential  access_token isinvalid or not latest 获取 access_token 时 AppSecret 错误，或者 access_token 无效。请开发者认真比对 AppSecret 的正确性，或查看是否正在为恰当的公众号调用接口<br>
 * 40013	invalid appid	不合法的 AppID ，请开发者检查 AppID 的正确性，避免异常字符，注意大小写<br>
 * 40002	invalid grant_type	不合法的凭证类型<br>
 * 40125	不合法的 secret	请检查 secret 的正确性，避免异常字符，注意大小写<br>
 * 40164	调用接口的IP地址不在白名单中	请在接口IP白名单中进行设置<br>
 * 41004	appsecret missing	缺少 secret 参数<br>
 * 50004	禁止使用 token 接口	<br>
 * 50007	账号已冻结	<br>
 * 61024	第三方平台 API 需要使用第三方平台专用 token	<br>
 * 40243	AppSecret已被冻结，请登录小程序平台解冻后再次调用。	<br>
 * 
 * @author Sandy
 * @since 1.0.0 04th 12 2024
 */
public class WeChatAccessTokenVO extends AbstractWechatVO {
	
	private static final long serialVersionUID = -871416941651556418L;

	/**
	 * 获取到的凭证
	 */
	@JsonProperty("access_token")
	private String accessToken;
	
	/**
	 * 凭证有效时间，单位：秒。目前是7200秒之内的值。
	 */
	@JsonProperty("expiresIn")
	private Integer expires_in;

	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Integer getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}
}
