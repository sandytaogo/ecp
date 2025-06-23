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
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

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
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

/**
 * 
 * @author Sandy
 * @date 2023-05-23 12:12:12
 */
public class Sm2KeyGenerator {
	
	static {
	//	Security.addProvider(new BouncyCastleProvider());
	}
	
	protected static final Logger logger = LoggerFactory.getLogger(Sm2KeyGenerator.class);

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
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
	}
	
	public void makeGenerator() throws Exception {
		//生成密钥对
		X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
		ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
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
		//公钥，16进制格式，发给前端，格式如04813d4d97ad31bd9d18d785f337f683233099d5abed09cb397152d50ac28cc0ba43711960e811d90453db5f5a9518d660858a8d0c57e359a8bf83427760ebcbba
		ECPoint publicEcPoint = ((ECPublicKeyParameters) asymmetricCipherKeyPair.getPublic()).getQ();
		String publicKeyHex = Hex.toHexString(publicEcPoint.getEncoded(false));
		if (logger.isDebugEnabled()) {
			logger.debug("publicKeyHex={}", publicKeyHex);
			byte[] encodeHex = org.apache.commons.codec.binary.Hex.decodeHex(publicKeyHex);
			String base64 = org.apache.commons.codec.binary.Base64.encodeBase64String(encodeHex);
			logger.debug("publicKeyBase64={}", base64);
		}
		//JS加密产生的密文
		String cipherData = "04be17bf6fe47da1f34a01ad0ff67901241b72d103e998f2f7cc78a004703bdfb8d2c6e3939f4f708f3a57d872d58ec5c41bbe5976666bcb01acea43f5a1c68a62cc117c24821d17c3023035641894d7c978a5521f8dc6798515550c73071f9703602e0ee490157729b648c1cc3eb929c1a0501e12a216d42461117402";
		cipherData = "04911841113af2a0b40483d6766f56e752960a67a62200947a4c4e565d5da05d48ee59020d106f7eb3bb923ebcbc5bd18d2e2f6850aa53548c240250b1a942778138bf63cf5adacb8942ad0b316f9ba36b5dac7f8130649f5cdae7166396a43122a51d575e8fc7";
		byte[] cipherDataByte = Hex.decode(cipherData);
		//刚才的私钥Hex，先还原私钥
		String privateKey = "a2081b5b81fbea0b6b973a3ab6dbbbc65b1164488bf22d8ae2ff0b8260f64853";
		       privateKey = "4d9172d94707377c88a95b6999b645f81de33a4227a5305cc91ea67819dbe6ba";
		BigInteger privateKeyD = new BigInteger(privateKey, 16);
		ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters);
		//用私钥解密
		SM2Engine sm2Engine = new SM2Engine();
		sm2Engine.init(false, privateKeyParameters);
		byte [] declode = sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length);
		//processBlock得到Base64格式，记得解码
		//byte[] arrayOfBytes = java.util.Base64.getDecoder().decode(declode);
		//得到明文：SM2 Encryption Test
		String data = new String(declode);
		if (logger.isDebugEnabled()) {
			logger.debug("解码标准={}", data);
		}
	}
}
