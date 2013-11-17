package com.trace.crawl.spider.frontier;

import com.trace.crawl.spider.CrawlUrl;

public interface Frontier {  
     public CrawlUrl getNext()throws Exception;  
     public boolean putUrl(CrawlUrl url) throws Exception;  
     public boolean visited(CrawlUrl url);  

	public boolean contain(CrawlUrl url);  
} 