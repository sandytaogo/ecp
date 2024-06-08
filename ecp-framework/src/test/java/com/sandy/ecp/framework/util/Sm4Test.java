package com.sandy.ecp.framework.util;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class Sm4Test {

	
	public static void main(String[] args) throws UnsupportedEncodingException {
		// 密钥
		byte[] key = { 0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef, (byte) 0xfe,
				(byte) 0xdc, (byte) 0xba, (byte) 0x98, 0x76, 0x54, 0x32, 0x10 };
		String plainText = "密码学测试！";
		byte[] enOut = Sm4.encode(plainText, key);
		System.out.println("加密结果：");
		System.out.println(Sm4.toHexString(enOut));

		byte[] deOut = Sm4.decode(enOut, key);
		System.out.println("解密结果(return byte[])：");
		System.out.println(Arrays.toString(deOut));

		String deOutStr = Sm4.decodeToString(enOut, key);
		System.out.println("解密结果(return String)：" + deOutStr);
	}
	
}
