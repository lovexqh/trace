package com.trace.thread.future;

import javax.swing.plaf.SliderUI;

public class RealData implements Data {
	private final String content;

	public RealData(int count, char c) {
		System.out.println("make realdata " + count + " , " + c + ") BEGIN");
		char[] buffer = new char[count];
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = c;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		content = new String(buffer);
		System.out.println("make realdata " + count + " , " + c + ") END");
	}

	public String getContent() {
		return this.content;
	}
}
