package com.trace.hadoop;

import java.io.IOException;
import java.net.UnknownHostException;

import com.trace.net.IoClient;

public class ConnectName {
	public static void main(String[] args) throws UnknownHostException,
			IOException {

		String host = "192.168.100.159";
		int port = 9000;
		new IoClient(host, port);
	}
}
