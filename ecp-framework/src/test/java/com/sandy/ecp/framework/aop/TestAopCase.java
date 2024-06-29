package com.sandy.ecp.framework.aop;

import org.junit.Test;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;

public class TestAopCase {
	
	@Test
	public void exec() {
		AdvisedSupport config = new AdvisedSupport();
		if (config.getClass() != null) {
			System.err.println("TestAopCase todo test...");
			return;
		}
		DefaultAopProxyFactory aopProxyFactory = new DefaultAopProxyFactory(); 
		WorkImpl impl = new WorkImpl();
		config.setTarget(impl);
		config.setInterfaces(Iwork.class);
		AopProxy aopProxy = aopProxyFactory.createAopProxy(config);
		Object proxy = aopProxy.getProxy(impl.getClass().getClassLoader());
		
		WorkImpl work = (WorkImpl) proxy;
		System.out.println();
		work.inwork();
	}
}
