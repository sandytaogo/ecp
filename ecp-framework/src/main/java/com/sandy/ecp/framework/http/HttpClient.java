/*
 * Copyright 2024-2030 the original author or authors.
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
package com.sandy.ecp.framework.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.sandy.ecp.framework.exception.HttpException;
import com.sandy.ecp.framework.http.ssl.NoopHostnameVerifier;
import com.sandy.ecp.framework.http.ssl.TrustAnyTrustManager;

import sun.net.www.protocol.http.HttpURLConnection;
import sun.net.www.protocol.https.Handler;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;

public class HttpClient {
	
	public static int CONNECT_TIMEOUT = 3000;
	
	public static int READ_TIMEOUT = 15000;

	public static void httpAllHost() {
		TrustManager[] trustManager = new TrustManager [] { new TrustAnyTrustManager() };
		try {
			SSLContext sc = SSLContext.getInstance("SSL", "SunJSSE");
			sc.init(null, trustManager, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(NoopHostnameVerifier.INSTANCE);
			HttpsURLConnectionImpl.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnectionImpl.setDefaultHostnameVerifier(NoopHostnameVerifier.INSTANCE);
		} catch (NoSuchAlgorithmException | NoSuchProviderException | KeyManagementException e) {
			e.printStackTrace();
		}
	}
	
	public static String get(Map<String, String> headers, String url) {
		java.net.HttpURLConnection conn = null;
		InputStream in = null;
		final StringBuilder sb = new StringBuilder();
		String charset = Charset.defaultCharset().name();
		try {
			try {
				final Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass("sun.net.www.protocol.http.HttpURLConnection");
				if (clazz != null) {
					conn = new HttpURLConnection(new URL(url), (Proxy) null);
				}
			} catch (ClassNotFoundException ex) {
				conn = (java.net.HttpURLConnection) new URL(url).openConnection();
			}
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			if (headers != null) {
				for (String key : headers.keySet() ) {
					conn.setRequestProperty(key, headers.get(key));
					if ("charset".equalsIgnoreCase(key)) {
						charset = headers.get(key);
					}
				}
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			final int statusCode = conn.getResponseCode();
			if (statusCode == HttpURLConnection.HTTP_OK) {
				in = conn.getInputStream();
			} else {
				in = conn.getErrorStream();
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));
			String readLine;
			while ( (readLine = br.readLine()) != null) {
				sb.append(readLine);
			}
			in.close();
			if (statusCode == HttpURLConnection.HTTP_OK) {
				return sb.toString();
			}
		} catch (Exception e) {
			throw new HttpException(e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return sb.toString();
	}
	
	public static String httpsGet(Map<String, String> headers, String url) {
		HttpsURLConnection conn = null;
		InputStream in = null;
		final StringBuilder sb = new StringBuilder();
		String charset = Charset.defaultCharset().name();
		try {
			URL urls = new URL(null, url, new Handler());
			conn = (HttpsURLConnection) urls.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			if (headers != null) {
				for (String key : headers.keySet() ) {
					conn.setRequestProperty(key, headers.get(key));
					if ("charset".equalsIgnoreCase(key)) {
						charset = headers.get(key);
					}
				}
			}
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			final int statusCode = conn.getResponseCode();
			if (statusCode == HttpURLConnection.HTTP_OK) {
				in = conn.getInputStream();
			} else {
				in = conn.getErrorStream();
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));
			String readLine;
			while ( (readLine = br.readLine()) != null) {
				sb.append(readLine);
			}
			in.close();
			if (statusCode == HttpURLConnection.HTTP_OK) {
				return sb.toString();
			}
		} catch (Exception e) {
			throw new HttpException(e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return sb.toString();
	}
}
