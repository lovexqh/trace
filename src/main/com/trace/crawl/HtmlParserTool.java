package com.trace.crawl;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.http.ConnectionManager;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;



public class HtmlParserTool {
	private static Log logger = LogFactory.getLog(HtmlParserTool.class);

	// 获取一个网站上的链接，filter 用来过滤链接
	public static Set<String> extracLinks(String url, LinkFilter filter,String enCoding) {
		Set<String> links = new HashSet<String>();
		try {
			ConnectionManager manager = new ConnectionManager();
			manager.setProxyHost("172.17.18.80");
			manager.setProxyPort(8080);
			Parser.setConnectionManager(manager);
			Parser parser = new Parser(url);
			parser.setEncoding(enCoding);
			// 过滤 <frame >标签的 filter，用来提取frame 标签里的 src 属性
			NodeFilter frameFilter = new NodeFilter() {
				public boolean accept(Node node) {
					if (node.getText().startsWith("frame src=")) {
						return true;
					} else {
						return false;
					}
				};
			};
			// OrFilter 来设置过滤 <a> 标签和 <frame> 标签
			OrFilter linkFilter = new OrFilter(new NodeClassFilter(
					LinkTag.class), frameFilter);
			// 得到所有经过过滤的标签
			NodeList list = parser.extractAllNodesThatMatch(linkFilter);
			for (int i = 0; i < list.size(); i++) {
				Node tag = list.elementAt(i);
				if (tag instanceof LinkTag)// <a> 标签
				{
					LinkTag link = (LinkTag) tag;
					String linkUrl = link.getLink();// URL
					if (filter.accept(linkUrl))
						links.add(linkUrl);
				} else// <frame> 标签
				{
					// 提取 frame 里 src 属性的链接，如 <frame src="test.html"/>
					String frame = tag.getText();
					int start = frame.indexOf("src=");
					frame = frame.substring(start);
					int end = frame.indexOf(" ");
					if (end == -1)
						end = frame.indexOf(">");
					String frameUrl = frame.substring(5, end - 1);
					if (filter.accept(frameUrl))
						links.add(frameUrl);
				}
			}
		} catch (ParserException e) {
			logger.error(e.getStackTrace());
		}
		return links;
	}
}