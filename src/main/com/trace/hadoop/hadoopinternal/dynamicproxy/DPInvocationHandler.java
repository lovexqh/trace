package com.trace.hadoop.hadoopinternal.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;

public class DPInvocationHandler implements InvocationHandler {
	private DPQueryStatusImpl dpqs;

	public DPInvocationHandler(DPQueryStatusImpl dpqs) {
		this.dpqs = dpqs;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object ret = null;
		String msg1 = MessageFormat.format("Calling method {0}({1})", 
				                           method.getName(), 
				                           Arrays.toString(args));
		System.out.println(msg1);

		ret = method.invoke(dpqs, args);

		String msg2 = MessageFormat.format("Method {0} returned with ({1})",
				method.getName(), ret.toString());
		System.out.println(msg2);
		return ret;
	}
}
