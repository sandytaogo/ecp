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
package com.sandy.ecp.framework.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.sandy.ecp.framework.util.StringUtil;

/**
 * Cross Site Scripting
 * @author Sandy
 * @since 1.0.0 2023-04-05 15:13:13
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
	
    private HttpServletRequest request;
    
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
    }
 
    @Override
    public String getHeader(String name) {
        return StringUtil.escapeHtml4(super.getHeader(name));
    }

    @Override
    public String getQueryString() {
        return StringUtil.escapeHtml4(super.getQueryString());
    }

    @Override
    public String getParameter(String name) {
        return StringUtil.escapeHtml4(super.getParameter(name));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
    	StringBuilder sb = new StringBuilder();
    	String readLine;
    	while ((readLine = br.readLine()) != null) {
    		sb.append(readLine);
    	}
    	final ByteArrayInputStream bais = new ByteArrayInputStream(sb.toString().getBytes());
        return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return bais.read();
			}
			@Override
			public void setReadListener(ReadListener listener) {
			}
			@Override
			public boolean isReady() {
				return bais.available() > 0;
			}
			@Override
			public boolean isFinished() {
				return bais.available() == 0;
			}
		};
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameterValues = super.getParameterValues(name);
        if (Objects.isNull(parameterValues)) {
            return null;
        }
        for (int i = 0; i < parameterValues.length; i++) {
            parameterValues[i] = StringUtil.escapeHtml4(parameterValues[i]);
        }
        return parameterValues;
    }
}