package com.trace;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.ReadableByteChannel;
import java.util.Date;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WeatherReport {
	/**
	 * ��ȡSOAP������ͷ�����滻���еı�־���Ϊ�û�����ĳ���
	 * 
	 * ��д�ߣ�������
	 * 
	 * @param city
	 *            �û�����ĳ������
	 * @return �ͻ���Ҫ���͸��������SOAP	����
	 */
	private static String getSoapRequest(String city) {
		StringBuilder sb = new StringBuilder();
		sb
				.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
						+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
						+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
						+ "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
						+ "<soap:Body>    <getWeatherbyCityName xmlns=\"http://WebXml.com.cn/\">"
						+ "<theCityName>" + city
						+ "</theCityName>    </getWeatherbyCityName>"
						+ "</soap:Body></soap:Envelope>");
		return sb.toString();
	}

	/**
	 * �û���SOAP�����͸�������ˣ������ط������㷵�ص�������
	 * 
	 * ��д�ߣ�������
	 * 
	 * @param city
	 *            �û�����ĳ������
	 * @return �������˷��ص������������ͻ��˶�ȡ
	 * @throws Exception
	 */
	private static InputStream getSoapInputStream(String city) throws Exception {
//		Properties prop = System.getProperties();
//		// ����http����Ҫʹ�õĴ���������ĵ�ַ
//		prop.setProperty("http.proxyHost", "172.17.18.80");
//		// ����http����Ҫʹ�õĴ���������Ķ˿�
//		prop.setProperty("http.proxyPort", "8080");
		try {
			String soap = getSoapRequest(city);
			if (soap == null) {
				return null;
			}
			URL url = new URL(
					"http://www.webxml.com.cn/WebServices/WeatherWebService.asmx");
			URLConnection conn = url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			conn.setRequestProperty("Content-Length", Integer.toString(soap
					.length()));
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			conn.setRequestProperty("SOAPAction",
					"http://WebXml.com.cn/getWeatherbyCityName");
	
			OutputStream os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
			osw.write(soap);
			osw.flush();
			osw.close();
			
			InputStream is = conn.getInputStream();			
			return is;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * �Է������˷��ص�XML���н���
	 * 
	 * ��д�ߣ�������
	 * 
	 * @param city
	 *            �û�����ĳ������
	 * @return �ַ� ��,�ָ�
	 * @throws Exception 
	 */
	public static String getWeather(String city) throws Exception {
		String a,b,c,d;
		int countTmp=0;
//		try {
			Document doc;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = getSoapInputStream(city);
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			
			String line;
//			while((line = br.readLine()) != null){
//				System.out.println(line);
//			}
//			return null;
//			return null;
			
			doc = db.parse(is);
			NodeList nl = doc.getElementsByTagName("string");
			StringBuffer sb = new StringBuffer();
			for (int count = 0; count < nl.getLength(); count++) {
				Node n = nl.item(count);
				if(n.getFirstChild().getNodeValue().equals("��ѯ���Ϊ�գ�")) {
					sb = new StringBuffer("#") ;
					break ;
				}
				String value =n.getFirstChild().getNodeValue();
//				if(value.contains("��")&&countTmp==0){
//					a=value.substring(0, value.indexOf("/")).substring(0, value.indexOf("��"));
//					b=value.substring(value.indexOf("/")+1,value.lastIndexOf("��"));;
//					System.out.println(a+"========"+b);
//					countTmp = count;
//				}
//				if(count==countTmp+1&&countTmp!=0){
//					c=value.substring(value.indexOf("��")+1,value.length());
//					System.out.println(c);
//				}
//				if(count==countTmp+2&&countTmp!=0){
//					d=value;
//					System.out.println(d);
//				}
				sb.append(value);
				sb.append("#\n");
			}
//			is.close();
//			return sb.toString();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
			return sb.toString();
	}
	
//	/**
//	 * �������xml�ļ�
//	 * @param args
//	 * @throws Exception
//	 */
//	public static void createXml(InputStream is){
//		try {
//			InputStreamReader isr = new InputStreamReader(is,"utf-8");
//	        BufferedReader in = new BufferedReader(isr);
//	        String inputLine;
//	        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("result.xml")));
//	        while ((inputLine = in.readLine()) != null){
//	            bw.write(inputLine);
//	            bw.newLine();
//	        }     
//	        bw.close();
//	        in.close();
//	        isr.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(getWeather("����"));
	}
}
