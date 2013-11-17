package com.trace.thread;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileTest {
public static void main(String[] args) throws IOException {
	
	File file= new File("D://bdb//unvisit");
	file.canRead();
	if(!file.exists()&&!file.isDirectory()){
		file.mkdirs();
	}
	FileWriter fw = new FileWriter(file);
	fw.write("aaa");
}
}
