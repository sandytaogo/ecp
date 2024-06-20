package com.sandy.ecp.framework;


import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.sandy.ecp.framework.service.JavaSmsSenderImpl;

@RunWith(JUnitPlatform.class)
public class SmsServiceTest {
	
	@Test
	public void test() {
		JavaSmsSenderImpl s = new JavaSmsSenderImpl();
		s.setUrl("http://utf8.api.smschinese.cn");
		s.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
		s.setDefaultEncoding("UTF-8");
		s.setUsername("testname");
		s.setPassword("test");
		String res = s.send("注册验证码为:5566", "test");
		System.out.println("反馈结果:" + res);
	}
}
