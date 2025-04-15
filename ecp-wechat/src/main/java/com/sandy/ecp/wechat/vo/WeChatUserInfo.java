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
 * 腾讯微信授权用户信息value object model.<br>

 * @author Sandy
 * @since 1.0.0 04th 12 2024
 */
public class WeChatUserInfo extends AbstractWechatVO {

	private static final long serialVersionUID = -661953997545720139L;
	
	/**
	 * 用户昵称
	 */
	private String nickName;
	/**
	 * 用户头像图片的 URL。
	 * URL 最后一个数值代表正方形头像大小（有 0、46、64、96、132 数值可选，0 代表 640x640 的正方形头像，46 表示 46x46 的正方形头像，
	 * 剩余数值以此类推。默认132），用户没有头像时该项为空。若用户更换头像，原有头像 URL 将失效。
	 */
	private String avatarUrl;
	/**
	 * 用户所在国家.
	 */
	private String country;
	/**
	 * 用户所在省份.
	 */
	private String province;
	/**
	 * 用户所在城市.
	 */
	private String city;
	/**
	 * 性别 0：未知、1：男、2：女
	 */
	private Integer	gender;
	/**
	 * 语言 显示 country，province，city 所用的语言。强制返回 “zh_CN”.
	 */
	private String language;
	
	@JsonProperty("is_demote")
	private Boolean isdemote;
	
	private WechatWatermark watermark;
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getGender() {
		return gender;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public Boolean getIsdemote() {
		return isdemote;
	}
	public void setIsdemote(Boolean isdemote) {
		this.isdemote = isdemote;
	}
	public WechatWatermark getWatermark() {
		return watermark;
	}
	public void setWatermark(WechatWatermark watermark) {
		this.watermark = watermark;
	}
}
