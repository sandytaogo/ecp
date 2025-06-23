/*
 * Copyright 2023-2033 the original author or authors.
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

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

/**
 * 国产sm2非对称加密工具.
 * @author Sandy
 * @date 2023-05-23 12:12:12
 */
public class Sm2Util {
	
	protected static final Logger logger = LoggerFactory.getLogger(Sm2KeyGenerator.class);
	
	private final static X9ECParameters sm2X9ecParameters;
	
	private final static ECParameterSpec ecDomainParameters;
	
	static {
		Security.addProvider(new BouncyCastleProvider());
		//椭圆曲线ECParameters ASN.1 结构
	   
		sm2X9ecParameters = GMNamedCurves.getByName("sm2p256v1");
	    //椭圆曲线公钥或私钥的基本域参数。
	    ecDomainParameters = new ECParameterSpec(sm2X9ecParameters.getCurve(), sm2X9ecParameters.getG(), sm2X9ecParameters.getN());
	}
	
	public static Map<String, String> keyGenerator1() throws Exception {
		final ECGenParameterSpec sm2Spec = new ECGenParameterSpec("sm2p256v1");
		// 获取一个椭圆曲线类型的密钥对生成器 new BouncyCastleProvider();
		final KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC", new BouncyCastleProvider());
		SecureRandom random = new SecureRandom();
		// 使用SM2的算法区域初始化密钥生成器
		kpg.initialize(sm2Spec, random);
		// 获取密钥对
		KeyPair keyPair = kpg.generateKeyPair();
		if (logger.isDebugEnabled()) {
			logger.debug("PrivateKey = {}", new String(Base64Utils.encode(keyPair.getPrivate().getEncoded())));
			logger.debug("PublicKey = {}", new String(Base64Utils.encode(keyPair.getPublic().getEncoded())));
		}
		Map<String, String> keys = new HashMap<String, String>();
		keys.put("privatekey", new String(Base64Utils.encode(keyPair.getPrivate().getEncoded())));
		keys.put("privatekeyHex", new String(org.apache.commons.codec.binary.Hex.encodeHex(keyPair.getPrivate().getEncoded())));
		
		keys.put("publickey", new String(Base64Utils.encode(keyPair.getPublic().getEncoded())));
		keys.put("publickeyHex", new String(org.apache.commons.codec.binary.Hex.encodeHex((keyPair.getPublic().getEncoded()))));
		return keys;
	}

	public static Map<String, String> keyGenerator2() throws Exception {
		Map<String, String> keys = new HashMap<String, String>();
		//sm2X9ecParameters sm2X9ecParameters = GMNamedCurves.getByName("sm2p256v1");
		ECDomainParameters domainParameters = new ECDomainParameters(sm2X9ecParameters.getCurve(), sm2X9ecParameters.getG(), sm2X9ecParameters.getN());
		//生成密钥对
		ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();
		keyPairGenerator.init(new ECKeyGenerationParameters(domainParameters, SecureRandom.getInstance("SHA1PRNG")));
		AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.generateKeyPair();
		//私钥，16进制格式，自己保存，格式如a2081b5b81fbea0b6b973a3ab6dbbbc65b1164488bf22d8ae2ff0b8260f64853
		BigInteger privatekey = ((ECPrivateKeyParameters) asymmetricCipherKeyPair.getPrivate()).getD();
		String privateKeyHex = privatekey.toString(16);
		HexBinaryAdapter fdsf = new HexBinaryAdapter();
		String pkey = DatatypeConverter.printBase64Binary(fdsf.unmarshal(privateKeyHex));
		if (logger.isDebugEnabled()) {
			logger.debug("privateKeyHex={}", privateKeyHex);
			logger.debug("privateKeyBase64={}", pkey);
		}
		keys.put("privatekey", privateKeyHex);
		//公钥，16进制格式，发给前端，格式如04813d4d97ad31bd9d18d785f337f683233099d5abed09cb397152d50ac28cc0ba43711960e811d90453db5f5a9518d660858a8d0c57e359a8bf83427760ebcbba
		ECPoint publicEcPoint = ((ECPublicKeyParameters) asymmetricCipherKeyPair.getPublic()).getQ();
		String publicKeyHex = Hex.toHexString(publicEcPoint.getEncoded(false));
		if (logger.isDebugEnabled()) {
			logger.debug("publicKeyHex={}", publicKeyHex);
			String base64 = org.apache.commons.codec.binary.Base64.encodeBase64String(publicEcPoint.getEncoded(false));
			logger.debug("publicKeyBase64={}", base64);
		}
		keys.put("publickey", publicKeyHex);
		return keys;
	}
	
	public static Map<String, String> keyGenerator() throws Exception {
		Map<String, String> keys = new HashMap<String, String>();
		//生成密钥对
//	    sm2X9ecParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
	    ECDomainParameters domainParameters = new ECDomainParameters(sm2X9ecParameters.getCurve(), sm2X9ecParameters.getG(), sm2X9ecParameters.getN());
	    ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();
	    try {
	        keyPairGenerator.init(new ECKeyGenerationParameters(domainParameters, SecureRandom.getInstance("SHA1PRNG")));
	    } catch (NoSuchAlgorithmException e) {
	        logger.error("NoSuchAlgorithmException", e);
	    }
	    AsymmetricCipherKeyPair asymmetricCipherKeyPair = keyPairGenerator.generateKeyPair();
	    //私钥，16进制格式
	    BigInteger privateKey = ((ECPrivateKeyParameters) asymmetricCipherKeyPair.getPrivate()).getD();
	    String privateKeyHex = privateKey.toString(16);
	    keys.put("privatekey", privateKeyHex);
	    //公钥，16进制格式
	    ECPoint ecPoint = ((ECPublicKeyParameters) asymmetricCipherKeyPair.getPublic()).getQ();
	    String publicKeyHex = Hex.toHexString(ecPoint.getEncoded(false));
//	    logger.info("privateKeyHex {}", privateKeyHex);
//	    logger.info("publicKeyHex {}", publicKeyHex);
	    keys.put("publickey", publicKeyHex);
		return keys;
	}
	
	/**
     * SM2加密算法
     * @param publicKey 公钥
     * @param data 待加密的数据
     * @param cipherMode 密文排列方式0-C1C2C3；1-C1C3C2；
     * @return 密文，BC库产生的密文带由04标识符，与非BC库对接时需要去掉开头的04
     */
    public static String encrypt(String publicKey, String data, int cipherMode) {
        // 获取一条SM2曲线参数
        //sm2X9ecParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        // 构造ECC算法参数，曲线方程、椭圆曲线G点、大整数N
        ECDomainParameters domainParameters = new ECDomainParameters(sm2X9ecParameters.getCurve(), sm2X9ecParameters.getG(), sm2X9ecParameters.getN());
        //提取公钥点
        ECPoint pukPoint = sm2X9ecParameters.getCurve().decodePoint(Hex.decode(publicKey));
        // 公钥前面的02或者03表示是压缩公钥，04表示未压缩公钥, 04的时候，可以去掉前面的04
        ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(pukPoint, domainParameters);
 
        SM2EngineExtend sm2Engine = new SM2EngineExtend();
         // 设置sm2为加密模式
        sm2Engine.init(true, cipherMode, new ParametersWithRandom(publicKeyParameters, new SecureRandom()));
 
        byte[] arrayOfBytes = null;
        try {
            byte[] in = data.getBytes();
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
        } catch (Exception e) {
            logger.error("SM2加密时出现异常:{}", e.getMessage(), e);
        }
        return Hex.toHexString(arrayOfBytes);
    }
    
    /**
     * @Description 公钥字符串转换为 BCECPublicKey 公钥对象
     * @Author msx
     * @param pubKeyHex 64字节十六进制公钥字符串(如果公钥字符串为65字节首个字节为0x04：表示该公钥为非压缩格式，操作时需要删除)
     * @return BCECPublicKey SM2公钥对象
     */
    public static BCECPublicKey getECPublicKeyByPublicKeyHex(String pubKeyHex) {
        //截取64字节有效的SM2公钥（如果公钥首个字节为0x04）
        if (pubKeyHex.length() > 128) {
            pubKeyHex = pubKeyHex.substring(pubKeyHex.length() - 128);
        }
        //将公钥拆分为x,y分量（各32字节）
        String stringX = pubKeyHex.substring(0, 64);
        String stringY = pubKeyHex.substring(stringX.length());
        //将公钥x、y分量转换为BigInteger类型
        BigInteger x = new BigInteger(stringX, 16);
        BigInteger y = new BigInteger(stringY, 16);
        //通过公钥x、y分量创建椭圆曲线公钥规范
        ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(sm2X9ecParameters.getCurve().createPoint(x, y), ecDomainParameters);
        //通过椭圆曲线公钥规范，创建出椭圆曲线公钥对象（可用于SM2加密及验签）
        return new BCECPublicKey("EC", ecPublicKeySpec, BouncyCastleProvider.CONFIGURATION);
    }    
    
    /**
     * @Description 公钥加密
     * @Author msx
     * @param publicKey SM2公钥
     * @param data      明文数据
     * @param modeType  加密模式
     * @return String
     */
    public static String encrypt(BCECPublicKey publicKey, String data, int modeType) {
        //加密模式
        SM2Engine.Mode mode = SM2Engine.Mode.C1C3C2;
        if (modeType != 1) {
            mode = SM2Engine.Mode.C1C2C3;
        }
        //通过公钥对象获取公钥的基本域参数。
        ECParameterSpec ecParameterSpec = publicKey.getParameters();
        ECDomainParameters ecDomainParameters = new ECDomainParameters(ecParameterSpec.getCurve(),
                ecParameterSpec.getG(), ecParameterSpec.getN());
        //通过公钥值和公钥基本参数创建公钥参数对象
        ECPublicKeyParameters ecPublicKeyParameters = new ECPublicKeyParameters(publicKey.getQ(), ecDomainParameters);
        //根据加密模式实例化SM2公钥加密引擎
        SM2Engine sm2Engine = new SM2Engine(mode);
        //初始化加密引擎
        sm2Engine.init(true, new ParametersWithRandom(ecPublicKeyParameters, new SecureRandom()));
        byte[] arrayOfBytes = null;
        try {
            //将明文字符串转换为指定编码的字节串
            byte[] in = data.getBytes(StandardCharsets.UTF_8);
            //通过加密引擎对字节数串行加密
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
        } catch (Exception e) {
            System.out.println("SM2加密时出现异常:" + e.getMessage());
            e.printStackTrace();
        }
        //将加密后的字节串转换为十六进制字符串
        return Hex.toHexString(arrayOfBytes);

    }
	
	public static String decrypt(String cipherData, String privateKey) throws Exception {
		//X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
		ECDomainParameters domainParameters = new ECDomainParameters(sm2X9ecParameters.getCurve(), sm2X9ecParameters.getG(), sm2X9ecParameters.getN());
		// 使用BC库加解密时密文以04开头，传入的密文前面没有04则补上
		if (!cipherData.startsWith("04")) {
			cipherData = "04" + cipherData;
		}
		byte[] cipherDataByte = Hex.decode(cipherData);
		//刚才的私钥Hex，先还原私钥
		BigInteger privateKeyD = new BigInteger(privateKey, 16);
		ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters);
		//用私钥解密
		SM2EngineExtend sm2Engine = new SM2EngineExtend();
		sm2Engine.init(false, 0, privateKeyParameters);
		byte [] declode = sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length);
		//processBlock得到Base64格式，记得解码
		//byte[] arrayOfBytes = java.util.Base64.getDecoder().decode(declode);
		String data = new String(declode, StandardCharsets.UTF_8);
		return data;
	}
}
