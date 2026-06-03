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
package com.sandy.ecp.framework.ssl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;

/**
 * SSL工具类 - 提供证书和密钥的转换功能
 * 
 * @author 文心快码
 * @version 2.0
 */
public class SSLUtils {
    
    private static final int LINE_LENGTH = 64;
    
    /**
     * 私有的构造方法，防止实例化
     */
    private SSLUtils() {
        throw new AssertionError("工具类不应被实例化");
    }
    
    /**
     * 将私钥转换为PEM格式字符串
     */
    public static String privateKeyToPEM(PrivateKey privateKey) {
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN PRIVATE KEY-----\n");
        
        String base64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        appendBase64WithLineBreaks(pem, base64);
        
        pem.append("-----END PRIVATE KEY-----\n");
        return pem.toString();
    }
    
    /**
     * 将证书转换为PEM格式字符串
     */
    public static String certificateToPEM(X509Certificate certificate) 
            throws CertificateException {
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN CERTIFICATE-----\n");
        
        String base64 = Base64.getEncoder().encodeToString(certificate.getEncoded());
        appendBase64WithLineBreaks(pem, base64);
        
        pem.append("-----END CERTIFICATE-----\n");
        return pem.toString();
    }
    
    /**
     * 将私钥保存为PEM格式文件
     */
    public static void writePrivateKeyAsPEM(PrivateKey privateKey, String filename) 
            throws IOException {
        String pemContent = privateKeyToPEM(privateKey);
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(pemContent);
        }
    }
    
    /**
     * 将证书保存为PEM格式文件
     */
    public static void writeCertificateAsPEM(X509Certificate certificate, String filename) 
            throws CertificateException, IOException {
        String pemContent = certificateToPEM(certificate);
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(pemContent);
        }
    }
    
    /**
     * 将证书链转换为PEM格式字符串
     */
    public static String certificateChainToPEM(Certificate[] chain) 
            throws CertificateException {
        StringBuilder pem = new StringBuilder();
        
        for (Certificate cert : chain) {
            if (cert instanceof X509Certificate) {
                pem.append(certificateToPEM((X509Certificate) cert));
                pem.append("\n");
            }
        }
        
        return pem.toString();
    }
    
    /**
     * 将PEM格式字符串写入文件
     */
    public static void writePEMToFile(String pemContent, String filename) 
            throws IOException {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(pemContent);
        }
    }
    
    /**
     * 从JKS密钥库提取证书和私钥
     */
    public static KeyStoreInfo loadFromKeyStore(String keystoreFile, 
            String keystorePassword, String alias, String keyPassword) 
            throws Exception {
        
        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(keystoreFile)) {
            keyStore.load(fis, keystorePassword.toCharArray());
        }
        
        Key key = keyStore.getKey(alias, keyPassword.toCharArray());
        if (!(key instanceof PrivateKey)) {
            throw new KeyException("指定的别名不是私钥");
        }
        
        Certificate[] chain = keyStore.getCertificateChain(alias);
        if (chain == null || chain.length == 0) {
            throw new CertificateException("未找到证书链");
        }
        
        if (!(chain[0] instanceof X509Certificate)) {
            throw new CertificateException("证书不是X.509格式");
        }
        
        return new KeyStoreInfo(keyStore, new KeyPair(null, (PrivateKey) key), 
            (X509Certificate) chain[0]);
    }
    
    /**
     * 生成证书信息摘要
     */
    public static String getCertificateSummary(X509Certificate cert) {
        StringBuilder summary = new StringBuilder();
        
        summary.append("证书信息摘要:\n");
        summary.append("  主题: ").append(cert.getSubjectX500Principal().getName()).append("\n");
        summary.append("  颁发者: ").append(cert.getIssuerX500Principal().getName()).append("\n");
        summary.append("  序列号: ").append(cert.getSerialNumber().toString(16)).append("\n");
        summary.append("  有效期: ").append(cert.getNotBefore()).append(" - ")
               .append(cert.getNotAfter()).append("\n");
        summary.append("  算法: ").append(cert.getSigAlgName()).append("\n");
        
        return summary.toString();
    }
    
    /**
     * 验证证书是否有效
     */
    public static boolean validateCertificate(X509Certificate cert) {
        try {
            cert.checkValidity(new Date());
            cert.verify(cert.getPublicKey());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 辅助方法：添加带换行符的Base64编码
     */
    private static void appendBase64WithLineBreaks(StringBuilder builder, String base64) {
        for (int i = 0; i < base64.length(); i += LINE_LENGTH) {
            int end = Math.min(base64.length(), i + LINE_LENGTH);
            builder.append(base64.substring(i, end)).append("\n");
        }
    }
    
    /**
     * 密钥库信息类
     */
    public static class KeyStoreInfo {
        public final KeyStore keyStore;
        public final KeyPair keyPair;
        public final X509Certificate certificate;
        
        public KeyStoreInfo(KeyStore keyStore, KeyPair keyPair, X509Certificate certificate) {
            this.keyStore = keyStore;
            this.keyPair = keyPair;
            this.certificate = certificate;
        }
    }
}

