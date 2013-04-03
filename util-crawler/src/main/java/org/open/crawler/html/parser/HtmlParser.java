package org.open.crawler.html.parser;

import java.io.ByteArrayInputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cyberneko.html.parsers.DOMParser;
import org.open.util.BeanUtil;
import org.open.util.CodeUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * HTML解析类
 *
 * @author Qin Zhipeng
 * @date 2010-09-16 14:54
 */
public abstract class HtmlParser {

    private static final Log log = LogFactory.getLog(HtmlParser.class);
    /**
     * 原始url地址，用于后面拼装全链接地址以修复有些网站不是全路径url地址
     */
    protected String         url;
    /**
     * 原始url对应html源代码内容
     */
    protected String         html;
    protected DOMParser      dOMParser;

    /**
     * 构造函数：
     *
     * <pre>
     * 1、初始化DOMParser，产生DOM树；
     * </pre>
     *
     * <pre>
     * 2、递归DOM树并计算每个节点的分值；
     * </pre>
     *
     * @param html
     */
    public HtmlParser(String url, String html) {
        try {
            this.url = url;
            this.html = html;

            dOMParser = new DOMParser();

            dOMParser.parse(new InputSource(new ByteArrayInputStream(CodeUtil.non_ascii2number(this.html).getBytes())));

            recurrenceNode(dOMParser.getDocument());
        } catch (Exception e) {
            log.error(BeanUtil.getMethodName(e) + "() error!", e);
        }
    }

    // 节点及其节点统计器Map
    protected Map<Node, NodeCounter> nodeCounterMap     = new LinkedHashMap<Node, NodeCounter>();

    protected final static String[]  DefaultRemoveNodes = new String[] { "script", "noscript", "style" };

    protected String[]               removeNodes        = DefaultRemoveNodes;

    protected boolean isRemoveNode(Node node) {
        if (Node.COMMENT_NODE == node.getNodeType()) {
            return true;
        }

        if (null != removeNodes && removeNodes.length > 0) {
            for (int i = 0; i < removeNodes.length; i++) {
                if (removeNodes[i].equalsIgnoreCase(node.getNodeName())) {
                    return true;
                }
            }
        }

        return false;
    }

    protected NodeCounter recurrenceNode(Node node) {
        if (null == node || isRemoveNode(node)) {
            return null;
        }

        NodeCounter nodeScore = new NodeCounter(node);
        // record position of node in page.
        nodeScore.index = nodeCounterMap.size();

        nodeCounterMap.put(node, nodeScore);

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            // 统计子孩子节点递归分数
            nodeScore.addNodeSocre(recurrenceNode(nodeList.item(i)));
        }

        // 计算当前node个节点信息数
        countNode(node);

        return nodeScore;
    }

    /**
     * 计算节点分值的方法体
     *
     * @core
     * @param node
     */
    protected void countNode(Node node) {
        Node parentNode = node.getParentNode();
        NodeCounter nodeCounter = nodeCounterMap.get(parentNode);
        switch (node.getNodeType()) {
            case Node.TEXT_NODE:
                String nodeValue = node.getNodeValue().trim();
                boolean availableText = true;
                // find href from parent node.
                if ("a".equalsIgnoreCase(parentNode.getNodeName())) {
                    nodeCounter.htmlATextNodeCount += nodeValue.length();
                } else if ("script".equalsIgnoreCase(parentNode.getNodeName())) {
                    availableText = false;
                    nodeCounter.htmlScriptNodeTextCount += nodeValue.length();
                } else if ("style".equalsIgnoreCase(parentNode.getNodeName())) {
                    availableText = false;
                    nodeCounter.htmlStyleNodeTextCount += nodeValue.length();
                }
                // find recurrence.
                else {
                    Node tmpParentNode = parentNode.getParentNode();
                    NodeCounter tmpNodeCounter = nodeCounterMap.get(tmpParentNode);
                    while (tmpParentNode != null) {
                        if ("a".equalsIgnoreCase(tmpParentNode.getNodeName())) {
                            tmpNodeCounter.htmlATextNodeCount += nodeValue.length();
                        } else if ("script".equalsIgnoreCase(tmpParentNode.getNodeName())) {
                            availableText = false;
                            tmpNodeCounter.htmlScriptNodeTextCount += nodeValue.length();
                        } else if ("style".equalsIgnoreCase(tmpParentNode.getNodeName())) {
                            availableText = false;
                            tmpNodeCounter.htmlStyleNodeTextCount += nodeValue.length();
                        }

                        tmpParentNode = tmpParentNode.getParentNode();
                        tmpNodeCounter = nodeCounterMap.get(tmpParentNode);
                    }
                }
                // 有效文本状态，排除script，style代码文本
                if (availableText) {
                    nodeCounter.htmlTextNodeCount += nodeValue.length();
                }
                break;
            case Node.ELEMENT_NODE:
                if ("a".equalsIgnoreCase(node.getNodeName())) {
                    nodeCounter.htmlANodeCount += 1;
                } else if ("input".equalsIgnoreCase(node.getNodeName())) {
                    nodeCounter.htmlInputNodeCount += 1;
                } else if ("img".equalsIgnoreCase(node.getNodeName())) {
                    nodeCounter.htmlImgNodeCount += 1;
                } else if ("script".equalsIgnoreCase(node.getNodeName())) {
                    nodeCounter.htmlScriptNodeCount += 1;
                } else if ("style".equalsIgnoreCase(node.getNodeName())) {
                    nodeCounter.htmlStyleNodeCount += 1;
                }
                break;
            default:
                break;
        }
    }

    public String[] getRemoveNodes() {
        return removeNodes;
    }

    public void setRemoveNodes(String[] removeNodes) {
        this.removeNodes = removeNodes;
    }

}
