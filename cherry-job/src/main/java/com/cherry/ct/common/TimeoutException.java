package com.cherry.ct.common;

public class TimeoutException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5981695095544630368L;

	public TimeoutException(String errMessage)
	{
		super(errMessage);
	}
}
