package com.trace.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateUtil {
	
	private static final Log logger = LogFactory.getLog(DateUtil.class);
	
	public static Date parseDate(String dateStr, String pattern) {
		DateFormat parser = new SimpleDateFormat(pattern, Locale.ENGLISH);
		try {
			return parser.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			logger.error(e);
		}
		return null;
	}

	/**
     * 将字符串转换为日期
     * @param dateStr
     * @return
     */
    public static Date parseDate(String dateStr) {
    	String[] patterns = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
    			"yyyy:MM:dd HH:mm:ss", "yyyy-MM-dd",
    			"dd.MM.yy HH:mm", "yyyyMMdd HHmmss",
    			"yyyyMMdd HHmm", "MM/dd/yy hh:mm a",
    			"HH:mm:ss dd.MM.yyyy", "yyyy:MM:dd",
    			"yyyy:MM:dd HH:mm", "dd.MM.yy", "yyyyMMdd", "EEE, dd MMM yyyy HH:mm:ss",
    			"MM/dd/yy", "yyyy:MM:dd HH:mm:sss",
    			"yyyy/MM/dd"};
    	for (int i=0; i<patterns.length; i++) {
    		try {
    			DateFormat parser = new SimpleDateFormat(patterns[i], Locale.ENGLISH);
    			return parser.parse(dateStr);
    		} catch (ParseException e) {
    		}
    	}
    	return null;
    }
    
    /**
     * 将输入的日期增加指定的年
     * @param inDate
     * @param year
     * @return
     */
    public static Date addYear(Date inDate, int year) {
    	Calendar calendar = new GregorianCalendar();
    	calendar.setTime(inDate);
    	calendar.add(Calendar.YEAR, year);
    	return calendar.getTime();
    }
    
    public static Date addYear(String inDate, int year) {
    	return addYear(parseDate(inDate), year);
    }
    
    /**
     * 将输入的日期增加指定的月
     * @param inDate
     * @param month
     * @return
     */
    public static Date addMonth(Date inDate, int month) {
    	Calendar calendar = new GregorianCalendar();
    	calendar.setTime(inDate);
    	calendar.add(Calendar.MONTH, month);
    	return calendar.getTime();
    }
    
    public static Date addMonth(String inDate, int month) {
    	return addMonth(parseDate(inDate), month);
    }
    
    /**
     * 将输入的日期增加指定的日
     * @param inDate
     * @param day
     * @return
     */
    public static Date addDay(Date inDate, int day) {
    	Calendar calendar = new GregorianCalendar();
    	calendar.setTime(inDate);
    	calendar.add(Calendar.DATE, day);
    	return calendar.getTime();
    }
    
    public static Date addDay(String inDate, int day) {
    	return addDay(parseDate(inDate), day);
    }
    
    /**
     * 将输入的日期增加指定单位的数量
     * @param unit
     * @param inDate
     * @param number
     * @return
     */
    public static Date addDate(char unit, Date inDate, int number) {
    	Calendar calendar = new GregorianCalendar();
    	calendar.setTime(inDate);
    	int field = -1;
    	unit = Character.toUpperCase(unit);
    	switch(unit) {
    		case 'D':
    			field = Calendar.DATE;
    			break;
    		case 'M':
    			field = Calendar.MONTH;
    			break;
    		case 'Y':
    			field = Calendar.YEAR;
    			break;
    		default:
    			break;
    	}
    	if (field == -1) {
    		throw new IllegalArgumentException("无效的单位！");
    	} else {
    		calendar.add(field, number);
    	}
    	return calendar.getTime();
    }
    
    public static Date addDate(char unit, String inDate, int number) {
    	return addDate(unit, parseDate(inDate), number);
    }
    
    /**
     * 比较两个日期相差的数量，当前只实现了计算天数，testDate - refDate = result
     * @param unit 'D' 天数
     * @param testDate
     * @param refDate
     * @return
     */
    public static int dateDiff(char unit, Date testDate, Date refDate) {
    	long testDateMillis = testDate.getTime();
    	long refDateMillis = refDate.getTime();
    	unit = Character.toUpperCase(unit);
    	if (unit != 'D') {
    		throw new IllegalArgumentException("无效的单位，当前只完成了 'D' (天数) 的计算!");
    	} else {
    		return (int) ( (testDateMillis - refDateMillis) / 1000L / 60L / 60L / 24L);
    	}
    	/*@todo,如果要完成其它的写在这里*/
    }
    
    public static int dateDiff(char unit, Date testDate, String refDate) {
    	return dateDiff(unit, testDate, parseDate(refDate));
    }
    public static int dateDiff(char unit, String testDate, Date refDate) {
    	return dateDiff(unit, parseDate(testDate), refDate);
    }
    public static int dateDiff(char unit, String testDate, String refDate) {
    	return dateDiff(unit, parseDate(testDate), parseDate(refDate));
    }
    
    /**
     * 将日期格式化成 yyyy-MM-dd 的形式
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
    	if (date == null) {
    		return "";
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    	return sdf.format(date);
    }
    public static String formatDateHasTime(Date date) {
    	if (date == null) {
    		return "";
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    	return sdf.format(date);
    }
    public static String formatDate(String date) {
    	return formatDate(parseDate(date));
    }
    public static String formatDate(Date date, String pattern) {
    	if (date == null) {
    		return "";
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
    	return sdf.format(date);
    }
    
    /**
     * 比较两个日期，不包括时间部分
     * @param compareDate
     * @param compareTo
     * @return  比较结果 -1 compareDate小于compareTo,
     *                    0 compareDate 等于 compareTo,
     *                    1 大于 compareDate 大于 compareTo
     */
    public static int compareDate(Date compareDate,Date compareTo){
	   int difVal = dateDiff('D',compareDate,compareTo) ;
	   int retVal = 0;
	   if ( difVal < 0 ){
	       retVal =  -1;
	   }else if( difVal == 0){
	       retVal =  0;
	   }if ( difVal > 0 ){
	       retVal =  1;
	   }
	   return retVal;
    }
    public static int compareDate(String compareDate,String compareTo){
        return compareDate(parseDate(compareDate),parseDate(compareTo));
    }
    public static int compareDate(Date compareDate,String compareTo){
   		return compareDate(compareDate,parseDate(compareTo));
    }
    public static int compareDate(String compareDate,Date compareTo){
    	return compareDate(parseDate(compareDate),compareTo);
    }
    
    /**
     * 返回当前时间的字符串
     * @return
     */
    public static String currentDate() {
    	GregorianCalendar calenda = new GregorianCalendar();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	return sdf.format(calenda.getTime());
    }
    
    public static String currentDate(String pattern) {
    	GregorianCalendar calenda = new GregorianCalendar();
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    	return sdf.format(calenda.getTime());
    }
        
    public static String currentDate_No_Time() {
    	GregorianCalendar calenda = new GregorianCalendar();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	return sdf.format(calenda.getTime());
    }
    
    public static String currentDate_Week_Monday() {
    	GregorianCalendar calenda = new GregorianCalendar();
    	calenda.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	return sdf.format(calenda.getTime());
    }
    public static String currentDate_Week_Sunday() {
    	GregorianCalendar calenda = new GregorianCalendar();
    	calenda.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	calenda.add(java.util.Calendar.DATE, 7);
    	return sdf.format(calenda.getTime());
    }
    public static String currentDate_No_Split() {
    	GregorianCalendar calenda = new GregorianCalendar();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	return sdf.format(calenda.getTime());
    }
    public static String currentDate_No_Split_Current_Hour() {
    	GregorianCalendar calenda = new GregorianCalendar();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
    	return sdf.format(calenda.getTime());
    }
    public static String currentDate_No_Split_Last_Hour() {
    	GregorianCalendar calenda = new GregorianCalendar();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
    	calenda.add(java.util.Calendar.HOUR, -1);
    	return sdf.format(calenda.getTime());
    }
    public static String formatCurrentDate(String date) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
    	return sdf.format(parseDate(date));
    }
    
    /**
     * 返回前一天（昨天）时间的字符串
     * @return
     */
    public static String yesterDate() {
    	GregorianCalendar calenda = new GregorianCalendar();
    	calenda.add(java.util.Calendar.DATE, -1);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	return sdf.format(calenda.getTime());
    }
    public static String yesterDate(String pattern) {
    	GregorianCalendar calenda = new GregorianCalendar();
    	calenda.add(java.util.Calendar.DATE, -1);
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    	return sdf.format(calenda.getTime());
    }
    public static String yesterDate_No_Time() {
    	GregorianCalendar calenda = new GregorianCalendar();
    	calenda.add(java.util.Calendar.DATE, -1);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	return sdf.format(calenda.getTime());
    }
    public static String yesterDate_No_Split() {
    	GregorianCalendar calenda = new GregorianCalendar();
    	calenda.add(java.util.Calendar.DATE, -1);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	return sdf.format(calenda.getTime());
    }
}
