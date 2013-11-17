package com.trace.net.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

import org.apache.log4j.Logger;

/**
 * Inflate or deflate utility
 * 
 * @author Jack Wang
 *
 */
public class CompressUtil {
	
	 /**
     * Log4j logger
     */
    static Logger logger = Logger.getLogger(CompressUtil.class);
	
	/**
	 * Use java gzip to compress input content
	 * 
	 * @param content
	 * @return null if IOException occurs
	 */
	public static byte [] gzip (byte [] content) {
		byte [] compressedContent = null;
		if (content == null) return compressedContent;
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		try {
			GZIPOutputStream gzipStream = new GZIPOutputStream(baos);
			gzipStream.write(content, 0, content.length);
			gzipStream.flush();
			gzipStream.finish();
			compressedContent = baos.toByteArray();
			
			gzipStream.close();
			baos.close();
		} catch (IOException e) {
			logger.warn("gzip error: " + e.getMessage(), e);
		}
		return compressedContent;
	}
	
	/**
	 * Use java gzip to uncompress input content
	 * 
	 * @param compressedContent
	 * @return null if IOException occurs
	 */
	public static byte [] ungzip (byte [] compressedContent) {
		byte [] content = null;
		if (compressedContent == null) return content;
		ByteArrayInputStream bais = new ByteArrayInputStream(compressedContent);
		try {
			GZIPInputStream gzipStream = new GZIPInputStream(bais);
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			byte [] buffer = new byte [1024];
			int i;
			while ((i = gzipStream.read(buffer)) != -1) {
				baos.write(buffer, 0, i);
			}
			content = baos.toByteArray();
			
			gzipStream.close();
			bais.close();
			baos.close();
		} catch (IOException e) {
			logger.warn("ungzip error: " + e.getMessage(), e);
		}
		return content;
	}
	
	/**
	 * Use java inflate to uncompress input content
	 * 
	 * @param compressedContent
	 * @return null if IOException occurs
	 */
	public static byte [] inflate (byte [] compressedContent) {
		byte [] content = null;
		if (compressedContent == null) return content;
		ByteArrayInputStream bais = new ByteArrayInputStream(compressedContent);
		try {
			InflaterInputStream inflaterStream = new InflaterInputStream(bais);
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
			byte [] buffer = new byte [1024];
			int i;
			while ((i = inflaterStream.read(buffer)) != -1) {
				baos.write(buffer, 0, i);
			}
			content = baos.toByteArray();
			
			inflaterStream.close();
			bais.close();
			baos.close();
		} catch (IOException e) {
			logger.warn("deflate error: " + e.getMessage(), e);
		}
		return content;
	}
	
	public static void main (String [] args) throws Exception {
		System.out.println("zip file - " + args[0]);
		FileInputStream fis = new FileInputStream(args[0]);
		byte [] compressed = CompressUtil.gzip(CommonUtil.getContentFromInputStream(fis));
		CommonUtil.writeToFile(args[0] + ".gz", compressed);
		System.out.println("write to zip file - " + args[0] + ".gz");
		fis = new FileInputStream(args[0] + ".gz");
		System.out.println("unzip to uncompress.java");
		CommonUtil.writeToFile("uncompress.java", 
				CompressUtil.ungzip(CommonUtil.getContentFromInputStream(fis)));
	}
}
