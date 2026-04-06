package com.sandy.ecp.framework.security.pkcs;


import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import com.sandy.ecp.framework.security.X500Signer;

import sun.misc.BASE64Encoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;
import sun.security.x509.X509Key;

public class PKCS10 {
    private X500Name subject;
    private PublicKey subjectPublicKeyInfo;
    private PKCS10Attributes attributeSet;
    private byte[] encoded;

    public PKCS10(PublicKey var1) {
        this.subjectPublicKeyInfo = var1;
        this.attributeSet = new PKCS10Attributes();
    }

    public PKCS10(PublicKey var1, PKCS10Attributes var2) {
        this.subjectPublicKeyInfo = var1;
        this.attributeSet = var2;
    }

    public PKCS10(byte[] var1) throws IOException, SignatureException, NoSuchAlgorithmException {
        this.encoded = var1;
        DerInputStream var2 = new DerInputStream(var1);
        DerValue[] var3 = var2.getSequence(3);
        if (var3.length != 3) {
            throw new IllegalArgumentException("not a PKCS #10 request");
        } else {
            var1 = var3[0].toByteArray();
            AlgorithmId var4 = AlgorithmId.parse(var3[1]);
            byte[] var5 = var3[2].getBitString();
            BigInteger var7 = var3[0].data.getBigInteger();
            if (!var7.equals(BigInteger.ZERO)) {
                throw new IllegalArgumentException("not PKCS #10 v1");
            } else {
                this.subject = new X500Name(var3[0].data);
                this.subjectPublicKeyInfo = X509Key.parse(var3[0].data.getDerValue());
                if (var3[0].data.available() != 0) {
                    this.attributeSet = new PKCS10Attributes(var3[0].data);
                } else {
                    this.attributeSet = new PKCS10Attributes();
                }

                if (var3[0].data.available() != 0) {
                    throw new IllegalArgumentException("illegal PKCS #10 data");
                } else {
                    try {
                        Signature var6 = Signature.getInstance(var4.getName());
                        var6.initVerify(this.subjectPublicKeyInfo);
                        var6.update(var1);
                        if (!var6.verify(var5)) {
                            throw new SignatureException("Invalid PKCS #10 signature");
                        }
                    } catch (InvalidKeyException var10) {
                        throw new SignatureException("invalid key");
                    }
                }
            }
        }
    }

    public void encodeAndSign(X500Signer var1) throws CertificateException, IOException, SignatureException {
        if (this.encoded != null) {
            throw new SignatureException("request is already signed");
        } else {
            this.subject = var1.getSigner();
            DerOutputStream var3 = new DerOutputStream();
            var3.putInteger(BigInteger.ZERO);
            this.subject.encode(var3);
            var3.write(this.subjectPublicKeyInfo.getEncoded());
            this.attributeSet.encode(var3);
            DerOutputStream var2 = new DerOutputStream();
            var2.write((byte)48, var3);
            byte[] var4 = var2.toByteArray();
            var3 = var2;
            var1.update(var4, 0, var4.length);
            byte[] var5 = var1.sign();
            var1.getAlgorithmId().encode(var2);
            var2.putBitString(var5);
            var2 = new DerOutputStream();
            var2.write((byte)48, var3);
            this.encoded = var2.toByteArray();
        }
    }

    public X500Name getSubjectName() {
        return this.subject;
    }

    public PublicKey getSubjectPublicKeyInfo() {
        return this.subjectPublicKeyInfo;
    }

    public PKCS10Attributes getAttributes() {
        return this.attributeSet;
    }

    public byte[] getEncoded() {
        return this.encoded != null ? (byte[])((byte[])this.encoded.clone()) : null;
    }

    public void print(PrintStream var1) throws IOException, SignatureException {
        if (this.encoded == null) {
            throw new SignatureException("Cert request was not signed");
        } else {
            BASE64Encoder var2 = new BASE64Encoder();
            var1.println("-----BEGIN NEW CERTIFICATE REQUEST-----");
            var2.encodeBuffer(this.encoded, var1);
            var1.println("-----END NEW CERTIFICATE REQUEST-----");
        }
    }

    public String toString() {
        return "[PKCS #10 certificate request:\n" + this.subjectPublicKeyInfo.toString() + " subject: <" + this.subject + ">" + "\n" + " attributes: " + this.attributeSet.toString() + "\n]";
    }

    public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        } else if (!(var1 instanceof PKCS10)) {
            return false;
        } else if (this.encoded == null) {
            return false;
        } else {
            byte[] var2 = ((PKCS10)var1).getEncoded();
            return var2 == null ? false : Arrays.equals(this.encoded, var2);
        }
    }

    public int hashCode() {
        int var1 = 0;
        if (this.encoded != null) {
            for(int var2 = 1; var2 < this.encoded.length; ++var2) {
                var1 += this.encoded[var2] * var2;
            }
        }

        return var1;
    }
}
