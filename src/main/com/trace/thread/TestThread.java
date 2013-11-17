package com.trace.thread;

public class TestThread extends Thread {
	String id;

	public TestThread(String s) {
		id = s;
	}

	public void doCalc(int i) {
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Perform complex calculation based on i.
	}

	public void run() {
		int i;
		for (i = 0; i < 100; i++) {
//			doCalc(i);
			System.out.println(id);
		}
	}
}
