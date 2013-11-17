package com.trace.hadoop.hadoopinternal.fs;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.fs.InMemoryFileSystem;
import org.apache.hadoop.fs.Path;

public class FsUrlDemo {
	public static void main(String[] args) {
		try {	
		    Configuration conf = new Configuration();
		    conf.set("fs.ramfs.impl",
                     "org.apache.hadoop.fs.InMemoryFileSystem");
		    FsUrlStreamHandlerFactory factory =
		     	new org.apache.hadoop.fs.FsUrlStreamHandlerFactory(conf);
			java.net.URL.setURLStreamHandlerFactory(factory);
						
		    URI uri = URI.create("ramfs://demo");
		    InMemoryFileSystem inMemFs =  (InMemoryFileSystem)FileSystem.get(uri, conf);
		    Path testPath = new Path("/file1");
		    inMemFs.reserveSpaceWithCheckSum(testPath, 1024);		    
		    FSDataOutputStream fout = inMemFs.create(testPath);
		    for(int ii=0; ii<512/7; ii++) {
		        fout.writeUTF("testing");
		    }
		    fout.close();	
		    
		    DataInputStream fin2=new DataInputStream((new URL("ramfs://demo/file1")).openStream());
            String instring=fin2.readUTF();
		    System.out.println(instring);
		    fin2.close();

		    inMemFs.delete(testPath, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
