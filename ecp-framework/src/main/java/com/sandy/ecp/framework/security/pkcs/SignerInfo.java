package com.sandy.ecp.framework.security.pkcs;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.Timestamp;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import sun.misc.HexDumpEncoder;
import sun.security.timestamp.TimestampToken;
import sun.security.util.Debug;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.DisabledAlgorithmConstraints;
import sun.security.util.JarConstraintsParameters;
import sun.security.util.ObjectIdentifier;
import sun.security.util.SignatureUtil;
import sun.security.x509.AlgorithmId;
import sun.security.x509.KeyUsageExtension;
import sun.security.x509.X500Name;

public class SignerInfo implements DerEncoder {
    private static final DisabledAlgorithmConstraints JAR_DISABLED_CHECK = DisabledAlgorithmConstraints.jarConstraints();
    BigInteger version;
    X500Name issuerName;
    BigInteger certificateSerialNumber;
    AlgorithmId digestAlgorithmId;
    AlgorithmId digestEncryptionAlgorithmId;
    byte[] encryptedDigest;
    Timestamp timestamp;
    private boolean hasTimestamp;
    private static final Debug debug = Debug.getInstance("jar");
    PKCS9Attributes authenticatedAttributes;
    PKCS9Attributes unauthenticatedAttributes;
    private Map<AlgorithmId, AlgorithmInfo> algorithms;

    public SignerInfo(X500Name var1, BigInteger var2, AlgorithmId var3, AlgorithmId var4, byte[] var5) {
        this.hasTimestamp = true;
        this.algorithms = new HashMap();
        this.version = BigInteger.ONE;
        this.issuerName = var1;
        this.certificateSerialNumber = var2;
        this.digestAlgorithmId = var3;
        this.digestEncryptionAlgorithmId = var4;
        this.encryptedDigest = var5;
    }

    public SignerInfo(X500Name var1, BigInteger var2, AlgorithmId var3, PKCS9Attributes var4, AlgorithmId var5, byte[] var6, PKCS9Attributes var7) {
        this.hasTimestamp = true;
        this.algorithms = new HashMap();
        this.version = BigInteger.ONE;
        this.issuerName = var1;
        this.certificateSerialNumber = var2;
        this.digestAlgorithmId = var3;
        this.authenticatedAttributes = var4;
        this.digestEncryptionAlgorithmId = var5;
        this.encryptedDigest = var6;
        this.unauthenticatedAttributes = var7;
    }

    public SignerInfo(DerInputStream var1) throws IOException, ParsingException {
        this(var1, false);
    }

    public SignerInfo(DerInputStream var1, boolean var2) throws IOException, ParsingException {
        this.hasTimestamp = true;
        this.algorithms = new HashMap();
        this.version = var1.getBigInteger();
        DerValue[] var3 = var1.getSequence(2);
        if (var3.length != 2) {
            throw new ParsingException("Invalid length for IssuerAndSerialNumber");
        } else {
            byte[] var4 = var3[0].toByteArray();
            this.issuerName = new X500Name(new DerValue((byte)48, var4));
            this.certificateSerialNumber = var3[1].getBigInteger();
            DerValue var5 = var1.getDerValue();
            this.digestAlgorithmId = AlgorithmId.parse(var5);
            if (var2) {
                var1.getSet(0);
            } else if ((byte)var1.peekByte() == -96) {
                this.authenticatedAttributes = new PKCS9Attributes(var1);
            }

            var5 = var1.getDerValue();
            this.digestEncryptionAlgorithmId = AlgorithmId.parse(var5);
            this.encryptedDigest = var1.getOctetString();
            if (var2) {
                var1.getSet(0);
            } else if (var1.available() != 0 && (byte)var1.peekByte() == -95) {
                this.unauthenticatedAttributes = new PKCS9Attributes(var1, true);
            }

            if (var1.available() != 0) {
                throw new ParsingException("extra data at the end");
            }
        }
    }

    public void encode(DerOutputStream var1) throws IOException {
        this.derEncode(var1);
    }

    public void derEncode(OutputStream var1) throws IOException {
        DerOutputStream var2 = new DerOutputStream();
        var2.putInteger(this.version);
        DerOutputStream var3 = new DerOutputStream();
        this.issuerName.encode(var3);
        var3.putInteger(this.certificateSerialNumber);
        var2.write((byte)48, var3);
        this.digestAlgorithmId.encode(var2);
        if (this.authenticatedAttributes != null) {
            this.authenticatedAttributes.encode((byte)-96, var2);
        }

        this.digestEncryptionAlgorithmId.encode(var2);
        var2.putOctetString(this.encryptedDigest);
        if (this.unauthenticatedAttributes != null) {
            this.unauthenticatedAttributes.encode((byte)-95, var2);
        }

        DerOutputStream var4 = new DerOutputStream();
        var4.write((byte)48, var2);
        var1.write(var4.toByteArray());
    }

    public X509Certificate getCertificate(PKCS7 var1) throws IOException {
        return var1.getCertificate(this.certificateSerialNumber, this.issuerName);
    }

    public ArrayList<X509Certificate> getCertificateChain(PKCS7 var1) throws IOException {
        X509Certificate var2 = var1.getCertificate(this.certificateSerialNumber, this.issuerName);
        if (var2 == null) {
            return null;
        } else {
            ArrayList var3 = new ArrayList();
            var3.add(var2);
            X509Certificate[] var4 = var1.getCertificates();
            if (var4 != null && !var2.getSubjectDN().equals(var2.getIssuerDN())) {
                Principal var5 = var2.getIssuerDN();
                int var6 = 0;

                boolean var7;
                do {
                    var7 = false;

                    for(int var8 = var6; var8 < var4.length; ++var8) {
                        if (var5.equals(var4[var8].getSubjectDN())) {
                            var3.add(var4[var8]);
                            if (var4[var8].getSubjectDN().equals(var4[var8].getIssuerDN())) {
                                var6 = var4.length;
                            } else {
                                var5 = var4[var8].getIssuerDN();
                                X509Certificate var9 = var4[var6];
                                var4[var6] = var4[var8];
                                var4[var8] = var9;
                                ++var6;
                            }

                            var7 = true;
                            break;
                        }
                    }
                } while(var7);

                return var3;
            } else {
                return var3;
            }
        }
    }

    SignerInfo verify(PKCS7 var1, byte[] var2) throws NoSuchAlgorithmException, SignatureException {
        try {
            Timestamp var3 = null;

            try {
                var3 = this.getTimestamp();
            } catch (Exception var19) {
                if (debug != null) {
                    debug.println("Unexpected exception while getting timestamp: " + var19);
                }
            }

            ContentInfo var4 = var1.getContentInfo();
            if (var2 == null) {
                var2 = var4.getContentBytes();
            }

            String var5 = this.digestAlgorithmId.getName();
            this.algorithms.put(this.digestAlgorithmId, new AlgorithmInfo("SignerInfo digestAlgorithm field", false));
            byte[] var6;
            if (this.authenticatedAttributes == null) {
                var6 = var2;
            } else {
                ObjectIdentifier var7 = (ObjectIdentifier)this.authenticatedAttributes.getAttributeValue(PKCS9Attribute.CONTENT_TYPE_OID);
                if (var7 == null || !var7.equals(var4.contentType)) {
                    return null;
                }

                byte[] var8 = (byte[])((byte[])this.authenticatedAttributes.getAttributeValue(PKCS9Attribute.MESSAGE_DIGEST_OID));
                if (var8 == null) {
                    return null;
                }

                MessageDigest var9 = MessageDigest.getInstance(var5);
                byte[] var10 = var9.digest(var2);
                if (!MessageDigest.isEqual(var8, var10)) {
                    return null;
                }

                var6 = this.authenticatedAttributes.getDerEncoding();
            }

            String var21 = this.digestEncryptionAlgorithmId.getName();
            String var22 = AlgorithmId.getEncAlgFromSigAlg(var21);
            if (var22 != null) {
                var21 = var22;
            }

            String var23 = AlgorithmId.makeSigAlg(var5, var21);

            try {
                AlgorithmId var24 = new AlgorithmId(AlgorithmId.get(var23).getOID());
                this.algorithms.put(var24, new AlgorithmInfo("SignerInfo digestEncryptionAlgorithm field", true));
            } catch (NoSuchAlgorithmException var18) {
            }

            X509Certificate var25 = this.getCertificate(var1);
            if (var25 == null) {
                return null;
            } else {
                PublicKey var11 = var25.getPublicKey();
                if (var25.hasUnsupportedCriticalExtension()) {
                    throw new SignatureException("Certificate has unsupported critical extension(s)");
                } else {
                    boolean[] var12 = var25.getKeyUsage();
                    if (var12 != null) {
                        KeyUsageExtension var13;
                        try {
                            var13 = new KeyUsageExtension(var12);
                        } catch (IOException var17) {
                            throw new SignatureException("Failed to parse keyUsage extension");
                        }

                        boolean var14 = var13.get("digital_signature");
                        boolean var15 = var13.get("non_repudiation");
                        if (!var14 && !var15) {
                            throw new SignatureException("Key usage restricted: cannot be used for digital signatures");
                        }
                    }

                    Signature var26 = Signature.getInstance(var23);
                    AlgorithmParameters var27 = this.digestEncryptionAlgorithmId.getParameters();

                    try {
                        SignatureUtil.initVerifyWithParam(var26, var11, SignatureUtil.getParamSpec(var23, var27));
                    } catch (InvalidAlgorithmParameterException | InvalidKeyException | ProviderException var16) {
                        throw new SignatureException(((Exception)var16).getMessage(), var16);
                    }

                    var26.update(var6);
                    return var26.verify(this.encryptedDigest) ? this : null;
                }
            }
        } catch (IOException var20) {
            throw new SignatureException("Error verifying signature", var20);
        }
    }

    SignerInfo verify(PKCS7 var1) throws NoSuchAlgorithmException, SignatureException {
        return this.verify(var1, (byte[])null);
    }

    public BigInteger getVersion() {
        return this.version;
    }

    public X500Name getIssuerName() {
        return this.issuerName;
    }

    public BigInteger getCertificateSerialNumber() {
        return this.certificateSerialNumber;
    }

    public AlgorithmId getDigestAlgorithmId() {
        return this.digestAlgorithmId;
    }

    public PKCS9Attributes getAuthenticatedAttributes() {
        return this.authenticatedAttributes;
    }

    public AlgorithmId getDigestEncryptionAlgorithmId() {
        return this.digestEncryptionAlgorithmId;
    }

    public byte[] getEncryptedDigest() {
        return this.encryptedDigest;
    }

    public PKCS9Attributes getUnauthenticatedAttributes() {
        return this.unauthenticatedAttributes;
    }

    public PKCS7 getTsToken() throws IOException {
        if (this.unauthenticatedAttributes == null) {
            return null;
        } else {
            PKCS9Attribute var1 = this.unauthenticatedAttributes.getAttribute(PKCS9Attribute.SIGNATURE_TIMESTAMP_TOKEN_OID);
            return var1 == null ? null : new PKCS7((byte[])((byte[])var1.getValue()));
        }
    }

    public Timestamp getTimestamp() throws IOException, NoSuchAlgorithmException, SignatureException, CertificateException {
        if (this.timestamp == null && this.hasTimestamp) {
            PKCS7 var1 = this.getTsToken();
            if (var1 == null) {
                this.hasTimestamp = false;
                return null;
            } else {
                byte[] var2 = var1.getContentInfo().getData();
                SignerInfo[] var3 = var1.verify(var2);
                if (var3 != null && var3.length != 0) {
                    ArrayList var4 = var3[0].getCertificateChain(var1);
                    CertificateFactory var5 = CertificateFactory.getInstance("X.509");
                    CertPath var6 = var5.generateCertPath(var4);
                    TimestampToken var7 = new TimestampToken(var2);
                    this.verifyTimestamp(var7);
                    this.algorithms.putAll(var3[0].algorithms);
                    this.timestamp = new Timestamp(var7.getDate(), var6);
                    return this.timestamp;
                } else {
                    throw new SignatureException("Unable to verify timestamp");
                }
            }
        } else {
            return this.timestamp;
        }
    }

    private void verifyTimestamp(TimestampToken var1) throws NoSuchAlgorithmException, SignatureException {
        AlgorithmId var2 = var1.getHashAlgorithm();
        this.algorithms.put(var2, new AlgorithmInfo("TimestampToken digestAlgorithm field", false));
        MessageDigest var3 = MessageDigest.getInstance(var2.getName());
        if (!MessageDigest.isEqual(var1.getHashedMessage(), var3.digest(this.encryptedDigest))) {
            throw new SignatureException("Signature timestamp (#" + var1.getSerialNumber() + ") generated on " + var1.getDate() + " is inapplicable");
        } else {
            if (debug != null) {
                debug.println();
                debug.println("Detected signature timestamp (#" + var1.getSerialNumber() + ") generated on " + var1.getDate());
                debug.println();
            }

        }
    }

    public String toString() {
        HexDumpEncoder var1 = new HexDumpEncoder();
        String var2 = "";
        var2 = var2 + "Signer Info for (issuer): " + this.issuerName + "\n";
        var2 = var2 + "\tversion: " + Debug.toHexString(this.version) + "\n";
        var2 = var2 + "\tcertificateSerialNumber: " + Debug.toHexString(this.certificateSerialNumber) + "\n";
        var2 = var2 + "\tdigestAlgorithmId: " + this.digestAlgorithmId + "\n";
        if (this.authenticatedAttributes != null) {
            var2 = var2 + "\tauthenticatedAttributes: " + this.authenticatedAttributes + "\n";
        }

        var2 = var2 + "\tdigestEncryptionAlgorithmId: " + this.digestEncryptionAlgorithmId + "\n";
        var2 = var2 + "\tencryptedDigest: \n" + var1.encodeBuffer(this.encryptedDigest) + "\n";
        if (this.unauthenticatedAttributes != null) {
            var2 = var2 + "\tunauthenticatedAttributes: " + this.unauthenticatedAttributes + "\n";
        }

        return var2;
    }

    public static Set<String> verifyAlgorithms(SignerInfo[] var0, JarConstraintsParameters var1, String var2) throws SignatureException {
        HashMap var3 = new HashMap();
        SignerInfo[] var4 = var0;
        int var5 = var0.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            SignerInfo var7 = var4[var6];
            var3.putAll(var7.algorithms);
        }

        HashSet var10 = new HashSet();

        try {
            Iterator var11 = var3.entrySet().iterator();

            while(var11.hasNext()) {
                Map.Entry var12 = (Map.Entry)var11.next();
                AlgorithmInfo var13 = (AlgorithmInfo)var12.getValue();
                var1.setExtendedExceptionMsg(var2, var13.getField());
                AlgorithmId var8 = (AlgorithmId)var12.getKey();
                JAR_DISABLED_CHECK.permits(var8.getName(), var8.getParameters(), var1, var13.getCheckKey());
                var10.add(var8.getName());
            }

            return var10;
        } catch (CertPathValidatorException var9) {
            throw new SignatureException(var9);
        }
    }

    class AlgorithmInfo {
        final String field;
        final boolean checkKey;

        public AlgorithmInfo(String var2, boolean var3) {
            this.field = var2;
            this.checkKey = var3;
        }

        String getField() {
            return this.field;
        }

        boolean getCheckKey() {
            return this.checkKey;
        }
    }
}
