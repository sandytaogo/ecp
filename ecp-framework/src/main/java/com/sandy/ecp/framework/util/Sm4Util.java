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
package com.sandy.ecp.framework.util;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

/**
 * 企业云平台国产sm4加密工具类.
 * @author Sandy
 * @date 2023-05-23 12:12:12
 */
public class Sm4Util {
	
	public static final String ENCODING = "UTF-8";
	public static final String ALGORIGTHM_NAME = "SM4";
	public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS7Padding";
	public static final int DEFAULT_KEY_SIZE=128;
	
	public static String defaultKey;
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public static void setDefaultKey(String defaultKey) {
		Sm4Util.defaultKey = defaultKey;
	}
	
	/**
     * @Description:生成ecb暗号
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException */
	private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance(algorithmName,BouncyCastleProvider.PROVIDER_NAME);
		Key sm4Key = new SecretKeySpec(key, ALGORIGTHM_NAME);
		cipher.init(mode, sm4Key);
		return cipher;
	}
	
	/**
     *  @Description:自动生成密钥
     */
	public static byte[] generateKey() throws Exception {
		return generateKey(DEFAULT_KEY_SIZE);
	}
	
	public static byte[] generateKey(int keySize) throws Exception {
		KeyGenerator kg = KeyGenerator.getInstance(ALGORIGTHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
		kg.init(keySize, new SecureRandom());
		return kg.generateKey().getEncoded();
	}
	
	/**
     *  @Description:自动生成密钥
     */
	public static String generateKeyString() throws Exception {
		return ByteUtils.toHexString(generateKey(DEFAULT_KEY_SIZE));
	}
	
	/**
     * @throws Exception 
	 * @Description:加密
     */
	public static String encryptEcb(String text) throws Exception {
		return encryptEcb(defaultKey, text, ENCODING);
	}
	
    /**
     *  @Description:加密
     */
	public static String encryptEcb(String hexKey, String paramStr, String charset) throws Exception {
		String cipherText = "";
		if (null != paramStr && !"".equals(paramStr)) {
			byte[] keyData= ByteUtils.fromHexString(hexKey);
			charset=charset.trim();
			if (charset.length() <= 0) {
				charset = ENCODING;
			}
			byte[] srcData = paramStr.getBytes(charset);
			byte[] cipherArray = encrypt_Ecb_Padding(keyData, srcData);
			cipherText = ByteUtils.toHexString(cipherArray);
        }
        return cipherText;
    }

    /**
     *  @Description:加密模式之ecb
     */
    public static byte[] encrypt_Ecb_Padding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);
        byte[] bs = cipher.doFinal(data);
        return bs;
    }
    
    /**
     *  @Description:sm4解密
     */
    public static String decryptEcb(String text) throws Exception {
    	return decryptEcb(defaultKey, text, ENCODING);
    }
     /**
     *  @Description:sm4解密
     */
    public static String decryptEcb(String hexKey, String cipherText, String charset) throws Exception {
        String decryptStr = "";
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        byte[] srcData = decrypt_Ecb_Padding(keyData, cipherData);
        charset = charset.trim();
        if (charset.length() <= 0) {
            charset = ENCODING;
        }
        decryptStr = new String(srcData, charset);
        return decryptStr;
    }

    /**
     *  @Description:解密
     */
    public static byte[] decrypt_Ecb_Padding(byte[] key, byte[] cipherText) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    /**
     *  @Description:密码校验
     */
    public static boolean verifyEcb(String hexKey,String cipherText,String paramStr) throws Exception {
        boolean flag = false;
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        byte[] decryptData = decrypt_Ecb_Padding(keyData,cipherData);
        byte[] srcData = paramStr.getBytes(ENCODING);
        flag = Arrays.equals(decryptData, srcData);
        return flag;
    }
}
