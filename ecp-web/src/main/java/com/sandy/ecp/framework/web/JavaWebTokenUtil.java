/*
 * Copyright 2024-2030 the original author or authors.
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
package com.sandy.ecp.framework.web;

import java.security.Key;
import java.security.SignatureException;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public final class JavaWebTokenUtil
{
    public static final String JWT_ERRCODE_EXPIRE = "JWT_ERRCODE_EXPIRE";
    public static final String JWT_ERRCODE_SIGNATURE_FAIL = "JWT_ERRCODE_SIGNATURE_FAIL";
    private static final String JWT_ERRCODE_FAIL = "JWT_ERRCODE_FAIL";
    private static final String JWT_ISSUER = "tianqingAdmin";
    
    private static String jwtSecert;
    private static SignatureAlgorithm signatureAlgorithm;
    
    static {
        jwtSecert = "javaWebToken";
        signatureAlgorithm = SignatureAlgorithm.HS256;
    }
    
    public static void setJwtSecert(final String jwtSecert) {
        JavaWebTokenUtil.jwtSecert = jwtSecert;
    }
    
    public static void setSignatureAlgorithm(final SignatureAlgorithm signatureAlgorithm) {
        JavaWebTokenUtil.signatureAlgorithm = signatureAlgorithm;
    }
    
    public static SecretKey generalKey() {
        final byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(JavaWebTokenUtil.jwtSecert);
        final SecretKey signingKey = new SecretKeySpec(apiKeySecretBytes, JavaWebTokenUtil.signatureAlgorithm.getJcaName());
        return signingKey;
    }
    
    public static String createJWTtoken(final String id, final String audience, final String subject, final Map<String, Object> claims, final Long ttlMillis) {
        final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        final long nowMillis = System.currentTimeMillis();
        final Date now = new Date(nowMillis);
        final SecretKey secretKey = generalKey();
        final JwtBuilder builder = Jwts.builder().setId(id).setAudience(audience).setClaims((Map)claims).setSubject(subject).setIssuer("tianqingAdmin").setIssuedAt(now).signWith(signatureAlgorithm, (Key)secretKey);
        if (ttlMillis != null && ttlMillis >= 0L) {
            final long expMillis = nowMillis + ttlMillis;
            final Date expDate = new Date(expMillis);
            builder.setExpiration(expDate);
        }
        return builder.compact();
    }
    
    public static String createJWTtoken(final String id, final String audience, final String subject, final Map<String, Object> claims, final Date expDate) {
        final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        final long nowMillis = System.currentTimeMillis();
        final Date now = new Date(nowMillis);
        final SecretKey secretKey = generalKey();
        final JwtBuilder builder = Jwts.builder().setId(id).setAudience(audience).setClaims((Map)claims).setSubject(subject).setIssuer("tianqingAdmin").setIssuedAt(now).signWith(signatureAlgorithm, (Key)secretKey);
        if (expDate != null) {
            builder.setExpiration(expDate);
        }
        return builder.compact();
    }
    
    public static JWTCheckResult validateJWT(final String jsonWebToken) {
        final JWTCheckResult checkResult = new JWTCheckResult();
        Claims claims = null;
        try {
            claims = parseJWT(jsonWebToken);
            checkResult.setSuccess(true);
            checkResult.setClaims(claims);
        }
        catch (ExpiredJwtException e) {
            checkResult.setErrCode("JWT_ERRCODE_EXPIRE");
            checkResult.setSuccess(false);
            checkResult.setCause((Throwable)e);
        }
        catch (SignatureException e2) {
            checkResult.setErrCode("JWT_ERRCODE_SIGNATURE_FAIL");
            checkResult.setSuccess(false);
            checkResult.setCause((Throwable)e2);
        }
        catch (Exception e3) {
            checkResult.setErrCode("JWT_ERRCODE_FAIL");
            checkResult.setSuccess(false);
            checkResult.setCause(e3);
        }
        return checkResult;
    }
    
    public static Claims parseJWT(final String jsonWebToken) throws Exception {
        final SecretKey secretKey = generalKey();
        return (Claims)Jwts.parser().setSigningKey((Key)secretKey).parseClaimsJws(jsonWebToken).getBody();
    }
    
}
