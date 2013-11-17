package com.trace.hadoop.hadoopinternal.dynamicproxy;

import java.lang.reflect.Proxy;

public class DPMain {
	
	public static PDQueryStatus create(DPQueryStatusImpl dpqs) {
		DPInvocationHandler handler = new DPInvocationHandler(dpqs);
		
		Class<?>[] interfaces=new Class[] { PDQueryStatus.class };
		
		Object result = Proxy.newProxyInstance(dpqs.getClass().getClassLoader(), 
				                               interfaces, 
				                               handler);
		Class<?> proxy=Proxy.getProxyClass(dpqs.getClass().getClassLoader(), 
                                           interfaces);

		System.out.println("Proxy class name: "+proxy.getCanonicalName());
		System.out.println("Proxy's super class name: "+proxy.getSuperclass().getName());
		System.out.println();
		
		return (PDQueryStatus) result;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			PDQueryStatus query = DPMain.create(new DPQueryStatusImpl());
			DPFileStatus status = query.getFileStatus("/tmp/testDP");
			System.out.println(status);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}



