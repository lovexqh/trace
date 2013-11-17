package com.trace.crawl.spider;

import java.util.Set;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Parser;

import com.trace.common.Config;
import com.trace.common.util.FileUtil;
import com.trace.crawl.spider.frontier.Frontier;
import com.trace.net.http.DownLoadUtil;
import com.trace.net.http.HtmlParserUtils;
import com.trace.net.http.MessageBean;


public class ChildThread extends Thread {
	private Log logger = LogFactory.getLog(ChildThread.class);
	public static int thread = 0;
	private Frontier unvisitFrontier;
	private Frontier visitedFrontier;
	private String host;
	private Cookie[] cookies;

	public ChildThread(Frontier unvisitFrontier, Frontier visitedFrontier) {
		this.unvisitFrontier = unvisitFrontier;
		this.visitedFrontier = visitedFrontier;
		String oriUrl = Config.getProperty("oriUrl");
		host = oriUrl.substring(7);
		if (host.indexOf("/") != -1)
			host = host.substring(0, host.indexOf("/"));
	}
	public ChildThread(Frontier unvisitFrontier, Frontier visitedFrontier,Cookie[] cookies) {
		this.unvisitFrontier = unvisitFrontier;
		this.visitedFrontier = visitedFrontier;
		this.cookies =  cookies;
		String oriUrl = Config.getProperty("oriUrl");
		host = oriUrl.substring(7);
		if (host.indexOf("/") != -1)
			host = host.substring(0, host.indexOf("/"));
	}

	@Override
	public void run() {
		logger.info(Thread.currentThread().getName());
		CrawlUrl url;
		try {
			while ((url = dequeueUrl()) != null) {
				MessageBean mb = DownLoadUtil.downloadGet(url.getOriUrl());
				if (mb.getM_errorCode() != HttpStatus.SC_OK)
					continue;
				String rootPath = Config.getProperty("rootPath");
				String filename = DownLoadUtil.getFileNameByUrl(
						url.getOriUrl(), mb.getM_content_type());

				FileUtil.saveFile(mb.getM_content(), rootPath + filename, mb
						.getM_responseCode());
				// int ip = Integer.parseInt(Config.getProperty("proxy.port"));
				// Parser parser = HtmlParserUtils.getParserWithUrlConn(url
				// .getOriUrl(), mb.getM_responseCode(), Config
				// .getProperty("proxy.ip"), ip);
				if (canExtract(url)) {
					Parser parser = HtmlParserUtils.createParser(mb
							.getM_content(), mb.getM_responseCode());
					Set<String> links = HtmlParserUtils.extracLinks(parser);
					addUrls(url, links);
				}
			}
			MainThread.WAIT_THREAD--;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addUrls(CrawlUrl url, Set<String> links) throws Exception {
		for (String unvisited : links) {
			CrawlUrl crawUrl = new CrawlUrl(unvisited, url);
			if (!visitedFrontier.contain(crawUrl))
				addUrl(unvisitFrontier, crawUrl);
		}
	}

	public void addUrl(Frontier forntier, CrawlUrl crawUrl) throws Exception {
		forntier.putUrl(crawUrl);
	}

	public CrawlUrl dequeueUrl() throws Exception {
		CrawlUrl crawlUrl = null;
		synchronized (unvisitFrontier) {
			while (true) {
				crawlUrl = unvisitFrontier.getNext();
				if (crawlUrl != null) {
					visitedFrontier.putUrl(crawlUrl);
					unvisitFrontier.notifyAll();
					return crawlUrl;
					// }
				} else {
					MainThread.WAIT_THREAD--;
					// System.out.println(MainThread.WAIT_THREAD);
					if (MainThread.WAIT_THREAD > 0) {
						unvisitFrontier.wait();
						MainThread.WAIT_THREAD++;
					} else {
						unvisitFrontier.notifyAll();
						return null;
					}
				}
			}
		}
	}

	private boolean canExtract(CrawlUrl url) {
		boolean flag = false;
		if (url.getLayer() == 0) {
			flag = true;
		} else if (host.equals(url.getParentUrl().getHost())) {
			flag = true;
		}
		return flag;
	}
}
