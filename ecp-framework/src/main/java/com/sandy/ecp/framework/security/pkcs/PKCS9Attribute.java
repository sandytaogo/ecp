package com.sandy.ecp.framework.security.pkcs;


import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.CertificateException;
import java.util.Date;
import sun.misc.HexDumpEncoder;
import sun.security.util.Debug;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.KnownOIDs;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.CertificateExtensions;

public class PKCS9Attribute implements DerEncoder {
    private static final Debug debug = Debug.getInstance("jar");
    static final ObjectIdentifier[] PKCS9_OIDS = new ObjectIdentifier[18];
    private static final Class<?> BYTE_ARRAY_CLASS;
    public static final ObjectIdentifier EMAIL_ADDRESS_OID;
    public static final ObjectIdentifier UNSTRUCTURED_NAME_OID;
    public static final ObjectIdentifier CONTENT_TYPE_OID;
    public static final ObjectIdentifier MESSAGE_DIGEST_OID;
    public static final ObjectIdentifier SIGNING_TIME_OID;
    public static final ObjectIdentifier COUNTERSIGNATURE_OID;
    public static final ObjectIdentifier CHALLENGE_PASSWORD_OID;
    public static final ObjectIdentifier UNSTRUCTURED_ADDRESS_OID;
    public static final ObjectIdentifier EXTENDED_CERTIFICATE_ATTRIBUTES_OID;
    public static final ObjectIdentifier ISSUER_SERIALNUMBER_OID;
    public static final ObjectIdentifier EXTENSION_REQUEST_OID;
    public static final ObjectIdentifier SIGNING_CERTIFICATE_OID;
    public static final ObjectIdentifier SIGNATURE_TIMESTAMP_TOKEN_OID;
    private static final Byte[][] PKCS9_VALUE_TAGS;
    private static final Class<?>[] VALUE_CLASSES;
    private static final boolean[] SINGLE_VALUED;
    private ObjectIdentifier oid;
    private int index;
    private Object value;

    public PKCS9Attribute(ObjectIdentifier var1, Object var2) throws IllegalArgumentException {
        this.init(var1, var2);
    }

    private void init(ObjectIdentifier var1, Object var2) throws IllegalArgumentException {
        this.oid = var1;
        this.index = indexOf(var1, PKCS9_OIDS, 1);
        Class var3 = this.index == -1 ? BYTE_ARRAY_CLASS : VALUE_CLASSES[this.index];
        if (!var3.isInstance(var2)) {
            throw new IllegalArgumentException("Wrong value class  for attribute " + var1 + " constructing PKCS9Attribute; was " + var2.getClass().toString() + ", should be " + var3.toString());
        } else {
            this.value = var2;
        }
    }

    public PKCS9Attribute(DerValue var1) throws IOException {
        DerInputStream var2 = new DerInputStream(var1.toByteArray());
        DerValue[] var3 = var2.getSequence(2);
        if (var2.available() != 0) {
            throw new IOException("Excess data parsing PKCS9Attribute");
        } else if (var3.length != 2) {
            throw new IOException("PKCS9Attribute doesn't have two components");
        } else {
            this.oid = var3[0].getOID();
            byte[] var4 = var3[1].toByteArray();
            DerValue[] var5 = (new DerInputStream(var4)).getSet(1);
            this.index = indexOf(this.oid, PKCS9_OIDS, 1);
            if (this.index == -1) {
                if (debug != null) {
                    debug.println("Unsupported signer attribute: " + this.oid);
                }

                this.value = var4;
            } else {
                if (SINGLE_VALUED[this.index] && var5.length > 1) {
                    this.throwSingleValuedException();
                }

                for(int var7 = 0; var7 < var5.length; ++var7) {
                    Byte var6 = new Byte(var5[var7].tag);
                    if (indexOf(var6, PKCS9_VALUE_TAGS[this.index], 0) == -1) {
                        this.throwTagException(var6);
                    }
                }

                int var8;
                switch (this.index) {
                    case 1:
                    case 2:
                    case 8:
                        String[] var10 = new String[var5.length];

                        for(var8 = 0; var8 < var5.length; ++var8) {
                            var10[var8] = var5[var8].getAsString();
                        }

                        this.value = var10;
                        break;
                    case 3:
                        this.value = var5[0].getOID();
                        break;
                    case 4:
                        this.value = var5[0].getOctetString();
                        break;
                    case 5:
                        this.value = (new DerInputStream(var5[0].toByteArray())).getUTCTime();
                        break;
                    case 6:
                        SignerInfo[] var9 = new SignerInfo[var5.length];

                        for(var8 = 0; var8 < var5.length; ++var8) {
                            var9[var8] = new SignerInfo(var5[var8].toDerInputStream());
                        }

                        this.value = var9;
                        break;
                    case 7:
                        this.value = var5[0].getAsString();
                        break;
                    case 9:
                        throw new IOException("PKCS9 extended-certificate attribute not supported.");
                    case 10:
                        throw new IOException("PKCS9 IssuerAndSerialNumberattribute not supported.");
                    case 11:
                    case 12:
                        throw new IOException("PKCS9 RSA DSI attributes11 and 12, not supported.");
                    case 13:
                        throw new IOException("PKCS9 attribute #13 not supported.");
                    case 14:
                        this.value = new CertificateExtensions(new DerInputStream(var5[0].toByteArray()));
                        break;
                    case 15:
                        throw new IOException("PKCS9 SMIMECapability attribute not supported.");
                    case 16:
                        this.value = new SigningCertificateInfo(var5[0].toByteArray());
                        break;
                    case 17:
                        this.value = var5[0].toByteArray();
                }

            }
        }
    }

    public void derEncode(OutputStream var1) throws IOException {
        DerOutputStream var2 = new DerOutputStream();
        var2.putOID(this.oid);
        DerOutputStream var3;
        int var5;
        String[] var7;
        DerOutputStream[] var8;
        switch (this.index) {
            case -1:
                var2.write((byte[])((byte[])this.value));
            case 0:
            default:
                break;
            case 1:
            case 2:
                var7 = (String[])((String[])this.value);
                var8 = new DerOutputStream[var7.length];

                for(var5 = 0; var5 < var7.length; ++var5) {
                    var8[var5] = new DerOutputStream();
                    var8[var5].putIA5String(var7[var5]);
                }

                var2.putOrderedSetOf((byte)49, var8);
                break;
            case 3:
                var3 = new DerOutputStream();
                var3.putOID((ObjectIdentifier)this.value);
                var2.write((byte)49, var3.toByteArray());
                break;
            case 4:
                var3 = new DerOutputStream();
                var3.putOctetString((byte[])((byte[])this.value));
                var2.write((byte)49, var3.toByteArray());
                break;
            case 5:
                var3 = new DerOutputStream();
                var3.putUTCTime((Date)this.value);
                var2.write((byte)49, var3.toByteArray());
                break;
            case 6:
                var2.putOrderedSetOf((byte)49, (DerEncoder[])((DerEncoder[])this.value));
                break;
            case 7:
                var3 = new DerOutputStream();
                var3.putPrintableString((String)this.value);
                var2.write((byte)49, var3.toByteArray());
                break;
            case 8:
                var7 = (String[])((String[])this.value);
                var8 = new DerOutputStream[var7.length];

                for(var5 = 0; var5 < var7.length; ++var5) {
                    var8[var5] = new DerOutputStream();
                    var8[var5].putPrintableString(var7[var5]);
                }

                var2.putOrderedSetOf((byte)49, var8);
                break;
            case 9:
                throw new IOException("PKCS9 extended-certificate attribute not supported.");
            case 10:
                throw new IOException("PKCS9 IssuerAndSerialNumberattribute not supported.");
            case 11:
            case 12:
                throw new IOException("PKCS9 RSA DSI attributes11 and 12, not supported.");
            case 13:
                throw new IOException("PKCS9 attribute #13 not supported.");
            case 14:
                var3 = new DerOutputStream();
                CertificateExtensions var4 = (CertificateExtensions)this.value;

                try {
                    var4.encode(var3, true);
                } catch (CertificateException var6) {
                    throw new IOException(var6.toString());
                }

                var2.write((byte)49, var3.toByteArray());
                break;
            case 15:
                throw new IOException("PKCS9 attribute #15 not supported.");
            case 16:
                throw new IOException("PKCS9 SigningCertificate attribute not supported.");
            case 17:
                var2.write((byte)49, (byte[])((byte[])this.value));
        }

        var3 = new DerOutputStream();
        var3.write((byte)48, var2.toByteArray());
        var1.write(var3.toByteArray());
    }

    public boolean isKnown() {
        return this.index != -1;
    }

    public Object getValue() {
        return this.value;
    }

    public boolean isSingleValued() {
        return this.index == -1 || SINGLE_VALUED[this.index];
    }

    public ObjectIdentifier getOID() {
        return this.oid;
    }

    public String getName() {
        String var1 = this.oid.toString();
        KnownOIDs var2 = KnownOIDs.findMatch(var1);
        return var2 == null ? var1 : var2.stdName();
    }

    public static ObjectIdentifier getOID(String var0) {
        KnownOIDs var1 = KnownOIDs.findMatch(var0);
        return var1 != null ? ObjectIdentifier.of(var1) : null;
    }

    public static String getName(ObjectIdentifier var0) {
        return KnownOIDs.findMatch(var0.toString()).stdName();
    }

    public String toString() {
        StringBuffer var1 = new StringBuffer(100);
        var1.append("[");
        if (this.index == -1) {
            var1.append(this.oid.toString());
        } else {
            var1.append(getName(this.oid));
        }

        var1.append(": ");
        if (this.index != -1 && !SINGLE_VALUED[this.index]) {
            boolean var5 = true;
            Object[] var3 = (Object[])((Object[])this.value);

            for(int var4 = 0; var4 < var3.length; ++var4) {
                if (var5) {
                    var5 = false;
                } else {
                    var1.append(", ");
                }

                var1.append(var3[var4].toString());
            }

            return var1.toString();
        } else {
            if (this.value instanceof byte[]) {
                HexDumpEncoder var2 = new HexDumpEncoder();
                var1.append(var2.encodeBuffer((byte[])((byte[])this.value)));
            } else {
                var1.append(this.value.toString());
            }

            var1.append("]");
            return var1.toString();
        }
    }

    static int indexOf(Object var0, Object[] var1, int var2) {
        for(int var3 = var2; var3 < var1.length; ++var3) {
            if (var0.equals(var1[var3])) {
                return var3;
            }
        }

        return -1;
    }

    private void throwSingleValuedException() throws IOException {
        throw new IOException("Single-value attribute " + this.oid + " (" + this.getName() + ") has multiple values.");
    }

    private void throwTagException(Byte var1) throws IOException {
        Byte[] var2 = PKCS9_VALUE_TAGS[this.index];
        StringBuffer var3 = new StringBuffer(100);
        var3.append("Value of attribute ");
        var3.append(this.oid.toString());
        var3.append(" (");
        var3.append(this.getName());
        var3.append(") has wrong tag: ");
        var3.append(var1.toString());
        var3.append(".  Expected tags: ");
        var3.append(var2[0].toString());

        for(int var4 = 1; var4 < var2.length; ++var4) {
            var3.append(", ");
            var3.append(var2[var4].toString());
        }

        var3.append(".");
        throw new IOException(var3.toString());
    }

    static {
        PKCS9_OIDS[0] = PKCS9_OIDS[11] = PKCS9_OIDS[12] = PKCS9_OIDS[13] = PKCS9_OIDS[15] = null;

        try {
            BYTE_ARRAY_CLASS = Class.forName("[B");
        } catch (ClassNotFoundException var2) {
            throw new ExceptionInInitializerError(var2.toString());
        }

        EMAIL_ADDRESS_OID = PKCS9_OIDS[1] = ObjectIdentifier.of(KnownOIDs.EmailAddress);
        UNSTRUCTURED_NAME_OID = PKCS9_OIDS[2] = ObjectIdentifier.of(KnownOIDs.UnstructuredName);
        CONTENT_TYPE_OID = PKCS9_OIDS[3] = ObjectIdentifier.of(KnownOIDs.ContentType);
        MESSAGE_DIGEST_OID = PKCS9_OIDS[4] = ObjectIdentifier.of(KnownOIDs.MessageDigest);
        SIGNING_TIME_OID = PKCS9_OIDS[5] = ObjectIdentifier.of(KnownOIDs.SigningTime);
        COUNTERSIGNATURE_OID = PKCS9_OIDS[6] = ObjectIdentifier.of(KnownOIDs.CounterSignature);
        CHALLENGE_PASSWORD_OID = PKCS9_OIDS[7] = ObjectIdentifier.of(KnownOIDs.ChallengePassword);
        UNSTRUCTURED_ADDRESS_OID = PKCS9_OIDS[8] = ObjectIdentifier.of(KnownOIDs.UnstructuredAddress);
        EXTENDED_CERTIFICATE_ATTRIBUTES_OID = PKCS9_OIDS[9] = ObjectIdentifier.of(KnownOIDs.ExtendedCertificateAttributes);
        ISSUER_SERIALNUMBER_OID = PKCS9_OIDS[10] = ObjectIdentifier.of(KnownOIDs.IssuerAndSerialNumber);
        EXTENSION_REQUEST_OID = PKCS9_OIDS[14] = ObjectIdentifier.of(KnownOIDs.ExtensionRequest);
        SIGNING_CERTIFICATE_OID = PKCS9_OIDS[16] = ObjectIdentifier.of(KnownOIDs.SigningCertificate);
        SIGNATURE_TIMESTAMP_TOKEN_OID = PKCS9_OIDS[17] = ObjectIdentifier.of(KnownOIDs.SignatureTimestampToken);
        PKCS9_VALUE_TAGS = new Byte[][]{null, {new Byte((byte)22)}, {new Byte((byte)22), new Byte((byte)19)}, {new Byte((byte)6)}, {new Byte((byte)4)}, {new Byte((byte)23)}, {new Byte((byte)48)}, {new Byte((byte)19), new Byte((byte)20)}, {new Byte((byte)19), new Byte((byte)20)}, {new Byte((byte)49)}, {new Byte((byte)48)}, null, null, null, {new Byte((byte)48)}, {new Byte((byte)48)}, {new Byte((byte)48)}, {new Byte((byte)48)}};
        VALUE_CLASSES = new Class[18];

        try {
            Class var0 = Class.forName("[Ljava.lang.String;");
            VALUE_CLASSES[0] = null;
            VALUE_CLASSES[1] = var0;
            VALUE_CLASSES[2] = var0;
            VALUE_CLASSES[3] = Class.forName("sun.security.util.ObjectIdentifier");
            VALUE_CLASSES[4] = BYTE_ARRAY_CLASS;
            VALUE_CLASSES[5] = Class.forName("java.util.Date");
            VALUE_CLASSES[6] = Class.forName("[Lsun.security.pkcs.SignerInfo;");
            VALUE_CLASSES[7] = Class.forName("java.lang.String");
            VALUE_CLASSES[8] = var0;
            VALUE_CLASSES[9] = null;
            VALUE_CLASSES[10] = null;
            VALUE_CLASSES[11] = null;
            VALUE_CLASSES[12] = null;
            VALUE_CLASSES[13] = null;
            VALUE_CLASSES[14] = Class.forName("sun.security.x509.CertificateExtensions");
            VALUE_CLASSES[15] = null;
            VALUE_CLASSES[16] = null;
            VALUE_CLASSES[17] = BYTE_ARRAY_CLASS;
        } catch (ClassNotFoundException var1) {
            throw new ExceptionInInitializerError(var1.toString());
        }

        SINGLE_VALUED = new boolean[]{false, false, false, true, true, true, false, true, false, false, true, false, false, false, true, true, true, true};
    }
}