package com.trace.thread.worker;

public class Main {
	public static void main(String[] args) {
		Channel channel = new Channel(5);
		new ClientThread("ALICE", channel).start();
		new ClientThread("BObby", channel).start();
		new ClientThread("Chris", channel).start();
		channel.startWorkers();
	}
}
