package com.trace.hadoop.hadoopinternal.rmi;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIQueryStatusServer {
	public static final String RMI_URL = "rmi://bincai-pc:12090/query";

	public static void main(String[] args) {
		try {		    
			RMIQueryStatusImpl queryService=new RMIQueryStatusImpl();
			
			LocateRegistry.createRegistry(12090);
			Naming.rebind(RMI_URL, queryService);
			
			System.out.println("Server ready");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
