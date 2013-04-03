package org.open.chinese;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.open.util.StringUtil;
import org.open.util.OperationUtil;

/**
 * 中文分词工具包
 * 
 * @author 覃芝鹏
 * @version $Id: ChineseUtil.java,v 1.40 2008/09/19 01:58:05 moon Exp $
 */
public class ChineseUtil {

    /**
     * 辅助类，一对key和value，key记录最大词长度，用于类处理中按词最大最小词匹配使用，value对应词的map集合
     * 
     * @author 覃芝鹏
     * @version $Id: ChineseUtil.java,v 1.40 2008/09/19 01:58:05 moon Exp $
     */
    private class IntegerTreeMap {

        /**
         * 最大词长度，用于类处理中按词最大最小词匹配使用
         */
        public Integer       maxWordLength = Integer.MIN_VALUE;

        /**
         * 对应 词的map集合
         */
        public List<Chinese> words         = new ArrayList<Chinese>();

        public void addChinese(Chinese word) {
            if (words.contains(word)) {
                words.remove(word);
            }
            words.add(word);
            maxWordLength = Math.max(maxWordLength, word.getWord().length());
        }
    }

    /**
     * 辅助类，定义父类的各种不同实例化方式 枚举类型定义
     * 
     * @author 覃芝鹏
     * @version $Id: ChineseUtil.java,v 1.40 2008/09/19 01:58:05 moon Exp $
     */
    public static enum InstanceModel {
        /**
         * 最快匹配模式:<br>
         * 特征一：能够快速装载词典；<br>
         * 特征二：最大优先匹配算法里面 没有新词校正算法，不过最小优先算法本身带新词校正功能，使用者可以考虑 使用最小优先算法 或 最小至最大并集算法 2种算法，亦可达到一定准确度；
         */
        FastModel("FastModel", "最快匹配模式"),

        /**
         * 最准匹配模式： 特征一：最大优先匹配算法里面 有新词校正算法，故而相对比较准确，不过时间略长；
         */
        ExactModel("ExactModel", "最准匹配模式"),

        /**
         * 数据分析、挖掘模式： 特征一：返回词的时候带有词性 和 频率等相关参数，很方便用于数据分析和数据挖掘；
         */
        MiningModel("MiningModel", "数据分析、挖掘模式");

        /**
         * 词性标识名字
         */
        private String name;

        /**
         * 词性标识描述
         */
        private String desc;

        private InstanceModel(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }

        public String getName() {
            return this.name;
        }

        public String getDesc() {
            return this.desc;
        }

        public String toString() {
            return "Model Name:" + this.name + ";Model Description:" + this.desc;
        }
    }

    private static final Log                          log                    = LogFactory.getLog(ChineseUtil.class);

    /**
     * @see Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
     */
    private final static Character.UnicodeBlock       chinese                = Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;

    /**
     * 默认分隔符
     */
    private final static char                         DEFAULT_SEPERATOR_CHAR = ' ';

    /**
     * 定义词的最大长度
     */
    public final static int                           MAX_WORD_LENGTH        = 50;

    /**
     * 实例化 池子
     */
    private static Map<InstanceModel, ChineseUtil>    Global                 = new HashMap<InstanceModel, ChineseUtil>();

    private InstanceModel                             instanceModel;

    /**
     * 词典树的跟点
     */
    private static TreeMap<Character, IntegerTreeMap> dicBasic               = new TreeMap<Character, IntegerTreeMap>();

    /**
     * 中文停词库
     */
    private static List<String>                       dicStopWordCN          = new ArrayList<String>();

    /**
     * 英文停词库
     */
    private static List<String>                       dicStopWordEN          = new ArrayList<String>();

    /**
     * 根据实例化模式类 实例化
     * 
     * @param instanceModel InstanceModel
     * @return 实例化对象
     */
    public static ChineseUtil instance(InstanceModel instanceModel) {
        ChineseUtil instance = Global.get(instanceModel);
        if (instance == null) {
            instance = new ChineseUtil(instanceModel);
            Global.put(instanceModel, instance);
        }
        return instance;
    }

    /**
     * 构造函数，初始化词典，按词的首字母为key，形成以首字段为key的词典树。
     */
    private ChineseUtil(InstanceModel model) {
        instanceModel = model;

        if (needLoadDic(model)) {
            loadDicBasic();

            loadDicStopWord(dicStopWordCN, "StopWord.cn.dic");
            loadDicStopWord(dicStopWordEN, "StopWord.en.dic");
        }
    }

    /**
     * 判断当前模式是否需要重新装载词库
     * 
     * @param model InstanceModel
     * @return true重新装载，false不要重新装载。
     */
    private boolean needLoadDic(InstanceModel model) {
        ChineseUtil FastModel = Global.get(InstanceModel.FastModel);
        ChineseUtil ExactModel = Global.get(InstanceModel.ExactModel);
        ChineseUtil MiningModel = Global.get(InstanceModel.MiningModel);
        switch (model) {
            case FastModel:
            case ExactModel:
                return FastModel == null && ExactModel == null && MiningModel == null;
            case MiningModel:
                return MiningModel == null;
        }
        return false;
    }

    /**
     * 装载基本词库
     */
    private void loadDicBasic() {
        InputStream is = null;
        BufferedReader in = null;
        try {
            is = getClass().getResourceAsStream("MySogouLib.dic");
            // is = getClass().getResourceAsStream("basic.dic");
            in = new BufferedReader(new InputStreamReader(is));

            String dataline;
            while ((dataline = in.readLine()) != null) {
                // 压词入基本词典库
                pushDictionary(dataline.trim());
            }
        } catch (Exception e) {
            log.error("ChineseUtil instance error!", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                log.error("ChineseUtil instance error!", e);
            }
        }
    }

    /**
     * 载入词典辅助方法体 从一行词典字符串中 读取信息 转换成Chinese
     * 
     * @param wordLine 一行词典字符串 格式： 一丁点儿;241923;ADV,ADJ,
     * @return Chinese 对象
     */
    private Chinese splitWordLineToChinese(String[] splitWordLine) {
        Chinese chinese = new Chinese();

        chinese.setWord(splitWordLine[0].intern());
        chinese.setWeight(Float.valueOf(splitWordLine[1]));

        switch (instanceModel) {
            case MiningModel:
                if (splitWordLine.length > 2) {
                    String[] cache = splitWordLine[2].split(",");
                    for (int j = 0; j < cache.length; j++) {
                        chinese.addCharacter(Chinese.Character.getCharacter(cache[j]));
                    }
                }
                break;
            default:
                break;
        }

        return chinese;
    }

    /**
     * 把词压入 基本词库
     * 
     * @param word 单个词
     */
    private void pushDictionary(String wordLine) {
        pushDictionary(splitWordLineToChinese(wordLine.split(";")));
    }

    /**
     * 把词压入 基本词库
     * 
     * @param chinese 单个词
     */
    private void pushDictionary(Chinese chinese) {
        String word = chinese.getWord();
        char c = word.charAt(0);

        IntegerTreeMap tmp = dicBasic.get(c);
        if (tmp == null) {
            tmp = new IntegerTreeMap();
            tmp.addChinese(chinese);

            dicBasic.put(c, tmp);
        } else {
            tmp.addChinese(chinese);
        }
    }

    /**
     * 装载停词库
     */
    private void loadDicStopWord(List<String> list, String fileName) {
        try {
            InputStream is = getClass().getResourceAsStream(fileName);
            BufferedReader in = new BufferedReader(new InputStreamReader(is));

            String dataline;
            while ((dataline = in.readLine()) != null) {
                list.add(dataline.trim());
            }

            in.close();
        } catch (Exception e) {
            log.error("ChineseUtil instance error!", e);
        }
    }

    /**
     * 装载其他词库 接口之一
     * 
     * @param words Chinese数组
     */
    public void loadOtherDic(Chinese[] words) {
        for (int i = 0; i < words.length; i++) {
            pushDictionary(words[i]);
        }
    }

    /**
     * 得到基本词库的词典树结构
     * 
     * @return 词典树结构 词的首字母为key，形成以首字段为key的词典树.
     */
    public TreeMap<Character, IntegerTreeMap> getDicBasic() {
        return dicBasic;
    }

    /**
     * 清除词两边的标点符号
     * 
     * @param word 原始词
     * @return 处理后的词
     */
    private String cutPunctuation(String word) {
        if (word.length() < 1) {
            return word;
        }

        char c = word.charAt(0);
        if (!isChinese(c) && !isLetterOrDigit(c)) {
            // 去掉末尾单个字符
            word = word.substring(1, word.length());
            // 递归遍历
            return cutPunctuation(word);
        }

        c = word.charAt(word.length() - 1);
        if (!isChinese(c) && !isLetterOrDigit(c)) {
            // 去掉开始单个字符
            word = word.substring(0, word.length() - 1);
            // 递归遍历
            return cutPunctuation(word);
        }

        return word;
    }

    /**
     * 将词压入 带词频统计的词库（包含中文停词过滤，英文停词过滤，标点符号过滤）
     * 
     * @param p 带词频统计的词库
     * @param word 需要压入的词
     * @param isRemoveStopWord true开启去掉停词功能；false反之。
     */
    private void pushWord(List<Chinese> lib, String word, boolean isRemoveStopWord, Collection<Chinese.Character> filter) {
        word = word.trim();

        // 过滤空内容 或 超出最大定义单词长度
        if (word.length() < 1 || word.length() > MAX_WORD_LENGTH) {
            return;
        }

        // 除去标点符号
        word = cutPunctuation(word);

        // 判断是否去除停词
        if (isRemoveStopWord) {
            // 过滤中文停词
            if (dicStopWordCN.contains(word)) {
                return;
            }

            // 过滤英文停词
            if (dicStopWordEN.contains(word)) {
                return;
            }
        }

        // 过滤空内容 或 超出最大定义单词长度
        if (word.length() < 1 || word.length() > MAX_WORD_LENGTH) {
            return;
        }

        // 查询词频，并累加统计。
        Chinese new_word = new Chinese(word);
        int index_lib = lib.indexOf(new_word);
        if (-1 == index_lib) {
            switch (instanceModel) {
                case MiningModel:
                    // 数据挖掘模式 则在词库中取带有词性的词
                    IntegerTreeMap itm = dicBasic.get(word.charAt(0));
                    if (itm != null) {
                        int index_itm = itm.words.indexOf(new_word);
                        if (-1 != index_itm) {
                            new_word = itm.words.get(index_itm);
                        }
                    }
            }

            // 当过滤集存在
            if (filter != null) {
                // 如果在词性过滤集合中则不压入词库
                if (!OperationUtil.isIntersect(new_word.getCharacter(), filter)) {
                    return;
                }
            }

            new_word.setFrequency(1);
            lib.add(new_word);
        } else {
            new_word = lib.get(index_lib);
            new_word.setFrequency(new_word.getFrequency() + 1);
        }
    }

    /**
     * 转换为 Chinese 对象数组
     * 
     * @param p 带词频统计的词库
     * @return Chinese 对象数组
     */
    private Chinese[] toArray(List<Chinese> lib) {
        return lib.toArray(new Chinese[lib.size()]);
    }

    /**
     * 判断一个char是否为中文。
     * 
     * @param c 字符
     * @return 是否为中文
     */
    public static boolean isChinese(char c) {
        return (chinese == Character.UnicodeBlock.of(c));
    }

    /**
     * 判断为数字或者字母
     * 
     * @param c 字符
     * @return 是否为数字或者字母
     */
    public static boolean isLetterOrDigit(char c) {
        return Character.isLetterOrDigit(c);
    }

    /**
     * 判断为字母
     * 
     * @param c 字符
     * @return 是否为字母
     */
    public static boolean isLetter(char c) {
        return Character.isLetter(c);
    }

    /**
     * 判断一个字符是否为空白字符（包括中文空白字符 和 英文空白字符）
     * 
     * @param c 原始字符
     * @return ture是空白字符，false不是空白字符。
     */
    public static boolean isWhitespace(char c) {
        return StringUtil.isWhitespace(c);
    }

    private static final String RegexpSingleChinese = "\\s[^\\s]\\s";

    /**
     * @param subStr 范例文本：“ 信 ”；
     * @return 单字前后都有“ ” 并且 中间是一个汉字 则返回 true；否则，反正。
     */
    private static boolean isSingleChinese(String subStr) {
        return subStr.matches(RegexpSingleChinese) && isChinese(subStr.charAt(1));
    }

    /**
     * 由_parserMaxFirst(...) 或者 _parserMixFirst(...)产生的 范例文本格式
     * 
     * @param text 范例文本："在杭州 信 雅 达 股份有限公司"；
     * @return 修正可能性 新词 后的分词字符串
     */
    private static String reviseNewWord(String text) {
        if (StringUtil.isEmpty(text)) {
            return "";
        }

        StringBuffer buf = new StringBuffer();
        text = text.replaceAll("\\s{2,}", " ");
        // 跳过前1个字符
        buf.append(text.substring(0, 1));

        // 分析中间文本
        for (int i = 1; i < text.length() - 1; i++) {
            String subStr = text.substring(i - 1, i + 2);
            int j = 0;
            boolean flag = false;
            // 查找出连续的单字词串
            while (isSingleChinese(subStr)) {
                j += 2;

                flag = true;

                if ((i + j + 2) > text.length()) {
                    break;
                }

                subStr = text.substring(i + j - 1, i + j + 2);
            }

            if (flag) {
                buf.append(text.substring(i, i + j - 1).replaceAll("\\s", "")).append(" ");
                i += j - 1;
            } else {
                buf.append(text.charAt(i));
            }
        }

        // 跳过最后3个文本
        buf.append(text.substring(text.length() - 1, text.length()));

        return buf.toString();
    }

    /**
     * 最大词优先匹配 法则切分 文章词
     * 
     * @param text 原始文本。
     * @return Chinese对象数组
     */
    public Chinese[] parserMaxFirst(String text) {
        return parserMaxFirst(text, false);
    }

    /**
     * 最大词优先匹配 法则切分 文章词
     * 
     * @param text 原始文本。
     * @param isRemoveStopWord true开启去掉停词功能；false反之。
     * @return Chinese对象数组
     */
    public Chinese[] parserMaxFirst(String text, boolean isRemoveStopWord) {
        return parserMaxFirst(text, isRemoveStopWord, null);
    }

    /**
     * 匹配我们需要的词性结果集合
     * 
     * @param text 原始文本。
     * @param isRemoveStopWord true开启去掉停词功能；false反之。
     * @param filter Collection<Chinese.Character>需要词性集合
     * @return 过滤我们需要的词性词结果集合
     */
    public Chinese[] parserMaxFirst(String text, boolean isRemoveStopWord, Collection<Chinese.Character> filter) {
        List<Chinese> lib = new ArrayList<Chinese>();

        String separatedText = _parserMaxFirst(text);
        switch (instanceModel) {
            case ExactModel:
                // 修正 带可能性新词 分析模块
                separatedText = reviseNewWord(separatedText);
                break;
            default:
                break;
        }

        String[] words = separatedText.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            pushWord(lib, words[i], isRemoveStopWord, filter);
        }

        return toArray(lib);
    }

    /**
     * 最大词优先匹配 法则切分 的词的字符串
     * 
     * @param text 原始文本。
     * @return 以空格隔开的词的字符串
     */
    public String parserMaxFirstBySeparator(String text) {
        String separatedText = _parserMaxFirst(text);
        switch (instanceModel) {
            case ExactModel:
            case MiningModel:
                // 修正 带可能性新词 分析模块
                separatedText = reviseNewWord(separatedText);
                break;
            default:
                break;
        }
        return separatedText;
    }

    /**
     * 最大词优先匹配 法则切分 的词的字符串
     * 
     * @param text 原始文本。
     * @param separator 分隔符
     * @return 以分隔符隔开的词的字符串
     */
    public String parserMaxFirstBySeparator(String text, String separator) {
        return parserMaxFirstBySeparator(text).replaceAll("\\s+", separator);
    }

    /**
     * 最大词优先匹配 法则切分 的词的字符串
     * 
     * @param text 原始文本。
     * @return 以 ' ' 隔开的词的字符串
     */
    private String _parserMaxFirst(String text) {
        // 为空跳出
        if (text == null) {
            return "";
        }

        // copy一个缓冲区
        StringBuffer buf = new StringBuffer(text);
        // 根据文本长度去遍历 分词
        for (int i = 0; i < buf.length(); i++) {
            char ci = buf.charAt(i);

            // 是否空白字符
            if (isWhitespace(ci)) {
                continue;
            }

            // 是中文的时候
            if (isChinese(ci)) {
                IntegerTreeMap tm = dicBasic.get(ci);

                // 以ci字排头的词树为空 则 跳出继续下一个字的循环
                if (tm == null) {
                    continue;
                }

                int maxWordLength = tm.maxWordLength;
                // 最大词匹配优先，按从最大的词开始匹配，如果匹配到则跳出。
                // 最大词匹配优先算法 将考虑单字词(j>0)
                for (int j = maxWordLength; j > 0; j--) {
                    // 临时切分新词片段 超出范围则继续
                    if ((i + j) > buf.length()) {
                        continue;
                    }

                    String word = buf.substring(i, i + j);
                    if (tm.words.contains(new Chinese(word))) {
                        // 分词 词前 插入分隔符
                        buf.insert(i, DEFAULT_SEPERATOR_CHAR);
                        // i步进j，然后跳出循环
                        i += j + 1;
                        // 分词 此后 插入分隔符
                        buf.insert(i, DEFAULT_SEPERATOR_CHAR);

                        // 跳出
                        break;
                    }
                }
                // 处理英文+数字的时候
            } else if (isLetterOrDigit(ci)) {
                int step = 0;
                do {
                    step += 1;

                    if ((i + step) < buf.length()) {
                        ci = buf.charAt(i + step);
                    } else {
                        break;
                    }
                } while (!isWhitespace(ci) && !isChinese(ci));

                // 分词 词前 插入分隔符
                buf.insert(i, DEFAULT_SEPERATOR_CHAR);
                // i步进
                i += step + 1;
                // 分词 此后 插入分隔符
                buf.insert(i, DEFAULT_SEPERATOR_CHAR);
            } else {// 其他的视为标点符号，标点符号时候前后各插入一个分隔符
                // 分词 词前 插入分隔符
                buf.insert(i, DEFAULT_SEPERATOR_CHAR);
                // i步进2，然后跳出循环
                i += 2;
                // 分词 此后 插入分隔符
                buf.insert(i, DEFAULT_SEPERATOR_CHAR);
            }
        }

        return buf.toString();
    }

    /**
     * 最小词优先匹配 法则切分 文章词
     * 
     * @param text 原始文本。
     * @return Chinese对象数组
     */
    public Chinese[] parserMinFirst(String text) {
        return parserMinFirst(text, false);
    }

    /**
     * 最小词优先匹配 法则切分 文章词
     * 
     * @param text 原始文本。
     * @param isRemoveStopWord true开启去掉停词功能；false反之。
     * @return Chinese对象数组
     */
    public Chinese[] parserMinFirst(String text, boolean isRemoveStopWord) {
        return parserMinFirst(text, isRemoveStopWord, null);
    }

    /**
     * 最小词优先匹配 法则切分 文章词 同时带词性匹配
     * 
     * @param text 原始文本。
     * @param isRemoveStopWord true开启去掉停词功能；false反之。
     * @param filter Collection<Chinese.Character>需要的词性结合
     * @return Chinese对象数组
     */
    public Chinese[] parserMinFirst(String text, boolean isRemoveStopWord, Collection<Chinese.Character> filter) {
        List<Chinese> lib = new ArrayList<Chinese>();
        String[] words = _parserMinFirst(text).split("\\s+");
        for (int i = 0; i < words.length; i++) {
            pushWord(lib, words[i], isRemoveStopWord, filter);
        }
        return toArray(lib);
    }

    /**
     * 最小词优先匹配 法则切分 的词的字符串
     * 
     * @param text 原始文本。
     * @return 以空格隔开的词的字符串
     */
    public String parserMinFirstBySeparator(String text) {
        return _parserMinFirst(text);
    }

    /**
     * 最小词优先匹配 法则切分 的词的字符串
     * 
     * @param text 原始文本。
     * @param separator 分隔符
     * @return 以分隔符隔开的词的字符串
     */
    public String parserMinFirstBySeparator(String text, String separator) {
        return _parserMinFirst(text).replaceAll("\\s+", separator);
    }

    /**
     * 最小词优先匹配 法则切分 的词的字符串
     * 
     * @param text 原始文本。
     * @return 以 ' ' 隔开的词的字符串
     */
    private String _parserMinFirst(String text) {
        // 为空跳出
        if (text == null) {
            return "";
        }

        // copy一个缓冲区
        StringBuffer buf = new StringBuffer(text);
        // 根据文本长度去遍历 分词
        for (int i = 0; i < buf.length(); i++) {
            char ci = buf.charAt(i);

            // 是否空白字符
            if (isWhitespace(ci)) {
                continue;
            }

            // 是中文的时候
            if (isChinese(ci)) {
                IntegerTreeMap tm = dicBasic.get(ci);

                // 以ci字排头的词树为空 则 跳出继续下一个字的循环
                if (tm == null) {
                    continue;
                }

                int wordMaxLength = tm.maxWordLength;
                // 最小词匹配优先，按从最大的词开始匹配，如果匹配到则跳出。
                // 起点为(j=2) 目的是去掉单字词
                for (int j = 2; j <= wordMaxLength; j++) {
                    if ((i + j) > buf.length()) {
                        continue;
                    }

                    String word = buf.substring(i, i + j);
                    if (tm.words.contains(new Chinese(word))) {
                        // 分词
                        buf.insert(i, DEFAULT_SEPERATOR_CHAR);
                        // i步进j，然后跳出循环
                        i += j + 1;
                        // 分词
                        buf.insert(i, DEFAULT_SEPERATOR_CHAR);

                        // 跳出
                        break;
                    }
                }
                // 处理英文+数字的时候
            } else if (isLetterOrDigit(ci)) {
                int step = 0;
                do {
                    step += 1;

                    if ((i + step) < buf.length()) {
                        ci = buf.charAt(i + step);
                    } else {
                        break;
                    }
                } while (!isWhitespace(ci) && !isChinese(ci));

                // 分词
                buf.insert(i, DEFAULT_SEPERATOR_CHAR);
                // i步进
                i += step + 1;
                // 分词
                buf.insert(i, DEFAULT_SEPERATOR_CHAR);
            } else {// 其他的视为标点符号，标点符号时候前后各插入一个分隔符
                // 分词 词前 插入分隔符
                buf.insert(i, DEFAULT_SEPERATOR_CHAR);
                // i步进2，然后跳出循环
                i += 2;
                // 分词 此后 插入分隔符
                buf.insert(i, DEFAULT_SEPERATOR_CHAR);
            }
        }

        return buf.toString();
    }

    /**
     * 按最小词 至 最大词 匹配法则切分 文章词
     * 
     * @param text 原始文本。
     * @return Chinese对象数组
     */
    public Chinese[] parserMinToMax(String text) {
        return parserMinToMax(text, false);
    }

    /**
     * 按最小词 至 最大词 匹配法则切分 文章词
     * 
     * @param text 原始文本。
     * @param isRemoveStopWord true开启去掉停词功能；false反之。
     * @return Chinese对象数组
     */
    public Chinese[] parserMinToMax(String text, boolean isRemoveStopWord) {
        return parserMinToMax(text, isRemoveStopWord, null);
    }

    /**
     * 按最小词 至 最大词 匹配法则切分 文章词
     * 
     * @param text 原始文本。
     * @param isRemoveStopWord true开启去掉停词功能；false反之。
     * @param filter 选择需要的词性集合
     * @return Chinese对象数组
     */
    public Chinese[] parserMinToMax(String text, boolean isRemoveStopWord, Collection<Chinese.Character> filter) {
        Chinese[] a = parserMaxFirst(text, isRemoveStopWord, filter);
        Chinese[] b = parserMinFirst(text, isRemoveStopWord, filter);
        Set<Chinese> set = new HashSet<Chinese>();
        for (int i = 0; i < a.length; i++) {
            set.add(a[i]);
        }
        for (int i = 0; i < b.length; i++) {
            boolean flag = true;
            for (int j = 0; j < a.length; j++) {
                if (a[j].equals(b[i])) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                set.add(b[i]);
            }
        }
        return set.toArray(new Chinese[set.size()]);
    }

    /**
     * 把Chinese对象数组 以 分隔符分开的字符串
     * 
     * @param words Chinese对象数组
     * @param separator 分隔字符
     * @return 以分隔字符分开的字符串
     */
    public static String toWordsWithSeparator(Chinese[] words, String separator) {
        StringBuffer buf = new StringBuffer();
        if (words.length == 1) {
            return words[0].getWord();
        } else if (words.length > 1) {
            for (int i = 0; i < words.length - 1; i++) {
                buf.append(words[i].getWord()).append(separator);
            }
            buf.append(words[words.length - 1].getWord());
        }
        return buf.toString();
    }

    /**
     * 转换Chinese对象数组 至 二维字符串数组
     * 
     * @param words Chinese对象数组
     * @return 二维字符串数组 一维下标 表示词匹配到的组数，二维下标 是个3长度数组[0下标对应词，1下标对应词频，2小标对应词性] 格式举例 ["[][0]中国","[][1]5","[][2]0"]
     */
    public static String[][] toArray(Chinese[] words) {
        String[][] _words = new String[words.length][3];
        for (int i = 0; i < words.length; i++) {
            Chinese word = words[i];

            _words[i][0] = word.getWord();
            _words[i][1] = String.valueOf(word.getFrequency());
            _words[i][2] = String.valueOf(word.getCharacter().toString());
        }

        return _words;
    }

    /**
     * 单词的 字符串数组
     * 
     * @param words Chinese对象数组
     * @return 一维字符串数组
     */
    public static String[] toWords(Chinese[] words) {
        String[] _words = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            Chinese word = words[i];

            _words[i] = word.getWord();
        }

        return _words;
    }

    /**
     * @see #sortByFrequency(Chinese[])
     */
    public static Chinese[] sort(Chinese[] words) {
        return sortByFrequency(words);
    }

    /**
     * 对Chinese对象数组进行排序，按Chinese词频递减排序
     * 
     * @param words Chinese对象数组
     * @return 排序后的Chinese对象数组
     */
    public static Chinese[] sortByFrequency(Chinese[] words) {
        for (int i = 0; i < words.length; i++) {
            for (int j = i; j < words.length; j++) {
                if (words[i].getFrequency() < words[j].getFrequency()) {
                    Chinese tmp = words[i];
                    words[i] = words[j];
                    words[j] = tmp;
                }
            }
        }

        return words;
    }

    /**
     * key为词性Chinese.Character，value为List&lt;Chinese&gt;
     * 
     * @param words Chinese对象数组
     * @return Map<Chinese.Character,List<Chinese>>
     */
    // public static Map<Chinese.Character,List<Chinese>> groupByCharacter(Chinese[] words)
    // {
    // Map<Chinese.Character,List<Chinese>> lib = new HashMap<Chinese.Character,List<Chinese>>();
    // for(int i=0;i<words.length;i++)
    // {
    // Set<Chinese.Character> set = words[i].getCharacter();
    // if(0==set.size())
    // {
    // List<Chinese> tmp = lib.get(Chinese.Character.NONE);
    // if(tmp == null){
    // tmp = new ArrayList<Chinese>();
    // lib.put(Chinese.Character.NONE, tmp);
    // }
    // tmp.add(words[i]);
    // }else{
    // Iterator<Chinese.Character> t = set.iterator();
    //				
    // while(t.hasNext()){
    // Chinese.Character cc = t.next();
    // List<Chinese> tmp = lib.get(cc);
    // if(tmp == null){
    // tmp = new ArrayList<Chinese>();
    // lib.put(cc, tmp);
    // }
    // tmp.add(words[i]);
    // }
    // }
    // }
    // return lib;
    // }
}
