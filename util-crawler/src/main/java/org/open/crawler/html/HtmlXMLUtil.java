package org.open.crawler.html;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.htmlcleaner.HtmlCleaner;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.open.util.FileUtil;
import org.open.util.RegexpUtil;
import org.open.util.StringUtil;

/**
 * 网页html代码处理工具包类
 * 
 * @author 覃芝鹏
 * @version $Id: HtmlXMLUtil.java,v 1.7 2009/07/06 07:23:48 moon Exp $
 */
public class HtmlXMLUtil {

    /**
     * 写日志
     */
    private static final Log log      = LogFactory.getLog(HtmlXMLUtil.class);
    private HtmlCleaner      cleaner;
    private String           html;
    private SAXReader        saxReader;
    private Document         document;
    private String           encoding = Charset.defaultCharset().name();

    /**
     * html代码中img节点信息类
     * 
     * @author Qin Zhipeng
     */
    public static class ImgInfo {

        /**
         * 图片链接属性alt值
         */
        private String htmlAlt;

        /**
         * 图片链接属性title值
         */
        private String htmlTitle;

        /**
         * 图片链接外围包含href内部链接文字
         */
        private String htmlLink;

        /**
         * 图片扩展描述Tag,多个Tag以逗号分开
         */
        private String tags;

        /**
         * 图片扩展文字描述
         */
        private String desc;

        /**
         * 图片后缀名
         */
        private String postfix;

        /**
         * 图片URL链接地址
         */
        private String url;

        /**
         * 图片base64编码流
         */
        private String base64data;

        /**
         * @return the htmlAlt
         */
        public String getHtmlAlt() {
            return htmlAlt;
        }

        /**
         * @param htmlAlt the htmlAlt to set
         */
        public void setHtmlAlt(String htmlAlt) {
            this.htmlAlt = htmlAlt;
        }

        /**
         * @return the htmlTitle
         */
        public String getHtmlTitle() {
            return htmlTitle;
        }

        /**
         * @param htmlTitle the htmlTitle to set
         */
        public void setHtmlTitle(String htmlTitle) {
            this.htmlTitle = htmlTitle;
        }

        /**
         * @return the htmlLink
         */
        public String getHtmlLink() {
            return htmlLink;
        }

        /**
         * @param htmlLink the htmlLink to set
         */
        public void setHtmlLink(String htmlLink) {
            this.htmlLink = htmlLink;
        }

        /**
         * @return the tags
         */
        public String getTags() {
            return tags;
        }

        /**
         * @param tags the tags to set
         */
        public void setTags(String tags) {
            this.tags = tags;
        }

        /**
         * @return the desc
         */
        public String getDesc() {
            return desc;
        }

        /**
         * @param desc the desc to set
         */
        public void setDesc(String desc) {
            this.desc = desc;
        }

        /**
         * @return the postfix
         */
        public String getPostfix() {
            return postfix;
        }

        /**
         * @param postfix the postfix to set
         */
        public void setPostfix(String postfix) {
            this.postfix = postfix;
        }

        /**
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @param url the url to set
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * @return the base64data
         */
        public String getBase64data() {
            return base64data;
        }

        /**
         * @param base64data the base64data to set
         */
        public void setBase64data(String base64data) {
            this.base64data = base64data;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "ImgInfo [base64data=" + base64data + ", desc=" + desc + ", htmlAlt=" + htmlAlt + ", htmlLink="
                   + htmlLink + ", htmlTitle=" + htmlTitle + ", postfix=" + postfix + ", tags=" + tags + ", url=" + url
                   + "]";
        }
    }

    /**
     * 剪切html中 指定节点 及其 其下的树节点（包括内容）
     * 
     * @param nodeName html节点名称
     * @return 剪切后的html代码
     */
    private static String _cutHtmlNode(String html, String nodeName) {
        return html.replaceAll("<" + nodeName + ".*?>.*?</" + nodeName + ">", "");
    }

    /**
     * 整理html中代码 包含过来JS代码 样式 以及注释信息 多余的空格
     * 
     * @param html
     * @return
     */
    public static String tidy(String html) {
        html = html.replaceAll("<!--.*?-->", "");
        html = html.replaceAll("&nbsp;", " ");
        html = html.replaceAll("[\\s]+", " ");
        html = html.replaceFirst("<html .*?>", "<html>");

        html = _cutHtmlNode(html, "script");
        html = _cutHtmlNode(html, "style");
        return html;
    }

    /**
     * 处理html中的 ‘&nbsp;’、‘&amp;’等等转义字符
     * 
     * @param str 需要处理的HMTL
     * @return 处理后的HTML
     */
    public static String cutESC(String str) {
        return (str == null) ? null : str.replaceAll("&.{1,4};", "").trim();
    }

    /**
     * 处理html中的 ‘&nbsp;’
     * 
     * @param str 需要处理的HMTL
     * @return 处理后的HTML
     */
    public static String cutNbsp(String str) {
        return (str == null) ? null : str.replaceAll("&nbsp;", " ").trim();
    }

    /**
     * 得到html里面纯文本信息
     * 
     * @param html 原始html代码
     * @return html里面纯文本信息
     */
    private static String _getInnerText(String html) {
        html = html.toLowerCase();

        html = tidy(html);
        html = html.replaceAll("<.*?>", "");

        return html;
    }

    /**
     * 剪切html代码中 指定 的节点 且 不删除 其子节点及其内容
     * 
     * @param tag html标签节点名称
     * @return 剪切后的html代码
     */
    private static String _cutHtmlTag(String html, String tag) {
        return html.replaceAll("<" + tag + ".*?>", " ").replaceAll("</" + tag + ">", " ");
    }

    /**
     * 剪除html代码中指定 标签节点 属性值
     * 
     * @param html 原始html代码
     * @param tag html标签名称
     * @return 剪除后的html代码
     */
    private static String _cutHtmlTagAttribute(String html, String tag) {
        return html.replaceAll("<" + tag + " .*?>", "<" + tag + ">");
    }

    /**
     * 提取html代码中所有的文字 信息
     * 
     * @param html 原始html代码
     * @return 提取的页面文字
     */
    private static String _getHtmlInnerText(String html) {
        return html.replaceAll("<.*?>", "");
    }

    /**
     * 提取html代码中 指定节点 标签里面 的内容
     * 
     * @param tag 指定的节点标签
     * @return html指定节点标签里面的内容 数组
     */
    private static String[] _getHtmlInnerHTMLByTag(String html, String tag) {
        return RegexpUtil.matchGroups(html, "<" + tag + ">(.*?)</" + tag + ">");
    }

    /**
     * 提取html代码中 指定节点 标签里面 的文本内容
     * 
     * @param tag 节点标签
     * @return html指定节点标签里面的文本内容 数组
     */
    private static String[] _getHtmlInnerTextByTag(String html, String tag) {
        String[] _html = RegexpUtil.matchGroups(html, "<" + tag + ">(.*?)</" + tag + ">");
        for (int i = 0; i < _html.length; i++) {
            _html[i] = _getHtmlInnerText(_html[i]);
            _html[i] = StringUtil.cutPunctuation(_html[i]);
        }
        return _html;
    }

    /**
     * 原始判断处理，当出错时直接抛出例外交给上层处理。
     * 
     * @param html 原始html
     */
    public HtmlXMLUtil(String html) {
        if (html == null) {
            throw new IllegalStateException();
        } else {
            try {
                // 利用htmlclear中间件把html标准xml化
                html = cutNbsp(html);
                cleaner = new HtmlCleaner(html);
                cleaner.clean();
                this.html = cleaner.getPrettyXmlAsString();
                this.html = tidy(html);
                saxReader = new SAXReader();
                saxReader.setEncoding(encoding);
                document = saxReader.read((new ByteArrayInputStream(this.html.getBytes())));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * 原始判断处理，当出错时直接抛出例外交给上层处理。
     * 
     * @param html 原始html
     */
    public HtmlXMLUtil(String html, String encoding) {
        this.encoding = encoding;
        if (html == null) {
            throw new IllegalStateException();
        } else {
            try {
                // 利用htmlclear中间件把html标准xml化
                html = cutNbsp(html);
                html = tidy(html);
                cleaner = new HtmlCleaner(html);
                cleaner.clean();

                this.html = cleaner.getPrettyXmlAsString();
                saxReader = new SAXReader();
                saxReader.setEncoding(encoding);
                document = saxReader.read((new ByteArrayInputStream(this.html.getBytes())));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * 剪切html中 指定节点 及其 其下的树节点（包括内容）
     * 
     * @param nodeName html节点名称
     * @return 剪切后的html代码
     */
    public String cutHtmlNode(String nodeName) {
        return _cutHtmlNode(html, nodeName);
    }

    /**
     * 剪切html中 指定一组节点 及其 其下的树节点（包括内容）
     * 
     * @see #cutHtmlNode(String, String)
     * @param nodeNames html节点名称 数组
     * @return 剪切后的html代码
     */
    public String cutHtmlNode(String[] nodeNames) {
        String tmp = html;
        for (int i = 0; i < nodeNames.length; i++) {
            tmp = _cutHtmlNode(tmp, nodeNames[i]);
        }

        return tmp;
    }

    /**
     * 剪切html代码中 指定 的节点 且 不删除 其子节点及其内容
     * 
     * @param tag html标签节点名称
     * @return 剪切后的html代码
     */
    public String cutHtmlTag(String tag) {
        return _cutHtmlTag(html, tag);
    }

    /**
     * 剪切html代码中 指定的 一组节点 且 不删除 其子节点及其内容
     * 
     * @see #cutHtmlTag(String, String)
     * @param tags html标签节点名称 数组
     * @return 剪切后的html代码
     */
    public String cutHtmlTag(String[] tags) {
        String tmp = html;
        for (int i = 0; i < tags.length; i++) {
            tmp = _cutHtmlTag(tmp, tags[i]);
        }

        return tmp;
    }

    /**
     * 剪除html代码中指定 标签节点 属性值
     * 
     * @param html 原始html代码
     * @param tag html标签名称
     * @return 剪除后的html代码
     */
    public String cutHtmlTagAttribute(String tag) {
        return _cutHtmlTagAttribute(html, tag);
    }

    /**
     * 剪除html代码中指定 一组标签节点 属性值
     * 
     * @see #cutHtmlNode(String, String)
     * @param tags html标签名称 数组
     * @return 剪除后的html代码
     */
    public String cutHtmlTagAttribute(String tags[]) {
        String tmp = html;
        for (int i = 0; i < tags.length; i++) {
            tmp = _cutHtmlTagAttribute(tmp, tags[i]);
        }

        return tmp;
    }

    /**
     * 得到html里面纯文本信息
     * 
     * @return html里面纯文本信息
     */
    public String getInnerText() {
        return _getInnerText(html);
    }

    /**
     * 提取html代码中 指定节点 标签里面 的内容
     * 
     * @param tag 指定的节点标签
     * @return html指定节点标签里面的内容 数组
     */
    public String[] getHtmlInnerHTMLByTag(String tag) {
        return _getHtmlInnerHTMLByTag(html, tag);
    }

    /**
     * 提取html代码中 指定一组节点 标签里面 的内容
     * 
     * @see #getHtmlInnerHTMLByTag(String, String)
     * @param html html代码
     * @param tags 一组节点标签
     * @return 二维数组 一维下标对应各个节点标签 二维下标对应其标题匹配的内容数组
     */
    public String[][] getHtmlInnerHTMLByTag(String[] tags) {
        String[][] strs = new String[tags.length][];
        for (int i = 0; i < tags.length; i++) {
            strs[i] = _getHtmlInnerHTMLByTag(html, tags[i]);
        }

        return strs;
    }

    /**
     * 提取html代码中 指定节点 标签里面 的文本内容
     * 
     * @param tag 节点标签
     * @return html指定节点标签里面的文本内容 数组
     */
    public String[] getHtmlInnerTextByTag(String tag) {
        return _getHtmlInnerTextByTag(html, tag);
    }

    /**
     * 提取html代码中 指定一组节点 标签里面 的文本内容
     * 
     * @param tags 一组节点标签
     * @return 二维数组 一维下标对应各个节点标签 二维下标对应其标题匹配的文本内容数组
     */
    public String[][] getHtmlInnerTextByTag(String[] tags) {
        String[][] strs = new String[tags.length][];
        for (int i = 0; i < tags.length; i++) {
            strs[i] = getHtmlInnerTextByTag(tags[i]);
        }

        return strs;
    }

    /**
     * 得到网页中所有的标签
     * 
     * @return 返回包含标签的数组
     */
    @SuppressWarnings("unchecked")
    public String[] getAllTags() {
        Set<String> set = cleaner.getAllTags();

        return (String[]) set.toArray(new String[set.size()]);
    }

    /**
     * 得到html table 单元格里面 文本内容
     * 
     * @return 文本内容 字符串数组
     */
    public String[] getHtmlTableCellText() {
        // 替换所有th到td
        String tmp = html.replaceAll("th", "td");
        ;

        // 得到td的innerText数组
        return _getHtmlInnerTextByTag(tmp, "td");
    }

    /**
     * 替换html中的js脚本,多余的空格,多余的回车,注释等信息
     * 
     * @param html
     * @return
     */
    public static String getFilterHtml(String html) {
        html = html.replaceAll("\\n*", "");
        html = html.replaceAll("\\r*", "");
        html = html.replaceAll("\\s+", " ");
        html = getFilterScript(html);
        html = html.replaceAll("<[s|S][e|E][l|L][e|E][c|C][t|T].*?</[s|S][e|E][l|L][e|E][c|C][t|T]>", "");
        html = html.replaceAll("<[s|S][t|T][y|Y][l|L][e|E].*?</[s|S][t|T][y|Y][l|L][e|E]>", "");
        html = html.replaceAll("<!--.*?-->", "");

        return html;
    }

    /**
     * 过滤网页中的各种脚本
     * 
     * @return
     */
    public static String getFilterScript(String html) {
        return html.replaceAll("<[s|S][c|C][r|R][i|I][p|P][t|T].*?</[s|S][c|C][r|R][i|I][p|P][t|T]>", "");
    }

    /**
     * @return 匹配到去掉html标签后的纯文本数组
     */
    public static String[] getText(String html) {
        try {
            Parser parser = Parser.createParser(html, null);
            NodeFilter textFilter = new NodeClassFilter(TextNode.class);
            NodeList nodelist = parser.parse(textFilter);
            Node[] nodes = nodelist.toNodeArray();

            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < nodes.length; i++) {
                TextNode textnode = (TextNode) nodes[i];
                String line = textnode.getText();

                line = StringUtil.cutHtmlTag(line);
                line = StringUtil.tidy(line);

                if (StringUtil.isBlank(line)) continue;
                list.add(line);
            }

            return list.toArray(new String[list.size()]);
        } catch (Exception e) {
            log.error("HtmlParser getText() error!=>" + e.getMessage());
        }

        return new String[0];
    }

    /**
     * 过滤html中的html 标签,提出纯文本字段
     * 
     * @param html
     * @return txt
     */
    public static String filterHtml(String html) {
        String[] lines = getText(html);

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < lines.length; i++) {
            buf.append(lines[i]).append(" ");
        }

        return buf.toString();
    }

    public String[] getMeta() throws ParserException {
        Parser parser = Parser.createParser(html, null);
        NodeFilter filter = new NodeClassFilter(MetaTag.class);
        NodeList nodelist = parser.parse(filter);
        Node[] nodes = nodelist.toNodeArray();
        String[] tmp = new String[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            tmp[i] = nodes[i].toHtml();
        }
        return tmp;
    }

    public String[] getHead() throws ParserException {
        Parser parser = Parser.createParser(html, null);
        NodeFilter filter = new NodeClassFilter(HeadTag.class);
        NodeList nodelist = parser.parse(filter);
        Node[] nodes = nodelist.toNodeArray();
        String[] tmp = new String[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            tmp[i] = nodes[i].toHtml();
        }
        return tmp;
    }

    public String[] getTitle() throws ParserException {
        Parser parser = Parser.createParser(html, null);
        NodeFilter filter = new NodeClassFilter(TitleTag.class);
        NodeList nodelist = parser.parse(filter);
        Node[] nodes = nodelist.toNodeArray();
        String[] tmp = new String[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            tmp[i] = nodes[i].toHtml();
        }
        return tmp;
    }

    /**
     * 判断td单元格里面的html当中是否有嵌套的td
     * 
     * @param tdSubHtml 要分析的td单元格html;
     * @return 有返回true,没有返回false;
     * @throws ParserException
     */
    private static boolean isTdNested(String tdSubHtml) throws ParserException {
        Parser parser = Parser.createParser(tdSubHtml, null);
        NodeFilter tdFilter = new NodeClassFilter(TableColumn.class);
        NodeList nodelist = parser.parse(tdFilter);
        return nodelist.toNodeArray().length > 0;
    }

    /**
     * @return 匹配html中TD标签的纯文本数组
     */
    public static String[] getAllTdText(String html) {
        try {
            Parser parser = Parser.createParser(html, null);
            NodeFilter tdFilter = new NodeClassFilter(TableColumn.class);
            NodeList nodelist = parser.parse(tdFilter);
            Node[] nodes = nodelist.toNodeArray();

            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < nodes.length; i++) {
                TableColumn td = (TableColumn) nodes[i];

                if (isTdNested(td.getChildrenHTML())) continue;

                String line = StringUtil.cutHtmlTag(td.toPlainTextString());
                line = StringUtil.tidy(line);

                if (StringUtil.isBlank(line)) continue;

                list.add(line);
            }

            return list.toArray(new String[list.size()]);
        } catch (Exception e) {
            log.error("HtmlParser getAllTdText() error!=>" + e.getMessage());
        }

        return new String[0];
    }

    /**
     * @return 匹配html中TD标签的纯文本数组
     */
    public static String[] getAllTable(String html) {
        try {
            Parser parser = Parser.createParser(html, null);
            NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
            NodeList nodelist = parser.parse(tableFilter);
            Node[] nodes = nodelist.toNodeArray();

            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < nodes.length; i++) {
                TableTag table = (TableTag) nodes[i];

                String tmp = StringUtil.tidy(table.toHtml());

                if (StringUtil.isBlank(tmp)) continue;

                list.add(tmp);
            }

            return list.toArray(new String[list.size()]);
        } catch (Exception e) {
            log.error("HtmlParser getAllTdText() error!=>" + e.getMessage());
        }

        return new String[0];
    }

    /**
     * @return 匹配html中所有网页链接数组
     */
    public static String[] getLink(String html) {
        try {
            Parser parser = Parser.createParser(html, null);
            NodeFilter textFilter = new NodeClassFilter(LinkTag.class);
            NodeList nodelist = parser.parse(textFilter);

            Node[] nodes = nodelist.toNodeArray();

            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < nodes.length; i++) {
                LinkTag linkTag = (LinkTag) nodes[i];
                list.add(linkTag.extractLink());
            }

            return list.toArray(new String[list.size()]);
        } catch (Exception e) {
            log.error("HtmlParser getLink() error!=>" + e.getMessage());
        }

        return new String[0];
    }

    /**
     * @return 匹配html中所有网页链接和文字段数组;基数位为网页链接,偶数位为文本段.
     */
    public static String[] getLinkAndText(String html) {
        try {
            Parser parser = Parser.createParser(html, null);
            NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);

            NodeList nodelist = parser.parse(linkFilter);
            Node[] nodes = nodelist.toNodeArray();

            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < nodes.length; i++) {
                LinkTag linkTag = (LinkTag) nodes[i];
                list.add(linkTag.extractLink());
                list.add(linkTag.getLinkText().trim());
            }

            return list.toArray(new String[list.size()]);
        } catch (Exception e) {
            log.error("HtmlParser getLink() error!=>" + e.getMessage());
        }

        return new String[0];
    }

    /**
     * @return 匹配html中所有网页标题
     */
    public static String getTitle(String html) {
        try {
            Parser parser = Parser.createParser(html, null);
            NodeFilter titleFilter = new NodeClassFilter(TitleTag.class);
            NodeList nodelist = parser.parse(titleFilter);
            Node[] nodes = nodelist.toNodeArray();

            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < nodes.length; i++) {
                TitleTag title = (TitleTag) nodes[i];
                buf.append(title.getTitle());
            }

            return buf.toString();
        } catch (Exception e) {
            log.error("HtmlParser getTitle() error!=>" + e.getMessage());
        }

        return null;
    }

    /**
     * @return 匹配html中所有图片url
     */
    public static String[] getAllImage(String html) {
        try {
            Parser parser = Parser.createParser(html, null);
            NodeFilter imageFilter = new NodeClassFilter(ImageTag.class);
            NodeList nodelist = parser.parse(imageFilter);
            Node[] nodes = nodelist.toNodeArray();

            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < nodes.length; i++) {
                ImageTag image = (ImageTag) nodes[i];
                list.add(image.getImageURL());
            }

            return list.toArray(new String[list.size()]);
        } catch (Exception e) {
            log.error("HtmlParser getAllImage() error!=>" + e.getMessage());
        }

        return new String[0];
    }

    /**
     * @return 匹配html中所有图片url
     */
    public static String getBody(String html) {
        try {
            Parser parser = Parser.createParser(html, null);
            NodeFilter bodyFilter = new NodeClassFilter(BodyTag.class);
            NodeList nodelist = parser.parse(bodyFilter);
            Node[] nodes = nodelist.toNodeArray();

            StringBuffer _body = new StringBuffer(10240);
            for (int i = 0; i < nodes.length; i++) {
                BodyTag body = (BodyTag) nodes[i];
                _body.append(body.getChildrenHTML());
            }

            return _body.toString();
        } catch (Exception e) {
            log.error("HtmlParser getBody() error!=>" + e.getMessage());
        }

        return null;
    }

    /**
     * 说明 dom4j方法调用 修改人 peng 时间 2009-06-16
     */

    /**
     * 结点下所有nodeTag节点
     * 
     * @param root 需要查询的跟结点
     * @param nodeTag 结点标签
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Element> getElementsByTag(Element root, String nodeTag) {
        String xpath = "//" + nodeTag;
        return root.selectNodes(xpath);
    }

    /**
     * 获取当前document的根结点下所有nodeTag节点
     * 
     * @param nodeTag 结点标签
     * @return 返回所有符合标签的结点
     */
    public List<Element> getElementsByTag(String nodeTag) {
        Element root = document.getRootElement();
        return getElementsByTag(root, nodeTag);
    }

    /**
     * 通过标签获取单个节点
     * 
     * @param root
     * @param nodeTag
     * @return
     */

    public Element getSinglelElementByTag(Element root, String nodeTag) {
        String xpath = "//" + nodeTag;
        return (Element) root.selectSingleNode(xpath);
    }

    /**
     * 获取输入节点下所有拥有输入属性的节点
     * 
     * @param root
     * @param attribute
     * @param attributeValue
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Element> getElementsByAttribute(Element root, String attribute, String attributeValue) {
        String xpath = "//*[@" + attribute + "='" + attributeValue + "']";
        return root.selectNodes(xpath);
    }

    /**
     * 获取输入节点下对应标签以及属性的所有节点
     * 
     * @param root
     * @param nodeTag
     * @param attribute
     * @param attributeValue
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Element> getElementsByAttribute(Element root, String nodeTag, String attribute, String attributeValue) {
        String xpath = "//" + nodeTag + "[@" + attribute + "='" + attributeValue + "']";
        return root.selectNodes(xpath);
    }

    /**
     * 获取输入根节点下对应标签以及属性的所有节点
     * 
     * @param root
     * @param nodeTag
     * @param attribute
     * @param attributeValue
     * @return
     */
    public List<Element> getElementsByAttribute(String nodeTag, String attribute, String attributeValue) {
        Element root = document.getRootElement();
        return getElementsByAttribute(root, nodeTag, attribute, attributeValue);
    }

    /**
     * 获取整个xml文档中所有拥有对应属性的节点
     * 
     * @param attribute
     * @param attributeValue
     * @return
     */
    public List<Element> getElementsByAttribute(String attribute, String attributeValue) {
        Element root = document.getRootElement();
        return getElementsByAttribute(root, attribute, attributeValue);
    }

    /**
     * 获取输入节点下首个拥有输入属性的节点
     * 
     * @param root
     * @param attribute
     * @param attributeValue
     * @return
     */
    public Element getSignalElementByAttribute(Element root, String attribute, String attributeValue) {
        String xpath = "//*[@" + attribute + "='" + attributeValue + "']";
        return (Element) root.selectSingleNode(xpath);
    }

    /**
     * 获取整个XML文档中对应相应属性的首个节点
     * 
     * @param attribute
     * @param attributeValue
     * @return
     */
    public Element getSignalElementByAttribute(String attribute, String attributeValue) {
        Element root = document.getRootElement();
        return getSignalElementByAttribute(root, attribute, attributeValue);
    }

    /**
     * 获取输入节点下符合ID属性的唯一节点
     * 
     * @param root
     * @param id
     * @return
     */
    public Element getElementById(Element root, String id) {
        return getSignalElementByAttribute(root, "id", id);
    }

    /**
     * 获取根节点下符合ID属性的唯一节点
     * 
     * @param id
     * @return
     */
    public Element getElementById(String id) {
        Element root = document.getRootElement();
        return getElementById(root, id);
    }

    /**
     * 获取根节点
     * 
     * @return
     */
    public Element getRootElement() {
        return document.getRootElement();
    }

    private final static String RegexpParseImg = "((<[a|A].+?>.*?<[i|I][m|M][g|G].+?>.*?</[a|A]>)|(<[i|I][m|M][g|G].+?>))";
    private final static String RegexpImgAlt   = "[a|A][l|L][t|T]=[\"|']?(.*?)[\"|']? ";
    private final static String RegexpImgTitle = "[t|T][i|I][t|T][l|L][e|E]=[\"|']?(.*?)[\"|']? ";
    private final static String RegexpImgUrl   = "[s|S][r|R][c|C]=[\"|']?(.*?)[\"|']? ";

    /**
     * 取得html代码里面图片链接信息，具体信息参考 内部类 ImgInfo
     * 
     * @see ImgInfo
     * @param html
     * @return
     */
    public static List<ImgInfo> parseImg(String html) {

        List<ImgInfo> listImgInfo = new ArrayList<ImgInfo>();

        String[] imgNodes = RegexpUtil.matchGroups(html, RegexpParseImg);
        for (String imgNode : imgNodes) {
            ImgInfo imgInfo = new ImgInfo();

            imgInfo.setHtmlAlt(RegexpUtil.matchGroup(imgNode, RegexpImgAlt));
            imgInfo.setHtmlTitle(RegexpUtil.matchGroup(imgNode, RegexpImgTitle));
            imgInfo.setHtmlLink(HtmlDataUtil.getInnerText(imgNode).trim());
            imgInfo.setUrl(RegexpUtil.matchGroup(imgNode, RegexpImgUrl));
            imgInfo.setPostfix(FileUtil.getSuffix(imgInfo.getUrl()));

            listImgInfo.add(imgInfo);
        }

        return listImgInfo;
    }

}
