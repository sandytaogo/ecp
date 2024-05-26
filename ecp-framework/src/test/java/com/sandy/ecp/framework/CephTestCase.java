/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the HUIFU  License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://huifuwang.com
 */
package com.sandy.ecp.framework;

/**
 * Ceph Object Storage Daemon
 * 
 * @author Sandy
 * @see https://www.ibm.com/developerworks/cn/linux/l-ceph/index.html
 */
public class CephTestCase {

	public static void main(String [] args) {
		int x = 8, y =11, z;
		System.out.println("x="+Integer.toBinaryString(x));
		System.out.println("y="+Integer.toBinaryString(y));
		z = ~(x ^ y);
		System.out.println("z="+z);
		System.out.println("z="+Integer.toBinaryString(z));
		System.out.println(31 * 0XFFFFFFFF);
		
		long i = 22;
		System.out.println((int) i ^ (i >>> 32) );
		
		Integer cent =0XFFFFFFFF;
		cent = cent >>> 1;
		System.out.println("cent="+cent.intValue());
		System.out.println("cent="+Integer.toBinaryString(cent.intValue()));
		
		System.out.println("a分士".toUpperCase());
	}
}
