package com.sandy.ecp.framework;

import org.junit.Test;

import com.sandy.ecp.framework.service.JavaSmsSenderImpl;

import jdk.nashorn.internal.ir.annotations.Ignore;

public class SmsServiceTest {
	
	@Test
	@Ignore
	public void test() {
		JavaSmsSenderImpl s = new JavaSmsSenderImpl();
		s.setUrl("http://utf8.api.smschinese.cn");
		s.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
		s.setDefaultEncoding("UTF-8");
		s.setUsername("mrtao");
		s.setPassword("2be867423fca4e235d66");
		String res = s.send("注册验证码为:5566", "13536598775");
		System.out.println("反馈结果:" + res);
	}
}
