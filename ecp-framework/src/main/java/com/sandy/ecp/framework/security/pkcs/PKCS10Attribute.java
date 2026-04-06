package com.sandy.ecp.framework.security.pkcs;


import java.io.IOException;
import java.io.OutputStream;
import sun.security.util.DerEncoder;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public class PKCS10Attribute implements DerEncoder {
    protected ObjectIdentifier attributeId = null;
    protected Object attributeValue = null;

    public PKCS10Attribute(DerValue var1) throws IOException {
        PKCS9Attribute var2 = new PKCS9Attribute(var1);
        this.attributeId = var2.getOID();
        this.attributeValue = var2.getValue();
    }

    public PKCS10Attribute(ObjectIdentifier var1, Object var2) {
        this.attributeId = var1;
        this.attributeValue = var2;
    }

    public PKCS10Attribute(PKCS9Attribute var1) {
        this.attributeId = var1.getOID();
        this.attributeValue = var1.getValue();
    }

    public void derEncode(OutputStream var1) throws IOException {
        PKCS9Attribute var2 = new PKCS9Attribute(this.attributeId, this.attributeValue);
        var2.derEncode(var1);
    }

    public ObjectIdentifier getAttributeId() {
        return this.attributeId;
    }

    public Object getAttributeValue() {
        return this.attributeValue;
    }

    public String toString() {
        return this.attributeValue.toString();
    }
}

