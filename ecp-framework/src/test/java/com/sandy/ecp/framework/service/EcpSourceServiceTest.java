package com.sandy.ecp.framework.service;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

//@RunWith(SpringJUnit4ClassRunner.class)
public class EcpSourceServiceTest {

//	@Autowired
	private BundleMessageSource bundleMessageSource = new BundleMessageSource();
	
	@Before
	public void before() throws Exception {
		bundleMessageSource.setLocationPattern("classpath*:/META-INF/ecp-i18n/*.properties");
		bundleMessageSource.init();
	}
	
	@Test
	public void messageTest() {
		String message = bundleMessageSource.getMessage("message", null, "必填项", Locale.SIMPLIFIED_CHINESE);
		System.out.println(message);
	}
}
