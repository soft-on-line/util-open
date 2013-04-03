package org.open.crawler.nekohtml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NekoHtmlUtil {

    /**
     * 去除文本结点的非法字符
     * 
     * @param node
     * @return
     */
    public static String codeHandle(Node node) {
        String rs = "";
        if (node.getNodeType() != Node.TEXT_NODE) {
            return null;
        }
        try {
            byte[] b = node.getNodeValue().trim().getBytes();
            if (b.length > 0) {
                for (int i = 0; i < b.length; i++) {
                    if (((int) 0x00ff & b[i]) == 63) b[i] = '\u0020';
                }
            }
            rs = new String(b).trim();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return rs;
        // 最后我在nekohtml.jar中找到了它使用的资源文件HTMLlat1.properties，在其中加入了一行：nbsp=\u00a0，问题就解决了。
    }

    /**
     *获取结点的第一个文本子结点
     * 
     * @param node
     * @return
     */

    public static Node getFristTextNode(Node node) {
        if (node == null) {
            return null;
        }
        if (node.getNodeType() == Node.TEXT_NODE) {
            if (node.getNodeValue().trim().length() > 2) {
                return node;
            }
        }
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node fristNode = getFristTextNode(children.item(i));
            if (fristNode != null) {
                return fristNode;
            }

        }
        return null;
    }

    /**
     * 获取最后一个文本类型节点
     * 
     * @param node
     * @return
     */
    public static Node getLastTextNode(Node node) {
        if (node == null) {
            return null;
        }
        if (node.getNodeType() == Node.TEXT_NODE) {
            if (node.getNodeValue().trim().length() > 2) {
                return node;
            }
        }
        NodeList children = node.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node lastNode = getLastTextNode(children.item(i));
            if (lastNode != null) {
                return lastNode;
            }

        }
        return null;
    }

}
