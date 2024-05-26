package com.sandy.ecp.framework;

public class SuduTest {

	public static void main(String[] args) {
		int a = 2;
		int b = 2;
		@SuppressWarnings("unused")
		int aResult;
		@SuppressWarnings("unused")
		int bResult;
		long startTime;
		long endTime;
		long time;
		
		startTime = System.currentTimeMillis();
		for (int i = 0; i < 100000000; i++) {
			bResult = b * 8;
		}
		endTime = System.currentTimeMillis();
		time = endTime - startTime;
		System.out.println("乘法100000000次时间：" + time);
		
		startTime = System.currentTimeMillis();
		for (int i = 0; i < 100000000; i++) {
			aResult = a << 3;
		}
		endTime = System.currentTimeMillis();
		time = endTime - startTime;
		System.out.println("位移100000000次时间：" + time);

		
	}

}
