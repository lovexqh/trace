package com.trace.hadoop.hadoopinternal.dynamicproxy;

import java.util.Date;

public class DPFileStatus {	
	private String filename;
    private long time;
    
	public DPFileStatus(String filename) {
		this.filename=filename;
		this.time=(new Date()).getTime();
	}

	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public String toString() {
		return "File: "+filename+" Create at "+(new Date(time)); 
	}
}
