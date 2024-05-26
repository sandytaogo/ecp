package com.sandy.ecp.framework;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AlgorithmTest implements Runnable {
	
	ReadWriteLock lock = new ReentrantReadWriteLock();
	/**
	 * 冒泡排序
	 * @param arr
	 * @return
	 */
	public int [] bubbleSort(int [] arr) {
		Lock l = lock.writeLock();
		System.out.println(l);
		return null;
	}
	
	public void run() {
		int [] arr = {65,56,33,4,34324,24,4,4,34,55,21,342,4,43656,32,4324,45435,5,33};
		arr = bubbleSort(arr);
		for (int i = 0 ; i < arr.length; i ++) {
			System.out.printf("%s,",arr[i]);
		}
		
		//MulticastSocket ms = null;
		System.out.print("\n");
		System.out.println(~~ (1 >>> 0));
	}
	
	public static void main(String [] args) {
		AlgorithmTest algorithm = new AlgorithmTest();
		algorithm.run();
		//long r = 111111111111111111L;
		//Double b = 1111111111.123123132333323324324324324344;
		System.out.println(System.currentTimeMillis());
	}
}
