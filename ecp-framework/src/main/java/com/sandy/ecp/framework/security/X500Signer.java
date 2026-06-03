/*
 * Copyright 2018-2030 the original author or authors.
 *
 * Licensed under the company, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.company.com/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.framework.security;

import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.Signer;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X500Name;

/**
 * X500 Signer
 * @author sandy 
 * @version 2.0
 */
public final class X500Signer extends Signer {
    private static final long serialVersionUID = -8609982645394364834L;
    private Signature sig;
    private X500Name agent;
    private AlgorithmId algid;

    public void update(byte[] var1, int var2, int var3) throws SignatureException {
        this.sig.update(var1, var2, var3);
    }

    public byte[] sign() throws SignatureException {
        return this.sig.sign();
    }

    public AlgorithmId getAlgorithmId() {
        return this.algid;
    }

    public X500Name getSigner() {
        return this.agent;
    }

    public X500Signer(Signature var1, X500Name var2) {
        if (var1 != null && var2 != null) {
            this.sig = var1;
            this.agent = var2;

            try {
                this.algid = AlgorithmId.getAlgorithmId(var1.getAlgorithm());
            } catch (NoSuchAlgorithmException var4) {
                throw new RuntimeException("internal error! " + var4.getMessage());
            }
        } else {
            throw new IllegalArgumentException("null parameter");
        }
    }
}
