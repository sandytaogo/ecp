package com.sandy.ecp.framework.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;

import org.springframework.core.env.Environment;

import sun.net.www.protocol.http.HttpURLConnection;

/**
 * @see https://www.smschinese.com.cn/main/
 * @see https://www.smschinese.com.cn/Login.shtml
 * @author Sandy
 * 	大于0 提交成功 短信发送数量
 *	-1	没有该用户账户
 *	-2	接口密钥不正确 [查看密钥]不是账户登陆密码
 *	-21	MD5接口密钥加密不正确
 *	-3	短信数量不足
 *	-6	IP限制
 *	-11	该用户被禁用
 *	-14	短信内容出现非法字符
 *	-4	手机号格式不正确
 *	-41	手机号码为空
 *	-42	短信内容为空
 *	-51	短信签名格式不正确接口签名格式为：【签名内容】
 *	-52	短信签名太长建议签名10个字符以内
 *  短信余额：5000 条
 *  2024-05-03 短信余额：4979 条
 */
public class JavaSmsSenderImpl {
	
	protected Environment environment;
	private String username;
	private String password;
	private String url;
	private String contentType;
	private String defaultEncoding;
	private Integer timeout = 3000;
	
	public JavaSmsSenderImpl() {
		super();
	}
	
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}
	
	public String send(String text, String mobile) {
		java.net.HttpURLConnection conn = null;
		InputStream in = null;
		PrintWriter out = null;
		if (defaultEncoding == null) {
			defaultEncoding = Charset.defaultCharset().name();
		}
		final StringBuilder sb = new StringBuilder();
		try {
			try {
				final Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass("sun.net.www.protocol.http.HttpURLConnection");
				if (clazz != null) {
					conn = new HttpURLConnection(new URL(url), (Proxy) null);
				}
			} catch (ClassNotFoundException ex) {
				conn = (java.net.HttpURLConnection) new URL(url).openConnection();
			}
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);
			if (contentType != null) {
				conn.setRequestProperty("Content-Type", contentType);
			}
			conn.setRequestProperty("Charset", defaultEncoding);
			conn.setRequestProperty("Content-Encoding", defaultEncoding);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			out = new PrintWriter(conn.getOutputStream());
			sb.append("Uid=").append(username);
			sb.append("&Key=").append(password);
			sb.append("&smsText=").append(text);
			sb.append("&smsMob=").append(mobile);
			out.write(sb.toString());
			out.flush();
			final int statusCode = conn.getResponseCode();
			if (statusCode == HttpURLConnection.HTTP_OK) {
				in = conn.getInputStream();
			} else {
				in = conn.getErrorStream();
			}
			final byte[] buffer = new byte[2048];
			int count = 0;
			sb.delete(0, sb.length());
			while ((count = in.read(buffer)) > 0) {
				sb.append(new String(buffer, 0, count, defaultEncoding));
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		return sb.toString();
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public void setDefaultEncoding(String defaultEncoding) {
		this.defaultEncoding = defaultEncoding;
	}
	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}
}

