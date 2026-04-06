package com.sandy.ecp.framework.security.x509;


import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import com.sandy.ecp.framework.security.X500Signer;
import com.sandy.ecp.framework.security.pkcs.PKCS10;

import sun.security.x509.X500Name;

public final class CertAndKeyGen {
    private SecureRandom prng;
    private String sigAlg;
    private KeyPairGenerator keyGen;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public CertAndKeyGen(String var1, String var2) throws NoSuchAlgorithmException {
        this.keyGen = KeyPairGenerator.getInstance(var1);
        this.sigAlg = var2;
    }

    public CertAndKeyGen(String var1, String var2, String var3) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (var3 == null) {
            this.keyGen = KeyPairGenerator.getInstance(var1);
        } else {
            try {
                this.keyGen = KeyPairGenerator.getInstance(var1, var3);
            } catch (Exception var5) {
                this.keyGen = KeyPairGenerator.getInstance(var1);
            }
        }

        this.sigAlg = var2;
    }

    public void setRandom(SecureRandom var1) {
        this.prng = var1;
    }

    public void generate(int var1) throws InvalidKeyException {
        KeyPair var2;
        try {
            if (this.prng == null) {
                this.prng = new SecureRandom();
            }

            this.keyGen.initialize(var1, this.prng);
            var2 = this.keyGen.generateKeyPair();
        } catch (Exception var4) {
            throw new IllegalArgumentException(var4.getMessage());
        }

        this.publicKey = var2.getPublic();
        this.privateKey = var2.getPrivate();
    }

//    public X509Key getPublicKey() {
//        return !(this.publicKey instanceof X509Key) ? null : (X509Key)this.publicKey;
//    }
//
//    public PrivateKey getPrivateKey() {
//        return this.privateKey;
//    }
//
//    /** @deprecated */
//    @Deprecated
//    public X509Cert getSelfCert(X500Name var1, long var2) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
//        try {
//            X509Certificate var4 = this.getSelfCertificate(var1, var2);
//            return new X509Cert(var4.getEncoded());
//        } catch (CertificateException var6) {
//            throw new SignatureException(var6.getMessage());
//        } catch (NoSuchProviderException var7) {
//            throw new NoSuchAlgorithmException(var7.getMessage());
//        } catch (IOException var8) {
//            throw new SignatureException(var8.getMessage());
//        }
//    }
//
//    public X509Certificate getSelfCertificate(X500Name var1, long var2) throws CertificateException, InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchProviderException {
//        try {
//            X500Signer var4 = this.getSigner(var1);
//            Date var6 = new Date();
//            Date var7 = new Date();
//            var7.setTime(var7.getTime() + var2 * 1000L);
//            CertificateValidity var8 = new CertificateValidity(var6, var7);
//            X509CertInfo var9 = new X509CertInfo();
//            if (System.getProperty("sun.security.internal.keytool.skid") != null) {
//                var9.set("version", new CertificateVersion(2));
//                CertificateExtensions var10 = new CertificateExtensions();
//                var10.set("SubjectKeyIdentifier", new SubjectKeyIdentifierExtension((new KeyIdentifier(this.publicKey)).getIdentifier()));
//                var9.set("extensions", var10);
//            } else {
//                var9.set("version", new CertificateVersion(0));
//            }
//
//            var9.set("serialNumber", new CertificateSerialNumber((int)(var6.getTime() / 1000L)));
//            AlgorithmId var12 = var4.getAlgorithmId();
//            var9.set("algorithmID", new CertificateAlgorithmId(var12));
//            var9.set("subject", new CertificateSubjectName(var1));
//            var9.set("key", new CertificateX509Key(this.publicKey));
//            var9.set("validity", var8);
//            var9.set("issuer", new CertificateIssuerName(var4.getSigner()));
//            X509CertImpl var5 = new X509CertImpl(var9);
//            var5.sign(this.privateKey, this.sigAlg);
//            return var5;
//        } catch (IOException var11) {
//            throw new CertificateEncodingException("getSelfCert: " + var11.getMessage());
//        }
//    }
//
//    public PKCS10 getCertRequest(X500Name var1) throws InvalidKeyException, SignatureException {
//        PKCS10 var2 = new PKCS10(this.publicKey);
//
//        try {
//            var2.encodeAndSign(this.getSigner(var1));
//            return var2;
//        } catch (CertificateException var4) {
//            throw new SignatureException(this.sigAlg + " CertificateException");
//        } catch (IOException var5) {
//            throw new SignatureException(this.sigAlg + " IOException");
//        } catch (NoSuchAlgorithmException var6) {
//            throw new SignatureException(this.sigAlg + " unavailable?");
//        }
//    }
//
//    private X500Signer getSigner(X500Name var1) throws InvalidKeyException, NoSuchAlgorithmException {
//        Signature var2 = Signature.getInstance(this.sigAlg);
//        var2.initSign(this.privateKey);
//        return new X500Signer(var2, var1);
//    }
}