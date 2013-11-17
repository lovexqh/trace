package com.trace.net.urlhandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import com.trace.net.urlhandler.echo.EchoContentHandlerFactory;
import com.trace.net.urlhandler.echo.EchoURLConnection;
import com.trace.net.urlhandler.echo.EchoURLStreamHandlerFactory;
public class EchoClient{
  public static void main(String args[])throws IOException{
    //����URLStreamHandlerFactory
    URL.setURLStreamHandlerFactory(new EchoURLStreamHandlerFactory());
    //����ContentHandlerFactory
    URLConnection.setContentHandlerFactory(new EchoContentHandlerFactory());
    URL url=new URL("echo://localhost:8000");
    EchoURLConnection connection=(EchoURLConnection)url.openConnection();
    connection.setDoOutput(true);
    PrintWriter pw=new PrintWriter(connection.getOutputStream(),true);
    while(true){
       BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
       String msg=br.readLine();
       pw.println(msg);  //�������������Ϣ
       String echoMsg=(String)connection.getContent(); //��ȡ���������ص���Ϣ
       System.out.println(echoMsg);
       if(echoMsg.equals("echo:bye")){
         connection.disconnect(); //�Ͽ�����
         break;
       }
    }
  }
}


/****************************************************
 * ���ߣ�������                                     *
 * ��Դ��<<Java�����̾���>>                       *
 * ����֧����ַ��www.javathinker.org                *
 ***************************************************/
