/*
 * Copyright 2018-2030 the original author or authors.
 *
 * Licensed under the company, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.company.com/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.framework.session;

import com.sandy.ecp.framework.vo.AbstractVO;

/**
 * Enterprise Cloud Platform Session Value Object
 * @author Sandy
 * @param <PK>
 * @version 1.0.0
 * @since 1.0.0 04th 12 2018
 */
public class SessionVO extends AbstractVO<String> {

	private static final long serialVersionUID = 7243856603048287782L;

	public static final String KEY = "SESSIONVO_AUTH_KEY";
	
	public static final String TEMPORARY_KEY = "TEMPORARY_SESSIONVO_KEY";
	
	public static final String BG_KEY = "BG_SESSIONVO_AUTH_KEY";
	
	public static final String BG_TEMPORARY_KEY = "BG_TEMPORARY_SESSIONVO_KEY";
	
	private Long userId;
	private String account;
	private String mobile;
	private String nickName;
	/**
	 * 密码学公钥
	 */
	private String publicKey;
	/**
	 * 密码学私钥
	 */
	private String privateKey;
	/**
	 * 用户公钥
	 */
	private String userPublicKey;
	private String sm4;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getUserPublicKey() {
		return userPublicKey;
	}
	public void setUserPublicKey(String userPublicKey) {
		this.userPublicKey = userPublicKey;
	}
	public String getSm4() {
		return sm4;
	}
	public void setSm4(String sm4) {
		this.sm4 = sm4;
	}
	@Override
	public String toString() {
		return "SessionVO [userId=" + userId + ", account=" + account + ", mobile=" + mobile + ", nickName=" + nickName + "]";
	}
}
