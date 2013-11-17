package com.trace.hadoop.hadoopinternal.rmi;

import java.io.Serializable;
import java.util.Date;

public class RMIFileStatus implements Serializable {
	private static final long serialVersionUID = -3573592998375792943L;
	
	private String filename;
    private long time;
    
	public RMIFileStatus(String filename) {
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
