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
package com.sandy.ecp.wechat.constant;

/**
 * 微信通信地址常量声明.
 * @author Sandy
 * @since 1.0.0 04th 12 2022
 */
public final class WechatConstants {

	private WechatConstants() {
		super();
	}
	
	/**
	 * 小程序登录api
	 */
   public static final String MINI_PROGRAM_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
   
	/**
	 * 获取access_token url
	 */
    public static final String URL_GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    /**
     * 生成带参数的二维码
     */
    public static final String URL_CREATE_QRCODE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
    /**
     * 获取带参数的二维码
     */
    public static final String URL_GET_QRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
    /**
     * 获取关注用户信息
     */
    public static final String URL_GET_USERINFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";

    /**
     * 发送模板消息
     */
    public static final String MESSAGE_TEMPLATE_SEND = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    /**
     * 小程序获取手机号
     */
    public static final String MINI_PROGRAM_GET_USER_PHONE_NUMBER = "https://api.weixin.qq.com/wxa/business/getuserphonenumber?access_token=ACCESS_TOKEN";

    /**
     * 小程序获取 generate scheme
     */
    public static final String MINI_PROGRAM_GENERATE_SCHEME = "https://api.weixin.qq.com/wxa/generatescheme?access_token=ACCESS_TOKEN";

    /**
     * 小程序获取 generate URL Link
     */
    public static final String MINI_PROGRAM_GENERATE_URL_LINK = "https://api.weixin.qq.com/wxa/generate_urllink?access_token=ACCESS_TOKEN";

    /**
     * 小程序获取 generate Short Link
     */
    public static final String MINI_PROGRAM_GENERATE_SHORT_LINK = "https://api.weixin.qq.com/wxa/genwxashortlink?access_token=ACCESS_TOKEN";
}
