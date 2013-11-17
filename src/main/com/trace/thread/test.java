package com.trace.thread;

import java.net.URL;

public class test {
	byte b1 = 2;
	byte b2 = 3;

	// byte b3 = b1 + b2;
public static void testUrl(){
}
	public static void main(String args[]) {
		TestThread t1, t2, t3;
		t1 = new TestThread("Thread1");
		t2 = new TestThread("Thread 2");
		t3 = new TestThread("Thread 3");
		t1.start();
		t2.start();
		t3.start();
		}
}
