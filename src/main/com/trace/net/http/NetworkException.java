package com.trace.net.http;

/**
 * 
 * Throws when network problem occurs
 * 
 * @author Jack Wang
 *
 */
public class NetworkException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7670936240419387749L;

	public NetworkException (String message) {
		super (message);
	}
	
	public NetworkException (Throwable error) {
		super (error);
	}
	
	public NetworkException (String message, Throwable error) {
		super (message, error);
	}
}
