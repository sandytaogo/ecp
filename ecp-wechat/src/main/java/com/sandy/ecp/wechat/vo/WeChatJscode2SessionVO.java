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
 * 腾讯微信小程序登录value object model.<br>
 * 40029	code 无效	js_code无效 <br>
 * 45011	api minute-quota reach limit  mustslower  retry next minute	API 调用太频繁，请稍候再试<br>
 * 40226	code blocked	高风险等级用户，小程序登录拦截 。风险等级详见用户安全解方案<br>
 * -1	    system error	系统繁忙，此时请开发者稍候再试<br>
 * @author Sandy
 * @since 1.0.0 04th 12 2024
 */
public class WeChatJscode2SessionVO extends AbstractWechatVO {

	private static final long serialVersionUID = -6050136358471353393L;

	/**
	 * 会话密钥
	 */
	@JsonProperty("session_key")
	private String sessionKey;
	/**
	 * 用户在开放平台的唯一标识符，若当前小程序已绑定到微信开放平台账号下会返回，详见 UnionID 机制说明。
	 * 开放能力 /用户信息 /UnionID 机制说明
	 * UnionID 机制说明
	 * 如果开发者拥有多个移动应用、网站应用、和公众账号（包括小程序），可通过 UnionID 来区分用户的唯一性，因为只要是同一个微信开放平台账号下的移动应用、网站应用和公众账号（包括小程序），
	 * 用户的 UnionID 是唯一的。换句话说，同一用户，对同一个微信开放平台下的不同应用，UnionID是相同的。
	 * UnionID获取途径
	 * 绑定了开发者账号的小程序，可以通过以下途径获取 UnionID。
	 * 开发者可以直接通过 wx.login + code2Session 获取到该用户 UnionID，无须用户授权。
	 * 小程序端调用云函数时，可在云函数中通过 Cloud.getWXContext 获取 UnionID。
	 * 用户在小程序（暂不支持小游戏）中支付完成后，开发者可以直接通过getPaidUnionId接口获取该用户的 UnionID，无需用户授权。注意：本接口仅在用户支付完成后的5分钟内有效，请开发者妥善处理。
	 * 微信开放平台绑定小程序流程
	 * 登录微信开放平台 — 管理中心 — 小程序 — 绑定小程序
	 */
	private String unionid;	
	/**
	 * 用户唯一标识
	 */
	private String openid;
	
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	@Override
	public String toString() {
		return "WeChatJscode2SessionVO [sessionKey=" + sessionKey + ", unionid=" + unionid + ", openid=" + openid + "]";
	}
}
