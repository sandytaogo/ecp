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
package com.sandy.ecp.framework.security;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.junit.jupiter.api.Test;

import com.sandy.ecp.framework.util.CertificateCoder;
import com.sandy.ecp.framework.util.RsaUtil;

import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.CertificateSubjectName;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

/**
 * Certificate Code 证书解码测试类.
 * @author Sandy
 * @since 2026-03-16 10:12:12
 */
public class CertificateCodeTestCase {
	
	
	public X509Certificate createSelfSignedCertificate(KeyPair keyPair) throws Exception {
	    String dn = "CN=Test, O=TestOrg, L=TestCity, ST=TestState, C=TestCountry";
	    X500Principal principal = new X500Principal(dn);
		/*
		 * X509v3CertificateBuilder builder = new X509v3CertificateBuilder( principal,
		 * BigInteger.valueOf(new SecureRandom().nextLong()), new
		 * Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24), // before date new
		 * Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365)), // after
		 * date, one year validity principal, keyPair.getPublic() );
		 */
	    PKCS10CertificationRequest RE = RsaUtil.generatePKCS10Request(keyPair, dn);
	    ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").setProvider("BC").build(keyPair.getPrivate());
	    
	    RE.getSubjectPublicKeyInfo();
	    
	    X509CertificateHolder certHolder = null;//builder.build(signer);
	    return new JcaX509CertificateConverter().setProvider("BC").getCertificate(certHolder);
	}
	
	public static X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {
        // 设置证书有效期等属性
        Calendar startDate = Calendar.getInstance(); startDate.add(Calendar.DAY_OF_MONTH, -1);
        Calendar endDate = Calendar.getInstance(); endDate.add(Calendar.YEAR, 1);
        X500Principal name = new X500Principal("CN=Test Certificate, OU=Test, O=Test, L=Test, ST=Test, C=US");
        AlgorithmId algorithmId = new AlgorithmId(AlgorithmId.SHA256_oid); // 使用SHA-256和RSA加密算法
        X509CertInfo info = new X509CertInfo();
        //X500Name issuerX500Name = (X500Name) rootX509Cert.get(X509CertImpl.NAME+ "." + X509CertImpl.INFO + "." + X509CertInfo.SUBJECT + "."+ CertificateSubjectName.DN_NAME);
        String subjectStr = "CN=zhaorb@jiangdatech.com,L=PU Dong,ST=Shang Hai,O=Jiangdatech,OU=ENTERPRISE APP,C=China,STREET=ZuChongZhi Road";
        X500Name subjectName = new X500Name(
        		"CN=www.jiangtech.com,L=ZuChongZhi road,ST=Shang Hai,O=Jiangdatech,OU=ENTERPRISE APP,C=China,STREET=ZuChongZhi Road");
        X500Name X500Name = new X500Name(subjectStr);
        info.set(X509CertInfo.VERSION, new CertificateVersion(CertificateVersion.V3)); // 版本3证书
        info.set(X509CertInfo.SERIAL_NUMBER, new CertificateSerialNumber(new BigInteger(64, new SecureRandom()))); // 随机序列号
        info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(X500Name)); // 主体名称
        info.set(X509CertInfo.ISSUER, new CertificateIssuerName(X500Name)); // 发行者名称，通常与主体相同，自签名证书时使用
        info.set(X509CertInfo.SUBJECT, X500Name);
        info.set(X509CertInfo.ISSUER, X500Name);
        info.set(X509CertInfo.NAME, X500Name); // 算法标识符
        info.set(X509CertInfo.VALIDITY, new CertificateValidity(startDate.getTime(), endDate.getTime())); // 有效期
        info.set(X509CertInfo.KEY, new CertificateX509Key(keyPair.getPublic())); // 公钥信息
        info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(algorithmId)); // 算法标识符
        info.set(X509CertInfo.KEY, new CertificateX509Key(keyPair.getPublic())); 
        // 签名证书（自签名）
        X509CertImpl cert = new X509CertImpl(info);
        return cert;
	}
	
	public static X509Certificate createSelfSignedCertifi(KeyPair keyPair) throws Exception {
        // Create a Certificate-related objects
        String algorithm = "SHA256withRSA"; // Signature algorithm
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        String certDN = "CN=CFCA Test CA, OU=JavaSoft, O=Sun Microsystems Inc., C=US,  L=City, ST=State";
        X500Principal issuerDN = new X500Principal(certDN); // Issuer Distinguished Name
        X500Principal subjectDN = new X500Principal(certDN); // Subject Distinguished Name
        long validity = 365L; // Validity period in days
        Date firstDate = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30); // Start date of certificate validity period
        Date lastDate = new Date(System.currentTimeMillis() + (validity * 1000L * 60 * 60 * 24)); // End date of certificate validity period
        BigInteger snBI = new BigInteger(64, new SecureRandom()); // Serial number of certificate (must be unique)
        byte[] encodedKey = keyPair.getPublic().getEncoded(); // Encode public key to byte array
        PublicKey pk = keyPair.getPublic(); // Public key of certificate subject (which is also the issuer in this case)
        PrivateKey sk = keyPair.getPrivate(); // Private key of certificate issuer (which is also the subject in this case)
        
        org.bouncycastle.asn1.x500.X500Name issuerD = new org.bouncycastle.asn1.x500.X500Name(certDN);
        org.bouncycastle.asn1.x500.X500Name subjectD = new org.bouncycastle.asn1.x500.X500Name(certDN);
        
        PKCS10CertificationRequest RE = RsaUtil.generatePKCS10Request(keyPair, certDN);
	    ContentSigner signer = new JcaContentSignerBuilder(algorithm).setProvider(BouncyCastleProvider.PROVIDER_NAME).build(keyPair.getPrivate());
        X509v3CertificateBuilder builder = new X509v3CertificateBuilder(issuerD, snBI, firstDate, lastDate, subjectD, RE.getSubjectPublicKeyInfo()); // Create certificate builder with all necessary parameters except signature field which is empty for now
        //signer = new JcaContentSignerBuilder(algorithm).setProvider("SUN").build(sk); // Create signer object with private key and signature algorithm
        X509CertificateHolder certHolder = builder.build(signer); // Build certificate holder with filled signature field using signer object created above
        return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certHolder.getEncoded())); // Convert certificate holder to X509Certificate object using CertificateFactory object created above and return it as result of method execution
    }
	
	/**
	 * 读取ukey 证书.
	 */
	@Test
	public void readUkeyTest() {
		File file = new File("i:/");
		if (!file.exists()) {
			System.out.println("ukey --- not exists ");
			return;
		}
		for (String list : file.list()) {
			System.out.println(list);
		}
	}
	
	/**
	 * 生产证书.
	 * @throws Exception 
	 */
	@Test
	public void saveCertTest() throws Exception {
		File file = new File("/store");
		if (!file.exists()) {
			file.mkdirs();
		}
		Security.addProvider(new BouncyCastleProvider());
		KeyPair kayPair = RsaUtil.generateRSAKeyPair();
		X509Certificate certificate = generateSelfSignedCertificate(kayPair);
		CertificateCoder.save("/store/test.pfx", "test", "123456", kayPair, certificate);
	}	
}
