package org.open.crawler.html;


import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.open.crawler.cpdetector.CharCodeUtil;
import org.open.crawler.httpclient.Browser;
import org.open.crawler.util.URLUtil;
import org.open.util.CodeUtil;
import org.open.util.StringUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class HtmlNewsUtil {

	private static final Log log = LogFactory.getLog(HtmlNewsUtil.class);

	/**
	 * @param html
	 * @param url
	 * @return
	 */
	public static String[] getNews(String html, String url) {
		// modify by Qin Zhipeng 2010-09-20 16:25 start
		html = CodeUtil.non_ascii2number(html);
		// modify by Qin Zhipeng 2010-09-20 16:25 end

		String[] newsValue = new String[3];
		newsValue[0] = "";
		newsValue[1] = "";
		newsValue[2] = "";
		NewsParser newsParse = new NewsParser();
		String charset = CharCodeUtil.getCharSet(html);
		if (Charset.isSupported(charset)) {
			try {
				newsParse.setCharSet(charset);
			}
			catch (UnsupportedEncodingException e) {
				log.error(e.getMessage());
			}
		}
		if (newsParse.parseHtml(html)) {
			if (newsParse.getTitleNode() != null) {
				newsValue[0] = newsParse.getTitleNode().getTextContent();
			}

			if (newsParse.getTimeNode() != null) {
				newsValue[1] = NewsParser.newsDateTimeMatch(newsParse.getTimeNode().getTextContent());
			}

			if (newsParse.getBodyNode() != null) {
				// log.info(newsParse.getBodyNode().getTextContent());
				newsValue[2] = _getNodeHtml(newsParse.getBodyNode(), url);
			}

		} else {
			log.info("parse fail maybe the input is not news");
		}
		return newsValue;
	}

	// public static String[] getNews(byte[] stream, String charSet)
	// {
	// String[] newsValue = new String[3];
	// NewsParser newsParse = new NewsParser();
	// newsParse.setCharSet(charSet);
	// if (newsParse.parseHtml(stream))
	// {
	// newsValue[0] = newsParse.getTitle();
	// newsValue[1] = newsParse.getTime();
	// newsValue[2] = newsParse.getBody();
	// }
	// else
	// {
	// log.info("parse fail maybe the input is not news");
	// }
	// return newsValue;
	// }

	/*******************************************************************************************************************
	 * 将Node结点转化成HTML文本
	 */
	private static void _dealNode(Node node, String originalurl, StringBuffer html) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {

			Element element = (Element) node;
			html.append("<" + element.getTagName());

			if (element.getTagName().equalsIgnoreCase("IMG")) {
				String src = element.getAttribute("src");
				try {
					src = URLUtil.canonicalURL(originalurl, src);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				element.setAttribute("src", src);
				element.setAttribute("alt", "");
			}
			if (!StringUtil.isEmpty(element.getAttribute("id"))) {
				element.removeAttribute("id");
			}
			if (!StringUtil.isEmpty(element.getAttribute("class"))) {
				element.removeAttribute("class");
			}

			NamedNodeMap attrs = element.getAttributes();
			if (attrs != null) {
				int length = attrs.getLength();
				for (int i = 0; i < length; i++) {
					Node attr = attrs.item(i);
					html.append(" " + attr.getNodeName());
					html.append("=\"" + attr.getNodeValue() + "\" ");
				}
			}
			html.append(">");
			Node child = node.getFirstChild();
			while (child != null) {
				_dealNode(child, originalurl, html);
				child = child.getNextSibling();
			}
			if (!element.getTagName().equals("BR")) {
				html.append(" </" + element.getTagName() + ">");
			}
		} else if (node.getNodeType() == Node.TEXT_NODE) {
			html.append(node.getTextContent());
		}
	}

	private static String _cutUnuseTags(String html, String[] tags) {
		String result = html;
		for (String tag : tags) {
			result = HtmlDataUtil.cutHtmlTag(result, tag);
		}
		return result;
	}

	private static String _cutUnuseTagsAndInnerHtml(String html, String[] tags) {
		String result = html;
		for (String tag : tags) {
			result = HtmlDataUtil.cutHtmlNode(html, tag);
		}
		return result;
	}

	/**
	 * @param node
	 * @param originalurl
	 * @return
	 */
	private static String _getNodeHtml(Node node, String originalurl) {
		StringBuffer nodeHtml = new StringBuffer();
		_dealNode(node, originalurl, nodeHtml);

		// ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
		// StreamResult streamresult = new StreamResult(outputstream);
		// try {
		// DOMSource source = new DOMSource(node);
		//
		// TransformerFactory.newInstance().newTransformer().transform(source, streamresult);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// String result ="";
		// try {
		// result = outputstream.toString("utf-8"); //默认
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		String result = nodeHtml.toString();
		// result = result.replaceAll("<\\?xml.*?\\?>", "");
		String[] tags = { "A", "IFRAME" };
		String[] cutContentTags = { "OBJECT" };
		result = _cutUnuseTags(result, tags);
		result = _cutUnuseTagsAndInnerHtml(result, cutContentTags);
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		// list.add("http://finance.eastmoney.com/100127,1292947.html");
		// list.add("http://www.shaoxing.com.cn/news/content/2010-04/01/content_479485.htm");
		list.add("http://gongyi.qq.com/a/20100430/000037.htm");
		list.add("http://hi.cenn.cn/info/shownews.asp?newsid=56980");
		list.add("http://news.szchehang.cn/html/qcwh/2010042241527572505.html");
		list.add("http://www.cenn.cn/News/2008-7/82424_2008711154656.shtml");
		for (String url : list) {
			Browser browser = new Browser();
			try {
				browser.getConnection(url);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			// browser.getHTML();
			//            String html = browser.getHTML();
			// log.info(browser.getCharSet());
			log.info(HtmlNewsUtil.getNews(browser.getHTML(), url));
		}
		// log.info(HtmlNewsUtil.getNews(html));
	}

}
