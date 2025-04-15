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
package com.sandy.ecp.wechat.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 微信通信数字签名工具类 signature = sha1( rawData + session_key )
 * 开发者将 signature、rawData 发送到开发者服务器进行校验。服务器利用用户对应的 key 使用相同的算法计算出签名 signature2 ，比对 signature 与 signature2 即可校验数据的完整性
 * @author Sandy
 * @since 1.0.0 04th 12 2018
 */
public final class WeChatSignUtil {	
	
	private WeChatSignUtil() {
		super();
	}
	
	/**
     * 验证签名
     *
     * @param content 签名数据.
     * @param signature 签名数据.
     * @return 验签是否通过.
     */
    public static boolean checkSignature(String content, String signature) {
        String verify = null;
        try {
        	MessageDigest md = MessageDigest.getInstance("SHA-1");
            //字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            verify = byteToHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return verify != null ? verify.equals(signature.toUpperCase()) : false;
    }

	/**
     * 验证签名
     *
     * @param token 令牌
     * @param timestamp 时间戳.
     * @param nonce 签名数据.
     * @param signature 签名数据.
     * @return 验签是否通过.
     */
    public static boolean checkSignature(String token, String timestamp, String nonce, String signature) {
    	//1.将token、timestamp、nonce三个参数进行字典序排序
        String[] arr = new String[] {token, timestamp, nonce};
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        String verify = null;
        try {
        	 MessageDigest md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            verify = byteToHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return verify != null ? verify.equals(signature.toUpperCase()) : false;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param bytes 字节数组.
     * @return hex string.
     */
    private static String byteToHexString(byte [] bytes) {
        char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        StringBuilder digest = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
        	char[] str = new char[2];
        	str[0] = digit[(bytes[i] >>> 4) & 0X0F];
        	str[1] = digit[bytes[i] & 0X0F];
            digest.append(str);
        }
        return digest.toString();
    }
}
