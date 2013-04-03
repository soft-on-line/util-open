package org.open.crawler.html;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.cyberneko.html.parsers.DOMParser;
import org.open.crawler.httpclient.Browser;
import org.open.crawler.nekohtml.MdrElementRemover;
import org.open.crawler.nekohtml.NekoHtmlUtil;
import org.open.util.RegexpUtil;
import org.open.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author soft-on-line工作室
 * @version $Id:$
 */

public class NewsParser extends org.open.util.debug.Test {

    private static final Log      log                            = LogFactory.getLog(NewsParser.class);
    private List<Node>            textNodeList                   = new ArrayList();
    private Map<Node, TextConsum> containerMap                   = new HashMap();

    private String                newsTime                       = null;
    private String                newsTitle                      = null;
    private Node                  newsBodyNode                   = null;
    private Node                  newsTitleNode                  = null;
    private Node                  newsTimeNode                   = null;

    private Document              document                       = null;
    private int                   titleCheckPos                  = -1;
    private String                charSet                        = "";
    private String                orgHtml                        = "";

    private final int             NEWS_BODY_MIN_COMMON_TEXT_SUM  = 20;                                 // 新闻正文最少字数
    private final double          NEWS_BODY_MAX_LINKDEN_SITYRATE = 0.45;                               // 新闻正文最多包含链接比率
    private final int             NEWS_TITLE_MIN_SAME_CHAR       = 5;                                  // 新闻标题于title内容最少匹配字数
    private final int             NEWS_BODY_MAX_SERES_LINK_NODE  = 5;                                  // 新闻标题于title内容最少匹配字数
    private final int             NEWS_BODY_COPYRIGHT_POST       = 500;                                // 版权信息离时间节点需大于此数值（节点数位置）

    public static String[]        NEWS_DATETIME_REGEXP_LIB       = {
            "([0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日[0-9]{1,2}:[0-9]{1,2})",
            "([0-9]{4}/[0-9]{1,2}/[0-9]{1,2}\\s[0-9]{1,2}:[0-9]{1,2})",
            "([0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日 [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2})",
            "([0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2})",
            "([0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{2}:[0-9]{2})", "([0-9]{4}年[0-9]{1,2}月[0-9]{1,2}日)",
            "([0-9]{4}-[0-9]{1,2}-[0-9]{1,2})"                  };

    /**
     * 文本计数对象
     *
     * @author peng
     */
    private class TextConsum {

        private int     linkTextSum       = 0;    // 链接文本总数
        private int     commonTextSum     = 0;    // 普通文本总数
        private int     linkNodeSum       = 0;
        private int     seriesLinkNodeSum = 0;    // 连续链接节点总数
        private int     commonTextNodeSum = 0;
        private boolean bLink             = false;

        public int getLinkTextSum() {
            return linkTextSum;
        }

        public int getCommonTextSum() {
            return commonTextSum;
        }

        public int getLinkNodeSum() {
            return linkNodeSum;
        }

        public int getCommonTextNodeSum() {
            return commonTextNodeSum;
        }

        public int getSeriesLinkNodeSum() {
            return seriesLinkNodeSum;
        }

        private void addLinkTextSum(int sum) {
            this.linkTextSum += sum;
        }

        private void addCommonTextSum(int sum) {
            this.commonTextSum += sum;
        }

        public void add(int sum, boolean isLink) {
            if (isLink) {
                addLinkTextSum(sum);
                linkNodeSum++;
                if (bLink) {
                    seriesLinkNodeSum++;
                }
                bLink = true;
            } else {
                addCommonTextSum(sum);
                commonTextNodeSum++;
                bLink = false;
            }
        }

        public double getLinkDensityRate() {
            double rate = (double) this.linkTextSum / (double) (this.commonTextSum + this.linkTextSum);
            return rate;
        }

    }

    /**
     * 解析新闻文本
     *
     * @param html 字符串
     * @return false 传入页面为非主题页面 解析失败 true 成功解析
     */
    public boolean parseHtml(String html) {
        orgHtml = html;
        html = _tidy(html);
        byte[] byteValue = null;
        try {
            if (!charSet.equals("") && charSet != null) {
                byteValue = html.getBytes(charSet);
            } else {
                byteValue = html.getBytes();
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return parseHtml(byteValue);
    }

    /**
     * @param html 原始网页源字节流
     * @return false 传入页面为非主题页面 解析失败 true 成功解析
     */
    public boolean parseHtml(byte[] html) {
        ByteArrayInputStream byteInput = new ByteArrayInputStream(html);
        InputSource inputSource = new InputSource(byteInput);
        if (!charSet.equals("") && charSet != null) {
            inputSource.setEncoding(charSet);
        }
        return _parseHtml(inputSource);

    }

    /**
     * 设置输入字符串字符编码格式
     *
     * @param charSet
     */
    public void setCharSet(String charSet) throws java.io.UnsupportedEncodingException {
        if (Charset.isSupported(charSet)) {
            this.charSet = charSet;
        } else {
            throw new java.io.UnsupportedEncodingException(charSet);
        }
    }

    /**
     * 解析新闻文本主块
     *
     * @param inputSource
     */
    private boolean _parseHtml(InputSource inputSource) {
        MdrElementRemover remover = new MdrElementRemover();
        remover.removeElement("script");
        remover.removeElement("style");
        remover.removeElement("option");
        XMLDocumentFilter[] filters = { remover };

        DOMParser parser = new DOMParser();
        try {
            parser.setProperty("http://cyberneko.org/html/properties/filters", filters);
            parser.parse(inputSource);
            document = parser.getDocument();
            _listNode(document);
            if (_parseNewsBody()) // 获取文章主体内容节点
            {
                _parseNewsTitle(); // 获取文章标题
                _parseNewsTime(); // 获取新闻时间
                if (newsTitleNode == null && newsTimeNode != null) {
                    _parseNewsTitle2(); // 如果标题结点未确定 通过时间节点获取标题
                }
                _removeNoise();
                if (newsBodyNode == null) // 去除噪音过程中发现bodynode节点为版权信息
                {
                    String body = _parseNewsBodyByJS(orgHtml); // 尝试在JS中获取body节点内容
                    if (body != null && body.length() > 0) {

                        newsBodyNode = document.createElement("DIV");
                        newsBodyNode.setTextContent(body);
                    }
                }
            } else {
                // String body = _parseNewsBodyByJS(inputSource);
                // log.info("body:"+body);
                // if (body != null && body.length() > 0)
                // {
                // return parseHtml(body.getBytes());
                // }
                return false;
            }
            // log.info(newsTimeNode.getNodeValue());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取新闻正文
     *
     * @return true 获取成功 false 获取失败 页面为非典型新闻内容页或内容为空
     */
    private boolean _parseNewsBody() {
        Node txtContainer1 = _getMaxCommonTextNode(containerMap);
        if (txtContainer1 != null) {
            if (containerMap.get(txtContainer1).getLinkDensityRate() > NEWS_BODY_MAX_LINKDEN_SITYRATE
                || containerMap.get(txtContainer1).getCommonTextSum() < NEWS_BODY_MIN_COMMON_TEXT_SUM) {
                return false;
            }
            newsBodyNode = txtContainer1; // 初步确定正文块
            // log.info("首次内容："+newsBodyNode.getTextContent());

            TextConsum textConsum = containerMap.remove(txtContainer1);
            Node txtContainer2 = _getMaxCommonTextNode(containerMap); // 获取第二大文本结点
            containerMap.put(txtContainer1, textConsum); // 保持map完整性
            if (txtContainer2 != null) {
                if ((containerMap.get(txtContainer2).getCommonTextSum() > textConsum.getCommonTextSum() / 2)
                    && (containerMap.get(txtContainer2).getLinkDensityRate() < NEWS_BODY_MAX_LINKDEN_SITYRATE)) // 次结点内容也比较多
                {
                    _parseNewsTime(); // 需要确定时间结点

                    if (newsTimeNode != null) {
                        int timepos = _getNodeListIndex(newsTimeNode);
                        int txtContainer1pos = _getNodeListIndex(txtContainer1);
                        int txtContainer2pos = _getNodeListIndex(txtContainer2);
                        if (Math.abs((timepos - txtContainer1pos)) > Math.abs((timepos - txtContainer2pos))) {
                            newsBodyNode = txtContainer2; // 次结点为内容块 因为其离时间节点近
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 主要通过文章的<title>节点内容比较获取 获取新闻标题
     *
     * @return
     */
    private boolean _parseNewsTitle() {

        NodeList nodelist = document.getElementsByTagName("TITLE");
        if (nodelist != null && nodelist.getLength() > 0) {
            String title = nodelist.item(0).getTextContent();
            // log.info("title:"+ title);
            if (title.length() > 0) {
                if (newsBodyNode != null) {
                    Node fristTextNode = NekoHtmlUtil.getFristTextNode(newsBodyNode);
                    Node lastTextNode = NekoHtmlUtil.getLastTextNode(newsBodyNode);
                    int fristPos = _getNodeListIndex(fristTextNode);
                    int lastPos = _getNodeListIndex(lastTextNode);
                    if (fristPos == -1 || lastPos == -1) {
                        log.info("结点查询错误：fristPos:" + String.valueOf(fristPos) + ";lastPos:" + lastPos);
                        log.info("textNodeList size:" + textNodeList.size());

                        return false;
                    }
                    titleCheckPos = fristPos + (lastPos - fristPos) / 2;
                    Node tempNode = null;
                    int max = 0;
                    int start = _getNodeListIndex(nodelist.item(0));
                    if (start == -1) {
                        start = 0;
                    }
                    for (int i = start + 2; i <= titleCheckPos && i < textNodeList.size(); i++) {
                        int sameSum = 0;
                        if (textNodeList.get(i).getNodeType() == Node.TEXT_NODE
                            && textNodeList.get(i) != nodelist.item(0).getFirstChild()) {
                            String nodeValue = textNodeList.get(i).getNodeValue().trim();
                            sameSum = StringUtil.getSameCharSum(title, nodeValue);
                            if (sameSum > max && sameSum > NEWS_TITLE_MIN_SAME_CHAR
                                && nodeValue.length() <= title.length()) {

                                max = sameSum;
                                tempNode = textNodeList.get(i);
                            }
                        }

                    }
                    if (tempNode != null) // 找到title结点
                    {
                        newsTitleNode = tempNode;
                        newsTitle = newsTitleNode.getTextContent();
                        return true;
                    }

                }
            }
        }
        return false; // title为空或s过小，可能title中并不包含正文的标题，待时间节点确定后重新确定标题
    }

    /**
     * 通过时间节点解析新闻标题
     *
     * @param timeNode
     * @return
     */
    private boolean _parseNewsTitle2() {
        // log.info("_parseNewsTitle2");
        if (newsTimeNode != null && newsBodyNode != null) {
            int timePos = _getNodeListIndex(newsTimeNode);
            Node fristNode = NekoHtmlUtil.getFristTextNode(newsBodyNode);
            String temp = "";
            if (fristNode != null) {
                temp = fristNode.getNodeValue();
                int maxSum = 0;
                Node tempNode = null;
                for (int i = timePos; i >= 0; i--) {
                    int sum = 0;
                    if (textNodeList.get(i).getNodeType() == Node.TEXT_NODE) {
                        sum = StringUtil.getSameCharSum(temp, textNodeList.get(i).getNodeValue());
                        if (sum > maxSum) {
                            maxSum = sum;
                            tempNode = textNodeList.get(i);
                        }
                    }
                }
                if (tempNode != null && tempNode.getNodeValue().length() > 5) {
                    newsTitleNode = tempNode;
                    newsTitle = newsTitleNode.getTextContent();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 解析新闻时间
     *
     * @return
     */
    private boolean _parseNewsTime() {
        // log.info("_parseNewsTime");

        int end = 0;
        if (newsTimeNode != null) // 在获取主体内容时已经成功获取时间 则无需再获取
        {
            return true;
        }

        if (newsTitleNode != null) // 标题已经获取
        {
            int newsTitleNodeIndex = textNodeList.indexOf(newsTitleNode);
            if (newsTitleNodeIndex < 0) {
                newsTitleNodeIndex = 0;
            }
            end = titleCheckPos;
            if (end < 0 || end < newsTitleNodeIndex) {
                end = textNodeList.size();
            }
            for (int i = newsTitleNodeIndex; i < end; i++) {
                if (textNodeList.get(i).getNodeType() == Node.TEXT_NODE) {
                    newsTime = newsDateTimeMatch(textNodeList.get(i).getNodeValue().trim());
                    if (newsTime != null) {
                        newsTimeNode = textNodeList.get(i); // get time node success
                        break;
                    }

                }
            }
        } else // 标题块未确定
        {
            end = titleCheckPos;
            if (end < 0) {
                end = textNodeList.size();
            }
            for (int i = 0; i < end; i++) {
                if (textNodeList.get(i).getNodeType() == Node.TEXT_NODE) {
                    newsTime = newsDateTimeMatch(textNodeList.get(i).getNodeValue().trim());
                    if (newsTime != null) {
                        newsTimeNode = textNodeList.get(i); // get time node success
                        break;
                    }

                }
            }
        }
        return false;
    }

    /**
     * 匹配常见新闻资讯中时间字符串
     *
     * @param text
     * @return
     */
    public static String newsDateTimeMatch(String text) {
        String newsTime = null;
        for (String regexp : NEWS_DATETIME_REGEXP_LIB) {
            newsTime = RegexpUtil.matchGroup(text, regexp);
            if (newsTime != null) {
                break;
            }
        }
        return newsTime;
    }

    private void _bodyAddtion() {

    }

    private void _parseImg() {

    }

    private boolean _isCopyright(Node node) {
        String content = node.getTextContent();
        if (RegexpUtil.isMatch(content, "\\s*[C|c]opyright")) {
            int timepos = _getNodeListIndex(newsTimeNode);
            int nodepos = _getNodeListIndex(node);
            int pos = nodepos - timepos;
            log.info("pos:" + pos + " node sum:" + this.textNodeList.size());
            if (pos >= this.NEWS_BODY_COPYRIGHT_POST) {
                return true;
            }

        }
        return false;
    }

    private void _removeNoise() {
        List<Node> bodynodelist = new ArrayList();
        Map<Node, TextConsum> bodycontainerMap = new HashMap();
        _listNode(newsBodyNode, bodynodelist, bodycontainerMap);
        Iterator iter = bodycontainerMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Node node = (Node) entry.getKey();
            TextConsum textconsum = (TextConsum) entry.getValue();
            // log.info("content:" +node.getTextContent());
            // log.info("CommonTextSum:"+textconsum.getCommonTextSum());
            // log.info("LinkTextSum:"+textconsum.getLinkTextSum());
            // log.info("rate:"+textconsum.getLinkDensityRate());
            if (textconsum.getLinkDensityRate() >= NEWS_BODY_MAX_LINKDEN_SITYRATE) {
                // 删除噪音内容
                node.getParentNode().removeChild(node);
            }
            if (_isCopyright(node)) {
                if (node == newsBodyNode) {
                    newsBodyNode = null;
                } else {
                    node.getParentNode().removeChild(node);
                }
            }
        }
        for (int i = 0; i < bodynodelist.size(); i++) {
            if (bodynodelist.get(i).equals(newsTitleNode) || bodynodelist.get(i).equals(newsTimeNode)) {
                bodynodelist.get(i).getParentNode().removeChild(bodynodelist.get(i));
            }
        }

    }

    /**
     * 获取主链接结点中的位置
     *
     * @param node
     * @return
     */

    private int _getNodeListIndex(Node node) {
        if (node == null) {
            return -1;
        }
        return textNodeList.indexOf(node);
    }

    /**
     * 建立文本容器结点树
     *
     * @param node
     */
    private void _buildMap(Node node, Map<Node, TextConsum> map) {

        Node parentNode = node.getParentNode();
        boolean isLink = false;
        while (parentNode != null) {
            if (parentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) parentNode;
                // log.info(element.getTagName());
                if (element.getTagName().equals("TABLE") || element.getTagName().equals("DIV")
                    || element.getTagName().equals("TBODY") || element.getTagName().equals("BODY")) // 找到父节点容器
                {
                    // log.info("get element:"+node.getNodeValue());
                    if (map.get(parentNode) == null) {
                        TextConsum textConsum = new TextConsum();
                        textConsum.add(node.getNodeValue().trim().length(), isLink);
                        // textConsum.setNode(parentNode);
                        map.put(parentNode, textConsum);
                    } else {
                        map.get(parentNode).add(node.getNodeValue().trim().length(), isLink);
                    }

                    return;
                } else if (element.getTagName().equals("A")) {
                    isLink = true;
                    // log.info("link");
                }
            }
            parentNode = parentNode.getParentNode();
        }
    }

    /**
     * 递归函数 用于遍历DOM节点 并建立文本结点链表
     *
     * @param node
     */
    private void _listNode(Node node) {
        _listNode(node, textNodeList, containerMap);
    }

    // private boolean _isFilterNode(Node node)
    // {
    // if (node.getNodeType() == Node.ELEMENT_NODE)
    // {
    // Element element = (Element)node;
    // if (element.getTagName().equalsIgnoreCase("SELECT")||
    // element.getTagName().equalsIgnoreCase("OPTION"))
    //
    // }
    // return false;
    // }

    /**
     * 递归函数 用于遍历DOM节点
     *
     * @param node
     */
    private void _listNode(Node node, List<Node> list, Map<Node, TextConsum> map) {
        if (node.getNodeType() == Node.TEXT_NODE) {

            node.setNodeValue(NekoHtmlUtil.codeHandle(node));
            if (map != null) {
                _buildMap(node, map);
            }
        }
        list.add(node);
        Node child = node.getFirstChild();
        while (child != null) {
            _listNode(child, list, map);
            child = child.getNextSibling();
        }
    }

    /**
     * 获取最大文章树的节点容器
     *
     * @param map
     * @return
     */
    private Node _getMaxCommonTextNode(Map<Node, TextConsum> map) {
        Iterator iter = map.entrySet().iterator();
        Node node = null;
        int maxCommonTextSum = 0;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Node key = (Node) entry.getKey();
            TextConsum textConsum = (TextConsum) entry.getValue();
            if (textConsum.getCommonTextSum() > maxCommonTextSum) {
                maxCommonTextSum = textConsum.getCommonTextSum();
                node = key;
            }
            // log.info("maxCommonTextSum:"+maxCommonTextSum);
        }

        return node;
    }

    /**
     * 获取结点下所有文本内容
     *
     * @param root
     * @return
     */

    private String _getText(Node root) {
        // 若是文本节点的话，直接返回
        if (root.getNodeType() == Node.TEXT_NODE) {
            return root.getNodeValue().trim();
        }
        if (root.getNodeType() == Node.ELEMENT_NODE) {
            Element elmt = (Element) root;
            // 抛弃脚本
            if (elmt.getTagName().equals("STYLE") || elmt.getTagName().equals("SCRIPT")) return "";

            NodeList children = elmt.getChildNodes();
            StringBuilder text = new StringBuilder();
            for (int i = 0; i < children.getLength(); i++) {
                text.append(_getText(children.item(i)));
            }
            return text.toString();
        }
        // 对其它类型的节点，返回空值
        return "";
    }

    private String _tidy(String html) {
        html = html.replaceAll("<!--.*?-->", "");
        return html;
    }

    private int _getHtmlTagSum(String str) {
        if (str == null || str.trim().length() <= 4) {
            return -1;
        }

        String regexp = "<(.+?)>";
        String[] values = RegexpUtil.matchMultiGroup(str, regexp);
        if (values == null) {
            return -1;
        }
        return values.length;
    }

    private String _parseNewsBodyByJS(String html) {
        DOMParser parser = new DOMParser();
        byte[] byteValue = null;
        try {
            if (!charSet.equals("") && charSet != null) {
                byteValue = html.getBytes(charSet);
            } else {
                byteValue = html.getBytes();
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteValue);
        InputSource inputSource = new InputSource(arrayInputStream);

        try {
            parser.parse(inputSource);
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Document _document = parser.getDocument();

        NodeList nodes = _document.getElementsByTagName("script");
        String ret = "";
        int max = 0;

        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String nodeContent = node.getTextContent();
            String regexp = "=\\s*[\"|\'](.+?)[\"|\']\\s*;";
            String[] values = RegexpUtil.matchMultiGroup(nodeContent, regexp);

            for (String value : values) {
                int tagSum = _getHtmlTagSum(value);
                if (tagSum > max) {
                    max = tagSum;
                    ret = value;
                } else if (tagSum == max) {
                    if (ret.length() < value.length()) {
                        ret = value;
                    }
                }
            }
        }

        // log.info("ret:"+ret);
        if (max > 0) {
            return ret;
        }

        return null;
    }

    public Node getBodyNode() {
        return newsBodyNode;
    }

    public Node getTitleNode() {
        return newsTitleNode;
    }

    public Node getTimeNode() {
        return newsTimeNode;
    }

    /**
     * @param args
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws UnsupportedEncodingException {
        Browser browser = new Browser();
        try {
            browser.getConnection("http://www.cenn.cn/News/2008-7/82424_2008711154656.shtml");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String html = browser.getHTML();
        // log.info(html);
        NewsParser parser = new NewsParser();
        String charset = browser.getCharSet();
        if(Charset.isSupported(charset)){
            parser.setCharSet(charset);
        }
        parser.parseHtml(html);
    }

}
