package org.open.crawler.html.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.open.crawler.nekohtml.NodeUtil;
import org.open.mining.NumberCluster;
import org.open.util.BeanUtil;
import org.open.util.StringUtil;
import org.w3c.dom.Node;

/**
 * 一般页面解析
 *
 * @author Qin Zhipeng
 * @date 2010-09-20 09:12
 */
public class TextHtmlParser extends HtmlParser {

    private static final Log log = LogFactory.getLog(TextHtmlParser.class);

    /**
     * @see HtmlParser#HtmlParser(String)
     * @param html
     */
    public TextHtmlParser(String url, String html) {
        super(url, html);
    }

    /**
     * 是否listNodeScore队列其中一个的父亲节点，并返回匹配到的孩子节点
     *
     * @param parentNodeScore
     * @param listNodeScore
     * @return
     */
    private static TextNodeScore isOneParentOfList(TextNodeScore parentNodeScore, List<TextNodeScore> listNodeScore) {
        for (TextNodeScore each : listNodeScore) {
            if (NodeUtil.isParent(each.nodeCounter.node, parentNodeScore.nodeCounter.node)) {
                return each;
            }
        }
        return null;
    }

    /**
     * 是否listNodeScore队列中其中一个的孩子节点，并返回匹配到父亲节点
     *
     * @param childNodeSocre
     * @param listNodeScore
     * @return
     */
    private static TextNodeScore isOneChildOfList(TextNodeScore childNodeSocre, List<TextNodeScore> listNodeScore) {
        for (TextNodeScore each : listNodeScore) {
            if (NodeUtil.isParent(childNodeSocre.nodeCounter.node, each.nodeCounter.node)) {
                return each;
            }
        }
        return null;
    }

    public final static String[] DefaultTextNodes = new String[] { "body", "table", "div" };

    /**
     * 过滤默认 文本（Text）容器节点
     *
     * @return
     */
    private List<NodeCounter> filterTextNode() {
        if (null == DefaultTextNodes || DefaultTextNodes.length < 1) {
            return new LinkedList<NodeCounter>(nodeCounterMap.values());
        }

        List<NodeCounter> filtedNodeScore = new ArrayList<NodeCounter>();
        Iterator<NodeCounter> values = nodeCounterMap.values().iterator();
        while (values.hasNext()) {
            NodeCounter nodeScore = values.next();
            for (String nodeName : DefaultTextNodes) {
                if (nodeScore.node.getNodeName().equalsIgnoreCase(nodeName)) {
                    filtedNodeScore.add(nodeScore);
                    break;
                }
            }
        }

        return filtedNodeScore;
    }

    private static void debug(TextNodeScore textNodeScore, int index) {
        log.debug("filtedGeneralNodeScore[" + (++index) + "]=>" + textNodeScore);
        log.debug("filtedGeneralNode[" + (index) + "]=>"
                  + StringUtil.tidyStrictly(NodeUtil.generateHtml(textNodeScore.nodeCounter.node)));
    }

    /**
     * 计算并返回分析的文本段容器节点
     *
     * <pre>
     * 1、一维下标为核心文本段容器；
     * </pre>
     *
     * <pre>
     * 2、二维下标为最差文本段容器；
     * </pre>
     * <p>
     * <strong>提示1</strong>：如果直接调用该方法体， 不需要过滤核心文本段中可能包括的最差容器节点，直接取一维数组即可， 如果需要过滤最差文本则需循环递归删除二维下标中包含的节点；
     * </p>
     * <p>
     * <strong>提示2</strong>:{@link #getDensityTextNodesHtml()}该方法体是默认剔除了最差文本容器节点；
     * </p>
     * <p>
     * 名词解释：<strong>"最差文本"</strong>，这里是指相应文本容器段里a href链接比重高的片段（该类似段多半为广告或其他链接性主题文本）。
     * </p>
     *
     * @return 二维node数组
     */
    public Node[][] getDensityTextNodes() {
        try {
            // filter container
            List<TextNodeScore> filtedGeneralNodeScore = NodeScoreFactory.buildGeneralNodeScore(filterTextNode());

            // sort by text available weight desc.
            Collections.sort(filtedGeneralNodeScore);

            // for debug
            if (log.isDebugEnabled()) {
                int index = 0;
                log.debug("<<<<<<<<<<<<<<<<<<<<<<<<<");
                for (TextNodeScore generalNodeScore : filtedGeneralNodeScore) {
                    debug(generalNodeScore, (index++));
                }
                log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>");
            }

            // for calculate average weight.
            // float sumWeight = 0;
            // for (TextNodeScore generalNodeScore : filtedGeneralNodeScore) {
            // sumWeight += generalNodeScore.calcWeight();
            // }
            // float averageWeight = sumWeight / filtedGeneralNodeScore.size();
            //
            // log.debug("averageWeight=>" + averageWeight);

            List<Double> listWeight = BeanUtil.getProperty(filtedGeneralNodeScore, "weight");

            log.debug("listWeight=>" + listWeight);

            // calculate boundary weight.
            Number[][] cluster = new NumberCluster(listWeight).getCluster();

            double topmostBoundaryWeight = cluster[2][0].doubleValue();
            double lowestBoundaryWeight = cluster[0][cluster[0].length - 1].doubleValue();

            log.debug("topmostBoundaryWeight=>" + topmostBoundaryWeight);
            log.debug("lowestBoundaryWeight=>" + lowestBoundaryWeight);

            // filter topmost weight nodes.
            List<TextNodeScore> topWeightNodeScore = new LinkedList<TextNodeScore>();
            // filter lowest weight nodes.
            List<TextNodeScore> lowWeightNodeScore = new LinkedList<TextNodeScore>();
            for (int i = 0; i < filtedGeneralNodeScore.size(); i++) {
                TextNodeScore generalNodeScore = filtedGeneralNodeScore.get(i);
                // filter topmost nodes.
                if (generalNodeScore.getWeight() >= topmostBoundaryWeight) {
                    // top队列优先父亲节点
                    log.debug("Topmost[" + i + "]=>" + generalNodeScore);
                    TextNodeScore child = isOneParentOfList(generalNodeScore, topWeightNodeScore);
                    if (null != child) {
                        // remove child and add parent then return.
                        if (log.isDebugEnabled()) {
                            debug(child, 0);
                            debug(generalNodeScore, 0);
                            log.debug(NodeUtil.isParent(child.nodeCounter.node, generalNodeScore.nodeCounter.node));
                            log.debug(NodeUtil.isParent(generalNodeScore.nodeCounter.node, child.nodeCounter.node));
                        }
                        topWeightNodeScore.remove(child);
                        topWeightNodeScore.add(generalNodeScore);
                    } else {
                        if (null == isOneChildOfList(generalNodeScore, topWeightNodeScore)) {
                            topWeightNodeScore.add(generalNodeScore);
                        }
                    }
                    // for debug
                    if (log.isDebugEnabled()) {
                        int index = 0;
                        log.debug("<<<<<<<<<<<<<<<<<<<<<<<<<");
                        for (TextNodeScore each : topWeightNodeScore) {
                            debug(each, (index++));
                        }
                        log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>");
                    }
                }
                // filter lowest nodes.
                if (generalNodeScore.getWeight() <= lowestBoundaryWeight || generalNodeScore.getWeight() == 0.0f) {
                    // low队列优先存储孩子节点
                    log.debug("Lowest[" + i + "]=>" + generalNodeScore);
                    TextNodeScore parent = isOneChildOfList(generalNodeScore, lowWeightNodeScore);
                    if (null != parent) {
                        // remove child and add parent then return.
                        lowWeightNodeScore.remove(parent);
                        lowWeightNodeScore.add(generalNodeScore);
                    } else {
                        if (null == isOneParentOfList(generalNodeScore, lowWeightNodeScore)) {
                            lowWeightNodeScore.add(generalNodeScore);
                        }
                    }
                }
            }

            // for debug
            if (log.isDebugEnabled()) {
                int index = 0;
                log.debug("<<<<<<<<<<<<<<<<<<<<<<<<<");
                for (TextNodeScore each : topWeightNodeScore) {
                    debug(each, (index++));
                }
                log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>");
            }

            // order by position of page(html).
            Collections.sort(topWeightNodeScore, new Comparator<TextNodeScore>() {

                @Override
                public int compare(TextNodeScore o1, TextNodeScore o2) {
                    return o1.nodeCounter.index - o2.nodeCounter.index;
                }
            });

            // building topmost node arrays.
            Node[] topmostNodes = new Node[topWeightNodeScore.size()];
            int index = 0;
            for (TextNodeScore topNodeScore : topWeightNodeScore) {
                topmostNodes[index++] = topNodeScore.nodeCounter.node;
                log.debug("TopmostNode:" + NodeUtil.generateHtml(topmostNodes[index - 1], url));
            }

            // building lowest node arrays.
            Node[] lowestNodes = new Node[lowWeightNodeScore.size()];
            index = 0;
            for (TextNodeScore lowNodeScore : lowWeightNodeScore) {
                lowestNodes[index++] = lowNodeScore.nodeCounter.node;
                log.debug("LowestNode:" + NodeUtil.generateHtml(lowestNodes[index - 1], url));
            }

            Node[][] out = new Node[2][];
            out[0] = topmostNodes;
            out[1] = lowestNodes;

            log.debug("topmostNodes Size:" + topmostNodes.length);
            log.debug("lowestNodes Size:" + lowestNodes.length);

            return out;
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            return null;
        }
    }

    /**
     * @see #getDensityTextNodes()
     * @return
     */
    public String getDensityTextNodesHtml() {
        StringBuilder html = new StringBuilder();
        Node[][] nodes = getDensityTextNodes();
        for (Node node : nodes[0]) {
            NodeUtil.generateHtml(node, removeNodes, nodes[1], url, html);
        }
        return html.toString();
    }
}
