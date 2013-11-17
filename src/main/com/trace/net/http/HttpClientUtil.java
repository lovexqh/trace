package com.trace.net.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

@SuppressWarnings("unused")
public class HttpClientUtil {

	public static final String MOZILLA_USER_AGENT = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)";

	static HttpClient httpClient = new HttpClient(
			new MultiThreadedHttpConnectionManager());

	/**
	 * Log4j logger
	 */
	static Logger logger = Logger.getLogger(HttpClientUtil.class);

	static Timer tmr;

	private static HttpClientUtil _instance;

	/**
	 * Constructor
	 * 
	 */
	private HttpClientUtil() {
		if (tmr == null)
			tmr = new Timer("HttpConnTimer");
	}

	/**
	 * Get instance of HttpClientUtil
	 * 
	 * @return
	 */
	public static HttpClientUtil getInstance() {
		synchronized (HttpClientUtil.class) {
			if (_instance == null)
				_instance = new HttpClientUtil();
		}
		return _instance;
	}

	/**
	 * Cancel timer
	 * 
	 */
	public static void cancelTimer() {
		if (tmr != null)
			tmr.cancel();
	}

	/**
	 * Recommend method to get http url response by Apache http client util
	 * 
	 * @param url
	 * @return
	 * @throws NetworkException
	 */
	public byte[] getHttpResponse(String url, String username, String password,
			int port) throws NetworkException {
		// test if it is a wellform url
		String host = "";
		HttpClient myHttpClient = new HttpClient();
		try {
			host = new URL(url).getHost();
		} catch (MalformedURLException e1) {
			throw new NetworkException(e1.getMessage(), e1);
		}
		byte[] response = new byte[0];
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setSoTimeout(60000);
		getMethod.addRequestHeader("User-Agent", MOZILLA_USER_AGENT);
		getMethod.getParams().setCookiePolicy(
				CookiePolicy.BROWSER_COMPATIBILITY);
		getMethod.setFollowRedirects(true);
		if (username != null) {
			Credentials defaultcreds = new UsernamePasswordCredentials(
					username, password);
			myHttpClient.getState().setCredentials(
					new AuthScope(host, port, AuthScope.ANY_REALM),
					defaultcreds);
		}
		TimerTask task = new ConnectionTimer(getMethod);
		long bodyLength = -1;
		try {
			tmr.schedule(task, 120000);
			int respCode = myHttpClient.executeMethod(getMethod);
			bodyLength = getMethod.getResponseContentLength();
			if (respCode != 200) {
				logger.warn("Get http response code is not 200, but "
						+ respCode + " (url = " + url + ")");
				throw new NetworkException(
						"Get http response code is not 200, but " + respCode
								+ " from url - " + url);
			}
			// console ("response code is : " + respCode);
			response = CommonUtil.getContentFromInputStream(getMethod
					.getResponseBodyAsStream());
			Header encoding = getMethod.getResponseHeader("Content-Encoding");
			if (encoding != null
					&& "gzip".equalsIgnoreCase(encoding.getValue())) {
				response = CompressUtil.ungzip(response);
			}
			// console ("content is : " + new String(urlBean.getContent()));
		} catch (IOException e) {
			logger.warn("getHttpResponse error: ", e);
			throw new NetworkException("Network I/O error: " + e.getMessage(),
					e);
		} finally {
			task.cancel();
			getMethod.releaseConnection();
		}
		if (bodyLength != -1 && bodyLength > response.length) {
			logger.warn("!!!Found response body length is " + response.length
					+ " which is less than expected length " + bodyLength);
			response = new byte[0];
		}
		return response;
	}

	/**
	 * Recommend method to get http url response by Apache http client util
	 * 
	 * @param url
	 * @return
	 * @throws NetworkException
	 */
	public byte[] getHttpResponse(String url, HttpClient client)
			throws NetworkException {
		return getHttpResponse(url, CookiePolicy.BROWSER_COMPATIBILITY, client);
	}

	/**
	 * Recommend method to get http url response by Apache http client util
	 * 
	 * @param url
	 * @param cookiePolicy
	 * @return
	 * @see org.apache.commons.httpclient.cookie.CookiePolicy
	 * @throws NetworkException
	 */
	public byte[] getHttpResponse(String url, String cookiePolicy,
			HttpClient client) throws NetworkException {

		return getHttpResponse(url, cookiePolicy, client, 60000, 120000);
	}

	public byte[] getHttpResponse(String url, String cookiePolicy,
			HttpClient client, int soTimeOut, int timeOut)
			throws NetworkException {
		// test if it is a wellform url
		try {
			new URL(url);
		} catch (MalformedURLException e1) {
			throw new NetworkException(e1.getMessage(), e1);
		}
		byte[] response = new byte[0];
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setSoTimeout(soTimeOut);
		getMethod.addRequestHeader("User-Agent", MOZILLA_USER_AGENT);
		getMethod
				.addRequestHeader(
						"Accept",
						"text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
		getMethod.addRequestHeader("Accept-Encoding", "gzip,deflate");
		getMethod.addRequestHeader("Accept-Language", "zh-cn,zh;q=0.5");
		;
		getMethod.addRequestHeader("Accept-Charset:",
				"gb2312,utf-8;q=0.7,*;q=0.7");
		getMethod.getParams().setCookiePolicy(cookiePolicy);
		getMethod.setFollowRedirects(true);

		TimerTask task = new ConnectionTimer(getMethod);
		long bodyLength = -1;
		try {
			tmr.schedule(task, timeOut);
			int respCode;
			if (client != null) {
				respCode = client.executeMethod(getMethod);
			} else {
				respCode = httpClient.executeMethod(getMethod);
			}
			bodyLength = getMethod.getResponseContentLength();
			if (respCode != 200) {
				logger.warn("Get http response code is not 200, but "
						+ respCode + " (url = " + url + ")");
				throw new NetworkException(
						"Get http response code is not 200, but " + respCode
								+ " from url - " + url);
			}
			// console ("response code is : " + respCode);
			response = CommonUtil.getContentFromInputStream(getMethod
					.getResponseBodyAsStream());
			Header encoding = getMethod.getResponseHeader("Content-Encoding");
			if (encoding != null
					&& "gzip".equalsIgnoreCase(encoding.getValue())) {
				response = CompressUtil.ungzip(response);
			}
			// console ("content is : " + new String(urlBean.getContent()));
		} catch (IOException e) {
			logger.warn("getHttpResponse error: ", e);
			e.printStackTrace();
			throw new NetworkException("Visit url (" + url
					+ ") network I/O error: " + e.getMessage(), e);
		} finally {
			task.cancel();
			getMethod.releaseConnection();
		}
		if (response != null && bodyLength != -1
				&& bodyLength > response.length) {
			logger.warn("!!!Found response body length is " + response.length
					+ " which is less than expected length " + bodyLength);
			response = new byte[0];
		}
		return response;
	}

	public AlertieHttpResponse getHttpResponse(String url, String cookiePolicy,
			HttpClient client, boolean flag) throws NetworkException {
		// test if it is a wellform url
		try {
			new URL(url);
		} catch (MalformedURLException e1) {
			throw new NetworkException(e1.getMessage(), e1);
		}
		AlertieHttpResponse response = new AlertieHttpResponse();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setSoTimeout(60000);
		getMethod.addRequestHeader("User-Agent", MOZILLA_USER_AGENT);
		getMethod.getParams().setCookiePolicy(cookiePolicy);
		getMethod.setFollowRedirects(flag);

		TimerTask task = new ConnectionTimer(getMethod);
		long bodyLength = -1;
		try {
			tmr.schedule(task, 120000);
			int respCode;
			if (client != null) {
				respCode = client.executeMethod(getMethod);
			} else {
				respCode = httpClient.executeMethod(getMethod);
			}

			// console ("response code is : " + respCode);
			response.setRespHeaders(getMethod.getResponseHeaders());
			response.setStatuscode(getMethod.getStatusCode());
			response.setResponse(CommonUtil.getContentFromInputStream(getMethod
					.getResponseBodyAsStream()));
			response.setUrl(getMethod.getURI().toString());
			Header encoding = getMethod.getResponseHeader("Content-Encoding");
			if (encoding != null
					&& "gzip".equalsIgnoreCase(encoding.getValue())) {
				response.setResponse(CompressUtil
						.ungzip(response.getResponse()));
			}
			// console ("content is : " + new String(urlBean.getContent()));
		} catch (IOException e) {
			logger.warn("getHttpResponse error: ", e);
			e.printStackTrace();
			throw new NetworkException("Visit url (" + url
					+ ") network I/O error: " + e.getMessage(), e);
		} finally {
			task.cancel();
			getMethod.releaseConnection();
		}
		/*
		 * if (bodyLength != -1 && bodyLength > response.length) {
		 * logger.warn("!!!Found response body length is " + response.length +
		 * " which is less than expected length " + bodyLength); response = new
		 * byte [0]; }
		 */
		return response;
	}

	public byte[] getHttpResponse(String url, String cookiePolicy,
			HttpClient client, String referer) throws NetworkException {
		// test if it is a wellform url
		try {
			new URL(url);
		} catch (MalformedURLException e1) {
			throw new NetworkException(e1.getMessage(), e1);
		}
		byte[] response = new byte[0];
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setSoTimeout(10000);
		getMethod.addRequestHeader("User-Agent", MOZILLA_USER_AGENT);
		getMethod.addRequestHeader("Referer", referer);
		// getMethod.addRequestHeader("Accept-Encoding", "compress,gzip");
		getMethod.getParams().setCookiePolicy(cookiePolicy);
		getMethod.setFollowRedirects(true);

		TimerTask task = new ConnectionTimer(getMethod);
		long bodyLength = -1;
		try {
			tmr.schedule(task, 120000);
			int respCode;
			if (client != null) {
				respCode = client.executeMethod(getMethod);
			} else {
				respCode = httpClient.executeMethod(getMethod);
			}
			bodyLength = getMethod.getResponseContentLength();
			if (respCode != 200) {
				logger.warn("Get http response code is not 200, but "
						+ respCode + " (url = " + url + ")");
				throw new NetworkException(
						"Get http response code is not 200, but " + respCode
								+ " from url - " + url);
			}
			// console ("response code is : " + respCode);
			response = CommonUtil.getContentFromInputStream(getMethod
					.getResponseBodyAsStream());
			Header encoding = getMethod.getResponseHeader("Content-Encoding");
			if (encoding != null
					&& "gzip".equalsIgnoreCase(encoding.getValue())) {
				response = CompressUtil.ungzip(response);
			}
			// console ("content is : " + new String(urlBean.getContent()));
		} catch (IOException e) {
			logger.warn("getHttpResponse error: ", e);
			e.printStackTrace();
			throw new NetworkException("Visit url (" + url
					+ ") network I/O error: " + e.getMessage(), e);
		} finally {
			task.cancel();
			getMethod.releaseConnection();
		}
		if (response != null && bodyLength != -1
				&& bodyLength > response.length) {
			logger.warn("!!!Found response body length is " + response.length
					+ " which is less than expected length " + bodyLength);
			response = new byte[0];
		}
		return response;
	}

	public AlertieHttpResponse getHttpResponseGzip(String url,
			String cookiePolicy, HttpClient client) throws NetworkException {
		// test if it is a wellform url
		try {
			new URL(url);
		} catch (MalformedURLException e1) {
			throw new NetworkException(e1.getMessage(), e1);
		}
		AlertieHttpResponse response = new AlertieHttpResponse();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setSoTimeout(60000);
		getMethod.addRequestHeader("User-Agent", MOZILLA_USER_AGENT);
		// getMethod.addRequestHeader("Accept-Encoding", "gzip,deflate");
		getMethod.getParams().setCookiePolicy(cookiePolicy);
		getMethod.setFollowRedirects(false);

		TimerTask task = new ConnectionTimer(getMethod);
		long bodyLength = -1;
		try {
			tmr.schedule(task, 120000);
			int respCode;
			if (client != null) {
				respCode = client.executeMethod(getMethod);
			} else {
				respCode = httpClient.executeMethod(getMethod);
			}

			// console ("response code is : " + respCode);
			response.setRespHeaders(getMethod.getResponseHeaders());
			response.setStatuscode(getMethod.getStatusCode());
			response.setResponse(CommonUtil.getContentFromInputStream(getMethod
					.getResponseBodyAsStream()));
			Header encoding = getMethod.getResponseHeader("Content-Encoding");
			if (encoding != null
					&& "gzip".equalsIgnoreCase(encoding.getValue())) {
				response.setResponse(CompressUtil
						.ungzip(response.getResponse()));
			}
			// console ("content is : " + new String(urlBean.getContent()));
		} catch (IOException e) {
			logger.warn("getHttpResponse error: ", e);
			e.printStackTrace();
			throw new NetworkException("Visit url (" + url
					+ ") network I/O error: " + e.getMessage(), e);
		} finally {
			task.cancel();
			getMethod.releaseConnection();
		}
		/*
		 * if (bodyLength != -1 && bodyLength > response.length) {
		 * logger.warn("!!!Found response body length is " + response.length +
		 * " which is less than expected length " + bodyLength); response = new
		 * byte [0]; }
		 */
		return response;
	}

	public AlertieHttpResponse getHttpResponseGzip(String url,
			String cookiePolicy, HttpClient client, String referer)
			throws NetworkException {
		// test if it is a wellform url
		try {
			new URL(url);
		} catch (MalformedURLException e1) {
			throw new NetworkException(e1.getMessage(), e1);
		}
		AlertieHttpResponse response = new AlertieHttpResponse();
		GetMethod getMethod = new GetMethod(url);
		getMethod.getParams().setSoTimeout(60000);
		getMethod.addRequestHeader("User-Agent", MOZILLA_USER_AGENT);
		getMethod.addRequestHeader("Referer", referer);
		// getMethod.addRequestHeader("Accept-Encoding", "gzip,deflate");
		getMethod.getParams().setCookiePolicy(cookiePolicy);
		getMethod.setFollowRedirects(false);

		TimerTask task = new ConnectionTimer(getMethod);
		long bodyLength = -1;
		try {
			tmr.schedule(task, 120000);
			int respCode;
			if (client != null) {
				respCode = client.executeMethod(getMethod);
			} else {
				respCode = httpClient.executeMethod(getMethod);
			}

			// console ("response code is : " + respCode);
			response.setRespHeaders(getMethod.getResponseHeaders());
			response.setStatuscode(getMethod.getStatusCode());
			response.setResponse(CommonUtil.getContentFromInputStream(getMethod
					.getResponseBodyAsStream()));
			Header encoding = getMethod.getResponseHeader("Content-Encoding");
			if (encoding != null
					&& "gzip".equalsIgnoreCase(encoding.getValue())) {
				response.setResponse(CompressUtil
						.ungzip(response.getResponse()));
			}
			// console ("content is : " + new String(urlBean.getContent()));
		} catch (IOException e) {
			logger.warn("getHttpResponse error: ", e);
			e.printStackTrace();
			throw new NetworkException("Visit url (" + url
					+ ") network I/O error: " + e.getMessage(), e);
		} finally {
			task.cancel();
			getMethod.releaseConnection();
		}
		/*
		 * if (bodyLength != -1 && bodyLength > response.length) {
		 * logger.warn("!!!Found response body length is " + response.length +
		 * " which is less than expected length " + bodyLength); response = new
		 * byte [0]; }
		 */
		return response;
	}

	/**
	 * Recommend method to post to http url and get the response by Apache http
	 * client util
	 * 
	 * @param url
	 * @param nameValues
	 * @return
	 * @throws NetworkException
	 */
	public AlertieHttpResponse postHttpResponse(String url,
			NameValuePair[] nameValues, HttpClient client)
			throws NetworkException {
		return postHttpResponse(url, nameValues,
				CookiePolicy.BROWSER_COMPATIBILITY, client);
	}

	/**
	 * Recommend method to post to http url and get the response by Apache http
	 * client util
	 * 
	 * @param url
	 * @param nameValues
	 * @return
	 * @throws NetworkException
	 */
	public AlertieHttpResponse postHttpResponse(String url,
			NameValuePair[] nameValues, String cookiePolicy, HttpClient client)
			throws NetworkException {
		// test if it is a wellform url
		try {
			new URL(url);
		} catch (MalformedURLException e1) {
			throw new NetworkException(e1.getMessage(), e1);
		}
		AlertieHttpResponse response = new AlertieHttpResponse();

		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setSoTimeout(60000);
		postMethod.addRequestHeader("User-Agent", MOZILLA_USER_AGENT);
		postMethod.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		postMethod.getParams().setCookiePolicy(cookiePolicy);
		postMethod.addParameters(nameValues);
		TimerTask task = new ConnectionTimer(postMethod);
		try {
			tmr.schedule(task, 120000);
			if (client != null) {
				client.executeMethod(postMethod);
			} else {
				httpClient.executeMethod(postMethod);
			}
			response.setRespHeaders(postMethod.getRequestHeaders());
			response.setStatuscode(postMethod.getStatusCode());
			// console ("response code is : " + respCode);
			response.setResponse(CommonUtil
					.getContentFromInputStream(postMethod
							.getResponseBodyAsStream()));
			Header encoding = postMethod.getResponseHeader("Content-Encoding");
			if (encoding != null
					&& "gzip".equalsIgnoreCase(encoding.getValue())) {
				response.setResponse(CompressUtil
						.ungzip(response.getResponse()));
			}
			// console ("content is : " + new String(urlBean.getContent()));
		} catch (IOException e) {
			logger.warn("postHttpResponse error: ", e);
			throw new NetworkException("Visit url (" + url
					+ ") network I/O error when visit - " + url + " -: "
					+ e.getMessage(), e);
		} finally {
			task.cancel();
			postMethod.releaseConnection();
		}
		return response;
	}

	public AlertieHttpResponse postHttpResponse(String url,
			NameValuePair[] nameValues, String cookiePolicy, HttpClient client,
			String refer) throws NetworkException {
		// test if it is a wellform url
		try {
			new URL(url);
		} catch (MalformedURLException e1) {
			throw new NetworkException(e1.getMessage(), e1);
		}
		AlertieHttpResponse response = new AlertieHttpResponse();

		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setSoTimeout(60000);
		postMethod.addRequestHeader("User-Agent", "My Session");
		postMethod.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded");
		postMethod.addRequestHeader("Accept-Encoding", "gzip,deflate");

		postMethod.getParams().setCookiePolicy(cookiePolicy);
		postMethod.addParameters(nameValues);
		TimerTask task = new ConnectionTimer(postMethod);
		try {
			tmr.schedule(task, 120000);
			if (client != null) {
				client.executeMethod(postMethod);
			} else {
				httpClient.executeMethod(postMethod);
			}
			response.setRespHeaders(postMethod.getRequestHeaders());
			response.setStatuscode(postMethod.getStatusCode());
			// console ("response code is : " + respCode);
			response.setResponse(CommonUtil
					.getContentFromInputStream(postMethod
							.getResponseBodyAsStream()));
			Header encoding = postMethod.getResponseHeader("Content-Encoding");
			if (encoding != null
					&& "gzip".equalsIgnoreCase(encoding.getValue())) {
				response.setResponse(CompressUtil
						.ungzip(response.getResponse()));
			}
			// console ("content is : " + new String(urlBean.getContent()));
		} catch (IOException e) {
			logger.warn("postHttpResponse error: ", e);
			throw new NetworkException("Visit url (" + url
					+ ") network I/O error when visit - " + url + " -: "
					+ e.getMessage(), e);
		} finally {
			task.cancel();
			postMethod.releaseConnection();
		}
		return response;
	}

	public AlertieHttpResponse postHttpResponse(String url,
			NameValuePair[] nameValues, String cookiePolicy, HttpClient client,
			String refer, String cookie) throws NetworkException {
		// test if it is a wellform url
		try {
			new URL(url);
		} catch (MalformedURLException e1) {
			throw new NetworkException(e1.getMessage(), e1);
		}
		AlertieHttpResponse response = new AlertieHttpResponse();

		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setSoTimeout(60000);
		postMethod.addRequestHeader("User-Agent", MOZILLA_USER_AGENT);
		postMethod.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=gb2312");
		postMethod.addRequestHeader("Referer", refer);

		if (cookie != null) {
			postMethod.addRequestHeader("Cookie", cookie);
		}

		postMethod.getParams().setCookiePolicy(cookiePolicy);
		postMethod.addParameters(nameValues);
		TimerTask task = new ConnectionTimer(postMethod);
		try {
			tmr.schedule(task, 120000);
			if (client != null) {
				client.executeMethod(postMethod);
			} else {
				httpClient.executeMethod(postMethod);
			}
			response.setRespHeaders(postMethod.getRequestHeaders());
			response.setStatuscode(postMethod.getStatusCode());
			// console ("response code is : " + respCode);
			response.setResponse(CommonUtil
					.getContentFromInputStream(postMethod
							.getResponseBodyAsStream()));
			Header encoding = postMethod.getResponseHeader("Content-Encoding");
			if (encoding != null
					&& "gzip".equalsIgnoreCase(encoding.getValue())) {
				response.setResponse(CompressUtil
						.ungzip(response.getResponse()));
			}
			// console ("content is : " + new String(urlBean.getContent()));
		} catch (IOException e) {
			logger.warn("postHttpResponse error: ", e);
			throw new NetworkException("Visit url (" + url
					+ ") network I/O error when visit - " + url + " -: "
					+ e.getMessage(), e);
		} finally {
			task.cancel();
			postMethod.releaseConnection();
		}
		return response;
	}

	// debug
	public static void console(Object object) {
		System.out.println(object);
	}

	/**
	 * Inner class to terminate http connection
	 * 
	 * @author Jack Wang
	 * 
	 */
	private class ConnectionTimer extends TimerTask {

		HttpMethod httpMethod;

		public ConnectionTimer(HttpMethod method) {
			httpMethod = method;
		}

		public void run() {
			httpMethod.abort();
		}
	}

	/**
	 * Test purpose Cookie: B=84cit611sbmlu&b=3&s=2i Cookie:
	 * F=a=_hV35hcsvdbPuWLzYzpkxhiAHvlkJAe6CGEtBfekxbgdA59103dcRsbInzSx&b=fUQO
	 * Cookie:
	 * Y=v=1&n=94dhg1s6btjce&l=9m0d6rqqr/o&p=m2dvvcn012080000&iz=100080&r
	 * =fh&lg=gb&intl=cn Cookie:
	 * T=z=FrdxDBFxyxDBhcwGlSvm7AENjI3BjQ1MDBOTjI3TzQ-&
	 * a=QAE&sk=DAAh6N4sTtrUiB&d=
	 * c2wBTVRVd0FUTXlOemM1T1RVd09ETS0BYQFRQUUBdGlwAWI3STk0RAF6egFGcmR4REJnV0E
	 * -&af=QUFBQkFBJnRzPTExMzcwNDAwNjkmcHM9RGhhZFZ5M2M2WnJ0RXlCOEtzYk5vQS0t
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// HttpClient client = new HttpClient();
		HttpClientUtil util = HttpClientUtil.getInstance();
		/**
		 * HttpState initialState = new HttpState(); // Initial set of cookies
		 * can be retrieved from persistent storage and // re-created, using a
		 * persistence mechanism of choice, Cookie bCookie = new
		 * Cookie(".yahoo.com", "B", "84cit611sbmlu&b=3&s=2i", "/", 3600 * 24 *
		 * 100, false); Cookie fCookie = new Cookie(".yahoo.com", "F",
		 * "a=_hV35hcsvdbPuWLzYzpkxhiAHvlkJAe6CGEtBfekxbgdA59103dcRsbInzSx&b=fUQO"
		 * , "/", 3600 * 24 * 100, false); Cookie yCookie = new
		 * Cookie(".yahoo.com", "Y","v=1&n=94dhg1s6btjce&l=9m0d6rqqr/o&p=m2dvvcn012080000&iz=100080&r=fh&lg=gb&intl=cn"
		 * , "/", 3600 * 24 * 100, false); Cookie tCookie = new
		 * Cookie(".yahoo.com", "T","z=FrdxDBFxyxDBhcwGlSvm7AENjI3BjQ1MDBOTjI3TzQ-&a=QAE&sk=DAAh6N4sTtrUiB&d=c2wBTVRVd0FUTXlOemM1T1RVd09ETS0BYQFRQUUBdGlwAWI3STk0RAF6egFGcmR4REJnV0E-&af=QUFBQkFBJnRzPTExMzcwNDAwNjkmcHM9RGhhZFZ5M2M2WnJ0RXlCOEtzYk5vQS0t"
		 * , "/", 3600 * 24 * 100, false); // and then added to your HTTP state
		 * instance initialState.addCookie(bCookie);
		 * initialState.addCookie(fCookie); initialState.addCookie(yCookie);
		 * initialState.addCookie(tCookie); client.setState(initialState); byte
		 * [] response = util.getHttpResponse(
		 * "http://cn.f150.mail.yahoo.com/ym/ShowFolder?rb=Inbox",
		 * CookiePolicy.DEFAULT, client); logger.debug("Response from yahoo: \n"
		 * + new String(response));
		 **/
		// byte [] content =
		// util.getHttpResponse("http://www.pcshow.net/css/css_v10a.css", null);
		// System.out.println(new
		// String(util.getHttpResponse("http://weather.tq121.com.cn/detail.php?city=%D6%D8%C7%EC",
		// null)));
		// System.out.println(new
		// String(util.getHttpResponseBean("http://laoniu.blog.techweb.com.cn/rss2.xml",
		// true).getContent()));
		// System.out.println(new
		// String(util.getHttpResponseBean("http://pic.daqi.com/",
		// true).getContent()));
		// System.out.println(new
		// String(util.getHttpResponseBean("http://www.kooxoo.com/Cat_sale.From_%E5%8C%97%E4%BA%AC.T_Rent.ct_RentSale.q_%E6%B0%B8%E5%AE%9A%E8%B7%AF.start_0.UI_RSS.htm",
		// true).getContent()));
		// System.out.println(new
		// String(util.getHttpResponseBean("http://learning.sohu.com/20070405/n249218931.shtml",
		// true).getContent()));

	}
}