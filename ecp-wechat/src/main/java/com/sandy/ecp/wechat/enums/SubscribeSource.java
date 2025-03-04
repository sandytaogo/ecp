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
package com.sandy.ecp.wechat.enums;

/**
 * 微信订阅来源声明.
 * @author Sandy
 * @since 1.0.0 04th 12 2022
 */
public enum SubscribeSource {

    ADD_SCENE_SEARCH(1,"公众号搜索"),
    ADD_SCENE_PROFILE_CARD(2,"名片分享"),
    ADD_SCENE_QR_CODE(4,"扫描二维码"),
    ADD_SCENE_PROFILE_LINK(8,"图文页内名称点击"),
    ADD_SCENE_PROFILE_ITEM(16,"图文页右上角菜单"),
    ADD_SCENE_PAID(32,"支付后关注"),
    ADD_SCENE_WECHAT_ADVERTISEMENT(64,"微信广告"),
    ADD_SCENE_REPRINT(128,"他人转载"),
    ADD_SCENE_LIVESTREAM(256,"视频号直播"),
    ADD_SCENE_CHANNELS(512,"视频号"),
    ADD_SCENE_ACCOUNT_MIGRATION(1024,"公众号迁移"),
    ADD_SCENE_OTHERS(2048,"其他");

    private int type;

    private String desc;

    SubscribeSource(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

	public int getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}

}
