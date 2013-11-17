package com.trace.thread.future;

public class FutureData implements Data{
private RealData realdata = null;
private boolean ready = false;
public synchronized void setRealData(RealData realdata){
	if(ready){
		return;
	}
	this.realdata = realdata;
	this.ready = true;
	notifyAll();
}
	public synchronized String getContent() {
		// TODO Auto-generated method stub
		while(!ready){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO: handle exception
			}
		}
		return realdata.getContent();
	}

}
