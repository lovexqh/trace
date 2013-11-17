package com.trace.net;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DownLoadFile {
	private Log logger = LogFactory.getLog(getClass());

	/**
	 * 根据 URL 和网页类型生成需要保存的网页的文件名，去除 URL 中的非文件名字符
	 */
	public static String getFileNameByUrl(String url, String contentType) {
		// 移除http:
		url = url.substring(7);
		// text/html类型
		if (contentType.indexOf("html") != -1) {
			// url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
			url = url.replaceAll("[\\?/:*|<>\"]", "") + ".html";
			return url;
		}
		// 如application/pdf类型
		else {
			return url.replaceAll("[\\?/:*|<>\"]", "_") + "."
					+ contentType.substring(contentType.lastIndexOf("/") + 1);
		}
	}

	/**
	 * 保存网页字节数组到本地文件，filePath 为要保存的文件的相对地址
	 */
	private void  saveToLocal(byte[] data, String filePath) {
		try {
			DataOutputStream out = new DataOutputStream(new

			FileOutputStream(new File(filePath)));
			for (int i = 0; i < data.length; i++)
			out.write(data[i]);
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error(e.getStackTrace());
		}
	}

	// 下载 URL 指向的网页
	public String downloadFile(String url) {
		String filePath = null;
		// 1.生成 HttpClinet 对象并设置参数
		HttpClient httpClient = new HttpClient();
		httpClient.getHostConfiguration().setProxy("172.17.18.80", 8080);
		// 设置 HTTP 连接超时 5s
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(
				5000);
		// 2.生成 GetMethod 对象并设置参数
		GetMethod getMethod = new GetMethod(url);
		// 设置 get请求超时 5s
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
		// 设置请求重试处理
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		// 3.执行 HTTP GET 请求
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			// 判断访问的状态码
			if (statusCode != HttpStatus.SC_OK) {
				System.err
						.println("Method failed:" + getMethod.getStatusLine());
				filePath = null;
			}

			// 4.处理 HTTP 响应内容
			System.out.println(Thread.currentThread().getName()
					+ "is downloading............" + url);

			InputStream responseBody = getMethod.getResponseBodyAsStream();// 读取为字节数组
			// 根据网页 url 生成保存时的文件名
			filePath = "d://html//"
					+ getFileNameByUrl(url, getMethod.getResponseHeader(
							"Content-Type").getValue());

			saveToLocal(getBytesFromIs(responseBody), filePath);
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			logger.error("Please check your provided http address!");
			logger.error(e.getStackTrace());
		} catch (IOException e) {
			// 发生网络异常
			logger.error(e.getStackTrace());
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
		return filePath;
	}

	private byte[] getBytesFromIs(InputStream is) throws IOException {
		int b;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((b = is.read()) != -1)
			bos.write(b);
		return bos.toByteArray();
	}
	
	
	public static void main(String[] args) {
		new DownLoadFile().downloadFile("http://www.baidu.com");
	}
}