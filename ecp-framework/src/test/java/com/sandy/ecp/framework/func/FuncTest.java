package com.sandy.ecp.framework.func;

import java.util.HashMap;

import org.junit.Test;

public class FuncTest {

	@Test
	public void tes () {
		FunCentent context = new FunCentent();
		context.exe(f -> f, new HashMap<String, Object>());
	}
}
