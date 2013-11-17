package com.trace.crawl.spider.frontier;

import java.io.FileNotFoundException;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.DatabaseException;
import com.trace.crawl.spider.CrawlUrl;

public class BDBFrontier extends AbstractFrontier implements Frontier {
	private static Log logger = LogFactory.getLog(BDBFrontier.class);
	private StoredMap pendingUrisDB = null;

	// 使用默认的路径和缓存大小构造函数
	public BDBFrontier(String homeDirectory) throws DatabaseException,
			FileNotFoundException {
		super(homeDirectory);
		EntryBinding keyBinding = new SerialBinding(javaCatalog, String.class);
		EntryBinding valueBinding = new SerialBinding(javaCatalog,
				CrawlUrl.class);
		pendingUrisDB = new StoredMap(database, keyBinding, valueBinding, true);
	}

	public StoredMap getPendingUrisDB() {
		return pendingUrisDB;
	}

	public void setPendingUrisDB(StoredMap pendingUrisDB) {
		this.pendingUrisDB = pendingUrisDB;
	}

	// 获得下一条记录
	public CrawlUrl getNext() throws Exception {
		CrawlUrl result = null;
		if (!pendingUrisDB.isEmpty()) {
			Set entrys = pendingUrisDB.entrySet();
			Entry<String, CrawlUrl> entry = (Entry<String, CrawlUrl>) pendingUrisDB
					.entrySet().iterator().next();
			String threadname = Thread.currentThread().getName();
			result = entry.getValue();
			logger.info(result + threadname + entry);
			delete(entry.getKey());
		}
		return result;
	}

	// 存入URL
	public boolean putUrl(CrawlUrl url) {

		put(url.getOriUrl(), url);

		return true;
	}

	// 存入数据库的方法
	protected void put(Object key, Object value) {
		pendingUrisDB.put(key, value);
	}

	// 取出
	protected Object get(Object key) {
		return pendingUrisDB.get(key);
	}

	// 删除
	protected Object delete(Object key) {
		return pendingUrisDB.remove(key);
	}

	// 根据URL计算键值，可以使用各种压缩算法，包括MD5等压缩算法
	private String caculateUrl(String url) {
		return url;
	}

	// 测试函数
	public static void main(String[] strs) {

		try {
			BDBFrontier bBDBFrontier = new BDBFrontier("e:\\testspider");
			CrawlUrl url = new CrawlUrl();
			url.setOriUrl("http://www.163.com");
			bBDBFrontier.putUrl(url);
			logger.info(((CrawlUrl) bBDBFrontier.getNext()).getOriUrl());
			bBDBFrontier.close();
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		} finally {

		}

	}

	public boolean visited(CrawlUrl url) {
		return false;
	}

	public boolean contain(CrawlUrl url) {
		Object o = get(url.getOriUrl());
		return o != null;
	}

}