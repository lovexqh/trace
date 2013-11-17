package com.trace.hadoop.hadoopinternal.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIQueryStatusClient {
	public static void main(String[] args) {
		try {
			RMIQueryStatus query=(RMIQueryStatus) Naming.lookup(RMIQueryStatusServer.RMI_URL);
			RMIFileStatus status=query.getFileStatus("/tmp/testRMI");
			System.out.println(status);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
}
