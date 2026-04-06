package com.sandy.ecp.framework.security.x509;


import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.ProviderException;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import sun.security.rsa.PSSParameters;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.KeyUtil;
import sun.security.util.KnownOIDs;
import sun.security.util.ObjectIdentifier;

public class AlgorithmId implements Serializable, DerEncoder {
    private static final long serialVersionUID = 7205873507486557157L;
    private ObjectIdentifier algid;
    private AlgorithmParameters algParams;
    private boolean constructedFromDer;
    protected DerValue params;
    private transient byte[] encodedParams;
    private static volatile Map<String, String> aliasOidsTable;
    public static final ObjectIdentifier MD2_oid;
    public static final ObjectIdentifier MD5_oid;
    public static final ObjectIdentifier SHA_oid;
    public static final ObjectIdentifier SHA224_oid;
    public static final ObjectIdentifier SHA256_oid;
    public static final ObjectIdentifier SHA384_oid;
    public static final ObjectIdentifier SHA512_oid;
    public static final ObjectIdentifier SHA512_224_oid;
    public static final ObjectIdentifier SHA512_256_oid;
    public static final ObjectIdentifier RSAEncryption_oid;
    public static final ObjectIdentifier RSASSA_PSS_oid;
    public static final ObjectIdentifier specifiedWithECDSA_oid;
    public static final ObjectIdentifier MGF1_oid;
    public static final ObjectIdentifier DSA_oid;
    public static final ObjectIdentifier EC_oid;

    /** @deprecated */
    @Deprecated
    public AlgorithmId() {
        this.constructedFromDer = true;
    }

    public AlgorithmId(ObjectIdentifier var1) {
        this.constructedFromDer = true;
        this.algid = var1;
    }

    public AlgorithmId(ObjectIdentifier var1, AlgorithmParameters var2) {
        this.constructedFromDer = true;
        this.algid = var1;
        this.algParams = var2;
        this.constructedFromDer = false;
        if (this.algParams != null) {
            try {
                this.encodedParams = this.algParams.getEncoded();
            } catch (IOException var4) {
            }
        }

    }

    private AlgorithmId(ObjectIdentifier var1, DerValue var2) throws IOException {
        this.constructedFromDer = true;
        this.algid = var1;
        this.params = var2;
        if (this.params != null) {
            this.encodedParams = var2.toByteArray();
            this.decodeParams();
        }

    }

    protected void decodeParams() throws IOException {
        String var1 = this.getName();
        if (var1.equals("DSA") || var1.equals("Diffie-Hellman")) {
            var1 = this.algid.toString();
        }

        try {
            this.algParams = AlgorithmParameters.getInstance(var1);
        } catch (NoSuchAlgorithmException var3) {
            this.algParams = null;
            return;
        }

        this.algParams.init((byte[])this.encodedParams.clone());
    }

    public final void encode(DerOutputStream var1) throws IOException {
        this.derEncode(var1);
    }

    public void derEncode(OutputStream var1) throws IOException {
        DerOutputStream var2 = new DerOutputStream();
        DerOutputStream var3 = new DerOutputStream();
        var2.putOID(this.algid);
        if (!this.constructedFromDer) {
            if (this.algParams != null) {
                if (this.encodedParams == null) {
                    this.encodedParams = this.algParams.getEncoded();
                }

                this.params = new DerValue(this.encodedParams);
            } else {
                this.params = null;
            }
        }

        if (this.params == null) {
            if (!this.algid.equals(RSASSA_PSS_oid)) {
                var2.putNull();
            }
        } else {
            var2.putDerValue(this.params);
        }

        var3.write((byte)48, var2);
        var1.write(var3.toByteArray());
    }

    public final byte[] encode() throws IOException {
        DerOutputStream var1 = new DerOutputStream();
        this.derEncode(var1);
        return var1.toByteArray();
    }

    public final ObjectIdentifier getOID() {
        return this.algid;
    }

    public String getName() {
        String var1 = this.algid.toString();
        KnownOIDs var2 = KnownOIDs.findMatch(var1);
        if (var2 == KnownOIDs.SpecifiedSHA2withECDSA && this.encodedParams != null) {
            try {
                AlgorithmId var6 = parse(new DerValue(this.encodedParams));
                String var4 = var6.getName();
                return var4.replace("-", "") + "withECDSA";
            } catch (IOException var5) {
            }
        }

        if (var2 != null) {
            return var2.stdName();
        } else {
            String var3 = (String)aliasOidsTable().get(var1);
            return var3 != null ? var3 : this.algid.toString();
        }
    }

    public AlgorithmParameters getParameters() {
        return this.algParams;
    }

    public byte[] getEncodedParams() {
        return this.encodedParams != null && !this.algid.equals(specifiedWithECDSA_oid) ? (byte[])this.encodedParams.clone() : null;
    }

    public boolean equals(AlgorithmId var1) {
        return this.algid.equals(var1.algid) && Arrays.equals(this.encodedParams, var1.encodedParams);
    }

    public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        } else if (var1 instanceof AlgorithmId) {
            return this.equals((AlgorithmId)var1);
        } else {
            return var1 instanceof ObjectIdentifier ? this.equals((ObjectIdentifier)var1) : false;
        }
    }

    public final boolean equals(ObjectIdentifier var1) {
        return this.algid.equals(var1);
    }

    public int hashCode() {
        int var1 = this.algid.hashCode();
        var1 = 31 * var1 + Arrays.hashCode(this.encodedParams);
        return var1;
    }

    protected String paramsToString() {
        if (this.encodedParams == null) {
            return "";
        } else {
            return this.algParams != null ? ", " + this.algParams.toString() : ", params unparsed";
        }
    }

    public String toString() {
        return this.getName() + this.paramsToString();
    }

    public static AlgorithmId parse(DerValue var0) throws IOException {
        if (var0.tag != 48) {
            throw new IOException("algid parse error, not a sequence");
        } else {
            DerInputStream var3 = var0.toDerInputStream();
            ObjectIdentifier var1 = var3.getOID();
            DerValue var2;
            if (var3.available() == 0) {
                var2 = null;
            } else {
                var2 = var3.getDerValue();
                if (var2.tag == 5) {
                    if (var2.length() != 0) {
                        throw new IOException("invalid NULL");
                    }

                    var2 = null;
                }

                if (var3.available() != 0) {
                    throw new IOException("Invalid AlgorithmIdentifier: extra data");
                }
            }

            return new AlgorithmId(var1, var2);
        }
    }

    /** @deprecated */
    @Deprecated
    public static AlgorithmId getAlgorithmId(String var0) throws NoSuchAlgorithmException {
        return get(var0);
    }

    public static AlgorithmId get(String var0) throws NoSuchAlgorithmException {
        ObjectIdentifier var1;
        try {
            var1 = algOID(var0);
        } catch (IOException var3) {
            throw new NoSuchAlgorithmException("Invalid ObjectIdentifier " + var0);
        }

        if (var1 == null) {
            throw new NoSuchAlgorithmException("unrecognized algorithm name: " + var0);
        } else {
            return new AlgorithmId(var1);
        }
    }

    public static AlgorithmId get(AlgorithmParameters var0) throws NoSuchAlgorithmException {
        String var2 = var0.getAlgorithm();

        ObjectIdentifier var1;
        try {
            var1 = algOID(var2);
        } catch (IOException var4) {
            throw new NoSuchAlgorithmException("Invalid ObjectIdentifier " + var2);
        }

        if (var1 == null) {
            throw new NoSuchAlgorithmException("unrecognized algorithm name: " + var2);
        } else {
            return new AlgorithmId(var1, var0);
        }
    }

    private static ObjectIdentifier algOID(String var0) throws IOException {
        if (var0.startsWith("OID.")) {
            var0 = var0.substring("OID.".length());
        }

        KnownOIDs var1 = KnownOIDs.findMatch(var0);
        if (var1 != null) {
            return ObjectIdentifier.of(var1);
        } else if (var0.indexOf(".") == -1) {
            var0 = var0.toUpperCase(Locale.ENGLISH);
            String var2 = (String)aliasOidsTable().get(var0);
            return var2 != null ? ObjectIdentifier.of(var2) : null;
        } else {
            return ObjectIdentifier.of(var0);
        }
    }

    private static ObjectIdentifier oid(int... var0) {
        return ObjectIdentifier.newInternal(var0);
    }

    private static Map<String, String> aliasOidsTable() {
        Object var0 = aliasOidsTable;
        if (var0 == null) {
            Class var1 = AlgorithmId.class;
            synchronized(AlgorithmId.class) {
                if ((var0 = aliasOidsTable) == null) {
                    aliasOidsTable = (Map)(var0 = collectOIDAliases());
                }
            }
        }

        return (Map)var0;
    }

    private static boolean isKnownProvider(Provider var0) {
        String var1 = var0.getName();
        String var2 = var0.getClass().getName();
        if (var1 != null && var2 != null) {
            return var2.equals("java.base") && (var1.equals("SUN") || var1.equals("SunRsaSign") || var1.equals("SunJCE") || var1.equals("SunJSSE")) || var2.equals("sun.security.ec") && var1.equals("SunEC") || var2.equals("sun.security.mscapi") && var1.equals("SunMSCAPI") || var2.equals("sun.security.pkcs11") && var1.startsWith("SunPKCS11");
        } else {
            return false;
        }
    }

    private static ConcurrentHashMap<String, String> collectOIDAliases() {
        ConcurrentHashMap var0 = new ConcurrentHashMap();
        Provider[] var1 = Security.getProviders();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Provider var4 = var1[var3];
            if (!isKnownProvider(var4)) {
                Iterator var5 = var4.keySet().iterator();

                while(var5.hasNext()) {
                    Object var6 = var5.next();
                    String var7 = (String)var6;
                    String var8 = var7.toUpperCase(Locale.ENGLISH);
                    int var9;
                    if (var8.startsWith("ALG.ALIAS") && (var9 = var8.indexOf("OID.", 0)) != -1) {
                        var9 += "OID.".length();
                        if (var9 == var7.length()) {
                            break;
                        }

                        String var10 = var7.substring(var9);
                        String var11 = var4.getProperty(var7);
                        if (var11 != null) {
                            var11 = var11.toUpperCase(Locale.ENGLISH);
                        }

                        if (KnownOIDs.findMatch(var11) == null) {
                            var0.putIfAbsent(var11, var10);
                        }

                        if (KnownOIDs.findMatch(var10) == null) {
                            var0.putIfAbsent(var10, var11);
                        }
                    }
                }
            }
        }

        return var0;
    }

    public static String makeSigAlg(String var0, String var1) {
        var0 = var0.replace("-", "");
        if (var1.equalsIgnoreCase("EC")) {
            var1 = "ECDSA";
        }

        return var0 + "with" + var1;
    }

    public static String getEncAlgFromSigAlg(String var0) {
        var0 = var0.toUpperCase(Locale.ENGLISH);
        int var1 = var0.indexOf("WITH");
        String var2 = null;
        if (var1 > 0) {
            int var3 = var0.indexOf("AND", var1 + 4);
            if (var3 > 0) {
                var2 = var0.substring(var1 + 4, var3);
            } else {
                var2 = var0.substring(var1 + 4);
            }

            if (var2.equalsIgnoreCase("ECDSA")) {
                var2 = "EC";
            }
        }

        return var2;
    }

    public static String getDigAlgFromSigAlg(String var0) {
        var0 = var0.toUpperCase(Locale.ENGLISH);
        int var1 = var0.indexOf("WITH");
        return var1 > 0 ? var0.substring(0, var1) : null;
    }

    public static void checkKeyAndSigAlgMatch(String var0, String var1) {
        String var2 = var1.toUpperCase(Locale.US);
        if (var2.endsWith("WITHRSA") && !var0.equalsIgnoreCase("RSA") || var2.endsWith("WITHECDSA") && !var0.equalsIgnoreCase("EC") || var2.endsWith("WITHDSA") && !var0.equalsIgnoreCase("DSA")) {
            throw new IllegalArgumentException("key algorithm not compatible with signature algorithm");
        }
    }

    public static String getDefaultSigAlgForKey(PrivateKey var0) {
        switch (var0.getAlgorithm().toUpperCase(Locale.ENGLISH)) {
            case "EC":
                return ecStrength(KeyUtil.getKeySize(var0)) + "withECDSA";
            case "DSA":
                return ifcFfcStrength(KeyUtil.getKeySize(var0)) + "withDSA";
            case "RSA":
                return ifcFfcStrength(KeyUtil.getKeySize(var0)) + "withRSA";
            case "RSASSA-PSS":
                return "RSASSA-PSS";
            default:
                return null;
        }
    }

    public static AlgorithmId getWithParameterSpec(String var0, AlgorithmParameterSpec var1) throws NoSuchAlgorithmException {
        if (var1 == null) {
            return get(var0);
        } else if (var1 == AlgorithmId.PSSParamsHolder.PSS_256_SPEC) {
            return AlgorithmId.PSSParamsHolder.PSS_256_ID;
        } else if (var1 == AlgorithmId.PSSParamsHolder.PSS_384_SPEC) {
            return AlgorithmId.PSSParamsHolder.PSS_384_ID;
        } else if (var1 == AlgorithmId.PSSParamsHolder.PSS_512_SPEC) {
            return AlgorithmId.PSSParamsHolder.PSS_512_ID;
        } else {
            try {
                AlgorithmParameters var2 = AlgorithmParameters.getInstance(var0);
                var2.init(var1);
                return get(var2);
            } catch (NoSuchAlgorithmException | InvalidParameterSpecException var3) {
                throw new ProviderException(var3);
            }
        }
    }

    public static PSSParameterSpec getDefaultAlgorithmParameterSpec(String var0, PrivateKey var1) {
        if (var0.equalsIgnoreCase("RSASSA-PSS")) {
            switch (ifcFfcStrength(KeyUtil.getKeySize(var1))) {
                case "SHA256":
                    return AlgorithmId.PSSParamsHolder.PSS_256_SPEC;
                case "SHA384":
                    return AlgorithmId.PSSParamsHolder.PSS_384_SPEC;
                case "SHA512":
                    return AlgorithmId.PSSParamsHolder.PSS_512_SPEC;
                default:
                    throw new AssertionError("Should not happen");
            }
        } else {
            return null;
        }
    }

    private static String ecStrength(int var0) {
        if (var0 >= 512) {
            return "SHA512";
        } else {
            return var0 >= 384 ? "SHA384" : "SHA256";
        }
    }

    private static String ifcFfcStrength(int var0) {
        if (var0 > 7680) {
            return "SHA512";
        } else {
            return var0 > 3072 ? "SHA384" : "SHA256";
        }
    }

    static {
        MD2_oid = ObjectIdentifier.of(KnownOIDs.MD2);
        MD5_oid = ObjectIdentifier.of(KnownOIDs.MD5);
        SHA_oid = ObjectIdentifier.of(KnownOIDs.SHA_1);
        SHA224_oid = ObjectIdentifier.of(KnownOIDs.SHA_224);
        SHA256_oid = ObjectIdentifier.of(KnownOIDs.SHA_256);
        SHA384_oid = ObjectIdentifier.of(KnownOIDs.SHA_384);
        SHA512_oid = ObjectIdentifier.of(KnownOIDs.SHA_512);
        SHA512_224_oid = ObjectIdentifier.of(KnownOIDs.SHA_512$224);
        SHA512_256_oid = ObjectIdentifier.of(KnownOIDs.SHA_512$256);
        RSAEncryption_oid = ObjectIdentifier.of(KnownOIDs.RSA);
        RSASSA_PSS_oid = ObjectIdentifier.of(KnownOIDs.RSASSA_PSS);
        specifiedWithECDSA_oid = ObjectIdentifier.of(KnownOIDs.SpecifiedSHA2withECDSA);
        MGF1_oid = ObjectIdentifier.of(KnownOIDs.MGF1);
        DSA_oid = ObjectIdentifier.of(KnownOIDs.DSA);
        EC_oid = ObjectIdentifier.of(KnownOIDs.EC);
    }

    private static class PSSParamsHolder {
        static final PSSParameterSpec PSS_256_SPEC = new PSSParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), 32, 1);
        static final PSSParameterSpec PSS_384_SPEC = new PSSParameterSpec("SHA-384", "MGF1", new MGF1ParameterSpec("SHA-384"), 48, 1);
        static final PSSParameterSpec PSS_512_SPEC = new PSSParameterSpec("SHA-512", "MGF1", new MGF1ParameterSpec("SHA-512"), 64, 1);
        static final AlgorithmId PSS_256_ID;
        static final AlgorithmId PSS_384_ID;
        static final AlgorithmId PSS_512_ID;

        private PSSParamsHolder() {
        }

        static {
            try {
                PSS_256_ID = new AlgorithmId(AlgorithmId.RSASSA_PSS_oid, new DerValue(PSSParameters.getEncoded(PSS_256_SPEC)));
                PSS_384_ID = new AlgorithmId(AlgorithmId.RSASSA_PSS_oid, new DerValue(PSSParameters.getEncoded(PSS_384_SPEC)));
                PSS_512_ID = new AlgorithmId(AlgorithmId.RSASSA_PSS_oid, new DerValue(PSSParameters.getEncoded(PSS_512_SPEC)));
            } catch (IOException var1) {
                throw new AssertionError("Should not happen", var1);
            }
        }
    }
}
