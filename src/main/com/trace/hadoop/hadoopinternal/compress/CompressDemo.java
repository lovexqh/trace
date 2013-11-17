package com.trace.hadoop.hadoopinternal.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.io.compress.Compressor;
import org.apache.hadoop.io.compress.CompressorStream;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.io.compress.zlib.BuiltInZlibDeflater;
import org.apache.hadoop.util.ReflectionUtils;

public class CompressDemo {	
	public static void compress(String method) throws ClassNotFoundException, IOException {
		File fileIn = new File("README.txt");
		InputStream in =  new FileInputStream(fileIn);
		
		Class<?> codecClass = Class.forName(method);
		
		Configuration conf = new Configuration();		
		CompressionCodec codec = (CompressionCodec)
		    ReflectionUtils.newInstance(codecClass, conf);
		
		File fileOut = new File("README.txt"+codec.getDefaultExtension());
		fileOut.delete();
		OutputStream out =  new FileOutputStream(fileOut);
		
		CompressionOutputStream cout =
			codec.createOutputStream(out);
		
		IOUtils.copyBytes(in, cout, 4096, false);
		
		in.close();
		cout.close();
	}	
	
	public static void decompress(File file) throws IOException {		
		Configuration conf = new Configuration();
		CompressionCodecFactory factory = new CompressionCodecFactory(conf);
		
		CompressionCodec codec = factory.getCodec(new Path(file.getName()));
		
		if( codec == null ) {
			System.out.println("Cannot find codec for file "+file);
			return;
		}

		File fileOut = new File(file.getName()+".txt");
		
		InputStream in = codec.createInputStream( new FileInputStream(file) );
		OutputStream out =  new FileOutputStream(fileOut);
		
		IOUtils.copyBytes(in, out, 4096, false);
		
		in.close();
		out.close();
	}

	static final int compressorOutputBufferSize=100;
	
	public static void compressor() throws IOException {
		File fileIn = new File("README.txt");
		InputStream in =  new FileInputStream(fileIn);
		int datalength=in.available();
		byte[] inbuf = new byte[datalength];     
		in.read(inbuf, 0, datalength);
		in.close();

		byte[] outbuf = new byte[compressorOutputBufferSize];
		
		Compressor compressor=new BuiltInZlibDeflater();
		
		int step=100;
		int inputPos=0;
		int putcount=0;
		int getcount=0;
		int compressedlen=0;
		
		while(inputPos < datalength) {
			int len=(datalength-inputPos>=step)? step:datalength-inputPos;
			compressor.setInput(inbuf, inputPos, len );
			putcount++;
		    while (!compressor.needsInput()) {
		    	compressedlen=compressor.compress(outbuf, 0, compressorOutputBufferSize);
		    	if(compressedlen>0) {
		    		getcount++;
		    	}
		    }
			inputPos+=step;
		}
			
		compressor.finish();

//      // loop by compressor.compress() return value
//		compressedlen=compressor.compress(outbuf, 0, compressorOutputBufferSize);
//		while( compressedlen > 0 ) {
//			getcount++;
//			compressedlen=compressor.compress(outbuf, 0, compressorOutputBufferSize);
//		}
		
		while(!compressor.finished()) {
			getcount++;
		    compressor.compress(outbuf, 0, compressorOutputBufferSize);
		}
		
		System.out.println("Compress "+compressor.getBytesRead()+" bytes into "+compressor.getBytesWritten());
		System.out.println("put "+putcount+" times and get "+getcount+" times");
		
		compressor.end();		
	}
	
	public static void compressorStream() throws IOException {
		File fileIn = new File("README.txt");
		InputStream in =  new FileInputStream(fileIn);
				
		File fileOut = new File("README.txt.stream.gz");
		fileOut.delete();
		OutputStream out =  new FileOutputStream(fileOut);
		
		GzipCodec codec=new GzipCodec();
		codec.setConf(new Configuration());
		CompressorStream cout = new CompressorStream(out, codec.createCompressor(), 10);
		
		IOUtils.copyBytes(in, cout, 10, false);
		
		in.close();
		cout.close();
	}
	
	public static void main(String[] args) {
		try {
			//compressorStream();
			
			compressor();
			
			compress("org.apache.hadoop.io.compress.DefaultCodec");
			compress("org.apache.hadoop.io.compress.GzipCodec");
			compress("org.apache.hadoop.io.compress.BZip2Codec");

			decompress(new File("README.txt.bz2"));
			decompress(new File("README.txt.deflate"));
			decompress(new File("README.txt"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
