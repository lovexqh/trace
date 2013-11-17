package com.trace.net.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.log4j.Logger;


/***
 * Common Utility class
 * @author Jack Wang
 *
 */
public class CommonUtil {
    
    
    
    /**
     * Log4j logger
     */
    static Logger logger = Logger.getLogger(CommonUtil.class);
    static HttpClientUtil httpClient = HttpClientUtil.getInstance();
    /**
     * Digest message
     * @param string
     * @param algorithm
     * @return
     * @throws NoSuchAlgorithmException 
     */
    public static String digestString (String string, String algorithm) 
    throws NoSuchAlgorithmException 
    {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        return (convert(digest.digest(string.getBytes())));
    }
    
    /**
     * Convert a byte array into a printable format containing a
     * String of hexadecimal digit characters (two per byte).
     *
     * @param bytes Byte array representation
     */
    public static String convert(byte bytes[]) {

        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            sb.append(convertDigit((int) (bytes[i] >> 4)));
            sb.append(convertDigit((int) (bytes[i] & 0x0f)));
        }
        return (sb.toString());

    }
    
    /**
     * [Private] Convert the specified value (0 .. 15) to the corresponding
     * hexadecimal digit.
     *
     * @param value Value to be converted
     */
    private static char convertDigit(int value) {

        value &= 0x0f;
        if (value >= 10)
            return ((char) (value - 10 + 'a'));
        else
            return ((char) (value + '0'));

    }
    
    /**
     * Get String content from java.io.Reader
     * @param reader
     * @return
     */
    public static String getContentFromReader (Reader reader) {
    	StringBuffer sb = new StringBuffer();
    	char [] inBuffer = new char [1024];
    	int i;
    	try {
			while ((i = reader.read(inBuffer)) != -1) {
				sb.append(inBuffer, 0, i);
			}
			reader.close();
		} catch (IOException e) {
			logger.warn ("getContentFromReader error: " + e);
		}
    	return sb.toString();
    }
    
    /**
     * Get byte [] from java.io.InputStream
     * @param reader
     * @return
     */
    public static byte [] getContentFromInputStream (InputStream inputStream) {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream (1024);
    	byte [] inBuffer = new byte [1024];
    	int i;
    	try {
			while ((i = inputStream.read(inBuffer)) != -1) {
				baos.write(inBuffer, 0, i);
			}
			inBuffer = baos.toByteArray();
			inputStream.close();
		} catch (IOException e) {
			logger.warn ("getContentFromInputStream error: " + e);
			inBuffer = null;
		} finally {
			try {
				baos.close();
			} catch (IOException e) {}
		}
		return inBuffer;
    }
    
    /**
     * Get byte [] from filename
     * @param reader
     * @return
     */
    public static byte [] getContentFromFile (File file) {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream (1024);
    	byte [] inBuffer = new byte [1024];
    	int i;
    	try {
    		FileInputStream inputStream = new FileInputStream(file);
			while ((i = inputStream.read(inBuffer)) != -1) {
				baos.write(inBuffer, 0, i);
			}
			inBuffer = baos.toByteArray();
			inputStream.close();
		} catch (IOException e) {
			logger.warn ("getContentFromInputStream error: " + e);
			inBuffer = null;
		} finally {
			try {
				baos.close();
			} catch (IOException e) {}
		}
		return inBuffer;
    }
    
    /**
     * Write string to file
     * @param file
     * @param content
     */
    public static void writeToFile (String file, String content) {
    	try {
			FileWriter fw = new FileWriter (file);
			fw.write(content);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			logger.error ("writeToFile error: " + e);
		}
    }
    
    /**
     * Write string to file
     * @param file
     * @param content
     */
    public static void writeToFile (File file, String content) {
    	try {
			FileWriter fw = new FileWriter (file);
			fw.write(content);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			logger.error ("writeToFile error: " + e);
		}
    }
    
    /**
     * Write byte [] to file
     * @param file
     * @param content
     */
    public static void writeToFile (String file, byte [] content) {
    	try {
			FileOutputStream fo = new FileOutputStream (file);
			fo.write(content);
			fo.flush();
			fo.close();
		} catch (IOException e) {
			logger.error ("writeToFile error: " + e);
		}
    }
    
    /**
     * Write byte [] to file
     * 
     * @param file
     * @param content
     */
    public static void writeToFile (File file, byte [] content) {
    	try {
			FileOutputStream fo = new FileOutputStream (file);
			fo.write(content);
			fo.flush();
			fo.close();
		} catch (IOException e) {
			logger.error ("writeToFile error: " + e);
		}
    }
    
    /**
     * Write byte [] to htmlblock file
     * 
     * @param file
     * @param monitorURLId
     * @param blockContent
     * @param blockCSS
     */
    public static void writeToHtmlBlockFile (File file, int blockid, byte [] blockContent, byte [] blockCSS) {
    	try {
			FileOutputStream fo = new FileOutputStream (file);
			if (blockCSS != null) fo.write(new String("<DIV id=\"aiddi_" + blockid + "\" align=\"left\">").getBytes());
			fo.write(blockContent);
			if (blockCSS != null) fo.write(new String("</DIV>\n").getBytes());
			if (blockCSS != null) fo.write(blockCSS);
			fo.flush();
			fo.close();
		} catch (IOException e) {
			logger.error ("writeToFile error: " + e);
		}
    }
    
    /**
     * Remove the last slash from url if it is the homepage url like http://www.wenxuecity.com/
     * http://tech.sina.com.cn/#hulianwang should be trimmed as http://tech.sina.com.cn
     * @param url
     * @return
     */
    public static String cleanURL (String url) {
    	if (url == null || url.length() == 0) return url;
    	url = url.trim().replaceAll("\\s", "+");
    	if (url.indexOf("://") == -1) {
            url = "http://" + url;
        }
    	//Remove anchor
    	int index = url.lastIndexOf("/");
    	if (index > 0 && index < url.length() - 1) {
    		if (url.charAt(index + 1) == '#') {
    			url = url.substring(0, index);
    		}
    	}
    	//Remove last slash character
    	if (url.length() > 7) {
    		index = url.indexOf("/", 7);
    		if (index == url.length() - 1) {
    			url = url.substring(0, url.length() - 1);
    		}
    	}
    	
    	return url;
    }
    
   
    
    /**
     * Tokenize the delimiter separated arguments
     * @param args
     * @param delimiter
     * @return
     */
    public static String [] tokenizeArguments (String args, String delimiter) {
        StringTokenizer st = new StringTokenizer (args, delimiter);
        List<String> list = new ArrayList<String>();
        while (st.hasMoreTokens()) {
        	list.add(st.nextToken());
        }
        return (String [])list.toArray(new String [list.size()]);
    }
    
    /**
     * Add new argument into delimited arguments
     * @param args
     * @param delimiter
     * @return
     */
    public static String addArgument (String args, String newArg, String delimiter) {
    	if (args == null || args.length() == 0) return newArg;
        StringTokenizer st = new StringTokenizer (args, delimiter);
        StringBuffer sb = new StringBuffer();
        while (st.hasMoreTokens()) {
        	String arg = st.nextToken();
        	if (arg.equalsIgnoreCase(newArg)) {
        		return args;
        	} else {
        		if (sb.length() > 0) sb.append(delimiter);
        		sb.append(arg);
        	}
        }
        sb.append(delimiter).append(newArg);
        return sb.toString();
    }
    
   
   
   
   
	/**
	 * Get host name with protocol from url
	 * @param url
	 * @return such as http://www.alertie.com
	 */
	public static String getHost (String url) {
		int index;
		if ((index = url.indexOf("//")) != -1) {
			index = url.indexOf("/", index + 2);
			if (index != -1) {
				return url.substring(0, index);
			} else {
				return url;
			}
		}
		return null;
	}
	
	/**
	 * Get full link from page url and page link
	 * 
	 * @param url
	 * @param link
	 * @return
	 */
	public static String getFullLink (String url, String link) {
		if (link == null) return link;
		int index;
		if (link.startsWith("http")) {
			if ((index = link.indexOf("http", 4)) != -1) {
				link = link.substring(index);
			}
			return link;
		}
		String host = getHost(url);
		if (host == null) return null;
		if (link.startsWith("/")) {
			return host + link;
		} else {
			index = url.lastIndexOf("/");
			if (index != -1) {
				return url.substring(0, index) + "/" + link;
			} else {
				return url + link;
			}
		}
	}
	
	
	
	/**
	 * Test if cookie expired
	 * @param cookieBean
	 * @return
	 */
	public static boolean isCookieExpired (Cookie cookie) {
		Date today = new Date();
		if (cookie.getExpiryDate().before(today)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Test is a string represents a number
	 * @param number
	 * @return
	 */
	public static boolean isNumber (String number) {
		if (number == null) return false;
		try {
			Integer.parseInt(number);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	 /**
     * Check email syntax
     * @param emails
     * @return
     */
    public static boolean checkEmail (String email) {
        String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[_A-Za-z0-9-]+)";
        return email.matches(regex);
    }
    
    public static String getComputerName (String hostname) {
    	int index = hostname.indexOf(".");
    	if (index != -1) return hostname.substring(0, index);
    	else return hostname;
    }
 
   
    public static String getUserTempDirectory () {
    	return System.getProperty("java.io.tmpdir");
    }
    
    public static String getTomcatDirectory () {
    	return System.getProperty("catalina.home");
    }
    
    public static String getTomcatImagesDirectory () {
    	return getTomcatDirectory() + "/webapps/ROOT/images";
    }
    
    public static boolean isProduction () {
    	return getTomcatDirectory() != null && getTomcatDirectory().startsWith("/opt/tomcat");
    }
    
    public static String createBaiduUrl( String keyword, String searchtype ){ 		
		String whole="http://news.baidu.com/ns?word=";
		String wholeend ="&ie=gb2312&cl=2&rn=20&ct=0&tn=newsrss&class=0";   
		String title="http://news.baidu.com/ns?word=title%3A";
		String titleend = "&ie=gb2312&cl=2&rn=20&ct=0&tn=newsrss&class=0";
		String send = null;
		String   test = whole + keyword + wholeend; 
		logger.debug("ENCODE :"+test); 
		try {
			keyword = URLDecoder.decode(keyword,"UTF-8");
			keyword = URLEncoder.encode(keyword, "GB2312");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		
		if(searchtype.equals("1")){
			  
			 send = whole + keyword + wholeend; 
		}else {
		     send = title + keyword + titleend; 
		}
		return send;
	}
    
    public static String createGoogleUrl( String keyword){ 		
		String url = "http://news.google.com/news?hl=zh-CN&newwindow=1&lr=&ie=UTF-8&oe=UTF-8&um=1&tab=wn&q=~keyword~&output=rss";
		
		try {
			keyword = URLDecoder.decode(keyword,"UTF-8");
			keyword = URLEncoder.encode(keyword, "GB2312");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		url = url.replaceAll("~keyword~", keyword);
		return url;
	}
    
    public static byte[] getBaiduRss(String keyword,String stockid){
    	byte[] b = new byte[0];
    	try {
    		File dir = new File("/var/guba/data");
    		if(!dir.exists()){
    			dir.mkdirs();
    		}
			File file = new File(dir,stockid+".xml");
			boolean isGet = true;
			if(file.exists()){
				Calendar c = Calendar.getInstance();
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.MILLISECOND, 0);
				c.set(Calendar.SECOND, 0);
				if(c.getTimeInMillis()<file.lastModified()){
					isGet = false;
					b = getContentFromFile(file);
				}
			}
			if(isGet){
				b = httpClient.getHttpResponse(CommonUtil.createGoogleUrl(keyword),CookiePolicy.DEFAULT,null);
				FileOutputStream out = new FileOutputStream(file);
				out.write(b);
				out.close();
			}	
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
    }
    
	public static void main (String [] args ) throws Exception {
        //String cipher = digestString (args[0], "SHA");
        //System.out.println(cipher);
    	//System.out.println(getHost("http://www.yahoo.com/ym/readmail"));
    	/****
    	String ip = "218.81.212.0";
    	BeanDelegate delegate = new BeanDelegate();
    	String location = delegate.getLocation(ip);
    	System.out.println("location = " + location);
    	String province = CommonUtil.getProvince(location, true);
    	String city = CommonUtil.getCity(location);
    	System.out.println("City: " + city);
    	int blockId = delegate.getLocalNewsBlockId(province);
    	System.out.println("Province: " + province + "  block id = " + blockId);
    	******/
    	//System.out.print(CommonUtil.isPaidExportedUser("ooexport"));
    	//System.out.println(CommonUtil.cleanURL("http://tech.sina.com.cn/#hulianwang"));
		
    }
}