package com.sandy.ecp.framework.util;

import org.junit.Test;

public class StringUtilTest {

	
	@Test
	public void toFirstTest() {
		System.out.println(StringUtil.toFirstChar("贵州茅台"));
		System.out.println(StringUtil.toFirstChar("寧德時代"));
	}
	
	@Test
	public void utilTest() {
		System.out.println(IdUtil.getUUID());
	}
	
}
