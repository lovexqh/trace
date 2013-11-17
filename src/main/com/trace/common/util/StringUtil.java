package com.trace.common.util;

public class StringUtil {

	/**
	 * 获得混合中英文的字符串长度
	 * @param str String
	 * @return int
	 */
	public static int getStringLength(String str) {
		int len = 0;
		int temp_len = str.length();
		String singleStr = "";
		byte bStr = 0;
		for (int i = 0; i < temp_len; i++) {
			singleStr = str.substring(i, i + 1);
			bStr = singleStr.getBytes()[0];
			if (bStr < 0 || bStr > 255) {
				len = len + 2;
			} else {
				len = len + 1;
			}
		}
		return len;
	}

	/**
	 * 判断是否是中文字符
	 * @param c 字符
	 * @return
	 */
	public static boolean isChinese(char c) {
		if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
			// 字母, 数字
			return false;
		else if (Character.isLetter(c))
			return true;
		else
			return false;
	}

	/**
	 * 使HTML的标签失去作用
	 * @param input 被操作的字符串
	 * @return String
	 */
	public static final String escapeHTMLTag(String input) {
		if (input == null) {
			return "";
		}
		input = input.trim().replaceAll("&", "&amp;");
		input = input.trim().replaceAll("<", "&lt;");
		input = input.trim().replaceAll(">", "&gt;");
		input = input.trim().replaceAll("\t", "    ");
		input = input.trim().replaceAll("\r\n", "\n");
		input = input.trim().replaceAll("\n", "<br>");
		input = input.trim().replaceAll("  ", " &nbsp;");
		input = input.trim().replaceAll("'", "&#39;");
		input = input.trim().replaceAll("\\\\", "&#92;");
		return input;
	}

	public static String cleanHtmlTag(String htmlText) {
		String reg = "</?[a-z][a-z0-9]*[^<>]*>?";
		return htmlText.replaceAll(reg, "");
	}

	/**
	 *返回字符串trim.如果为空,返回""
	 *
	 * @author zhaolei
	 *
	 * 2010-3-26
	 *
	 * @param str
	 * @return
	 */
	public static String null2Trim(String str) {
		return str == null ? "" : str.trim();
	}
	
	public static String replaceXmlEntity(String xml){
    	xml = xml.replaceAll("&amp;", "&");
    	xml = xml.replaceAll("&quot;", "\"");
    	xml = xml.replaceAll("&gt;", ">");
    	xml = xml.replaceAll("&nbsp;", " ");
    	xml = xml.replaceAll("&apos;", "'");
    	return xml;
    }
}
