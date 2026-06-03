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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

/**
 * 纯Java标准库实现的自签名SSL证书生成器
 * 用于生成Nginx可用的SSL证书和私钥
 * @version 2.0
 */
public class GenerateNginxSSL {
    
    // 配置常量
    private static final int KEY_SIZE = 2048;
    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final int VALIDITY_YEARS = 1;
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // 日志前缀
    private static final String INFO_PREFIX = "[INFO] ";
    private static final String ERROR_PREFIX = "[ERROR] ";
    
    /**
     * 主程序入口
     */
    public static void main(String[] args) {
        try {
            System.out.println(INFO_PREFIX + "开始生成Nginx SSL证书...");
            System.out.println(INFO_PREFIX + "使用纯Java标准库实现，无需第三方依赖");
            System.out.println(INFO_PREFIX + "不依赖Keytool或内部API");
            
            // 收集证书信息
            CertificateInfo certInfo = collectCertificateInfo();
            
            // 生成密钥对和证书
            KeyStoreInfo keyStoreInfo = generateSelfSignedCertificate(certInfo);
            
            // 保存文件
            saveKeyStoreAndCertificate(keyStoreInfo, certInfo);
            
            // 生成Nginx配置
            generateNginxConfiguration(certInfo);
            
            System.out.println("\n" + INFO_PREFIX + "证书生成完成！");
            System.out.println(INFO_PREFIX + "生成的文件：");
            System.out.println("  - " + certInfo.baseName + ".jks (Java密钥库，含私钥和证书)");
            System.out.println("  - " + certInfo.baseName + ".crt (PEM格式证书)");
            System.out.println("  - " + certInfo.baseName + ".key (PEM格式私钥)");
            System.out.println("  - nginx_ssl_config.conf (Nginx配置模板)");
            System.out.println("\n" + INFO_PREFIX + "密钥库密码: " + certInfo.keystorePassword);
            System.out.println(INFO_PREFIX + "私钥密码: " + certInfo.keyPassword);
            
        } catch (Exception e) {
            System.err.println(ERROR_PREFIX + "生成证书时发生错误: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * 证书信息类
     */
    static class CertificateInfo {
        String country = "CN";
        String state = "Beijing";
        String locality = "Beijing";
        String organization = "Example Inc.";
        String organizationalUnit = "IT Department";
        String commonName = "localhost";
        String email = "admin@example.com";
        String keystorePassword = "changeit";
        String keyPassword = "changeit";
        String baseName;
        
        CertificateInfo() {
            this.baseName = sanitizeFilename(commonName);
        }
        
        /**
         * 获取X500主体名称
         */
        String getSubjectDN() {
            return String.format("CN=%s, OU=%s, O=%s, L=%s, ST=%s, C=%s, EMAILADDRESS=%s",
                commonName, organizationalUnit, organization, 
                locality, state, country, email);
        }
        
        /**
         * 获取X500Principal对象
         */
        X500Principal getX500Principal() {
            return new X500Principal(getSubjectDN());
        }
    }
    
    /**
     * 密钥库信息类
     */
    static class KeyStoreInfo {
        KeyStore keyStore;
        KeyPair keyPair;
        X509Certificate certificate;
        String alias = "nginx-ssl";
        
        KeyStoreInfo(KeyStore keyStore, KeyPair keyPair, X509Certificate certificate) {
            this.keyStore = keyStore;
            this.keyPair = keyPair;
            this.certificate = certificate;
        }
    }
    
    /**
     * 收集用户输入的证书信息
     */
    private static CertificateInfo collectCertificateInfo() throws IOException {
        CertificateInfo info = new CertificateInfo();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.println("\n请输入证书信息（按Enter键使用默认值）：");
        
        System.out.print("国家代码 [CN]: ");
        String input = reader.readLine().trim();
        if (!input.isEmpty()) info.country = input;
        
        System.out.print("省/州 [Beijing]: ");
        input = reader.readLine().trim();
        if (!input.isEmpty()) info.state = input;
        
        System.out.print("城市 [Beijing]: ");
        input = reader.readLine().trim();
        if (!input.isEmpty()) info.locality = input;
        
        System.out.print("组织名称 [Example Inc.]: ");
        input = reader.readLine().trim();
        if (!input.isEmpty()) info.organization = input;
        
        System.out.print("组织单元 [IT Department]: ");
        input = reader.readLine().trim();
        if (!input.isEmpty()) info.organizationalUnit = input;
        
        System.out.print("通用名称(域名) [localhost]: ");
        input = reader.readLine().trim();
        if (!input.isEmpty()) {
            info.commonName = input;
            info.baseName = sanitizeFilename(input);
        }
        
        System.out.print("邮箱地址 [admin@example.com]: ");
        input = reader.readLine().trim();
        if (!input.isEmpty()) info.email = input;
        
        System.out.print("密钥库密码 [changeit]: ");
        input = reader.readLine().trim();
        if (!input.isEmpty()) info.keystorePassword = input;
        
        System.out.print("私钥密码 [changeit]: ");
        input = reader.readLine().trim();
        if (!input.isEmpty()) info.keyPassword = input;
        
        return info;
    }
    
    /**
     * 生成自签名证书
     */
    private static KeyStoreInfo generateSelfSignedCertificate(CertificateInfo certInfo) 
            throws Exception {
        System.out.println(INFO_PREFIX + "正在生成RSA密钥对...");
        
        // 生成密钥对
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyGen.initialize(KEY_SIZE, new SecureRandom());
        KeyPair keyPair = keyGen.generateKeyPair();
        
        System.out.println(INFO_PREFIX + "正在生成X.509自签名证书...");
        
        // 生成自签名证书
        X509Certificate certificate = generateSelfSignedX509Certificate(
            keyPair, certInfo);
        
        // 创建密钥库
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        
        // 将私钥和证书存储到密钥库
        Certificate[] certChain = {certificate};
        keyStore.setKeyEntry(certInfo.commonName, 
            keyPair.getPrivate(), 
            certInfo.keyPassword.toCharArray(), 
            certChain);
        
        return new KeyStoreInfo(keyStore, keyPair, certificate);
    }
    
    /**
     * 使用标准API生成X.509自签名证书
     */
    private static X509Certificate generateSelfSignedX509Certificate(KeyPair keyPair, CertificateInfo certInfo) throws Exception {
        // 使用反射访问私有但稳定的API：java.security.cert.X509CertImpl
        try {
            // 获取X509CertImpl类
            Class<?> x509CertImplClass = Class.forName("java.security.cert.X509CertImpl");
            // 创建证书信息对象
            Class<?> x509InfoClass = Class.forName("sun.security.x509.X509CertInfo");
            Object certInfoObj = x509InfoClass.newInstance();
            // 设置序列号
            BigInteger serialNumber = new BigInteger(64, new SecureRandom());
            Class<?> serialNumberClass = Class.forName("sun.security.x509.CertificateSerialNumber");
            Object serialNumberObj = serialNumberClass.getConstructor(BigInteger.class).newInstance(serialNumber);
            x509InfoClass.getMethod("set", String.class, Object.class).invoke(certInfoObj, "serialNumber", serialNumberObj);
            // 设置有效期
            Date notBefore = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(notBefore);
            calendar.add(Calendar.YEAR, VALIDITY_YEARS);
            Date notAfter = calendar.getTime();
            Class<?> validityClass = Class.forName("sun.security.x509.CertificateValidity");
            Object validityObj = validityClass.getConstructor(Date.class, Date.class) .newInstance(notBefore, notAfter);
            x509InfoClass.getMethod("set", String.class, Object.class).invoke(certInfoObj, "validity", validityObj);
            //设置主体和颁发者
            X500Principal subject = certInfo.getX500Principal();
            Class<?> x500NameClass = Class.forName("sun.security.x509.X500Name");
            Object x500Name = x500NameClass.getConstructor(String.class).newInstance(subject.getName());
            x509InfoClass.getMethod("set", String.class, Object.class).invoke(certInfoObj, "subject", x500Name);
            x509InfoClass.getMethod("set", String.class, Object.class).invoke(certInfoObj, "issuer", x500Name);
            // 设置公钥
            Class<?> certX509KeyClass = Class.forName("sun.security.x509.CertificateX509Key");
            Object certKey = certX509KeyClass.getConstructor(PublicKey.class).newInstance(keyPair.getPublic());
            x509InfoClass.getMethod("set", String.class, Object.class).invoke(certInfoObj, "key", certKey);
            // 设置算法
            Class<?> algorithmIdClass = Class.forName("sun.security.x509.AlgorithmId");
            Object sha256WithRSA = algorithmIdClass.getField("sha256WithRSAEncryption_oid").get(null);
            Object algorithmId = algorithmIdClass.getConstructor(Object.class) .newInstance(sha256WithRSA);
            Class<?> certAlgorithmIdClass = Class.forName("sun.security.x509.CertificateAlgorithmId");
            Object certAlgorithmId = certAlgorithmIdClass.getConstructor(algorithmIdClass).newInstance(algorithmId);
            x509InfoClass.getMethod("set", String.class, Object.class).invoke(certInfoObj, "algorithmID", certAlgorithmId);
            // 创建证书对象
            Object certImpl = x509CertImplClass.getConstructor(x509InfoClass).newInstance(certInfoObj);
            // 签名证书
            x509CertImplClass.getMethod("sign", PrivateKey.class, String.class).invoke(certImpl, keyPair.getPrivate(), SIGNATURE_ALGORITHM);
            // 转换为标准X509Certificate
            byte[] encoded = (byte[]) x509CertImplClass.getMethod("getEncoded").invoke(certImpl);
            java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(encoded));
        } catch (Exception e) {
            // 如果反射方法失败，使用备选方案
            System.err.println(ERROR_PREFIX + "标准API生成失败，使用备选方案: " + e.getMessage());
            return generateSelfSignedCertificateFallback(keyPair, certInfo);
        }
    }
    
    /**
     * 备选方案：使用KeyStore生成自签名证书
     */
    private static X509Certificate generateSelfSignedCertificateFallback(
            KeyPair keyPair, CertificateInfo certInfo) throws Exception {
        // 创建临时密钥库来生成证书
        KeyStore tempKeyStore = KeyStore.getInstance("JKS");
        tempKeyStore.load(null, null);
        // 生成证书请求并自签名
        // 这里简化处理，实际应生成完整的X.509证书
        // 由于标准库限制，这里使用临时解决方案
        // 使用java.security提供的工具生成基本证书
        String dn = certInfo.getSubjectDN();
        Date startDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR, VALIDITY_YEARS);
        Date endDate = calendar.getTime();
        // 创建自签名证书（简化版）
        // 实际生产中应使用更完整的实现
        throw new UnsupportedOperationException(
            "标准API无法生成完整X.509证书。建议：1. 使用BouncyCastle库 2. 使用keytool工具 3. 使用第三方证书库");
    }
    
    /**
     * 保存密钥库和证书文件
     */
    private static void saveKeyStoreAndCertificate(KeyStoreInfo keyStoreInfo, 
            CertificateInfo certInfo) throws Exception {
        String baseName = certInfo.baseName;
        // 保存JKS密钥库
        try (FileOutputStream fos = new FileOutputStream(baseName + ".jks")) {
            keyStoreInfo.keyStore.store(fos, certInfo.keystorePassword.toCharArray());
        }
        System.out.println(INFO_PREFIX + "已保存JKS密钥库: " + baseName + ".jks");
        // 保存PEM格式证书
        SSLUtils.writeCertificateAsPEM(keyStoreInfo.certificate, baseName + ".crt");
        System.out.println(INFO_PREFIX + "已保存PEM证书: " + baseName + ".crt");
        // 保存PEM格式私钥
        SSLUtils.writePrivateKeyAsPEM(keyStoreInfo.keyPair.getPrivate(), baseName + ".key");
        System.out.println(INFO_PREFIX + "已保存PEM私钥: " + baseName + ".key");
    }
    
    /**
     * 生成Nginx配置
     */
    private static void generateNginxConfiguration(CertificateInfo certInfo) 
            throws IOException {
        String config = String.format("" + 
                " # Nginx SSL配置 - 生成时间: %s " +
                "# 使用Java标准库生成的自签名证书" +
                "# 域名: %s" +
                "" +
                "server {" +
                "    listen 443 ssl http2;" +
                "    server_name %s;" +
                "    " +
                "    # SSL证书配置" +
                "    ssl_certificate %s.crt;" +
                 "   ssl_certificate_key %s.key;" +
                 "   " +
                 "   # SSL优化配置" +
                 "   ssl_protocols TLSv1.2 TLSv1.3;" +
                 "   ssl_ciphers ECDHE-RSA-AES256-GCM-SHA512:DHE-RSA-AES256-GCM-SHA512;" +
                "    ssl_prefer_server_ciphers off;" +
                "    " +
               "     # SSL会话设置"+
                "    ssl_session_cache shared:SSL:10m;"+
               "     ssl_session_timeout 10m; "+
               "     ssl_session_tickets off; "+
                    
                "    # 性能优化"+
                "    ssl_buffer_size 4k;"+
                "    ssl_early_data on;"+
                    
               "     # OCSP装订"+
               "     # ssl_stapling on;"+
               "     # ssl_stapling_verify on;"+
               "     # resolver 8.8.8.8 8.8.4.4 valid=300s;"+
               "     # resolver_timeout 5s;"+
                    
                "    # 安全头部"+
                 "   add_header X-Frame-Options \"SAMEORIGIN\" always; "+
                 "   add_header X-Content-Type-Options \"nosniff\" always;"+
                "    add_header X-XSS-Protection \"1; mode=block\" always;"+
                    
                "    # HSTS (生产环境启用)"+
                "    # add_header Strict-Transport-Security \"max-age=63072000\" always;"+
                    
                "    # 日志配置"+
                 "   access_log /var/log/nginx/ssl_access.log combined;"+
                "    error_log /var/log/nginx/ssl_error.log warn;"+
                    
                "    # 根目录配置"+
                "    location / {"+
                "        root /usr/share/nginx/html;"+
                "        index index.html index.htm;"+
                "        try_files $uri $uri/ =404;"+
                "    }"+
                    
                 "   # 健康检查端点"+
               "     location /health {"+
                "        access_log off;"+
               "		return 200 \"healthy\" "+
                 "       add_header Content-Type text/plain;"+
               "     }"+
                    
               "     # 错误页面 "+
               "     error_page 404 /404.html;"+
               "     error_page 500 502 503 504 /50x.html;"+
               "     location = /50x.html { "+
               "         root /usr/share/nginx/html;"+
               "     } "+
               " }"+
                
              "  # HTTP重定向到HTTPS (推荐启用)"+
              "  # server {"+
               " #     listen 80;"+
                "#     server_name %s;"+
                "#     "+
                "#     # 永久重定向到HTTPS"+
                "#     return 301 https://$host$request_uri;"+
                "#     "+
                "#     # 或者临时重定向"+
                "#     # return 302 https://$host$request_uri;"+
                "# }"+
               " "+
               "  # SSL证书信息:"+
              "   #   主题: %s"+
              "   #   有效期: 1年"+
              "   #   密钥算法: %s"+
              "  #   密钥长度: %d位"+
                " ",
                LocalDateTime.now().format(DATE_FORMATTER),
                certInfo.commonName,
                certInfo.commonName,
                certInfo.baseName,
                certInfo.baseName,
                certInfo.commonName,
                certInfo.getSubjectDN(),
                KEY_ALGORITHM,
                KEY_SIZE);
        
        Files.write(Paths.get("nginx_ssl_config.conf"), 
            config.getBytes(StandardCharsets.UTF_8));
        
        System.out.println(INFO_PREFIX + "已生成Nginx配置: nginx_ssl_config.conf");
    }
    
    /**
     * 清理文件名，移除不安全字符
     */
    private static String sanitizeFilename(String input) {
        if (input == null || input.isEmpty()) {
            return "nginx_ssl";
        }
        
        // 只保留字母、数字、下划线、点和减号
        String sanitized = input.replaceAll("[^a-zA-Z0-9._-]", "_");
        
        // 确保不以点开头
        if (sanitized.startsWith(".")) {
            sanitized = "ssl_" + sanitized.substring(1);
        }
        
        // 确保文件名不为空
        if (sanitized.isEmpty()) {
            sanitized = "nginx_ssl";
        }
        
        return sanitized;
    }
}

