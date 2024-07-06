package com.sandy.ecp.framework.service;


import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import com.sandy.ecp.framework.service.JavaSmsSenderImpl;

@RunWith(JUnitPlatform.class)
public class EcpSmsServiceTest {
	
	@Autowired
	private Environment environment;
	
	@Test
	public void test() {
		JavaSmsSenderImpl s = new JavaSmsSenderImpl();
		s.setTimeout(5000);
		s.setEnvironment(environment);
		s.setUrl("http://utf8.api.smschinese.cn");
		s.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
		s.setDefaultEncoding("UTF-8");
		s.setUsername("testname");
		s.setPassword("test");
		String res = s.send("注册验证码为:5566", "test");
		System.out.println("反馈结果:" + res);
	}
}
