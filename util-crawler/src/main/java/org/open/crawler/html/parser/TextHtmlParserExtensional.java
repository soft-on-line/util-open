package org.open.crawler.html.parser;

import org.open.crawler.nekohtml.NodeUtil;
import org.open.util.StringUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * TextHtmlParser延伸的工具类
 *
 * @author Qin Zhipeng
 */
public class TextHtmlParserExtensional extends TextHtmlParser {

    public TextHtmlParserExtensional(String html) {
        super(null, html);
    }

    private static void firstNodeGenerateHtml(Node node, String style, String styleClassName, StringBuilder outHtml) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            outHtml.append("<" + element.getTagName());

            // 移除原始网页的ID属性标识
            if (!StringUtil.isEmpty(element.getAttribute("id"))) {
                element.removeAttribute("id");
            }
            // 移除原始网页的class样式属性标识
            if (!StringUtil.isEmpty(element.getAttribute("class"))) {
                element.removeAttribute("class");
            }
            // 移除原始网页的class样式属性标识
            if (!StringUtil.isEmpty(element.getAttribute("style"))) {
                element.removeAttribute("style");
            }

            NamedNodeMap attrs = element.getAttributes();
            if (attrs != null) {
                int length = attrs.getLength();
                for (int i = 0; i < length; i++) {
                    Node attr = attrs.item(i);
                    outHtml.append(" " + attr.getNodeName()).append("=\"" + attr.getNodeValue() + "\" ");
                }
            }
            if (null != style) {
                outHtml.append(" style=\"").append(new String(style)).append("\"");
            }
            if (null != styleClassName) {
                outHtml.append(" class=\"").append(new String(styleClassName)).append("\"");
            }
            outHtml.append(">");
            Node child = node.getFirstChild();
            while (child != null) {
                NodeUtil.generateHtml(child, outHtml);
                child = child.getNextSibling();
            }
            if (!element.getTagName().equals("BR")) {
                outHtml.append(" </" + element.getTagName() + ">");
            }
        } else if (node.getNodeType() == Node.TEXT_NODE) {
            outHtml.append(node.getTextContent());
        }
    }

    /**
     * <pre>
     * 给该构造函数传入的html片段第一级文本节点（主要包括body，table，div）按奇偶增加不同样式
     * 主要目的：让不同奇偶文本节点之间样式分明，更清楚明了层次关系，主要美化前台页面显示。
     * </pre>
     *
     * @param oddStyle 奇文本节点样式
     * @param evenStyle 偶文本节点样式
     * @return 扩展修正的html片段代码
     */
    public String extendFirstTextNodesStyleForOdd_Even(String oddStyle, String evenStyle) {

        StringBuilder html = new StringBuilder();
        NodeList nodeList = dOMParser.getDocument().getElementsByTagName("body").item(0).getChildNodes();
        int odd_or_even = -1;
        for (int i = 0; i < nodeList.getLength(); i++) {
            StringBuilder each = new StringBuilder();
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                odd_or_even = -odd_or_even;
            }
            firstNodeGenerateHtml(nodeList.item(i), ((odd_or_even > 0) ? evenStyle : oddStyle), null, each);
            html.append(each);
        }
        return html.toString();
    }

    /**
     * <pre>
     * 给该构造函数传入的html片段第一级文本节点（主要包括body，table，div）按奇偶增加不同样式
     * 主要目的：让不同奇偶文本节点之间样式分明，更清楚明了层次关系，主要美化前台页面显示。
     * </pre>
     *
     * @param oddStyle 奇文本节点样式
     * @param evenStyle 偶文本节点样式
     * @return 扩展修正的html片段代码
     */
    public String extendFirstTextNodesStyleClassNameForOdd_Even(String oddStyleClassName, String evenStyleClassName) {

        StringBuilder html = new StringBuilder();
        NodeList nodeList = dOMParser.getDocument().getElementsByTagName("body").item(0).getChildNodes();
        int odd_or_even = -1;
        for (int i = 0; i < nodeList.getLength(); i++) {
            StringBuilder each = new StringBuilder();
            if (nodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                odd_or_even = -odd_or_even;
            }
            firstNodeGenerateHtml(nodeList.item(i), null, ((odd_or_even > 0) ? evenStyleClassName : oddStyleClassName),
                                  each);
            html.append(each);
        }
        return html.toString();
    }

    /**
     * <pre>
     * 给该构造函数传入的html片段第一级文本节点（主要包括body，table，div）前后分别添加代码
     * 主要目的：可以为第一层txt容器包含样式
     * </pre>
     * @param preAppend
     * @param sufAppend
     * @return
     */
    public String extendFirstTextNodesWrapped(String preAppend, String sufAppend) {
        StringBuilder html = new StringBuilder();
        NodeList nodeList = dOMParser.getDocument().getElementsByTagName("body").item(0).getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeType() == Node.TEXT_NODE) {
                if (StringUtil.isEmpty(nodeList.item(i).getNodeValue())) {
                    continue;
                }
            }
            html.append(preAppend);
            NodeUtil.generateHtml(nodeList.item(i), html);
            html.append(sufAppend);
        }
        return html.toString();
    }

}
