package com.trace.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JavaMD5 {
	private static final Log logger = LogFactory.getLog(JavaMD5.class);
	private static ThreadLocal<MessageDigest> MD5 = new ThreadLocal<MessageDigest>() {
		protected MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalStateException("no md5 algorythm found");
			}
		}
	};
	
	public static String byteHEX(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0F];
		ob[1] = Digit[ib & 0X0F];
		String s = new String(ob);
		return s;
	}
	
	public static String getMD5Str(String str, String charset) {
		try {
			MessageDigest md = MD5.get();
			byte[] data = str.getBytes(charset);
			md.update(data);
			data = md.digest();
			StringBuilder digestHexStr = new StringBuilder(16);
			for (int i = 0; i < 16; i++) {
				digestHexStr.append(byteHEX(data[i]));
			}
			return digestHexStr.toString().toLowerCase();
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
			return "";
		}
	}
	
	public static int getMD5(String str) {
		try {
			MessageDigest md = MD5.get();
			byte[] data = str.getBytes("GBK");
			md.update(data);
			data = md.digest();
			int s = byteToInt(data);
			if(s < 0){
			    s = -s;
			}
			return s;
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
			return 0;
		}
	}
	
	public static int byteToInt(byte[] bRefArr) {
		int iOutcome = 0; 
		byte bLoop; 
		for (int i=0; i<4 ; i++) { 
			bLoop = bRefArr[i]; 
			iOutcome+= (bLoop & 0xFF) << (8 * i); 
		}   
		return iOutcome; 
	}
	
	public static String getCMSMD5Str(String str){
		String res = getMD5Str(str,"gbk");
		if(res!=null && res.length()==32){
			String temp = res.substring(0,16);
			StringBuilder sb = new StringBuilder(16);
			for(int i=7;i>=0;i--){
				sb.append(temp.substring(i*2, i*2+2).toUpperCase());
			}
			res = sb.toString();
		}else{
			logger.error("get cms md5str error");
			res = "";
		}
		return res;
	}
	
	public static void main(String[] args) {
		System.out.println(getCMSMD5Str("http://news.baidu.com/ns?word=公积金&tn=newsrss&sr=0&cl=2rn=20&ct=0"));
		System.out.println(getMD5("weiyujieju1"));

	}
}
