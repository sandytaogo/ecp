package com.sandy.ecp.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Vector;

import sun.security.x509.X500Name;
import com.sandy.ecp.framework.security.X500Signer;
import com.sandy.ecp.framework.security.x509.CertAndKeyGen;

import sun.misc.BASE64Encoder;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificateAlgorithmId;
import sun.security.x509.CertificateExtensions;
import sun.security.x509.CertificateSerialNumber;
import sun.security.x509.CertificateValidity;
import sun.security.x509.CertificateVersion;
import sun.security.x509.CertificateX509Key;
import sun.security.x509.ExtendedKeyUsageExtension;
import sun.security.x509.Extension;
import sun.security.x509.KeyIdentifier;
import sun.security.x509.KeyUsageExtension;
import sun.security.x509.SubjectKeyIdentifierExtension;

import sun.security.x509.X509CertImpl;
import sun.security.x509.X509CertInfo;

/**
 * 首先生成CA的根证书，然后有CA的根证书签署生成ScriptX的证书
 * 
 * @author Administrator
 * 
 */
public class GenX509Cert {
	
	/** 提供强加密随机数生成器 (RNG)* */
	private SecureRandom sr;

	
	public GenX509Cert() throws NoSuchAlgorithmException,
			NoSuchProviderException {
		// 返回实现指定随机数生成器 (RNG) 算法的 SecureRandom 对象。
		sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
	}

	public void createCert(X509Certificate certificate, PrivateKey rootPrivKey,
			KeyPair kp) throws CertificateException, IOException,
			InvalidKeyException, NoSuchAlgorithmException,
			NoSuchProviderException, SignatureException {

		// X.509 v1 证书的抽象类。此类提供了一种访问 X.509 v1 证书所有属性的标准方式。
		byte certbytes[] = certificate.getEncoded();
		// The X509CertImpl class represents an X.509 certificate.
		X509CertImpl x509certimpl = new X509CertImpl(certbytes);
		// The X509CertInfo class represents X.509 certificate information.
		X509CertInfo x509certinfo = (X509CertInfo) x509certimpl.get("x509.info");
		// This class defines the X509Key attribute for the Certificate.
		x509certinfo.set("key", new CertificateX509Key(kp.getPublic()));
		// This class defines the Extensions attribute for the Certificate
		CertificateExtensions certificateextensions = new CertificateExtensions();
		certificateextensions.set("SubjectKeyIdentifier",
				new SubjectKeyIdentifierExtension((new KeyIdentifier(kp
						.getPublic())).getIdentifier()));
		x509certinfo.set("extensions", certificateextensions);
		// 设置issuer域
		X500Name issuer = new X500Name("CN=RootCA,OU=hackwp,O=wp,L=BJ,S=BJ,C=CN");
		x509certinfo.set("issuer.dname", issuer);
		// Constructs a name from a conventionally formatted string, such as
		// "CN=Dave, OU=JavaSoft, O=Sun Microsystems, C=US". (RFC 1779 or RFC
		// 2253 style).
		X500Name subject = new X500Name("CN=scriptx, OU=wps, O=wps, L=BJ, ST=BJ, C=CN");
		x509certinfo.set("subject.dname", subject);
		// 此 Signature 类用来为应用程序提供数字签名算法功能。返回实现指定签名算法的 Signature 对象。
		Signature signature = Signature.getInstance("MD5WithRSA");
		// 初始化这个用于签名的对象。如果使用其他参数再次调用此方法，此调用的结果将无效。
		signature.initSign(kp.getPrivate());
		// This class provides a binding between a Signature object and an
		// authenticated X.500 name (from an X.509 certificate chain), which is
		// needed in many public key signing applications.
		X500Signer signer = new X500Signer(signature, issuer);
		// This class identifies algorithms, such as cryptographic transforms,
		// each of which may be associated with parameters.
		AlgorithmId algorithmid = signer.getAlgorithmId();
		// This class defines the AlgorithmId for the Certificate.
		x509certinfo.set("algorithmID", new CertificateAlgorithmId(algorithmid));
		// 开始时间
		Date bdate = new Date();
		// 结束时间
		Date edate = new Date();
		// 天 小时 分 秒 毫秒
		edate.setTime(bdate.getTime() + 3650 * 24L * 60L * 60L * 1000L);
		// validity为有效时间长度 单位为秒,This class defines the interval for which the
		// certificate is valid.证书的有效时间
		CertificateValidity certificatevalidity = new CertificateValidity(bdate, edate);
		x509certinfo.set("validity", certificatevalidity);
		// This class defines the SerialNumber attribute for the Certificate.
		// 设置有效期域（包含开始时间和到期时间）域名等同与x509certinfo.VALIDITY
		x509certinfo.set("serialNumber", new CertificateSerialNumber((int) (new Date().getTime() / 1000L)));
		// 设置序列号域,This class defines the version of the X509 Certificate.
		CertificateVersion cv = new CertificateVersion(CertificateVersion.V3);
		x509certinfo.set(X509CertInfo.VERSION, cv);
		// 设置版本号 只有v1 ,v2,v3这几个合法值
		/**
		 * 以上是证书的基本信息 如果要添加用户扩展信息 则比较麻烦 首先要确定version必须是v3否则不行 然后按照以下步骤
		 */
		ObjectIdentifier oid = new ObjectIdentifier(new int[] { 2, 5, 29, 15 });
		// 生成扩展域的id 是个int数组 第1位最大2 第2位最大39 最多可以几位不明....
		String userData = "Digital Signature, Non-Repudiation, Key Encipherment, Data Encipherment (f0)";
		byte l = (byte) userData.length();// 数据总长17位
		byte f = 0x04;
		byte[] bs = new byte[userData.length() + 2];
		bs[0] = f;
		bs[1] = l;
		for (int i = 2; i < bs.length; i++) {
			bs[i] = (byte) userData.charAt(i - 2);
		}
		Extension ext = new Extension(oid, true, bs);
		// 生成一个extension对象 参数分别为 oid，是否关键扩展，byte[]型的内容值
		// 其中内容的格式比较怪异 第一位是flag 这里取4暂时没出错 估计用来说明数据的用处的 第2位是后面的实际数据的长度，然后就是数据
		// 密钥用法
		KeyUsageExtension keyUsage = new KeyUsageExtension();
		keyUsage.set(KeyUsageExtension.DIGITAL_SIGNATURE, true);
		keyUsage.set(KeyUsageExtension.NON_REPUDIATION, true);
		keyUsage.set(KeyUsageExtension.KEY_ENCIPHERMENT, true);
		keyUsage.set(KeyUsageExtension.DATA_ENCIPHERMENT, true);
		// 增强密钥用法
		ObjectIdentifier ekeyOid = new ObjectIdentifier(new int[] { 1, 3, 6, 1, 5, 5, 7, 3, 3 });
		Vector<ObjectIdentifier> vkeyOid = new Vector<ObjectIdentifier>();
		vkeyOid.add(ekeyOid);
		ExtendedKeyUsageExtension exKeyUsage = new ExtendedKeyUsageExtension(vkeyOid);
		CertificateExtensions exts = new CertificateExtensions();
		exts.set("keyUsage", keyUsage);
		exts.set("extendedKeyUsage", exKeyUsage);
		// 如果有多个extension则都放入CertificateExtensions 类中，
		x509certinfo.set(X509CertInfo.EXTENSIONS, exts);
		// 设置extensions域

		X509CertImpl x509certimpl1 = new X509CertImpl(x509certinfo);
		x509certimpl1.sign(rootPrivKey, "MD5WithRSA");
		// 使用另一个证书的私钥来签名此证书 这里使用 md5散列 用rsa来加密
		BASE64Encoder base64 = new BASE64Encoder();
		FileOutputStream fos = new FileOutputStream(new File("f:\\ScriptX.crt"));
		base64.encodeBuffer(x509certimpl1.getEncoded(), fos);
		try {
			Certificate[] certChain = { x509certimpl1 };
			savePfx("scriptx", kp.getPrivate(), "123456", certChain,
					"f:\\ScriptX.pfx");
			FileInputStream in = new FileInputStream("F:\\ScriptX.pfx");
			KeyStore inputKeyStore = KeyStore.getInstance("pkcs12");
			inputKeyStore.load(in, "123456".toCharArray());
			Certificate cert = inputKeyStore.getCertificate("scriptx");
			System.out.print(cert.getPublicKey());
			PrivateKey privk = (PrivateKey) inputKeyStore.getKey("scriptx",
					"123456".toCharArray());
			FileOutputStream privKfos = new FileOutputStream(new File(
					"f:\\ScriptX.pvk"));
			privKfos.write(privk.getEncoded());
			System.out.print(privk);
			// base64.encode(key.getEncoded(), privKfos);
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 生成文件
		//x509certimpl1.verify(certificate.getPublicKey(), null);
	}

	/**
	 * 保存此根证书信息KeyStore Personal Information Exchange
	 * 
	 * @param alias
	 * @param privKey
	 * @param pwd
	 * @param certChain
	 * @param filepath
	 * @throws Exception
	 */
	public void savePfx(String alias, PrivateKey privKey, String pwd,
			Certificate[] certChain, String filepath) throws Exception {
		// 此类表示密钥和证书的存储设施。
		// 返回指定类型的 keystore 对象。此方法从首选 Provider 开始遍历已注册安全提供者列表。返回一个封装 KeyStoreSpi
		// 实现的新 KeyStore 对象，该实现取自第一个支持指定类型的 Provider。
		KeyStore outputKeyStore = KeyStore.getInstance("pkcs12");
		System.out.println("KeyStore类型：" + outputKeyStore.getType());
		// 从给定输入流中加载此 KeyStore。可以给定一个密码来解锁 keystore（例如，驻留在硬件标记设备上的 keystore）或检验
		// keystore 数据的完整性。如果没有指定用于完整性检验的密码，则不会执行完整性检验。如果要创建空
		// keystore，或者不能从流中初始化 keystore，则传递 null 作为 stream 的参数。注意，如果此 keystore
		// 已经被加载，那么它将被重新初始化，并再次从给定输入流中加载。
		outputKeyStore.load(null, pwd.toCharArray());
		// 将给定密钥（已经被保护）分配给给定别名。如果受保护密钥的类型为
		// java.security.PrivateKey，则它必须附带证明相应公钥的证书链。如果底层 keystore 实现的类型为
		// jks，则必须根据 PKCS #8 标准中的定义将 key 编码为
		// EncryptedPrivateKeyInfo。如果给定别名已经存在，则与别名关联的 keystore
		// 信息将被给定密钥（还可能包括证书链）重写。
		outputKeyStore.setKeyEntry(alias, privKey, pwd.toCharArray(), certChain);
		// KeyStore.PrivateKeyEntry pke=new
		// KeyStore.PrivateKeyEntry(kp.getPrivate(),certChain);
		// KeyStore.PasswordProtection password=new
		// KeyStore.PasswordProtection("123456".toCharArray());
		// outputKeyStore.setEntry("scriptx", pke, password);
		FileOutputStream out = new FileOutputStream(filepath);
		// 将此 keystore 存储到给定输出流，并用给定密码保护其完整性。
		outputKeyStore.store(out, pwd.toCharArray());
		out.close();
	}

	public void saveJks(String alias, PrivateKey privKey, String pwd,
			Certificate[] certChain, String filepath) throws Exception {
		KeyStore outputKeyStore = KeyStore.getInstance("jks");
		System.out.println(outputKeyStore.getType());
		outputKeyStore.load(null, pwd.toCharArray());
		outputKeyStore.setKeyEntry(alias, privKey, pwd.toCharArray(), certChain);
		// KeyStore.PrivateKeyEntry pke=new
		// KeyStore.PrivateKeyEntry(kp.getPrivate(),certChain);
		// KeyStore.PasswordProtection password=new
		// KeyStore.PasswordProtection("123456".toCharArray());
		// outputKeyStore.setEntry("scriptx", pke, password);
		FileOutputStream out = new FileOutputStream(filepath);
		outputKeyStore.store(out, pwd.toCharArray());
		out.close();
	}

	/**
	 * 颁布根证书，自己作为CA
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws InvalidKeyException
	 * @throws IOException
	 * @throws CertificateException
	 * @throws SignatureException
	 * @throws UnrecoverableKeyException
	 */
	public void createRootCA() throws NoSuchAlgorithmException,
			NoSuchProviderException, InvalidKeyException, IOException,
			CertificateException, SignatureException, UnrecoverableKeyException {
		// 参数分别为公钥算法、签名算法 providername（因为不知道确切的 只好使用null 既使用默认的provider）
		// Generate a pair of keys, and provide access to them.
		//CertAndKeyGen cak = new CertAndKeyGen("RSA", "MD5WithRSA", null);
		// Sets the source of random numbers used when generating keys.
		//cak.setRandom(sr);
		// Generates a random public/private key pair, with a given key size.
		//cak.generate(1024);
		// Constructs a name from a conventionally formatted string, such as
		// "CN=Dave, OU=JavaSoft, O=Sun Microsystems, C=US". (RFC 1779 or RFC
		// 2253 style)
		X500Name subject = new X500Name("CN=RootCA,OU=hackwp,O=wp,L=BJ,S=BJ,C=CN");
		// Returns a self-signed X.509v3 certificate for the public key. The
		// certificate is immediately valid. No extensions.
		// Such certificates normally are used to identify a "Certificate
		// Authority" (CA). Accordingly, they will not always be accepted by
		// other parties. However, such certificates are also useful when you
		// are bootstrapping your security infrastructure, or deploying system
		// prototypes.自签名的根证书
		//X509Certificate certificate = cak.getSelfCertificate(subject,new Date(), 3650 * 24L * 60L * 60L);
	//	X509Certificate[] certs = { certificate };
		try {
			//savePfx("RootCA", cak.getPrivateKey(), "123456", certs, "f:\\RootCa.pfx");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 后一个long型参数代表从现在开始的有效期 单位为秒（如果不想从现在开始算 可以在后面改这个域）
		BASE64Encoder base64 = new BASE64Encoder();
		FileOutputStream fos = new FileOutputStream(new File("f:\\RootCa.crt"));
		// fos.write(certificate.getEncoded());
		// 生成（保存）cert文件 base64加密 当然也可以不加密
		//base64.encodeBuffer(certificate.getEncoded(), fos);
		fos.close();
	}

	public void signCert() throws NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException,
			InvalidKeyException, NoSuchProviderException, SignatureException {
		try {
			KeyStore ks = KeyStore.getInstance("pkcs12");
			FileInputStream ksfis = new FileInputStream("f:\\RootCa.pfx");
			char[] storePwd = "123456".toCharArray();
			char[] keyPwd = "123456".toCharArray();
			// 从给定输入流中加载此 KeyStore。
			ks.load(ksfis, storePwd);
			ksfis.close();
			// 返回与给定别名关联的密钥(私钥)，并用给定密码来恢复它。必须已经通过调用 setKeyEntry，或者以
			// PrivateKeyEntry
			// 或 SecretKeyEntry 为参数的 setEntry 关联密钥与别名。
			PrivateKey privK = (PrivateKey) ks.getKey("RootCA", keyPwd);

			// 返回与给定别名关联的证书。如果给定的别名标识通过调用 setCertificateEntry 创建的条目，或者通过调用以
			// TrustedCertificateEntry 为参数的 setEntry
			// 创建的条目，则返回包含在该条目中的可信证书。如果给定的别名标识通过调用 setKeyEntry 创建的条目，或者通过调用以
			// PrivateKeyEntry 为参数的 setEntry 创建的条目，则返回该条目中证书链的第一个元素。
			X509Certificate certificate = (X509Certificate) ks.getCertificate("RootCA");
			createCert(certificate, privK, genKey());
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public KeyPair genKey() throws NoSuchAlgorithmException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(1024, sr);
		System.out.print(kpg.getAlgorithm());
		KeyPair kp = kpg.generateKeyPair();
		return kp;
	}

	public static void main(String[] args) {
		try {
			GenX509Cert gcert = new GenX509Cert();
			gcert.createRootCA();
			gcert.signCert();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

