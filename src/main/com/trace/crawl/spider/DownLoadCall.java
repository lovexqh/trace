package com.trace.crawl.spider;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.trace.net.DownLoadFile;


public class DownLoadCall implements Callable<String> {
	private String url; // 待下载的URL

	public DownLoadCall(String u) {
		url = u;
	}

	public String call() throws Exception {
		String content = null;
		// 下载网页
		DownLoadFile downLoader = new DownLoadFile();
		content = downLoader.downloadFile(url);
		return content;
	}

	public static void main(String[] args) throws InterruptedException,
			ExecutionException {
		int threads = 4; // 并发线程数量
		ExecutorService es = Executors.newFixedThreadPool(threads);// 创建线程池
		Set<Future<String>> set = new HashSet<Future<String>>();
		String urls[] = { "http://www.google.com/" };
		for (final String url : urls) {
			DownLoadCall task = new DownLoadCall(url);
			Future<String> future = es.submit(task);// 提交下载任务
			set.add(future);
		}
		// 通过future对象取得结果
		for (Future<String> future : set) {
			String content = future.get();
			System.out.println(content);
			// 处理下载网页的结果
		}
	}
}