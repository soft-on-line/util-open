package org.open.crawler.nekohtml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.open.crawler.util.URLUtil;
import org.open.util.StringUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NodeUtil {

    private static final Log log = LogFactory.getLog(NodeUtil.class);

    public static boolean isParent(Node child, Node parent) {
        Node tmp = child;
        while (null != (tmp = tmp.getParentNode())) {
            if (tmp.isSameNode(parent)) {
                return true;
            }
            if ("body".equalsIgnoreCase(tmp.getNodeName())) {
                return false;
            }
        }
        return false;
    }

    public static String generateHtml(Node node) {
        StringBuilder outHtml = new StringBuilder();
        generateHtml(node, null, null, null, outHtml);
        return outHtml.toString();
    }

    public static String generateHtml(Node node, String srcURL) {
        StringBuilder outHtml = new StringBuilder();
        generateHtml(node, null, null, srcURL, outHtml);
        return outHtml.toString();
    }

    public static String generateHtml(Node node, StringBuilder outHtml) {
        generateHtml(node, null, null, null, outHtml);
        return outHtml.toString();
    }

    public static void generateHtml(Node node, String srcURL, StringBuilder outHtml) {
        generateHtml(node, null, null, srcURL, outHtml);
    }

    public static String generateHtml(Node node, String[] skipNodes, String srcURL) {
        StringBuilder outHtml = new StringBuilder();
        generateHtml(node, skipNodes, null, srcURL, outHtml);
        return outHtml.toString();
    }

    public static String generateHtml(Node node, String[] skipNodes, Node[] skipSubNodes, String srcURL) {
        StringBuilder outHtml = new StringBuilder();
        generateHtml(node, skipNodes, skipSubNodes, srcURL, outHtml);
        return outHtml.toString();
    }

    public static void generateHtml(Node node, String[] skipNodes, String srcURL, StringBuilder outHtml) {
        generateHtml(node, skipNodes, null, srcURL, outHtml);
    }

//    public static void generateHtml(Node node, String[] skipNodes, Node[] skipSubNodes, String srcURL,
//                                    StringBuilder outHtml) {
//        _generateHtml(node, skipNodes, skipSubNodes, srcURL, outHtml);
//    }

    private static boolean _inSkipNodes(String[] skipNodes, Node node) {
        if (null == skipNodes || skipNodes.length < 1) {
            return false;
        } else {
            for (String each : skipNodes) {
                if (node.getNodeName().equalsIgnoreCase(each)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static boolean _inSkipNodes(Node[] skipNodes, Node node) {
        if (null == skipNodes || skipNodes.length < 1) {
            return false;
        } else {
            for (Node each : skipNodes) {
                if (node.equals(each)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static void generateHtml(Node node, String[] skipNodesByName, Node[] skipSubNodes, String srcURL,
                                      StringBuilder outHtml) {
        if (_inSkipNodes(skipNodesByName, node)) {
            log.debug("SkipNode(by node name):" + node);
            return;
        }

        if (_inSkipNodes(skipSubNodes, node)) {
            log.debug("SkipNode(by node):" + node);
            return;
        }

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

            NamedNodeMap attrs = element.getAttributes();
            if (attrs != null) {
                int length = attrs.getLength();
                for (int i = 0; i < length; i++) {
                    Node attr = attrs.item(i);
                    outHtml.append(" " + attr.getNodeName());

                    String nodeValue = attr.getNodeValue();
                    // 如果有原始地址传入
                    if (null != srcURL) {
                        // 如果是链接href类型、img图片类型,则拼装全url路径地址
                        if (("a".equalsIgnoreCase(element.getTagName()) && "href".equalsIgnoreCase(attr.getNodeName()))
                            || ("img".equalsIgnoreCase(element.getTagName()) && "src".equalsIgnoreCase(attr.getNodeName()))) {

                            try {
                                nodeValue = URLUtil.canonicalURL(srcURL, attr.getNodeValue());
                            } catch (Exception e) {
                                nodeValue = attr.getNodeValue();
                            }

                        }
                    }
                    outHtml.append("=\"" + nodeValue + "\" ");
                }
            }
            outHtml.append(">");
            Node child = node.getFirstChild();
            while (child != null) {
                generateHtml(child, skipNodesByName, skipSubNodes, srcURL, outHtml);
                child = child.getNextSibling();
            }
            if (!element.getTagName().equals("BR")) {
                outHtml.append(" </" + element.getTagName() + ">");
            }
        } else if (node.getNodeType() == Node.TEXT_NODE) {
            outHtml.append(node.getTextContent());
        }
    }
}
