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

import io.jsonwebtoken.Claims;

public class JWTCheckResult
{
    private Claims claims;
    private boolean isSuccess;
    private String errCode;
    private Throwable cause;
    
    public Claims getClaims() {
        return this.claims;
    }
    
    public void setClaims(final Claims claims) {
        this.claims = claims;
    }
    
    public boolean isSuccess() {
        return this.isSuccess;
    }
    
    public void setSuccess(final boolean isSuccess) {
        this.isSuccess = isSuccess;
    }
    
    public String getErrCode() {
        return this.errCode;
    }
    
    public void setErrCode(final String errCode) {
        this.errCode = errCode;
    }
    
    public Throwable getCause() {
        return this.cause;
    }
    
    public void setCause(final Throwable cause) {
        this.cause = cause;
    }
}
