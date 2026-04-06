package com.sandy.ecp.framework.security.pkcs;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class PKCS10Attributes implements DerEncoder {
    private Hashtable map = new Hashtable(3);

    public PKCS10Attributes() {
    }

    public PKCS10Attributes(PKCS10Attribute[] var1) {
        for(int var2 = 0; var2 < var1.length; ++var2) {
            this.map.put(var1[var2].getAttributeId().toString(), var1[var2]);
        }

    }

    public PKCS10Attributes(DerInputStream var1) throws IOException {
        DerValue[] var2 = var1.getSet(3, true);
        if (var2 == null) {
            throw new IOException("Illegal encoding of attributes");
        } else {
            for(int var3 = 0; var3 < var2.length; ++var3) {
                PKCS10Attribute var4 = new PKCS10Attribute(var2[var3]);
                this.map.put(var4.getAttributeId().toString(), var4);
            }

        }
    }

    public void encode(OutputStream var1) throws IOException {
        this.derEncode(var1);
    }

    public void derEncode(OutputStream var1) throws IOException {
        Collection var2 = this.map.values();
        PKCS10Attribute[] var3 = (PKCS10Attribute[])((PKCS10Attribute[])var2.toArray(new PKCS10Attribute[this.map.size()]));
        DerOutputStream var4 = new DerOutputStream();
        var4.putOrderedSetOf(DerValue.createTag((byte)-128, true, (byte)0), var3);
        var1.write(var4.toByteArray());
    }

    public void setAttribute(String var1, Object var2) {
        this.map.put(var1, var2);
    }

    public Object getAttribute(String var1) {
        return this.map.get(var1);
    }

    public void deleteAttribute(String var1) {
        this.map.remove(var1);
    }

    public Enumeration getElements() {
        return this.map.elements();
    }

    public Collection getAttributes() {
        return Collections.unmodifiableCollection(this.map.values());
    }

    public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        } else if (!(var1 instanceof PKCS10Attributes)) {
            return false;
        } else {
            Collection var2 = ((PKCS10Attributes)var1).getAttributes();
            Object[] var3 = var2.toArray();
            int var4 = var3.length;
            if (var4 != this.map.size()) {
                return false;
            } else {
                String var7 = null;

                for(int var8 = 0; var8 < var4; ++var8) {
                    if (var3[var8] instanceof PKCS10Attribute) {
                        PKCS10Attribute var6 = (PKCS10Attribute)var3[var8];
                        var7 = var6.getAttributeId().toString();
                        if (var7 == null) {
                            return false;
                        }

                        PKCS10Attribute var5 = (PKCS10Attribute)this.map.get(var7);
                        if (var5 == null) {
                            return false;
                        }

                        if (!var5.equals(var6)) {
                            return false;
                        }
                    }
                }

                return true;
            }
        }
    }

    public int hashCode() {
        return this.map.hashCode();
    }

    public String toString() {
        String var1 = this.map.size() + "\n" + this.map.toString();
        return var1;
    }
}
