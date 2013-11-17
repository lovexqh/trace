package com.trace.crawl.spider;

import java.util.HashSet;
import java.util.Set;

import org.htmlparser.Parser;
import org.htmlparser.Remark;
import org.htmlparser.Tag;
import org.htmlparser.Text;
import org.htmlparser.visitors.NodeVisitor;

import com.trace.net.http.HtmlParserUtils;

public class LinkVisitor extends NodeVisitor {
	// 记录Remark Node数量
	private int remark_node_count;
	// 记录Text Node数量
	private int tag_node_count;
	// 记录Tag Node数量
	private int text_node_count;

	private Set<String> links = new HashSet<String>();

	public int getRemark_node_count() {
		return remark_node_count;
	}

	public void setRemark_node_count(int remarkNodeCount) {
		remark_node_count = remarkNodeCount;
	}

	public int getTag_node_count() {
		return tag_node_count;
	}

	public void setTag_node_count(int tagNodeCount) {
		tag_node_count = tagNodeCount;
	}

	public int getText_node_count() {
		return text_node_count;
	}

	public void setText_node_count(int textNodeCount) {
		text_node_count = textNodeCount;
	}

	public Set<String> getLinks() {
		return links;
	}

	public void setLinks(Set<String> links) {
		this.links = links;
	}

	public void visitRemarkNode(Remark remark) {
		// System.out.println("正在访问第 "+(++remark_node_count)+" 个Remark Node ");
	}

	public void visitStringNode(Text text) {
		// System.out.println("正在访问第 "+(++tag_node_count)+" 个Text Node ");
	}

	public void visitTag(Tag tag) {
		// System.out.println("正在访问第 "+(++text_node_count)+" 个Tag Node ");

		String src = tag.getAttribute("src");
		String href = tag.getAttribute("href");
//		String regex = "^(http|ftp|file)://.*jpg$";
		if (src != null) {
			links.add(src);
		}
		if (href != null)
			links.add(href);

	}

	public static void main(String[] args) {
		try {
			// 方式一：
			String urlStr = "http://www.qq.com";
			Parser parser = HtmlParserUtils.getParserWithUrlConn(urlStr, "GBK",
					"172.17.18.80", 8080);

			LinkVisitor visitor = new LinkVisitor();
			parser.visitAllNodesWith(visitor);

			System.out.println("=========================================");
			// 方式二（常用）：
			// parser.reset();
			// Set<String> links = new HashSet<String>();
			// NodeVisitor visitor2 = new NodeVisitor() {
			//                        	
			// public void visitTag(Tag tag) {
			// System.out.println("正在访问的tag：" + tag.getTagName());
			// System.out.println("==============================");
			// System.out.println("正在访问的href：" + tag.getAttribute("href"));
			// System.out.println("==============================");
			// System.out.println("正在访问的src：" + tag.getAttribute("src"));
			// System.out.println("==============================");
			// System.out.println("正在访问的tag：" + tag.getText());
			// System.out.println("==============================");
			// }

			// };
			parser.visitAllNodesWith(visitor);
			// Iterator<String> i = visitor.links.iterator();
			// while (i.hasNext()) {
			// System.out.println(i.next());
			// ;
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}