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

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERBMPString;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.bc.BcX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.crypto.engines.DESedeEngine;
import org.bouncycastle.crypto.engines.RC2Engine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.PKCS12PfxPdu;
import org.bouncycastle.pkcs.PKCS12PfxPduBuilder;
import org.bouncycastle.pkcs.PKCS12SafeBag;
import org.bouncycastle.pkcs.PKCS12SafeBagBuilder;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfoBuilder;
import org.bouncycastle.pkcs.bc.BcPKCS12MacCalculatorBuilder;
import org.bouncycastle.pkcs.bc.BcPKCS12PBEOutputEncryptorBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS12SafeBagBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS8EncryptedPrivateKeyInfoBuilder;
import org.bouncycastle.util.encoders.Base64;

import javafx.util.Duration;
/**
 * RSA非对称加密工具.
 * @author Sandy
 * @see https://blog.csdn.net/weixin_35390150/article/details/114567487
 */
public class RsaUtil {
	
	public static final String MD5_WITH_RSA = "MD5withRSA";
    public static final String SHA1_WITH_RSA = "SHA1withRSA";
    public static final String SHA256_WITH_RSA = "SHA256withRSA";
    
    public static final int KEY_SIZE_1024 = 1024;
    public static final int KEY_SIZE_2048 = 2048;

	public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048); // 可以选择不同的密钥长度，例如2048位
        return keyGen.generateKeyPair();
    }
	
	public static PKCS10CertificationRequest generatePKCS10Request(KeyPair keyPair, String subjectDN) throws Exception {
        PrivateKey privKey = keyPair.getPrivate();
        X500Name subject = new X500Name(subjectDN); // 例如 "CN=Test, OU=Test, O=Test, L=Test, ST=Test, C=US"
        AlgorithmIdentifier sigAlgId = new DefaultSignatureAlgorithmIdentifierFinder().find(SHA256_WITH_RSA);
        ContentSigner signer = new JcaContentSignerBuilder(SHA256_WITH_RSA).setProvider("BC").build(privKey);
        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(subject, keyPair.getPublic());
        PKCS10CertificationRequest csr = p10Builder.build(signer);
        return csr;
    }
	
	public static void writePKCS10ToFile(PKCS10CertificationRequest csr, String filename) throws IOException {
	    FileOutputStream fos = new FileOutputStream(filename);
	    fos.write(csr.getEncoded());
	    fos.close();

	}
	
	/**
	 * https://www.cnblogs.com/mylibs/p/cert-util-forged-by-swing-and-bouncycastle.html#rsa%E6%A0%B9%E8%AF%81%E4%B9%A6%E7%94%9F%E6%88%90%E9%A1%B5%E9%9D%A2
	 * @param signatureAlgorithm
	 * @param keyPair
	 * @param subject
	 * @param duration
	 * @return
	 * @throws OperatorCreationE
	 */
	public static X509Certificate generateCertificate(String signatureAlgorithm, KeyPair keyPair, X500Name subject, Duration duration) throws Exception {
	    ContentSigner contentSigner = null;//makeContentSigner(signatureAlgorithm, keyPair.getPrivate());
	    PKCS10CertificationRequest csr = null;//generateCsr(contentSigner, keyPair, subject, true);
	    BcX509ExtensionUtils extUtils = new BcX509ExtensionUtils();
	    final Date notBefore = new Date();
	    X509v3CertificateBuilder v3CertificateBuilder = new X509v3CertificateBuilder(
	            csr.getSubject(),
	            BigInteger.valueOf(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE),
	            notBefore,
	            new Date((long) (notBefore.getTime() + duration.toMillis())),
	            Locale.CHINA,
	            csr.getSubject(),
	            csr.getSubjectPublicKeyInfo());
	    v3CertificateBuilder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
	    v3CertificateBuilder.addExtension(Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign));
	    v3CertificateBuilder.addExtension(Extension.subjectKeyIdentifier, false, extUtils.createSubjectKeyIdentifier(csr.getSubjectPublicKeyInfo()));
	    v3CertificateBuilder.addExtension(Extension.authorityKeyIdentifier, false, extUtils.createAuthorityKeyIdentifier(csr.getSubjectPublicKeyInfo()));
	    return new JcaX509CertificateConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME).getCertificate(v3CertificateBuilder.build(contentSigner));
	}

	public static PKCS8EncryptedPrivateKeyInfo makePKCS8EncryptedKey(PrivateKey privateKey, char[] passwordChars) {
	    PKCS8EncryptedPrivateKeyInfoBuilder privateKeyInfoBuilder = new JcaPKCS8EncryptedPrivateKeyInfoBuilder(privateKey);
	    return privateKeyInfoBuilder.build(new BcPKCS12PBEOutputEncryptorBuilder(
	            PKCSObjectIdentifiers.pbeWithSHAAnd3_KeyTripleDES_CBC,
	            new CBCBlockCipher(new DESedeEngine())).build(passwordChars));
	}
	
	public static PKCS12PfxPdu makePfx(X509Certificate x509Certificate, PrivateKey privateKey, char[] passwordChars) throws IOException, Exception {
	    PKCS12SafeBagBuilder certBagBuilder = new JcaPKCS12SafeBagBuilder(x509Certificate);
	    certBagBuilder.addBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_friendlyName, new DERBMPString("Forge CA Certification"));
	    certBagBuilder.addBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_localKeyId, ASN1Primitive.fromByteArray(x509Certificate.getExtensionValue(Extension.subjectKeyIdentifier.getId())));
	    PKCS12SafeBagBuilder keyBagBuilder = new JcaPKCS12SafeBagBuilder(privateKey,
	            new BcPKCS12PBEOutputEncryptorBuilder(
	                    PKCSObjectIdentifiers.pbeWithSHAAnd3_KeyTripleDES_CBC,
	                    new CBCBlockCipher(new DESedeEngine())).build(passwordChars));
	    keyBagBuilder.addBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_friendlyName, new DERBMPString("Forge CA Key"));
	    keyBagBuilder.addBagAttribute(PKCSObjectIdentifiers.pkcs_9_at_localKeyId, ASN1Primitive.fromByteArray(x509Certificate.getExtensionValue(Extension.subjectKeyIdentifier.getId())));
	    PKCS12PfxPduBuilder pfxPduBuilder = new PKCS12PfxPduBuilder();
	    PKCS12SafeBag[] certs = new PKCS12SafeBag[1];
	    certs[0] = certBagBuilder.build();
	    pfxPduBuilder.addEncryptedData(new BcPKCS12PBEOutputEncryptorBuilder(
	                    PKCSObjectIdentifiers.pbeWithSHAAnd40BitRC2_CBC,
	                    new CBCBlockCipher(new RC2Engine())).build(passwordChars),
	            certs);
	    pfxPduBuilder.addData(keyBagBuilder.build());
	    return pfxPduBuilder.build(new BcPKCS12MacCalculatorBuilder(), passwordChars);
	}
	
    public static void main(String[] args) throws Exception {
        try {
            KeyPair keyPair = RsaUtil.generateRSAKeyPair();
            String subjectDN = "CN=Test, OU=Test, O=Test, L=Test, ST=Test, C=US"; // 修改为你的主题信息
            PKCS10CertificationRequest csr = RsaUtil.generatePKCS10Request(keyPair, subjectDN);
            RsaUtil.writePKCS10ToFile(csr, "request.p10"); // 输出
        } finally {
			
		}
    }
    
    public void markp10(String dn, KeyPair myKeyPair) throws Exception {
    	X500Principal subject = new X500Principal(dn);
    	JcaPKCS10CertificationRequestBuilder builder = new JcaPKCS10CertificationRequestBuilder(subject, myKeyPair.getPublic());
    	ContentSigner signer = new JcaContentSignerBuilder("SHA1WithRSA").setProvider("BC").build(myKeyPair.getPrivate());
    	PKCS10CertificationRequest csr = builder.build(signer);
    	byte[] csrbuf=csr.getEncoded();
    	String P10Str= new String(Base64.encode(csrbuf));
    }
    
    /**
     * 生成 P10
     * @param pubKey  用户公钥
     * @param signAlg   签名算法
     * @param signer   私钥签名 对象
     * @return  p10
     */
    public static String makeCertificate(String pubKey, String signedPriKey, String signAlg, ContentSigner signer) {
        try {
            byte[] publicKey = Base64.decode(pubKey);
            byte[] privateKey = Base64.decode(signedPriKey);
            PublicKey pubkey = null;
            PrivateKey prikey = null;
            if(signAlg.equalsIgnoreCase("SM3withSM2")){
              //  pubkey = BCECUtil.convertX509ToECPublicKey(publicKey);
                pubkey = Sm2Util.getECPublicKeyByPublicKeyHex(pubKey);
              //  prikey = BCECUtil.convertPKCS8ToECPrivateKey(privateKey);
            }
            if(signAlg.equalsIgnoreCase("SHA1WITHRSA") || signAlg.equalsIgnoreCase("SHA256WITHRSA") || signAlg.equalsIgnoreCase("SHA384WITHRSA")|| signAlg.equalsIgnoreCase("SHA512WITHRSA")){
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
                //转公钥
                pubkey = keyFactory.generatePublic(x509EncodedKeySpec);
                //转私钥
                PKCS8EncodedKeySpec x509EncodedKeySpec1 = new PKCS8EncodedKeySpec(privateKey);
                prikey = keyFactory.generatePrivate(x509EncodedKeySpec1);
            }
            X500NameBuilder localX500NameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
            localX500NameBuilder.addRDN(BCStyle.CN, "userName or CompName"); //个人，企业用户名
            localX500NameBuilder.addRDN(BCStyle.C, "CN");  //国家
            localX500NameBuilder.addRDN(BCStyle.O, "39dian blog"); //机构名称
            localX500NameBuilder.addRDN(BCStyle.ST, "shanghai");  //省
            localX500NameBuilder.addRDN(BCStyle.L, "shanghai");  //市
            localX500NameBuilder.addRDN(BCStyle.E, "admin@39dian.com"); //邮箱
            org.bouncycastle.asn1.x500.X500Name localX500Name = localX500NameBuilder.build();
            JcaPKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(localX500Name, pubkey);
            JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder(signAlg);// 签名算法
          /*  ContentSigner signer = null;
            if(signAlg.equalsIgnoreCase(SignAlg.SIGN_ALGO_SM3WITHSM2)){
                signer = csBuilder.setProvider("BC").build(prikey);
            }*/
           // signer = csBuilder.build(prikey);    //  私钥签名    localKeyPair.getPrivate()
            PKCS10CertificationRequest csr = p10Builder.build(signer);// PKCS10的请求
            return new String(Base64.encode(csr.getEncoded()));  //P10 Base64 CSR
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
