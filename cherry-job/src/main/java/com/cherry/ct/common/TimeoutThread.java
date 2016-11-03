package com.cherry.ct.common;

public class TimeoutThread implements Runnable{
	private long timeout;
	
	private boolean isCanceled = false;
	
	private TimeoutException timeoutException;
	
	public TimeoutThread(long timeout, TimeoutException timeoutErr){
		super();
		this.timeout = timeout;
		this.timeoutException = timeoutErr;
	}
	
	public synchronized void cancel(){
		this.isCanceled = true;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(timeout);
			if(!isCanceled){
				throw timeoutException;
			}
		}catch(InterruptedException e){
			throw timeoutException;
		}
	}

}
