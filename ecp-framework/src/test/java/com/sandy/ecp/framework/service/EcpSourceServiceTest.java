package com.sandy.ecp.framework.service;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.sandy.ecp.framework.i18n.EcpBundleMessageSource;

//@RunWith(SpringJUnit4ClassRunner.class)
public class EcpSourceServiceTest {

//	@Autowired
	private EcpBundleMessageSource bundleMessageSource = new EcpBundleMessageSource();
	
	@Before
	public void before() throws Exception {
		bundleMessageSource.setLocationPattern("classpath*:/ecp-i18n/*.properties");
		bundleMessageSource.init();
	}
	
	@Test
	public void messageTest() {
		String message = bundleMessageSource.getMessage("message", null, "必填项", Locale.SIMPLIFIED_CHINESE);
		System.out.println(message);
	}
}
