package com.sandy.ecp.framework.spi;

import java.util.ServiceLoader;

import org.junit.Test;

import com.sandy.ecp.framework.datasource.SpiProvider;


public class DatabaseSPItTest {
	
	@Test
	public void spiTest() {
		ServiceLoader<SpiProvider> spis = ServiceLoader.load(SpiProvider.class);
		spis.forEach(f ->  {
			System.out.println(String.format("箭头函数%s", f));
		});
	}
	
}
