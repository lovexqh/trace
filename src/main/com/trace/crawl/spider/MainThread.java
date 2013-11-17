package com.trace.crawl.spider;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.trace.common.Config;
import com.trace.crawl.spider.frontier.BDBFrontier;
import com.trace.crawl.spider.frontier.Frontier;
import com.trace.net.http.HttpClientUtil;





public class MainThread {
	private Log logger = LogFactory.getLog(MainThread.class);
	public static int THREAD_NUMBER = 1;
	public static int WAIT_THREAD = 1;
	public static  LinkedList<Thread> threadList = new LinkedList<Thread>();

	public static void main(String[] args) throws Exception {
		String oriUrlPro  = Config.getProperty("oriUrl");
		String unvisitFrontierPro  = Config.getProperty("unvisitFrontier");
		String visitedFrontierPro  = Config.getProperty("visitedFrontier");
//		String proxyHostPro = (String) p.get("http.proxyHost");
//		String proxyPortPro = (String) p.get("http.proxyPort");
		
		
		Frontier unvisitFrontier = new BDBFrontier(unvisitFrontierPro);
		Frontier visitedFrontier = new BDBFrontier(visitedFrontierPro);
		
		
		if(unvisitFrontier.getNext()==null){
		CrawlUrl crawlUrl = new CrawlUrl();
//		crawlUrl.setOriUrl("http://85st.com/bbs/forumdisplay.php?fid=2&page=1");
		crawlUrl.setOriUrl(oriUrlPro);
		unvisitFrontier.putUrl(crawlUrl);
		}
//		LinkQueue.addUnvisitedUrl("http://www.163.com");
		for (int i = 0; i < THREAD_NUMBER; i++) {
			ChildThread childThread = new ChildThread(unvisitFrontier,visitedFrontier);
			Thread child = new Thread(childThread,"Spider Thread#-------"+i);
			child.start();
			threadList.add(child);
		}
		while(threadList.size()>0){
			Thread t = threadList.removeFirst();
			t.join();
		}
//		Thread.sleep(100000000);
	}
}
