package com.trace.hadoop.hadoopinternal.fs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class DFSDemo {
	public static void main(String[] args) {
		Configuration conf=new Configuration();		
		
		try {
			Path inPath  = new Path("hdfs://192.168.159.100:9000/user/alice/in/hello.txt");

			FileSystem hdfs = FileSystem.get(inPath.toUri(), conf);
			
			FSDataOutputStream fout = hdfs.create(inPath);
			
			String data="testingtesting";
			
			for(int ii=0;ii<256;ii++) {
				fout.write(data.getBytes());
			}
			fout.close();

			FileStatus stat = hdfs.getFileStatus(inPath);
			System.out.println("Replication number of file "+inPath+" is "+stat.getReplication());
			
			hdfs.delete(inPath);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
