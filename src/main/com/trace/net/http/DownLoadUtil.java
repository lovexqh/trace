package com.trace.net.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.log4j.Logger;

import com.trace.common.util.DateUtil;


/**
 * <p>
 * Module: zhuge-net
 * </p>
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: ZhongSou.com
 * </p>
 * <p>
 * Copyright: Copyright @ 2003-2010 ZhongSou.com
 * </p>
 * <p>
 * All right reserved.
 * </p>
 * 
 * @author: cvs_liudl
 * @version: 1.0
 * @date 2010-4-12
 */
public class DownLoadUtil {
	static Logger logger = Logger.getLogger(DownLoadUtil.class);
	// 获得ConnectionManager，设置相关参数
	private static MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
	private static int CONNECTION_TIMEOUT = 60000;
	private static int SOCKET_TIMEOUT = 60000;
	private static int MAX_CONNECTION_PERHOST = 5;
	private static int MAX_TOTAL_CONNECTIONS = 40;
	// 标志初始化是否完成的flag
	private static boolean initialed = true;

	// 初始化ConnectionManger的方法
	public static void SetPara() {
		manager.getParams().setConnectionTimeout(CONNECTION_TIMEOUT);
		manager.getParams().setSoTimeout(SOCKET_TIMEOUT);
		manager.getParams().setDefaultMaxConnectionsPerHost(
				MAX_CONNECTION_PERHOST);
		manager.getParams().setMaxTotalConnections(MAX_TOTAL_CONNECTIONS);
	}

	/**
	 * 通过get方法获取网页内容
	 * 
	 * @param requestBean
	 * @return
	 */
	public static MessageBean downloadGet(String url) {
		logger.info(Thread.currentThread().getName());
		MessageBean messageBean = new MessageBean();
		HttpState initialState = new HttpState();
		Cookie cookie1 = new Cookie();
		cookie1.setDomain("d.hlmaya.com");
		cookie1.setPath("/");
		cookie1.setName("cdb_auth");
		cookie1.setValue("vUkqxeIAepPWaeIqGvwp16dBqBeJED6UI36O0Ht6u7EtiDsPZdEuAAwJ8gU5ZTpc6Q");
//		cookie1.setExpiryDate(DateUtil.parseDate("2011-5-13 12:41:43"));
		Cookie cookie2 = new Cookie();
		cookie2.setDomain("d.hlmaya.com");
		cookie2.setPath("/");
		cookie2.setName("cdb_cookietime");
		cookie2.setValue("315360000");
		cookie2.setExpiryDate(DateUtil.parseDate("2011-5-13 12:41:43"));
		Cookie cookie3 = new Cookie();
		cookie3.setDomain("d.hlmaya.com");
		cookie3.setPath("/");
		cookie3.setName("l_c");
		cookie3.setValue("1302705188062");
		cookie3.setExpiryDate(DateUtil.parseDate("2011-5-13 12:41:43"));
		Cookie cookie4 = new Cookie();
		cookie4.setDomain("d.hlmaya.com");
		cookie4.setPath("/");
		cookie4.setName("u_c");
		cookie4.setValue("2");
		cookie4.setExpiryDate(DateUtil.parseDate("2011-5-13 12:41:43"));
		Cookie cookie5 = new Cookie();
		cookie5.setDomain("d.hlmaya.com");
		cookie5.setPath("/");
		cookie5.setName("cdb_fid53");
		cookie5.setValue("1302670580");
		cookie5.setExpiryDate(DateUtil.parseDate("2011-5-13 12:41:43"));
		initialState.addCookie(cookie1);
		initialState.addCookie(cookie2);
		initialState.addCookie(cookie3);
		initialState.addCookie(cookie4);
		initialState.addCookie(cookie5);
		HttpClient client = new HttpClient(manager);
		client.getState().addCookies(new Cookie[]{cookie1,cookie2,cookie3,cookie4
				,cookie5});
//		client.getHostConfiguration().setProxy("172.17.18.80", 8080);
		int statusCode = HttpStatus.SC_OK;
		messageBean.setM_errorCode(statusCode);
		byte[] result = null;
		GetMethod get = null;
		if (initialed) {
			DownLoadUtil.SetPara();
		}
		try {
			get = new GetMethod(convertUrl(url));
//			get.setRequestHeader("Accept", "image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/x-shockwave-flash, */*");  
//			get.setRequestHeader("Accept-Language","zh-CN");  
//			get.setRequestHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13");  
//			get.setRequestHeader("Content-Encoding","gzip,deflate");
//			get.setRequestHeader("Accept-Charset","GB2312,utf-8;q=0.7,*;q=0.7");
//			get.setRequestHeader("Keep-Alive","115");
//			get.setRequestHeader("Connection","Keep-Alive");  
//			get.setRequestHeader("Cookie","cdb_sid=Dkhp0M; cdb_oldtopics=D841352D; cdb_fid53=1302670580; u_c=2; l_c=1302705188062; cdb_cookietime=315360000; cdb_auth=vUkqxeIAepPWaeIqGvwp16dBqBeJED6UI36O0Ht6u7EtiDsPZdEuAAwJ8gU5ZTpc6Q; cdb_visitedfid=53");  
			if (get == null) {
				messageBean.setM_errorCode(-1001);
				messageBean.setM_errorStr("can not download url: " + url);
				logger
						.error("DownLoadUtil --> downloadGet() Method failed: new getMethod() return null");
				return messageBean;
			} else {
				statusCode = client.executeMethod(get);
			}
			if (statusCode != HttpStatus.SC_OK) {
				messageBean.setM_errorCode(statusCode);
				messageBean.setM_errorStr("response http status code: "
						+ statusCode);
				logger.error("response http status code: " + statusCode
						+ ",url=" + url);
				return messageBean;
			}

			get.setFollowRedirects(true);
			InputStream in = get.getResponseBodyAsStream();
			result = getBytesFromIS(in);
			String responseCode = get.getResponseCharSet();
			String reponseHeader = get.getResponseHeader("Content-Type").getValue();
			if ("ISO-8859-1".equals(responseCode)&&reponseHeader.contains("html")) {
				responseCode = "GBK";
			}
			messageBean.setM_content(new String(result, responseCode).trim());
			System.out.println(messageBean.getM_content().contains("lovexqh"));
			messageBean.setM_content_type(get.getResponseHeader("Content-Type")
					.getValue());
			messageBean.setM_responseCode(responseCode);
			if (in != null) {
				in.close();
			}
			return messageBean;
		} catch (SocketTimeoutException e) {
			logger.error("error url: " + url);
			logger.error(e.getMessage());
			e.printStackTrace();
			messageBean.setM_errorCode(-703);
			messageBean.setM_errorStr(e.getMessage());
			return messageBean;
		} catch (HttpException e) {
			logger.error("error url: " + url);
			logger.error(e.getMessage());
			e.printStackTrace();
			messageBean.setM_errorCode(-703);
			messageBean.setM_errorStr(e.getMessage());
			return messageBean;
		} catch (IOException e) {
			logger.error("error url: " + url);
			logger.error(e.getMessage());
			e.printStackTrace();
			messageBean.setM_errorCode(-704);
			messageBean.setM_errorStr(e.getMessage());
			return messageBean;
		} catch (Exception e) {
			logger.error("error url: " + url);
			logger.error(e.getMessage());
			e.printStackTrace();
			messageBean.setM_errorCode(-705);
			messageBean.setM_errorStr(e.getMessage());
			return messageBean;
		} finally {
			get.releaseConnection();
		}
	}

	/**
	 * 通过get方法获取网页内容(带cookie)
	 * 
	 * @param requestBean
	 * @return
	 */
	public static MessageBean downloadGet(String url, String cookies) {
		MessageBean messageBean = new MessageBean();
		HttpClient client = new HttpClient(manager);
		int statusCode = HttpStatus.SC_OK;
		messageBean.setM_errorCode(statusCode);
		byte[] result = null;
		GetMethod get = null;
		if (initialed) {
			DownLoadUtil.SetPara();
		}
		try {
			get = new GetMethod(convertUrl(url));

			get.setRequestHeader("Cookie", cookies);

			// get = new GetMethod(url);
			if (get == null) {
				messageBean.setM_errorCode(-701);
				messageBean.setM_errorStr("can not download url: " + url);
				logger
						.error("DownLoadUtil --> downloadGet() Method failed: new getMethod() return null");
				return messageBean;
			} else {
				statusCode = client.executeMethod(get);
			}
			if (statusCode != HttpStatus.SC_OK) {
				messageBean.setM_errorCode(statusCode);
				messageBean.setM_errorStr("response http status code: "
						+ statusCode);
				logger.error("response http status code: " + statusCode
						+ ",url=" + url);
				return messageBean;
			}

			get.setFollowRedirects(true);
			InputStream in = get.getResponseBodyAsStream();
			result = getBytesFromIS(in);
			String responseCode = get.getResponseCharSet();
			if ("ISO-8859-1".equals(responseCode)) {
				responseCode = "GBK";
			}
			messageBean.setM_content(new String(result, responseCode).trim());
			if (in != null) {
				in.close();
			}
			return messageBean;
		} catch (HttpException e) {
			logger.error("error url: " + url);
			e.printStackTrace();
			messageBean.setM_errorCode(-703);
			messageBean.setM_errorStr("downloadGet-->HttpException");
			return messageBean;
		} catch (IOException e) {
			logger.error("error url: " + url);
			messageBean.setM_errorCode(-704);
			messageBean.setM_errorStr("downloadGet-->IOException");
			return messageBean;
		} catch (Exception e) {
			logger.error("error url: " + url);
			logger.error(e.getMessage());
			messageBean.setM_errorCode(-705);
			messageBean.setM_errorStr("downloadGet-->Exception");
			return messageBean;
		} finally {
			get.releaseConnection();
		}
	}

	/**
	 * @param url
	 * @param parmMap
	 * @param charSet
	 * @return
	 */
	public static MessageBean downloadPost(String url,
			HashMap<String, String> parmMap, String charSet) {
		MessageBean messageBean = new MessageBean();
		HttpClient client = new HttpClient(manager);
		if (initialed) {
			DownLoadUtil.SetPara();
		}
		byte[] result = null;
		int statusCode = HttpStatus.SC_OK;
		messageBean.setM_errorCode(statusCode);
		PostMethod post = null;
		try {
			post = new PostMethod(convertUrl(url));
			post.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=" + charSet);
			if (parmMap != null) {
				setRequestBody(post, parmMap, charSet);
			}

			if (post == null) {
				messageBean.setM_errorCode(-701);
				messageBean.setM_errorStr("download postMethod is null");
				logger
						.error("DownLoadUtil --> downloadPost() Method failed: getMethod is null");
				return messageBean;
			} else {
				statusCode = client.executeMethod(post);
			}
			if (statusCode != HttpStatus.SC_OK) {
				messageBean.setM_errorCode(statusCode);
				messageBean.setM_errorStr("response http error code: "
						+ statusCode);
				logger.error("DownLoadUtil --> downloadPost(): "
						+ "response http error code: " + statusCode);
				return messageBean;
			}

			post.setFollowRedirects(false);

			InputStream in = post.getResponseBodyAsStream();
			result = getBytesFromIS(in);
			messageBean.setM_content(new String(result, charSet));
			if (in != null) {
				in.close();
			}
			return messageBean;
		} catch (ConnectException e) {
			logger.error(e.getMessage());
			logger.error("url=" + url);
			logger.error("charSet=" + charSet);
			messageBean.setM_errorCode(-706);
			messageBean.setM_errorStr("downloadPost-->ConnectException");
			return messageBean;
		} catch (SocketTimeoutException e) {
			logger.error(e.getMessage());
			logger.error("url=" + url);
			logger.error("charSet=" + charSet);
			messageBean.setM_errorCode(-707);
			messageBean.setM_errorStr("downloadPost-->SocketTimeoutException");
			return messageBean;
		} catch (HttpException e) {
			logger.error(e.getMessage());
			logger.error("url=" + url);
			logger.error("charSet=" + charSet);
			messageBean.setM_errorCode(-703);
			messageBean.setM_errorStr("downloadPost-->HttpException");
			return messageBean;
		} catch (IOException e) {
			logger.error(e.getMessage());
			logger.error("url=" + url);
			logger.error("charSet=" + charSet);
			messageBean.setM_errorCode(-704);
			messageBean.setM_errorStr("downloadPost-->IOException");
			return messageBean;
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error("url=" + url);
			logger.error("charSet=" + charSet);
			messageBean.setM_errorCode(-705);
			messageBean.setM_errorStr("downloadPost-->Exception");
			return messageBean;
		} finally {
			post.releaseConnection();
		}
	}

	/**
	 * @param url
	 * @param paramUrl
	 *            e.g: a=1&b=2
	 * @param charSet
	 * @return
	 */
	public static MessageBean downloadPost(String url, String paramUrl,
			String charSet) {
		MessageBean messageBean = new MessageBean();
		HttpClient client = new HttpClient(manager);
		if (initialed) {
			DownLoadUtil.SetPara();
		}
		byte[] result = null;
		int statusCode = 200;
		messageBean.setM_errorCode(statusCode);
		PostMethod post = null;
		try {
			post = new PostMethod(convertUrl(url, charSet));
			post.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=" + charSet);
			if (paramUrl != null && paramUrl.length() > 0) {
				setRequestBody(post, paramUrl, charSet);
			}

			if (post == null) {
				messageBean.setM_errorCode(-701);
				messageBean.setM_errorStr("download postMethod is null");
				logger
						.error("DownLoadUtil --> downloadPost() Method failed: getMethod is null");
				return messageBean;
			} else {
				statusCode = client.executeMethod(post);
			}
			if (statusCode != HttpStatus.SC_OK) {
				messageBean.setM_errorCode(-702);
				messageBean.setM_errorStr("response http error code: "
						+ statusCode);
				logger.error("DownLoadUtil --> downloadPost(): "
						+ "response http error code: " + statusCode);
				logger.error("url=" + url);
				logger.error("paramUrl=" + paramUrl);
				logger.error("charSet=" + charSet);
				return messageBean;
			}

			post.setFollowRedirects(false);

			InputStream in = post.getResponseBodyAsStream();
			result = getBytesFromIS(in);
			messageBean.setM_content(new String(result, charSet));
			if (in != null) {
				in.close();
			}
			return messageBean;
		} catch (ConnectException e) {
			logger.error(e.getMessage());
			logger.error("url=" + url);
			logger.error("charSet=" + charSet);
			messageBean.setM_errorCode(-706);
			messageBean.setM_errorStr("downloadPost-->ConnectException");
			return messageBean;
		} catch (SocketTimeoutException e) {
			logger.error(e.getMessage());
			logger.error("url=" + url);
			logger.error("charSet=" + charSet);
			messageBean.setM_errorCode(-707);
			messageBean.setM_errorStr("downloadPost-->SocketTimeoutException");
			return messageBean;
		} catch (HttpException e) {
			logger.error(e.getMessage());
			logger.error("url=" + url);
			logger.error("paramUrl=" + paramUrl);
			logger.error("charSet=" + charSet);
			messageBean.setM_errorCode(-703);
			messageBean.setM_errorStr("downloadPost-->HttpException");
			return messageBean;
		} catch (IOException e) {
			logger.error(e.getMessage());
			logger.error("url=" + url);
			logger.error("paramUrl=" + paramUrl);
			logger.error("charSet=" + charSet);
			messageBean.setM_errorCode(-704);
			messageBean.setM_errorStr("downloadPost-->IOException");
			return messageBean;
		} catch (Exception e) {
			logger.error(e.getMessage());
			logger.error("url=" + url);
			logger.error("paramUrl=" + paramUrl);
			logger.error("charSet=" + charSet);
			e.printStackTrace();
			messageBean.setM_errorCode(-705);
			messageBean.setM_errorStr("downloadPost-->Exception");
			return messageBean;
		} finally {
			post.releaseConnection();
		}
	}

	/**
	 * @param method
	 * @param parmMap
	 * @param charSet
	 */
	@SuppressWarnings("unchecked")
	private static void setRequestBody(PostMethod method, HashMap parmMap,
			String charSet) {
		Set keySet = parmMap.keySet();
		Iterator it = keySet.iterator();
		List parmList = new ArrayList();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) parmMap.get(key);
			// 设置参数
			NameValuePair nameValuePair = new NameValuePair(key, value);
			parmList.add(nameValuePair);
		}
		method.setRequestBody((NameValuePair[]) parmList
				.toArray(new NameValuePair[parmList.size()]));
	}

	/**
	 * @param str
	 * @return
	 */
	private static String convertUrl(String str) {
		StringBuffer sb = new StringBuffer(1024);
		sb.setLength(0);
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c > 255) {
				try {
					sb.append(URLEncoder.encode(String.valueOf(c), "GBK"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else if (c == '#') {
				sb.append("%23");
			} else {
				sb.append(c);
			}
		}
		return (new String(sb));
	}

	/**
	 * @param str
	 * @return
	 */
	private static String convertUrl(String str, String charSet) {
		StringBuffer sb = new StringBuffer(1024);
		sb.setLength(0);
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c > 255) {
				try {
					sb.append(URLEncoder.encode(String.valueOf(c), charSet));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else if (c == '#') {
				sb.append("%23");
			} else {
				sb.append(c);
			}
		}
		return (new String(sb));
	}

	/**
	 * @param method
	 * @param paramUrl
	 * @param charSet
	 */
	@SuppressWarnings( { "unchecked" })
	private static void setRequestBody(PostMethod method, String paramUrl,
			String charSet) {
		List parmList = new ArrayList();
		HashMap map = new HashMap();
		if (paramUrl != null && paramUrl.length() > 0) {
			if (paramUrl.indexOf("&") > -1) {
				String[] prs = paramUrl.split("&");
				if (prs != null && prs.length > 0) {
					for (int i = 0; i < prs.length; i++) {
						String pr = prs[i];
						String[] p = pr.split("=");
						NameValuePair nvp = new NameValuePair(p[0],
								p.length == 2 ? p[1] : "");
						parmList.add(nvp);
					}
				}
			} else {
				String[] p = paramUrl.split("=");
				NameValuePair nvp = new NameValuePair(p[0],
						p.length == 2 ? p[1] : "");
				parmList.add(nvp);
			}
		}
		method.setRequestBody((NameValuePair[]) parmList
				.toArray(new NameValuePair[parmList.size()]));
	}

	/**
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static byte[] getBytesFromIS(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int b = 0;
		while ((b = is.read()) != -1)
			baos.write(b);
		return baos.toByteArray();
	}

	public static void main(String[] a) {

		String url = "http://cq.qq.com/fl/";
		String paramUrl = null;
		// try {
		// //paramUrl = "data=";
		// paramUrl =
		// "data="+URLEncoder.encode("<xml type=\"get\" club=\"卫浴洁具\"><item type=\"domainuser\" action=\"list\"><param name=\"expired\" value=\"all\"/><param name=\"pageindex\" value=\"1\"/><param name=\"pagesize\" value=\"20\"/><param name=\"ignoredomainrole\" value=\"true\"/></item></xml>","UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// //String paramUrl = "data=sssss";
		// MessageBean mbean = DownLoadUtil.downloadPost(url, paramUrl,
		// "UTF-8");
		//		
		// HashMap map = new HashMap();
		// map.put("apikey", "ba5169615c854abf9516fc492a566f1c");
		// map.put("method", "group.getRole");
		// map.put("roleid", "3014");
		// // MessageBean mbean = DownLoadUtil.downloadPost(url, map, "UTF-8");
		//		
		// System.out.println(mbean.getM_errorCode());
		// System.out.println(mbean.getM_content());
		// System.exit(0);
		MessageBean mb = downloadGet(url);
		logger.info(mb.getM_content());
	}

	public static String getFileNameByUrl(String url, String contentType) {
		// 移除http:
		url = url.substring(7);
		// text/html类型
		if (contentType.indexOf("html") != -1) {
			// url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
			url = url.replaceAll("[\\?/:*|<>\"]", "");
			url = url.replaceAll("\\.", "//") + "index.htm";
			return url;
		}
		// 如application/pdf类型
		else {
			return url.replaceAll("[\\?/:*|<>\"]", "_") + "."
					+ contentType.substring(contentType.lastIndexOf("/") + 1);
		}
	}
}
