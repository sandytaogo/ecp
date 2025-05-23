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
 * 腾讯微信 weixin 通信状态代码说明.
 * @author Sandy
 * @since 1.0.0 04th 12 2024
 */
public enum WechatCodeStatus {

    CODE_1(-1,"系统繁忙，此时请开发者稍候再试"),
    CODE_0(0,"请求成功"),
    CODE_40001(40001,"获取 access_token 时 AppSecret 错误，或者 access_token 无效。请开发者认真比对 AppSecret 的正确性，或查看是否正在为恰当的公众号调用接口"),
    CODE_40002(40002,"不合法的凭证类型"),
    CODE_40003(40003,"不合法的 OpenID ，请开发者确认 OpenID (该用户）是否已关注公众号，或是否是其他公众号的 OpenID"),
    CODE_40004(40004,"不合法的媒体文件类型"),
    CODE_40005(40005,"不合法的文件类型"),
    CODE_40006(40006,"不合法的文件大小"),
    CODE_40007(40007,"不合法的媒体文件 id"),
    CODE_40008(40008,"不合法的消息类型"),
    CODE_40009(40009,"不合法的图片文件大小"),
    CODE_40010(40010,"不合法的语音文件大小"),
    CODE_40011(40011,"不合法的视频文件大小"),
    CODE_40012(40012,"不合法的缩略图文件大小"),
    CODE_40013(40013,"不合法的 AppID ，请开发者检查 AppID 的正确性，避免异常字符，注意大小写"),
    CODE_40014(40014,"不合法的 access_token ，请开发者认真比对 access_token 的有效性(如是否过期），或查看是否正在为恰当的公众号调用接口"),
    CODE_40015(40015,"不合法的菜单类型"),
    CODE_40016(40016,"不合法的按钮个数"),
    CODE_40017(40017,"不合法的按钮类型"),
    CODE_40018(40018,"不合法的按钮名字长度"),
    CODE_40019(40019,"不合法的按钮 KEY 长度"),
    CODE_40020(40020,"不合法的按钮 URL 长度"),
    CODE_40021(40021,"不合法的菜单版本号"),
    CODE_40022(40022,"不合法的子菜单级数"),
    CODE_40023(40023,"不合法的子菜单按钮个数"),
    CODE_40024(40024,"不合法的子菜单按钮类型"),
    CODE_40025(40025,"不合法的子菜单按钮名字长度"),
    CODE_40026(40026,"不合法的子菜单按钮 KEY 长度"),
    CODE_40027(40027,"不合法的子菜单按钮 URL 长度"),
    CODE_40028(40028,"不合法的自定义菜单使用用户"),
    CODE_40029(40029,"无效的 oauth_code"),
    CODE_40030(40030,"不合法的 refresh_token"),
    CODE_40031(40031,"不合法的 openid 列表"),
    CODE_40032(40032,"不合法的 openid 列表长度"),
    CODE_40033(40033,"不合法的请求字符，不能包含 \\uxxxx 格式的字符"),
    CODE_40035(40035,"不合法的参数"),
    CODE_40038(40038,"不合法的请求格式"),
    CODE_40039(40039,"不合法的 URL 长度"),
    CODE_40048(40048,"无效的url"),
    CODE_40050(40050,"不合法的分组 id"),
    CODE_40051(40051,"分组名字不合法"),
    CODE_40060(40060,"删除单篇图文时，指定的 article_idx 不合法"),
    CODE_40117(40117,"分组名字不合法"),
    CODE_40118(40118,"media_id 大小不合法"),
    CODE_40119(40119,"button 类型错误"),
    CODE_40120(40120,"子 button 类型错误"),
    CODE_40121(40121,"不合法的 media_id 类型"),
    CODE_40125(40125,"无效的appsecret"),
    CODE_40132(40132,"微信号不合法"),
    CODE_40137(40137,"不支持的图片格式"),
    CODE_40155(40155,"请勿添加其他公众号的主页链接"),
    CODE_40163(40163,"oauth_code已使用"),
    CODE_41001(41001,"缺少 access_token 参数"),
    CODE_41002(41002,"缺少 appid 参数"),
    CODE_41003(41003,"缺少 refresh_token 参数"),
    CODE_41004(41004,"缺少 secret 参数"),
    CODE_41005(41005,"缺少多媒体文件数据"),
    CODE_41006(41006,"缺少 media_id 参数"),
    CODE_41007(41007,"缺少子菜单数据"),
    CODE_41008(41008,"缺少 oauth code"),
    CODE_41009(41009,"缺少 openid"),
    CODE_42001(42001,"access_token 超时，请检查 access_token 的有效期，请参考基础支持 - 获取 access_token 中，对 access_token 的详细机制说明"),
    CODE_42002(42002,"refresh_token 超时"),
    CODE_42003(42003,"oauth_code 超时"),
    CODE_42007(42007,"用户修改微信密码， accesstoken 和 refreshtoken 失效，需要重新授权"),
    CODE_43001(43001,"需要 GET 请求"),
    CODE_43002(43002,"需要 POST 请求"),
    CODE_43003(43003,"需要 HTTPS 请求"),
    CODE_43004(43004,"需要接收者关注"),
    CODE_43005(43005,"需要好友关系"),
    CODE_43019(43019,"需要将接收者从黑名单中移除"),
    CODE_44001(44001,"多媒体文件为空"),
    CODE_44002(44002,"POST 的数据包为空"),
    CODE_44003(44003,"图文消息内容为空"),
    CODE_44004(44004,"文本消息内容为空"),
    CODE_45001(45001,"多媒体文件大小超过限制"),
    CODE_45002(45002,"消息内容超过限制"),
    CODE_45003(45003,"标题字段超过限制"),
    CODE_45004(45004,"描述字段超过限制"),
    CODE_45005(45005,"链接字段超过限制"),
    CODE_45006(45006,"图片链接字段超过限制"),
    CODE_45007(45007,"语音播放时间超过限制"),
    CODE_45008(45008,"图文消息超过限制"),
    CODE_45009(45009,"接口调用超过限制"),
    CODE_45010(45010,"创建菜单个数超过限制"),
    CODE_45011(45011,"API 调用太频繁，请稍候再试"),
    CODE_45015(45015,"回复时间超过限制"),
    CODE_45016(45016,"系统分组，不允许修改"),
    CODE_45017(45017,"分组名字过长"),
    CODE_45018(45018,"分组数量超过上限"),
    CODE_45047(45047,"客服接口下行条数超过上限"),
    CODE_45064(45064,"创建菜单包含未关联的小程序"),
    CODE_45065(45065,"相同 clientmsgid 已存在群发记录，返回数据中带有已存在的群发任务的 msgid"),
    CODE_45066(45066,"相同 clientmsgid 重试速度过快，请间隔1分钟重试"),
    CODE_45067(45067,"clientmsgid 长度超过限制"),
    CODE_46001(46001,"不存在媒体数据"),
    CODE_46002(46002,"不存在的菜单版本"),
    CODE_46003(46003,"不存在的菜单数据"),
    CODE_46004(46004,"不存在的用户"),
    CODE_47001(47001,"解析 JSON/XML 内容错误"),
    CODE_48001(48001,"api 功能未授权，请确认公众号已获得该接口，可以在公众平台官网 - 开发者中心页中查看接口权限"),
    CODE_48002(48002,"粉丝拒收消息(粉丝在公众号选项中，关闭了 “ 接收消息 ” ）"),
    CODE_48004(48004,"api 接口被封禁，请登录 mp.weixin.qq.com 查看详情"),
    CODE_48005(48005,"api 禁止删除被自动回复和自定义菜单引用的素材"),
    CODE_48006(48006,"api 禁止清零调用次数，因为清零次数达到上限"),
    CODE_48008(48008,"没有该类型消息的发送权限"),
    CODE_50001(50001,"用户未授权该 api"),
    CODE_50002(50002,"用户受限，可能是违规后接口被封禁"),
    CODE_50005(50005,"用户未关注公众号"),
    CODE_53500(53500,"发布功能被封禁"),
    CODE_53501(53501,"频繁请求发布"),
    CODE_53502(53502,"Publish ID 无效"),
    CODE_53600(53600,"Article ID 无效"),
    CODE_61451(61451,"参数错误 (invalid parameter)"),
    CODE_61452(61452,"无效客服账号 (invalid kf_account)"),
    CODE_61453(61453,"客服帐号已存在 (kf_account exsited)"),
    CODE_61454(61454,"客服帐号名长度超过限制 ( 仅允许 10 个英文字符，不包括 @ 及 @ 后的公众号的微信号 )(invalid   kf_acount length)"),
    CODE_61455(61455,"客服帐号名包含非法字符 ( 仅允许英文 + 数字 )(illegal character in     kf_account)"),
    CODE_61456(61456,"客服帐号个数超过限制 (10 个客服账号 )(kf_account count exceeded)"),
    CODE_61457(61457,"无效头像文件类型 (invalid   file type)"),
    CODE_61450(61450,"系统错误 (system error)"),
    CODE_61500(61500,"日期格式错误"),
    CODE_63001(63001,"部分参数为空"),
    CODE_63002(63002,"无效的签名"),
    CODE_65301(65301,"不存在此 menuid 对应的个性化菜单"),
    CODE_65302(65302,"没有相应的用户"),
    CODE_65303(65303,"没有默认菜单，不能创建个性化菜单"),
    CODE_65304(65304,"MatchRule 信息为空"),
    CODE_65305(65305,"个性化菜单数量受限"),
    CODE_65306(65306,"不支持个性化菜单的帐号"),
    CODE_65307(65307,"个性化菜单信息为空"),
    CODE_65308(65308,"包含没有响应类型的 button"),
    CODE_65309(65309,"个性化菜单开关处于关闭状态"),
    CODE_65310(65310,"填写了省份或城市信息，国家信息不能为空"),
    CODE_65311(65311,"填写了城市信息，省份信息不能为空"),
    CODE_65312(65312,"不合法的国家信息"),
    CODE_65313(65313,"不合法的省份信息"),
    CODE_65314(65314,"不合法的城市信息"),
    CODE_65316(65316,"该公众号的菜单设置了过多的域名外跳(最多跳转到 3 个域名的链接）"),
    CODE_65317(65317,"不合法的 URL"),
    CODE_87009(87009,"无效的签名"),
    CODE_9001001(9001001,"POST 数据参数不合法"),
    CODE_9001002(9001002,"远端服务不可用"),
    CODE_9001003(9001003,"Ticket 不合法"),
    CODE_9001004(9001004,"获取摇周边用户信息失败"),
    CODE_9001005(9001005,"获取商户信息失败"),
    CODE_9001006(9001006,"获取 OpenID 失败"),
    CODE_9001007(9001007,"上传文件缺失"),
    CODE_9001008(9001008,"上传素材的文件类型不合法"),
    CODE_9001009(9001009,"上传素材的文件尺寸不合法"),
    CODE_9001010(9001010,"上传失败"),
    CODE_9001020(9001020,"帐号不合法"),
    CODE_9001021(9001021,"已有设备激活率低于 50% ，不能新增设备"),
    CODE_9001022(9001022,"设备申请数不合法，必须为大于 0 的数字"),
    CODE_9001023(9001023,"已存在审核中的设备 ID 申请"),
    CODE_9001024(9001024,"一次查询设备 ID 数量不能超过 50"),
    CODE_9001025(9001025,"设备 ID 不合法"),
    CODE_9001026(9001026,"页面 ID 不合法"),
    CODE_9001027(9001027,"页面参数不合法"),
    CODE_9001028(9001028,"一次删除页面 ID 数量不能超过 10"),
    CODE_9001029(9001029,"页面已应用在设备中，请先解除应用关系再删除"),
    CODE_9001030(9001030,"一次查询页面 ID 数量不能超过 50"),
    CODE_9001031(9001031,"时间区间不合法"),
    CODE_9001032(9001032,"保存设备与页面的绑定关系参数错误"),
    CODE_9001033(9001033,"门店 ID 不合法"),
    CODE_9001034(9001034,"设备备注信息过长"),
    CODE_9001035(9001035,"设备申请参数不合法"),
    CODE_9001036(9001036,"查询起始值 begin 不合法");

    private int code;

    private String msg;

    WechatCodeStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
