package com.sandy.ecp.framework.lib;

public class LibTest {

	static {
		System.loadLibrary("ygCrypt32");
	}
	
	public native String SelectCertFromStore();
	
	public static void main(String[] args) {
		System.out.println("sdf");
	}
	
}
