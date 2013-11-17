package com.trace.hadoop.hadoopinternal.fs;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.InMemoryFileSystem;
import org.apache.hadoop.fs.Path;

public class RamFileSystemDemo {
	public static void main(String[] args) {
		try {
			Configuration conf = new Configuration();

			URI uri = URI.create("ramfs://demo");
			InMemoryFileSystem inMemFs =  (InMemoryFileSystem)FileSystem.get(uri, conf);
			
			Path testPath = new Path("/file1");
			inMemFs.reserveSpaceWithCheckSum(testPath, 1024);		    
			FSDataOutputStream fout = inMemFs.create(testPath);
			for(int ii=0; ii<512/7; ii++) {
				fout.writeUTF("testing");
			}
			fout.close();

			if(inMemFs.exists(inMemFs.getChecksumFile(testPath))) {
				System.out.println("Checksum File existed!");
			} else {
				System.out.println("Checksum File NOT existed!");
			}

			FSDataInputStream fin=inMemFs.open(testPath);
			String instring=fin.readUTF();
			System.out.println(instring);
			fin.close();

			inMemFs.delete(testPath, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
