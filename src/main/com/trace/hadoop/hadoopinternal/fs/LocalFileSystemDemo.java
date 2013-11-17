package com.trace.hadoop.hadoopinternal.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Checksum;

import lava.clib.Ctype;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.PureJavaCrc32;

public class LocalFileSystemDemo {
	public static int crc_block_len=512;

	public static void crcDemo() {
		Checksum crc32=new PureJavaCrc32();

		String data="testingtesting";
		byte[] buf=data.getBytes();

		int buflen=buf.length;
		for(int ii=0; ii<crc_block_len/buflen; ii++) {
			crc32.update(buf, 0, buflen);
		}
		crc32.update(buf, 0, crc_block_len%buflen);

		System.out.println("CRC-32 checksum is: "+crc32.getValue()+" Hex is:"+Long.toHexString(crc32.getValue()).toUpperCase());	
	}

	public static void LFSWriteReadDemo() {
		try {	
			Configuration conf = new Configuration();
			LocalFileSystem localFs = FileSystem.getLocal(conf);

			Path testPath = new Path("LocalFileDemo.txt");
			localFs.delete(testPath, false);

			FSDataOutputStream fout = localFs.create(testPath);
			String data="testingtesting";

			for(int ii=0;ii<256;ii++) {
				fout.write(data.getBytes());
			}
			fout.close();

			if(localFs.exists(localFs.getChecksumFile(testPath))) {
				System.out.println("Checksum File "+localFs.getChecksumFile(testPath)+" existed!");
			} else {
				System.out.println("Checksum File NOT existed!");
			}

			FSDataInputStream fin=localFs.open(testPath);
			byte indata[]=new byte[data.getBytes().length];
			fin.read(indata);
			System.out.println(new String(indata));
			fin.close();
			//localFs.delete(testPath, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void print16(byte[] buffer, int length) {
		int iPos = 0;
		while (iPos < length) {
			int iLinePos = 0;
			String output = "";
			String charOutput = "";

			while (iPos < length && iLinePos < 16) {
				String hexString = Integer.toHexString(0x0FF & buffer[iPos]);
				output += ((hexString.length() < 2 ? "0" : "") + hexString + " ");
				charOutput += (Ctype.isprint(buffer[iPos]) ? Character
						.valueOf((char) buffer[iPos]) : ".");

				if (iLinePos == 7) {
					output += " ";
					charOutput += " ";
				}

				iLinePos++;
				iPos++;
			}

			while (iLinePos < 16) {
				output += "   ";
				iLinePos++;
			}

			System.out.println(output.toUpperCase() + "   " + charOutput);
		}
	}

	public static void PrintCRCFile() {
		try {
			Configuration conf = new Configuration();
			LocalFileSystem localFs = FileSystem.getLocal(conf);			
			Path inputPath = localFs.getChecksumFile(new Path("LocalFileDemo.txt"));
			
			File fileIn = new File(inputPath.getName());
			InputStream in = new FileInputStream(fileIn);
			
			int datalength=in.available();
			byte[] inbuf = new byte[datalength];     
			in.read(inbuf, 0, datalength);
			print16(inbuf, datalength);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public static void LFSCRCErrorDemo() {
		try {	
			Configuration conf = new Configuration();
			LocalFileSystem localFs = FileSystem.getLocal(conf);

			Path testPath = new Path("LocalFileDemo.txt");
			localFs.delete(testPath, false);

			FSDataOutputStream fout = localFs.create(testPath);
			String data="testingtesting";

			for(int ii=0;ii<256;ii++) {
				fout.write(data.getBytes());
			}
			fout.close();

			if(localFs.exists(localFs.getChecksumFile(testPath))) {
				File fileIn = new File(localFs.getChecksumFile(testPath).getName());
				InputStream in = new FileInputStream(fileIn);
				
				int datalength=in.available();
				byte[] inbuf = new byte[datalength];     
				in.read(inbuf, 0, datalength);
				in.close();
				fileIn.delete();
				
				inbuf[8]=0;  // change FC to 0, cause checksum error.
				OutputStream out = new FileOutputStream(fileIn);
				out.write(inbuf);
				out.close();
			} else {
				System.out.println("Checksum File NOT existed!");
				return;
			}

			FSDataInputStream fin=localFs.open(testPath);
			byte indata[]=new byte[data.getBytes().length];
			fin.read(indata);
			System.out.println(new String(indata));
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		crcDemo();
		LFSWriteReadDemo();
		PrintCRCFile();
		LFSCRCErrorDemo();
	}
}
