package com.trace.net.http;

public class MessageBean {
	private int m_errorCode = 1; // default 1, error <0
	private String m_errorStr = "";
	private String m_content = "";
	private String m_view_url = "";
	private String m_xml = "";
	private String m_title = "";// html/header/title
	private String m_content_type = ""; //文件类型如html/text,application/pdf等
	private String m_responseCode = "UTF-8";//meta charset

	public String getM_responseCode() {
		return m_responseCode;
	}

	public void setM_responseCode(String mResponseCode) {
		m_responseCode = mResponseCode;
	}

	public String getM_content_type() {
		return m_content_type;
	}

	public void setM_content_type(String mContentType) {
		m_content_type = mContentType;
	}

	public String getM_xml() {
		return m_xml;
	}

	public void setM_xml(String m_xml) {
		this.m_xml = m_xml;
	}

	public String getM_view_url() {
		return m_view_url;
	}

	public void setM_view_url(String m_view_url) {
		this.m_view_url = m_view_url;
	}

	public int getM_errorCode() {
		return m_errorCode;
	}

	public void setM_errorCode(int code) {
		m_errorCode = code;
	}

	public String getM_errorStr() {
		return m_errorStr;
	}

	public void setM_errorStr(String str) {
		m_errorStr = str;
	}

	public String getM_content() {
		return m_content;
	}

	public void setM_content(String m_content) {
		this.m_content = m_content;
	}

	public String getM_title() {
		return m_title;
	}

	public void setM_title(String m_title) {
		this.m_title = m_title;
	}

}
