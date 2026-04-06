package com.sandy.ecp.framework.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Test;

import com.sandy.ecp.framework.security.X500Signer;
import com.sandy.ecp.framework.security.pkcs.PKCS10;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import sun.security.tools.KeyStoreUtil;
import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateIssuerName;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateSubjectName;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;  
  
public class ReadKeyStoreTest {  
    /** 
     * 列出store中所有的私钥和公钥 以及签名信息 
     *  
     * @param ks 
     * @param storePass 
     * @param priKeyPass 
     * @throws Exception 
     */  
    private void listKeyAndCertificate(KeyStore ks, String storePass,  
            String priKeyPass) throws Exception {  
        System.out.println("size=" + ks.size());  
        Enumeration<String> enum1 = ks.aliases();  
        int i = 0;  
        while (enum1.hasMoreElements()) {  
            String alias = enum1.nextElement();  
            System.out.println("第" + (++i) + "个");  
            System.out.println("alias=" + alias);  
            java.security.cert.Certificate c = ks.getCertificate(alias);// alias为条目的别名  
            readX509Certificate((X509Certificate) c);  
            readPriKey(ks, alias, priKeyPass);  
        }  
    }  
  
    /** 
     * 列出store中私钥和cert chain信息 
     *  
     * @param ks 
     * @param alias 
     * @param pass 
     * @throws Exception 
     */  
    private void readPriKey(KeyStore ks, String alias, String pass)  
            throws Exception {  
        Key key = ks.getKey(alias, pass.toCharArray());  
        if (null == key) {  
            System.out.println("no priviate key of " + alias);  
            return;  
        }  
        System.out.println();  
        System.out.println("algorithm=" + key.getAlgorithm());  
        System.out.println("format=" + key.getFormat());  
        System.out.println("toString=" + key);  
        readCertChain(ks, alias);  
    }  
  
    /** 
     * 列出store中 cert chain信息 
     *  
     * @param ks 
     * @param alias 
     * @throws Exception 
     */  
    private void readCertChain(KeyStore ks, String alias) throws Exception {  
        Certificate[] certChain = ks.getCertificateChain(alias);  
        System.out.println("chain of " + alias);  
        if (null == certChain) {  
            System.out.println("no chain");  
            return;  
        }  
        int i = 0;  
        for (Certificate c : certChain) {  
            System.out.println("index " + (i++) + " in chain of " + alias);  
            readX509Certificate((X509Certificate) c);  
        }  
    }  
  
    /** 
     * 列出x509Certificate的基本信息 
     *  
     * @param t 
     */  
    private void readX509Certificate(X509Certificate t) {  
        System.out.println(t);  
        System.out.println("输出证书信息:\n" + t.toString());  
        System.out.println("版本号:" + t.getVersion());  
        System.out.println("序列号:" + t.getSerialNumber().toString(16));  
        System.out.println("主体名：" + t.getSubjectDN());  
        System.out.println("签发者：" + t.getIssuerDN());  
        System.out.println("有效期：" + t.getNotBefore());  
        System.out.println("签名算法：" + t.getSigAlgName());  
        byte[] sig = t.getSignature();// 签名值  
        PublicKey pk = t.getPublicKey();  
        byte[] pkenc = pk.getEncoded();  
        System.out.println("签名 ：");  
        for (int i = 0; i < sig.length; i++)  
            System.out.print(sig[i] + ",");  
        System.out.println();  
        System.out.println("公钥： ");  
        for (int i = 0; i < pkenc.length; i++)  
            System.out.print(pkenc[i] + ",");  
        System.out.println();  
    }  
  
    /** 
     * 创建一个新的keystore 
     *  
     * @param storePass 
     * @param storeType 
     *            PKCS12/JKS 
     * @return 
     * @throws Exception 
     */  
    private KeyStore createKeyStore(String storePass, String storeType)  
            throws Exception {  
        KeyStore ks = KeyStore.getInstance(storeType);  
        ks.load(null, storePass.toCharArray());  
        return ks;  
    }  
  
    /** 
     * 加载一个已有的keyStore 
     *  
     * @param path 
     * @param storePass 
     * @param storeType 
     *            PKCS12/JKS 
     * @return 
     * @throws Exception 
     */  
    private KeyStore loadKeyStore(String path, String storePass,  
            String storeType) throws Exception {  
        FileInputStream in = new FileInputStream(path);  
        KeyStore ks = KeyStore.getInstance(storeType);  
        ks.load(in, storePass.toCharArray());  
        in.close();  
        return ks;  
    }  
  
    /** 
     * 从文件加载一个证书 
     *  
     * @param path 
     * @param certType 
     * @return 
     * @throws Exception 
     */  
    private Certificate loadCert(String path, String certType) throws Exception {  
        CertificateFactory cf = CertificateFactory.getInstance(certType);  
        FileInputStream in = new FileInputStream(path);  
        Certificate c = cf.generateCertificate(in);  
        in.close();  
        return c;  
    }  
  
    /** 
     * 生成一个由根证书签名的store 
     *  
     * @param rootStore 
     * @param rootAlias 
     * @param rootKeyPass 
     * @param subjectStr 
     * @param storeType 
     * @param storePass 
     * @param alg 
     * @param keySize 
     * @param keyPass 
     * @return 
     * @throws Exception 
     */  
    public KeyStore generateSignedKeyStore(KeyStore rootStore,  
            String rootAlias, String rootKeyPass, String subjectStr,  
            String storeType, String storePass, String alias, String alg,  
            int keySize, String keyPass) throws Exception {  
  
        PrivateKey rootKey = null;  
        X509CertImpl rootCert = null;  
        X509CertInfo rootInfo = null;  
        CertificateSubjectName rootsubject = null;  
        // 签发者  
        X500Name issueX500Name = new X500Name(subjectStr);  
  
        if (null != rootStore) {  
            rootKey = (PrivateKey) rootStore.getKey(rootAlias,  
                    rootKeyPass.toCharArray());  
            rootCert = (X509CertImpl) rootStore.getCertificate(rootAlias);  
            rootInfo = (X509CertInfo) rootCert.get(X509CertImpl.NAME + "."  
                    + X509CertImpl.INFO);  
            rootsubject = (CertificateSubjectName) rootInfo  
                    .get(X509CertInfo.SUBJECT);  
            issueX500Name = (X500Name) rootsubject  
                    .get(CertificateIssuerName.DN_NAME);  
        }  
  
        // 签发者  
        CertificateIssuerName issuerName = new CertificateIssuerName(  
                issueX500Name);  
        // 被签发者  
        X500Name subjectX500Name = new X500Name(subjectStr);  
        CertificateSubjectName subjectName = new CertificateSubjectName(  
                subjectX500Name);  
  
        // 有效期设置  
        Calendar calendar = Calendar.getInstance();  
        Date startDate = calendar.getTime();  
        calendar.add(Calendar.DATE, 85);  
        Date endDate = calendar.getTime();  
        CertificateValidity certificateValidity = new CertificateValidity(  
                startDate, endDate);  
  
        // 序列号  
        CertificateSerialNumber sn = new CertificateSerialNumber(  
                (int) (startDate.getTime() / 1000L));  
  
        // 版本  
        CertificateVersion certVersion = new CertificateVersion(  
                CertificateVersion.V3);  
  
        // 算法  
        // TODO 获取算法的代码有问题  
      //  AlgorithmId algorithmId = new AlgorithmId("RSA".equals(alg) ? AlgorithmId.sha1WithRSAEncryption_oid : AlgorithmId.sha1WithDSA_oid);  
        AlgorithmId algorithmId = new AlgorithmId("RSA".equals(alg) ? AlgorithmId.RSAEncryption_oid : AlgorithmId.SHA256_oid);  
        // 密钥对  
        KeyPairGenerator keygen = KeyPairGenerator.getInstance(alg);  
        keygen.initialize(keySize, new SecureRandom());  
        KeyPair kp = keygen.genKeyPair();  
        X509CertInfo certInfo = new X509CertInfo();  
        certInfo.set("version", certVersion);  
        certInfo.set("serialNumber", sn);  
        // localX500Signer.getAlgorithmId();  
        certInfo.set("algorithmID", new CertificateAlgorithmId(algorithmId));  
        certInfo.set("key", new CertificateX509Key(kp.getPublic()));  
        certInfo.set("validity", certificateValidity);  
        certInfo.set("subject", subjectName);  
        certInfo.set("issuer", issuerName);  
        // 扩展信息  
        // if (System.getProperty("sun.security.internal.keytool.skid") !=  
        // null)  
        // {  
        // CertificateExtensions localCertificateExtensions = new  
        // CertificateExtensions();  
        // localCertificateExtensions.set("SubjectKeyIdentifier", new  
        // SubjectKeyIdentifierExtension(new  
        // KeyIdentifier(this.publicKey).getIdentifier()));  
        // certInfo.set("extensions", localCertificateExtensions);  
        // }  
  
        X509CertImpl newcert = new X509CertImpl(certInfo);  
        // TODO 这里的签名算法可能有问题 貌似应该用rootcert的签名算法 待测试  
        KeyStore ks = this.createKeyStore(storePass, storeType);  
        Certificate[] certChain = null;  
        // 如果rootStore为空 则生成自签名证书  
        if (null == rootStore) {  
            newcert.sign(kp.getPrivate(), "SHA1WithRSA");  
            certChain = new Certificate[] { newcert };  
        } else {  
            newcert.sign(rootKey, "SHA1WithRSA");  
            certChain = new Certificate[] { newcert, rootCert };  
        }  
  
        // ks.setCertificateEntry("zrbin", newcert);  
        ks.setKeyEntry(alias, kp.getPrivate(), keyPass.toCharArray(), certChain);  
        return ks;  
  
    }  
  
    @Test  
    public void testReadCer() throws Exception {  
        String path = "d:\\test.cer";  
        String certType = "X.509";  
        CertificateFactory cf = CertificateFactory.getInstance(certType);  
        FileInputStream in = new FileInputStream(path);  
        Collection<Certificate> cs = (Collection<Certificate>) cf.generateCertificates(in);  
        in.close();  
        System.out.println("size=" + cs.size());  
        for (Certificate c : cs) {  
            readX509Certificate((X509Certificate) c);  
        }  
    }  
  
    @Test  
    public void testReadP12() throws Exception {  
        String storePass = "123456";  
        String keyPass = "123456";  
        String path = "d:\\zrbin.p12";  
        KeyStore ks = loadKeyStore(path, storePass, "PKCS12");  
        listKeyAndCertificate(ks, storePass, keyPass);  
    }  
  
    @Test  
    public void testReadKeyStore() throws Exception {  
        String storePass = "123456";  
        String keyPass = "123456";  
        String path = "d:\\test.keystore";  
        KeyStore ks = loadKeyStore(path, storePass, "JCEKS");  
        listKeyAndCertificate(ks, storePass, keyPass);  
    }  
  
    @Test  
    public void testExportCert() throws FileNotFoundException, Exception {  
        String pass = "123456";  
        FileInputStream in = new FileInputStream("d:\\zrbin.p12");  
        boolean rfc = true;  
        KeyStore ks = KeyStore.getInstance("PKCS12");  
        ks.load(in, pass.toCharArray());  
        Certificate cert = ks.getCertificate("zrbin");  
        PrintStream out = new PrintStream("D:\\zrbin.cer");  
        if (rfc) {  
            BASE64Encoder encoder = new BASE64Encoder();  
            out.println("-----BEGIN CERTIFICATE-----");  
            encoder.encodeBuffer(cert.getEncoded(),  
                    out);  
            out.println("-----END CERTIFICATE-----");  
        } else {  
            out.write(cert.getEncoded());  
        }  
        out.write(cert.getEncoded());  
    }  
  
    @Test  
    public void testImportCert() throws Exception {  
        CertificateFactory cf = CertificateFactory.getInstance("X.509");  
        FileInputStream storeIn = new FileInputStream("d:\\server.keystore");  
        FileInputStream in = new FileInputStream("d:\\zrbin.cer");  
        FileInputStream rootin = new FileInputStream("d:\\root.cer");  
  
        X509CertImpl cert = (X509CertImpl) cf.generateCertificate(in);  
        X509CertImpl rootcert = (X509CertImpl) cf.generateCertificate(rootin);  
  
        KeyStore ks = KeyStore.getInstance("JKS");  
        ks.load(null, "123456".toCharArray());  
        ks.deleteEntry("zrbin");  
        // ks.setCertificateEntry("zrbin", cert);  
        ks.setCertificateEntry("root", rootcert);  
        in.close();  
        FileOutputStream out = new FileOutputStream("d:\\server.keystore");  
        ks.store(out, "123456".toCharArray());  
    }  
  
    @Test  
    public void testImportSigenedCert() throws Exception {  
        String alias = "test";  
        CertificateFactory cf = CertificateFactory.getInstance("X.509");  
        FileInputStream storeIn = new FileInputStream("d:\\test.keystore");  
        KeyStore ks = KeyStore.getInstance("JKS");  
        ks.load(storeIn, "123456".toCharArray());  
        PrivateKey priKey = (PrivateKey) ks.getKey(alias,  
                "123456".toCharArray());  
        FileInputStream in = new FileInputStream("d:\\test.cer");  
        Collection<Certificate> certCollection = (Collection<Certificate>) cf.generateCertificates(in);  
        System.out.println(certCollection.size());  
        if (certCollection.size() == 0) {  
            System.out.println("没有要导入的证书");  
            return;  
        }  
        // 如果没有对应的私钥，直接导入certficateEntry  
        if (null == priKey) {  
            for (Certificate _cert : certCollection) {  
                ks.setCertificateEntry(alias, _cert);  
                break;  
            }  
        } else {  
            Certificate importCert = null;  
            for (Certificate cert : certCollection) {  
                if (ks.getCertificate(alias).getPublicKey()  
                        .equals(cert.getPublicKey())) {  
                    importCert = cert;  
                    break;  
                }  
            }  
            if (null == importCert) {  
                System.out.println("错误：no replay cert");  
            }  
            certCollection.remove(importCert);  
            if (X509CertImpl.isSelfSigned((X509Certificate) importCert, null)) {  
                System.out.println("证书未被ca签名,无需导入");  
            } else {  
                // 构建认证链  
                List<Certificate> certList = new ArrayList<Certificate>(ks.size());  
                Map<Principal, Certificate> cerMap = new HashMap<Principal, Certificate>();  
                Enumeration<String> aliasEnum = ks.aliases();  
                // 把不包括当前回复的都加到map里  
                while (aliasEnum.hasMoreElements()) {  
                    String _alias = aliasEnum.nextElement();  
                    if (!_alias.equals(alias)) {  
                        X509CertImpl _cert = (X509CertImpl) ks.getCertificate(_alias);  
                        cerMap.put(_cert.getSubjectDN(), _cert);  
                    }  
                }  
                for (Certificate cert : certCollection) {  
                    cerMap.put(((X509Certificate) cert).getSubjectDN(), cert);  
                }  
                certList.add(importCert);  
                Principal issuerName = ((X509Certificate) importCert)  
                        .getIssuerDN();  
                while (cerMap.keySet().contains(issuerName)) {  
                    X509Certificate _rootCert = (X509Certificate) cerMap  
                            .remove(issuerName);  
                    if (null == _rootCert) {  
                        System.out.println(issuerName + "的根证书为空");  
                        return;  
                    }  
                    certList.add(_rootCert);  
                    issuerName = _rootCert.getIssuerDN();  
                }  
  
                X509CertImpl rootCert = (X509CertImpl) certList.get(certList  
                        .size() - 1);  
                if (!X509CertImpl.isSelfSigned(rootCert, null)) {  
                    System.out.println("构建证书链错误,请先导入颁发者(" + issuerName  
                            + ")的CA证书");  
                    return;  
                }  
                Certificate[] certChain = certList  
                        .toArray(new Certificate[certList.size()]);  
                ks.setKeyEntry(alias, priKey, "123456".toCharArray(), certChain);  
  
            }  
        }  
        in.close();  
        FileOutputStream out = new FileOutputStream("d:\\test.keystore");  
        ks.store(out, "123456".toCharArray());  
        out.close();  
  
    }  
  
    @Test  
    public void testGenerateKeyStore() throws Exception {  
        KeyPairGenerator kg = KeyPairGenerator.getInstance("RSA");  
        KeyPair kp = kg.genKeyPair();  
        System.out.println(KeyStoreUtil.niceStoreTypeName("PKCS12"));  
        System.out.println(kp.getPrivate());  
        System.out.println(kp.getPublic());  
        KeyStore ks = KeyStore.getInstance("JKS");  
    }  
  
    @Test  
    public void testX500Name() throws IOException, CertificateException {  
        // for(byte i=48;i<=57;i++){  
        // System.out.println((char)i);  
        // }  
        // RFC 1779 (CN, L, ST, O, OU, C, STREET)  
        // RFC 2253 (CN/name, L/location, ST/station, O/org, OU/orgunit,  
        // C/country, STREET, DC, UID)  
        X500Name subjectName = new X500Name(  
                "CN=www.jiangtech.com,L=ZuChongZhi road,ST=Shang Hai,O=Jiangdatech,OU=ENTERPRISE APP,C=China,STREET=ZuChongZhi Road");  
        X500Name subjectName1 = new X500Name(  
                "CN=www.jiangtech.com,L=ZuChongZhi road,ST=Shang Hai,O=Jiangdatech,OU=ENTERPRISE APP,C=China,STREET=ZuChongZhi Road");  
        // X509CertInfo certInfo = new X509CertInfo();  
        // certInfo.set(X509CertInfo.SUBJECT, new CertificateSubjectName(  
        // subjectName));  
        System.out.println(subjectName.hashCode());  
        System.out.println(subjectName1.hashCode());  
    }  
  
    /** 
     * 证书验证 
     *  
     * @throws Exception 
     */  
    @Test  
    public void testValidate() throws Exception {  
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");  
        // kpg.initialize()  
        KeyPair kp = kpg.genKeyPair();  
        KeyStore rootStore = this.loadKeyStore("d:/root.keystore", "123456",  
                "JKS");  
        PrivateKey rootKey = (PrivateKey) rootStore.getKey("jdcert",  
                "123456".toCharArray());  
        KeyStore store1 = this.loadKeyStore("d:/jd_signed.keystore", "123456",  
                "JKS");  
        X509CertImpl rootCert = (X509CertImpl) rootStore  
                .getCertificate("jdcert");  
        X509CertInfo rootInfo = (X509CertInfo) rootCert.get(X509CertImpl.NAME  
                + "." + X509CertImpl.INFO);  
        CertificateSubjectName rootsubject = (CertificateSubjectName) rootInfo  
                .get(X509CertInfo.SUBJECT);  
        Certificate[] chain = rootStore.getCertificateChain("jdcert");  
        rootCert.verify(kp.getPublic());  
  
    }  
  
    /** 
     * 测试签发证书 
     */  
    @Test  
    public void testGenerateSignedKeyStore() {  
        try {  
            KeyStore rootStore = this.loadKeyStore("d:/root.keystore",  
                    "123456", "JKS");  
            String rootAlias = "test";  
            String subjectStr = "CN=zhaorb@jiangdatech.com,L=PU Dong,ST=Shang Hai,O=Jiangdatech,OU=ENTERPRISE APP,C=China,STREET=ZuChongZhi Road";  
            String alg = "RSA";  
            String storeType = "JKS";  
            int keySize = 1024;  
            String keyPass = "123456";  
            String rootKeyPass = "123456";  
            String storePass = "123456";  
            String alias = "test";  
            KeyStore ks = this.generateSignedKeyStore(null, rootAlias,  
                    rootKeyPass, subjectStr, storeType, storePass, alias, alg,  
                    keySize, keyPass);  
            OutputStream out = new FileOutputStream(  
                    new File("d:/test.keystore"));  
            ks.store(out, "123456".toCharArray());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
    }  
      
      
    /** 
     * 测试签发证书 
     */  
    @Test  
    public void testGenerateSecKeyStore() {  
        try {  
            String rootAlias = "test";  
            String subjectStr = "CN=zhaorb@jiangdatech.com,L=PU Dong,ST=Shang Hai,O=Jiangdatech,OU=ENTERPRISE APP,C=China,STREET=ZuChongZhi Road";  
            String alg = "DES";  
            String storeType = "JKS";  
            int keySize = 1024;  
            String keyPass = "123456";  
            String rootKeyPass = "123456";  
            String storePass = "123456";  
            String alias = "test";  
            KeyStore ks = this.createKeyStore("123456", "JCEKS");  
            KeyGenerator keygen = KeyGenerator.getInstance("DES");  
            SecretKey secKey = keygen.generateKey();  
            ks.setKeyEntry(alias, secKey, "123456".toCharArray(),null);  
            OutputStream out = new FileOutputStream(  
                    new File("d:/test.keystore"));  
            ks.store(out, "123456".toCharArray());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
    }  
      
    @Test  
    /** 
     * 关于p7b的操作 未实现 
     */  
    public void testGeneratePKCS7KeyStore() {  
        try {  
            /*ContentInfo info = new ContentInfo(arg0); 
            //PKCS7 pkcs7 = new PKCS7() 
            String rootAlias = "test"; 
            String subjectStr = "CN=zhaorb@jiangdatech.com,L=PU Dong,ST=Shang Hai,O=Jiangdatech,OU=ENTERPRISE APP,C=China,STREET=ZuChongZhi Road"; 
            String alg = "DES"; 
            String storeType = "JKS"; 
            int keySize = 1024; 
            String keyPass = "123456"; 
            String rootKeyPass = "123456"; 
            String storePass = "123456"; 
            String alias = "test"; 
            KeyStore ks = this.createKeyStore("123456", "PKCS7"); 
            KeyGenerator keygen = KeyGenerator.getInstance("RSA"); 
            //SecretKey secKey = keygen.generateKey(); 
            //ks.setKeyEntry(alias, secKey, "123456".toCharArray(),null); 
            OutputStream out = new FileOutputStream( 
                    new File("d:/test.keystore")); 
            ks.store(out, "123456".toCharArray());*/  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
    }  
      
      
    @Test  
    public void testReadJCEKS() throws Exception{  
        KeyStore ks = this.loadKeyStore("D:/test.keystore","123456", "JCEKS");  
        Enumeration<String> aliasEnum = ks.aliases();  
        while(aliasEnum.hasMoreElements()){  
            String alias = aliasEnum.nextElement();  
            SecretKeySpec secKey = (SecretKeySpec) ks.getKey(alias, "123456".toCharArray());  
            System.out.println(ks.getCertificate(alias));  
            //System.out.println(ks.);  
            System.out.println(secKey.getClass());  
            System.out.println(secKey.getFormat());  
            System.out.println(secKey.getEncoded());  
        }  
    }  
  
    public PKCS10 readCsr() throws Exception {  
        File f = new File("D:/test.csr");  
        InputStream in = new FileInputStream(f);  
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);  
        byte[] bytes = new byte[(int) f.length()];  
        in.read(bytes);  
        String base64String = new String(bytes, "ISO-8859-1");  
        System.out.println(base64String);  
        Pattern p = Pattern  
                .compile("-----BEGIN NEW CERTIFICATE REQUEST-----([\\s\\S]*?)-----END NEW CERTIFICATE REQUEST-----([\\s\\S]*)");  
        BASE64Decoder decoder = new BASE64Decoder();  
        Matcher m = p.matcher(base64String);  
        if (m.find()) {  
            String s = m.group(1);  
            System.out.println(s.trim());  
            byte[] bArray = decoder.decodeBuffer(s);  
            PKCS10 csr = new PKCS10(bArray);  
            System.out.println(csr);  
            return csr;  
        }  
        throw new Exception("文件错误 ，无法读取csr");  
    }  
  
    @Test  
    public void testReadCsr() throws Exception {  
        PKCS10 csr = readCsr();  
    }  
  
    @Test  
    public void createCsr() throws Exception {  
        String storePass = "123456";  
        String alias = "test";  
        String alg = null;  
  
        KeyStore ks = this.loadKeyStore("d:/test.keystore", storePass, "JKS");  
        Certificate cert = ks.getCertificate(alias);  
        PrivateKey priKey = (PrivateKey) ks.getKey(alias,  
                "123456".toCharArray());  
        PublicKey pubKey = cert.getPublicKey();  
        PKCS10 csr = new PKCS10(pubKey);  
        String signAlg = null;  
        if (alg == null) {  
            alg = priKey.getAlgorithm();  
            if (("DSA".equalsIgnoreCase(alg)) || ("DSS".equalsIgnoreCase(alg)))  
                signAlg = "SHA1WithDSA";  
            else if ("RSA".equalsIgnoreCase((String) alg))  
                signAlg = "SHA1WithRSA";  
            else  
                throw new Exception("Cannot derive signature algorithm");  
        }  
        Signature signature = Signature.getInstance(signAlg);  
        signature.initSign(priKey);  
        X500Name x500Name = new X500Name(((X509Certificate) cert)  
                .getSubjectDN().toString());  
        X500Signer x500Signer = new X500Signer(signature, x500Name);  
        ((PKCS10) csr).encodeAndSign(x500Signer);  
        File f = new File("D:/test.csr");  
        if (f.exists()) {  
            f.delete();  
        }  
        ((PKCS10) csr).print(new PrintStream(new File("D:/test.csr")));  
    }  
  
    /** 
     * 签名 
     *  
     * @throws Exception 
     */  
    @Test  
    public void testSignature() throws Exception {  
        KeyStore rootStore = this.loadKeyStore("d:/root.keystore", "123456",  
                "JKS");  
        PrivateKey rootKey = (PrivateKey) rootStore.getKey("root",  
                "123456".toCharArray());  
        X509CertImpl rootX509Cert = (X509CertImpl) rootStore  
                .getCertificate("root");  
        X500Name issuerX500Name = (X500Name) rootX509Cert.get(X509CertImpl.NAME  
                + "." + X509CertImpl.INFO + "." + X509CertInfo.SUBJECT + "."  
                + CertificateSubjectName.DN_NAME);  
  
        // 有效期设置  
        Calendar calendar = Calendar.getInstance();  
        Date startDate = calendar.getTime();  
        calendar.add(Calendar.DATE, 85);  
        Date endDate = calendar.getTime();  
        CertificateValidity certificateValidity = new CertificateValidity(  
                startDate, endDate);  
  
        // 序列号  
        CertificateSerialNumber sn = new CertificateSerialNumber(  
                (int) (startDate.getTime() / 1000L));  
  
        PKCS10 csr = this.readCsr();  
        PublicKey pubKey = csr.getSubjectPublicKeyInfo();  
        X500Name subjectX500Name = csr.getSubjectName();  
        // TODO 未实现  
        Signature signature = Signature.getInstance("Sha1WithRSA");  
        X500Signer signer = new X500Signer(signature, subjectX500Name);  
        AlgorithmId algorithmId = signer.getAlgorithmId();  
  
        X509CertInfo info = new X509CertInfo();  
        info.set(X509CertInfo.ALGORITHM_ID, new CertificateAlgorithmId(  
                algorithmId));  
        info.set(X509CertInfo.SUBJECT, new CertificateSubjectName(  
                subjectX500Name));  
        info.set(X509CertInfo.ISSUER, new CertificateIssuerName(issuerX500Name));  
        info.set(X509CertInfo.KEY, new CertificateX509Key(pubKey));  
        info.set(X509CertInfo.VERSION, new CertificateVersion(  
                CertificateVersion.V3));  
        info.set(X509CertInfo.VALIDITY, certificateValidity);  
        info.set(X509CertInfo.SERIAL_NUMBER, sn);  
  
        X509CertImpl newCert = new X509CertImpl(info);  
        newCert.sign(rootKey, "SHA1WithRSA");  
        OutputStream out = new FileOutputStream("d:/test.cer");  
        out.write(newCert.getEncoded());  
        out.write(rootX509Cert.getEncoded());  
        out.close();  
    }  
  
}  
