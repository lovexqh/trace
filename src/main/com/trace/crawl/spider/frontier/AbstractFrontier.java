package com.trace.crawl.spider.frontier;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public abstract class AbstractFrontier {
	private static Log logger = LogFactory.getLog(AbstractFrontier.class);
	private Environment env;
	private static final String CLASS_CATALOG = "java_class_catalog";
	protected StoredClassCatalog javaCatalog;
	protected Database catalogdatabase;
	protected Database database;

	public AbstractFrontier(String homeDirectory) throws DatabaseException,
			FileNotFoundException {
		// 打开env
		logger.info("Opening environment in: " + homeDirectory);
		File file = new File(homeDirectory);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setTransactional(true);
		envConfig.setAllowCreate(true);
		env = new Environment(file, envConfig);
		// 设置DatabaseConfig
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setTransactional(true);
		dbConfig.setAllowCreate(true);
		dbConfig.setSortedDuplicates(false);
		// dbConfig.setDeferredWrite(true);
		// 打开
		catalogdatabase = env.openDatabase(null, CLASS_CATALOG, dbConfig);
		javaCatalog = new StoredClassCatalog(catalogdatabase);
		// 设置DatabaseConfig
		// DatabaseConfig dbConfig0 = new DatabaseConfig();
		// dbConfig0.setTransactional(true);
		// dbConfig0.setAllowCreate(true);
		// 打开
		database = env.openDatabase(null, "URL", dbConfig);
	}

	// 关闭数据库，关闭环境
	public void close() throws DatabaseException {
		database.close();
		javaCatalog.close();
		env.close();
	}

	// put方法
	protected abstract void put(Object key, Object value);

	// get方法
	protected abstract Object get(Object key);

	// delete方法
	protected abstract Object delete(Object key);
}