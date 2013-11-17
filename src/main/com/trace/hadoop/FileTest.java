package com.trace.hadoop;

import java.io.IOException;
import java.net.URL;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.fs.Path;

public class FileTest {
//	static {
//		URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
//	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
//		 Path inPath = new Path("hftp://192.168.159.100:50070");
		Path inPath = new Path("hdfs://tiger:9000/user/xqh/urls");
		FileSystem hdfs = FileSystem.get(inPath.toUri(), conf);
		hdfs.printStatistics();
		// FSDataOutputStream fout = hdfs.create(inPath);
		// String data = "testingtesting";
		// for (int i = 0; i < 256; i++) {
		// fout.write(data.getBytes());
		//
		// }
		// fout.close();
		 FileStatus fsta = hdfs.getFileStatus(inPath);
		 fsta.getOwner();
		 System.out.println( fsta.getOwner());

	}

}
