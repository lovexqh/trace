package com.trace;

  

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Properties;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class Time {
	protected static Format format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	  
	public static void printSysProperties(){  
	    Properties props=System.getProperties();   
	    Iterator iter=props.keySet().iterator();   
	    while(iter.hasNext()){   
	    String key=(String)iter.next();   
	    System.out.println(key+" = "+ props.get(key));   
	    }   
	}  
	  
	/** 
	 * timeZoneOffset表示时区，如中国一般使用东八区，因此timeZoneOffset就是8 
	 * @param timeZoneOffset 
	 * @return 
	 */  
	public String getFormatedDateString(int timeZoneOffset){  
	    if (timeZoneOffset > 13 || timeZoneOffset < -12) {  
	        timeZoneOffset = 0;  
	    }  
	    TimeZone timeZone;  
	    String[] ids = TimeZone.getAvailableIDs(timeZoneOffset * 60 * 60 * 1000);  
	    if (ids.length == 0) {  
	        // if no ids were returned, something is wrong. use default TimeZone  
	        timeZone = TimeZone.getDefault();  
	    } else {  
	        timeZone = new SimpleTimeZone(timeZoneOffset * 60 * 60 * 1000, ids[0]);  
	    }  
	  
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    sdf.setTimeZone(timeZone);  
	    return sdf.format(new Date());  
	}  
	  
	public static String getFormatedDateString(String _timeZone){  
	    TimeZone timeZone = null;  
	    if(_timeZone==null){  
	        timeZone = TimeZone.getDefault();  
	    }else{  
	        timeZone = TimeZone.getTimeZone(_timeZone);  
	    }  
	   
	    SimpleDateFormat sdf = new SimpleDateFormat("HHmm");  
	    sdf.setTimeZone(timeZone);  
	    //TimeZone.setDefault(timeZone);  
	    return sdf.format(new Date());  
	  }  
	  
	public static void main(String args[]){  
		
		System.out.println(getFormatedDateString("Rome"));  
	    System.out.println(getFormatedDateString("Detroit"));  
	    System.out.println(getFormatedDateString("Asia/Shanghai"));  
	    System.out.println(getFormatedDateString("Japan"));  
	    System.out.println(getFormatedDateString("Europe/Madrid"));  
	    System.out.println(getFormatedDateString("American/America"));  
	    System.out.println(getFormatedDateString("GMT+8"));  
	  
//	    printSysProperties();  
	}  
}