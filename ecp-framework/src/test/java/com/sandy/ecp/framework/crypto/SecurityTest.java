package com.sandy.ecp.framework.crypto;

import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SecurityTest {
	
	public final static String ENCODING = "UTF-8";
	//广西医科大学第一附属医院，桂林医学院附属医院,广西自治区人民医院
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException {
		 //随机生成iv
        SecureRandom srandom = new SecureRandom();
        byte[] iv = new byte[16];
        srandom.nextBytes(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		byte[] raw = "werewrrewrewrewr".getBytes(Charset.forName(ENCODING));
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		 //随机生成 aes密钥
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        @SuppressWarnings("unused")
		SecretKey skey = kgen.generateKey();
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);
		System.out.println("security out successly.");
		
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);
		System.out.println();
	}
}
