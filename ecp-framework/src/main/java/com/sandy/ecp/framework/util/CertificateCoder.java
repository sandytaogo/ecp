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
package com.sandy.ecp.framework.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.security.crypto.codec.Hex;

public class CertificateCoder {
	
	/**
	获得私钥，获得私钥后，通过RSA算方法实现进行"私钥加密，公钥解密"和"公钥加密，私钥解密"操作
	@param keyStorePath 密钥库路径
	@param alias 别名
	@param password 密码
	@return 私钥
	*/
	private static PrivateKey getPrivateKeyByKeyStore(String keyStorePath,String alias,String password)throws Exception {
		//获得密钥库
		KeyStore ks = getKeyStore(keyStorePath,password);
		//获得私钥
		return (PrivateKey)ks.getKey(alias, password.toCharArray());
	}

	/**
	由Certificate获得公钥，获得公钥后，通过RSA算方法实现进行"私钥加密，公钥解密"和"公钥加密，私钥解密"操作
	@param certificatePath 证书路径
	@return 公钥
	*/
	private static PublicKey getPublicKeyByCertificate(String certificatePath)throws Exception {
		//获得证书
		Certificate certificate = getCertificate(certificatePath);
		//获得公钥
		return certificate.getPublicKey();
	}

	/*
	加载数字证书，JAVA 6仅支持x.509的数字证书
	@param certificatePath 证书路径
	@return 证书
	@throws Exception
	*/
	private static Certificate getCertificate(String certificatePath) throws Exception {
		//实例化证书工厂
		CertificateFactory certificateFactory = CertificateFactory.getInstance("x.509");
		//取得证书文件流
		FileInputStream in = new FileInputStream(certificatePath);
		//生成证书
		Certificate certificate = certificateFactory.generateCertificate(in);
		//关闭证书文件流
		in.close();
		return certificate;

	}

	/**
	 获得Certificate
	 @param keyStorePath 密钥库路径
	 @param alias 别名
	 @param password 密码
	 @return 证书
	 @throws Exception
	 */
	private static Certificate getCertificate(String keyStorePath,String alias,String password) throws Exception {
		//由密钥库获得数字证书构建数字签名对象
		//获得密钥库
		KeyStore ks = getKeyStore(keyStorePath,password);
		//获得证书
		return ks.getCertificate(alias);
	}

	/**
	加载密钥库，加载了以后，我们就能通过相应的方法获得私钥，也可以获得数字证书
	@param keyStorePath 密钥库路径
	@param password 密码
	@return 密钥库
	@throws Exception
	*/
	private static KeyStore getKeyStore(String keyStorePath,String password) throws Exception {
		//实例化密钥库
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		//获得密钥库文件流
		FileInputStream is = new FileInputStream(keyStorePath);
		//加载密钥库
		ks.load(is,password.toCharArray());
		//关闭密钥库文件流
		is.close();
		return ks;
	}

	/**
	 私钥加密
	 @param data 待加密的数据
	 @param keyStorePath 密钥库路径
	 @param alias 别名
 	 @param password 密码
	 @return 加密数据
	 @throws Exception
	*/
	public static byte[] encryptByPriateKey(byte[] data,String keyStorePath,String alias,String password) throws Exception {
		//获得私钥
		PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath,alias,password);
		//对数据加密
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		return cipher.doFinal(data);
	}

	/**
	私钥解密
	@param data 待解密数据
	@param keyStorePath 密钥库路径
	@param alias 别名
	@param password 密码
	@return 解密数据
	@throws Exception
	*/
	public static byte[] decryptByPrivateKey(byte[] data,String keyStorePath,String alias,String password) throws Exception {
		//取得私钥
		PrivateKey privateKey = getPrivateKeyByKeyStore(keyStorePath,alias,password);
		//对数据解密
		Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE,privateKey);
		return cipher.doFinal(data);
	}

	/**
	公钥加密
	@param data 等待加密数据
	@param certificatePath 证书路径
	@return 加密数据
	@throws Exception
	*/
	public static byte[] encryptByPublicKey(byte[] data,String certificatePath) throws Exception {
		//取得公钥
		PublicKey publicKey = getPublicKeyByCertificate(certificatePath);
		//对数据加密
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE,publicKey);
		return cipher.doFinal(data);
	}

	/**
	公钥解密
	@param data 等待解密的数据
	@param certificatePath 证书路径
	@return 解密数据
	@throws Exception
	*/
	public static byte[] decryptByPublicKey(byte[] data,String certificatePath)throws Exception {
		//取得公钥
		//代码效果参考：http://www.zidongmutanji.com/bxxx/327188.html
		PublicKey publicKey = getPublicKeyByCertificate(certificatePath);
		//对数据解密
		Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	@param sign 签名
	@param keyStorePath 密钥库路径
	@param alias 别名
	@param password 密码
	@return 签名
	@throws Exception
	*/
	public static byte[] sign(byte[] sign,String keyStorePath,String alias,String password)throws Exception {
		//获得证书
		X509Certificate x509Certificate = (X509Certificate) getCertificate(keyStorePath,alias,password);
		//构建签名,由证书指定签名算法
		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		//获取私钥
		PrivateKey privateKey = //代码效果参考：http://www.zidongmutanji.com/bxxx/137524.html
		getPrivateKeyByKeyStore(keyStorePath, alias, password);
		//初始化签名，由私钥构建
		signature.initSign(privateKey);
		signature.update(sign);
		return signature.sign();
	}

	/**
	验证签名
	@param data 数据
	@param sign 签名
	@param certificatePath 证书路径
	@return 验证通过为真
	@throws Exception
	*/
	public static boolean verify(byte[] data,byte[] sign,String certificatePath) throws Exception {
		//获得证书
		X509Certificate x509Certificate = (X509Certificate) getCertificate(certificatePath);
		//由证书构建签名
		Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
		//由证书初始化签名，实际上是使用了证书中的公钥
		signature.initVerify(x509Certificate);
		signature.update(data);
		return signature.verify(sign);
	}
	
	public void certConverterPEM() throws IOException, CertificateException {
        FileInputStream in = new FileInputStream("cert.cer");
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) factory.generateCertificate(in);
        StringWriter sw = new StringWriter();
        JcaPEMWriter pemWriter = new JcaPEMWriter(sw);
        pemWriter.writeObject(cert);
        pemWriter.close();
        String pemString = sw.toString();
        System.out.println(pemString);
	}
	
	public static void save(String filePath, String alias, String passwrod, KeyPair keyPair, X509Certificate certificate) throws Exception {
		// 创建KeyStore实例
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(null, passwrod.toCharArray());
        // 获取公钥和私钥
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        // 存储密钥对到KeyStore（别名设置为"mykey"）
        keyStore.setKeyEntry(alias, privateKey, passwrod.toCharArray(), new Certificate[]{certificate});
        // 创建文件输出流以保存PFX文件
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            keyStore.store(fos, passwrod.toCharArray());
        }
	}
	
	private  static String keyStorePath = "",certificatePath = "", alias = "test", password = "test";

	/**
	//我们假定密钥库文件yale.keystore存储在D盘根目录，//代码效果参考：http://www.zidongmutanji.com/zsjx/171105.html
	数字证书文件yale.cer也存储在D盘根目录
	/**
	公钥加密---私钥解密
	@throws Exception
	*/
	public static void test1() throws Exception {
		System.err.println("公钥加密---私钥解密");
		String inputStr = "数字证书";
		byte[] data = inputStr.getBytes();
		//公钥加密
		byte[] encrypt = CertificateCoder.encryptByPublicKey(data, certificatePath);
		//私钥解密
		byte[] decrypt = CertificateCoder.decryptByPrivateKey(encrypt, keyStorePath, alias, password);
		String outputStr = new String(decrypt);
		System.err.println("加密前：\n" + inputStr);
		System.err.println("解密后：\n" + outputStr);
	}
	
	/**
	 * 证书DN（Distinguished Name，可分辨名称）‌ 是X.509数字证书中用于‌唯一标识证书持有者（实体）‌ 的字段，广泛用于SSL/TLS、IPSec、PKI等安全场景中。
	 * DN 的核心作用唯一标识证书主题（Subject），如网站、服务器、公司或个人。
	 * 在身份验证过程中，客户端通过比对DN中的信息（如域名）确认服务端身份。
	 * 若DN与实际访问地址不匹配，浏览器或系统会发出安全警告 ‌
	 * 常见DN字段及含义
	 * ‌CN（Common Name）‌：通用名，通常是域名（如 www.example.com）或服务器主机名。
	 * ‌O（Organization）‌：组织名称，如公司或机构全称（如 Example Inc.）。
	 * ‌OU（Organizational Unit）‌：组织单位，如部门（如 IT Department）。
	 * ‌L（Locality）‌：所在地（城市）。
	 * ‌ST（State or Province）‌：州或省。
	 * ‌C（Country）‌：国家代码（2位ISO代码，如 CN、US）‌
	 * 示例DN字符串：CN=www.example.com, O=Example Inc., OU=Marketing, L=New York, ST=New York, C=US ‌10
	 */
	public void genDistinguishedName() {
		
	}
	
	/**
	私钥加密---公钥解密
	@throws Exception
	*/
	public static void test2()throws Exception {
		System.err.println("私钥加密---公钥解密");
		String inputStr = "数字签名";
		byte[] data = inputStr.getBytes();
		//私钥加密
		byte[] encodedData = CertificateCoder.encryptByPriateKey(data, keyStorePath, alias, password);
		//公钥加密
		byte[] decodeData = CertificateCoder.decryptByPublicKey(encodedData, certificatePath);
		String outputStr = new String (decodeData);
		System.err.println("加密前：\n" + inputStr);
		System.err.println("解密后：\n" + outputStr);
	}

	public static void testSign()throws Exception {
		String inputStr = "签名";
		byte[] data = inputStr.getBytes();
		System.err.println("私钥签名---公钥验证");
		//产生签名
		byte[] sign = CertificateCoder.sign(data, keyStorePath, alias, password);
		System.err.println("签名:\n" + Hex.encode(sign));
		//验证签名
		boolean status = CertificateCoder.verify(data, sign, certificatePath);
		System.err.println("状态：\n " + status);
	}
}
