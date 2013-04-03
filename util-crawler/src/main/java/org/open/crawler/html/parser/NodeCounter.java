package org.open.crawler.html.parser;

import org.open.crawler.nekohtml.NodeUtil;
import org.w3c.dom.Node;

/**
 * 友好类，仅包内可见
 *
 * @author Qin Zhipeng 2010-09-17 22:13
 */
class NodeCounter {

    /**
     * Document node.
     */
    Node node;

    /**
     * node position do document.
     */
    int  index;

    /**
     * node inner text length.(with trim)
     */
    int  htmlTextNodeCount;

    /**
     * a href node innner text length.(with trim)
     */
    int  htmlATextNodeCount;

    /**
     * a href node count.
     */
    int  htmlANodeCount;

    /**
     * input node count.(contain all input type)
     */
    int  htmlInputNodeCount;

    /**
     * img node count.
     */
    int  htmlImgNodeCount;

    /**
     * script node count.
     */
    int  htmlScriptNodeCount;

    /**
     * script node inner text count.
     */
    int  htmlScriptNodeTextCount;

    /**
     * style node count;
     */
    int  htmlStyleNodeCount;

    /**
     * style node inner text count.
     */
    int  htmlStyleNodeTextCount;

    public NodeCounter(Node node) {
        this.node = node;
    }

    public void addNodeSocre(NodeCounter nodeCounter) {
        if (null == nodeCounter) {
            return;
        }
        this.htmlATextNodeCount += nodeCounter.htmlATextNodeCount;
        this.htmlTextNodeCount += nodeCounter.htmlTextNodeCount;
        this.htmlANodeCount += nodeCounter.htmlANodeCount;
        this.htmlInputNodeCount += nodeCounter.htmlInputNodeCount;
        this.htmlImgNodeCount += nodeCounter.htmlImgNodeCount;
    }

    public boolean isParent(NodeCounter child) {
        return NodeUtil.isParent(child.node, node);
    }

    public boolean isChild(NodeCounter parent) {
        return NodeUtil.isParent(node, parent.node);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NodeCounter [index=");
        builder.append(index);
        builder.append(", node=");
        builder.append(node);
        builder.append(", htmlANodeCount=");
        builder.append(htmlANodeCount);
        builder.append(", htmlATextNodeCount=");
        builder.append(htmlATextNodeCount);
        builder.append(", htmlImgNodeCount=");
        builder.append(htmlImgNodeCount);
        builder.append(", htmlInputNodeCount=");
        builder.append(htmlInputNodeCount);
        builder.append(", htmlScriptNodeCount=");
        builder.append(htmlScriptNodeCount);
        builder.append(", htmlScriptNodeTextCount=");
        builder.append(htmlScriptNodeTextCount);
        builder.append(", htmlStyleNodeCount=");
        builder.append(htmlStyleNodeCount);
        builder.append(", htmlStyleNodeTextCount=");
        builder.append(htmlStyleNodeTextCount);
        builder.append(", htmlTextNodeCount=");
        builder.append(htmlTextNodeCount);
        builder.append("]");
        return builder.toString();
    }

}
