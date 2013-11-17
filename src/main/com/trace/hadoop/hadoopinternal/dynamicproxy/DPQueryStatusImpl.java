package com.trace.hadoop.hadoopinternal.dynamicproxy;

public class DPQueryStatusImpl implements PDQueryStatus {
	public DPFileStatus getFileStatus(String filename) {
		DPFileStatus status=new DPFileStatus(filename);
		System.out.println("Method getFileStatus Called, return: "+status);
		return status;
	}
}