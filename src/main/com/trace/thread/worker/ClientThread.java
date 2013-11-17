package com.trace.thread.worker;

import java.util.Random;

public class ClientThread extends Thread {
	private final Channel channel;
	private static final Random random = new Random();

	public ClientThread(String name, Channel channel) {
		super(name);
		this.channel = channel;
	}

	public void run() {
		try {
			for (int i = 0; true; i++) {
				Request request = new Request(getName(), i);
				channel.putRequest(request);
				Thread.sleep(random.nextInt(100));
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
