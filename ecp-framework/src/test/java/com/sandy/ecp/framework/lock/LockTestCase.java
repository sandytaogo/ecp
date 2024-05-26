package com.sandy.ecp.framework.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.xmlbeans.impl.common.Mutex;

//import EDU.oswego.cs.dl.util.concurrent.ReentrantWriterPreferenceReadWriteLock;

public class LockTestCase {
	
	Lock lock = new ReentrantLock();
	
	Mutex mutex = null;
	
	Condition condition = null;
	
//	ReentrantWriterPreferenceReadWriteLock edulock = new ReentrantWriterPreferenceReadWriteLock();

	public void test () throws InterruptedException {
		condition = lock.newCondition();
		int i = 0;
		lock.lock();
		try {
			System.out.println(String.format("%s, %s", Thread.currentThread(), System.currentTimeMillis()));
			//Thread.sleep(1000);
			i ++;
			System.out.println(i);
			condition.signal();
			condition.await();
		} finally {
			lock.unlock();
			System.out.println("unlock");
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		final LockTestCase test = new LockTestCase();
		Thread a = new Thread() {
			public void run() {
				try {
					test.test();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		};
		a.setName("aaaaaaaaa1");
		a.start();
		Thread b = new Thread() {
			public void run() {
				try {
					test.test();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		};
		b.setName("aaaaaaaaa2");
		b.start();
	}
	
}
