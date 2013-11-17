package com.trace.net.http;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;

/**
 * Http response class which represents the response from http request
 * 
 * @author Jack Wang
 *
 */
public class AlertieHttpResponse {
	
	Header [] respHeaders = null;
	
	int statuscode;
	
	byte [] response = new byte [0];
	
	Cookie [] cookie;
	
	String url = "";
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}

	public Header[] getRespHeaders() {
		return respHeaders;
	}

	public void setRespHeaders(Header[] respHeaders) {
		this.respHeaders = respHeaders;
	}

	public byte[] getResponse() {
		return response;
	}

	public void setResponse(byte[] response) {
		this.response = response;
	}

	public int getStatuscode() {
		return statuscode;
	}

	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}
}