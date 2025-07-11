/*
 * Copyright 2023-2033 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.framework.util;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECConstants;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;

/**
 * 对org.bouncycastle:bcprov-jdk15on:1.57扩展
 * <br/>BC库加密结果是按C1C2C3，国密标准是C1C3C2（加密芯片也是这个排列），
 * <br/>本扩展主要实现加密结果排列方式可选
 * @Author sandy
 * @Version 2021/1/20
 */
public class SM2EngineExtend {
	
    private final Digest digest;
 
    /**是否为加密模式*/
    private boolean forEncryption;
    private ECKeyParameters ecKey;
    private ECDomainParameters ecParams;
    private int curveLength;
    private SecureRandom random;
    /**密文排序方式*/
    private int cipherMode;
 
    /**BC库默认排序方式-C1C2C3*/
    public static int CIPHERMODE_BC = 0;
    /**国密标准排序方式-C1C3C2*/
    public static int CIPHERMODE_NORM = 1;
 
    public SM2EngineExtend() {
        this(new SM3Digest());
    }
 
    public SM2EngineExtend(Digest digest) {
        this.digest = digest;
    }
 
    /**
     * 设置密文排序方式
     * @param cipherMode
     */
    public void setCipherMode(int cipherMode){
        this.cipherMode = cipherMode;
    }
 
    /**
     * 默认初始化方法，使用国密排序标准
     * @param forEncryption - 是否以加密模式初始化
     * @param param - 曲线参数
     */
    public void init(boolean forEncryption, CipherParameters param) {
        init(forEncryption, CIPHERMODE_NORM, param);
    }
 
    /**
     * 默认初始化方法，使用国密排序标准
     * @param forEncryption 是否以加密模式初始化
     * @param cipherMode 加密数据排列模式：1-标准排序；0-BC默认排序
     * @param param 曲线参数
     */
    public void init(boolean forEncryption, int cipherMode, CipherParameters param) {
        this.forEncryption = forEncryption;
        this.cipherMode = cipherMode;
        if (forEncryption) {
            ParametersWithRandom rParam = (ParametersWithRandom) param;
 
            ecKey = (ECKeyParameters) rParam.getParameters();
            ecParams = ecKey.getParameters();
 
            ECPoint s = ((ECPublicKeyParameters) ecKey).getQ().multiply(ecParams.getH());
            if (s.isInfinity()) {
                throw new IllegalArgumentException("invalid key: [h]Q at infinity");
            }
 
            random = rParam.getRandom();
        } else {
            ecKey = (ECKeyParameters) param;
            ecParams = ecKey.getParameters();
        }
 
        curveLength = (ecParams.getCurve().getFieldSize() + 7) / 8;
    }
 
    /**
     * 加密或解密输入数据
     * @param in
     * @param inOff
     * @param inLen
     * @return
     * @throws InvalidCipherTextException
     */
    public byte[] processBlock( byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
        if (forEncryption) {
            // 加密
            return encrypt(in, inOff, inLen);
        } else {
            return decrypt(in, inOff, inLen);
        }
    }
 
    /**
     * 加密实现，根据cipherMode输出指定排列的结果，默认按标准方式排列
     * @param in
     * @param inOff
     * @param inLen
     * @return
     * @throws InvalidCipherTextException
     */
    private byte[] encrypt(byte[] in, int inOff, int inLen)
            throws InvalidCipherTextException {
        byte[] c2 = new byte[inLen];
 
        System.arraycopy(in, inOff, c2, 0, c2.length);
 
        byte[] c1;
        ECPoint kPB;
        do {
            BigInteger k = nextK();
 
            ECPoint c1P = ecParams.getG().multiply(k).normalize();
 
            c1 = c1P.getEncoded(false);
 
            kPB = ((ECPublicKeyParameters) ecKey).getQ().multiply(k).normalize();
 
            kdf(digest, kPB, c2);
        }
        while (notEncrypted(c2, in, inOff));
 
        byte[] c3 = new byte[digest.getDigestSize()];
 
        addFieldElement(digest, kPB.getAffineXCoord());
        digest.update(in, inOff, inLen);
        addFieldElement(digest, kPB.getAffineYCoord());
 
        digest.doFinal(c3, 0);
        if (cipherMode == CIPHERMODE_NORM){
            return Arrays.concatenate(c1, c3, c2);
        }
        return Arrays.concatenate(c1, c2, c3);
    }
 
    /**
     * 解密实现，默认按标准排列方式解密，解密时解出c2部分原文并校验c3部分
     * @param in
     * @param inOff
     * @param inLen
     * @return
     * @throws InvalidCipherTextException
     */
    private byte[] decrypt(byte[] in, int inOff, int inLen)
            throws InvalidCipherTextException {
        byte[] c1 = new byte[curveLength * 2 + 1];
 
        System.arraycopy(in, inOff, c1, 0, c1.length);
 
        ECPoint c1P = ecParams.getCurve().decodePoint(c1);
 
        ECPoint s = c1P.multiply(ecParams.getH());
        if (s.isInfinity()) {
            throw new InvalidCipherTextException("[h]C1 at infinity");
        }
 
        c1P = c1P.multiply(((ECPrivateKeyParameters) ecKey).getD()).normalize();
 
        byte[] c2 = new byte[inLen - c1.length - digest.getDigestSize()];
        if (cipherMode == CIPHERMODE_BC) {
            System.arraycopy(in, inOff + c1.length, c2, 0, c2.length);
        }else{
            // C1 C3 C2
            System.arraycopy(in, inOff + c1.length + digest.getDigestSize(), c2, 0, c2.length);
        }
 
        kdf(digest, c1P, c2);
 
        byte[] c3 = new byte[digest.getDigestSize()];
 
        addFieldElement(digest, c1P.getAffineXCoord());
        digest.update(c2, 0, c2.length);
        addFieldElement(digest, c1P.getAffineYCoord());
 
        digest.doFinal(c3, 0);
 
        int check = 0;
        // 检查密文输入值C3部分和由摘要生成的C3是否一致
        if (cipherMode == CIPHERMODE_BC) {
            for (int i = 0; i != c3.length; i++) {
                check |= c3[i] ^ in[c1.length + c2.length + i];
            }
        }else{
            for (int i = 0; i != c3.length; i++) {
                check |= c3[i] ^ in[c1.length + i];
            }
        }
 
        clearBlock(c1);
        clearBlock(c3);
 
        if (check != 0) {
            clearBlock(c2);
            throw new InvalidCipherTextException("invalid cipher text");
        }
 
        return c2;
    }
 
    private boolean notEncrypted(byte[] encData, byte[] in, int inOff) {
        for (int i = 0; i != encData.length; i++) {
            if (encData[i] != in[inOff]) {
                return false;
            }
        }
 
        return true;
    }
 
    private void kdf(Digest digest, ECPoint c1, byte[] encData) {
        int ct = 1;
        int v = digest.getDigestSize();
 
        byte[] buf = new byte[digest.getDigestSize()];
        int off = 0;
 
        for (int i = 1; i <= ((encData.length + v - 1) / v); i++) {
            addFieldElement(digest, c1.getAffineXCoord());
            addFieldElement(digest, c1.getAffineYCoord());
            digest.update((byte) (ct >> 24));
            digest.update((byte) (ct >> 16));
            digest.update((byte) (ct >> 8));
            digest.update((byte) ct);
 
            digest.doFinal(buf, 0);
 
            if (off + buf.length < encData.length) {
                xor(encData, buf, off, buf.length);
            } else {
                xor(encData, buf, off, encData.length - off);
            }
 
            off += buf.length;
            ct++;
        }
    }
 
    private void xor(byte[] data, byte[] kdfOut, int dOff, int dRemaining) {
        for (int i = 0; i != dRemaining; i++) {
            data[dOff + i] ^= kdfOut[i];
        }
    }
 
    private BigInteger nextK() {
        int qBitLength = ecParams.getN().bitLength();
 
        BigInteger k;
        do {
            k = new BigInteger(qBitLength, random);
        }
        while (k.equals(ECConstants.ZERO) || k.compareTo(ecParams.getN()) >= 0);
 
        return k;
    }
 
    private void addFieldElement(Digest digest, ECFieldElement v) {
        byte[] p = BigIntegers.asUnsignedByteArray(curveLength, v.toBigInteger());
 
        digest.update(p, 0, p.length);
    }
 
    /**
     * clear possible sensitive data
     */
    private void clearBlock(byte[] block) {
        for (int i = 0; i != block.length; i++) {
            block[i] = 0;
        }
    }
}