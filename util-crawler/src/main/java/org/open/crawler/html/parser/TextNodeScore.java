package org.open.crawler.html.parser;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 友好类，仅包内可见
 *
 * @author Qin Zhipeng 2010-09-17 22:13
 */
public class TextNodeScore implements Comparator<TextNodeScore>, Comparable<TextNodeScore> {

    private final static float DefaultWeight = 0f;
    private final static float MaxWeight     = 1f;
    private final static float MinWeight     = -MaxWeight;

    enum ScoreWeight {

        /**
         * "text"
         */
        Text("text", MaxWeight),

        /**
         * "a"
         */
        A("a", MinWeight),

        /**
         * "img"
         */
        Img("img", DefaultWeight),

        /**
         * "input"
         */
        Input("input", MinWeight);

        private String                          nodeName;
        private double                          weight;
        private static Map<String, ScoreWeight> pool = new LinkedHashMap<String, ScoreWeight>();

        private ScoreWeight(String nodeName, float weight) {
            this.nodeName = nodeName;
            this.weight = weight;
            if (MinWeight > this.weight || MaxWeight < this.weight) {
                throw new IllegalArgumentException("range -1~1.");
            }
        }

        static {
            for (ScoreWeight scoreWeight : ScoreWeight.values()) {
                pool.put(scoreWeight.nodeName, scoreWeight);
            }
        }

        public static double getWeight(String nodeName) {
            ScoreWeight scoreWeight = pool.get(nodeName);
            return (null == scoreWeight) ? Double.NaN : scoreWeight.weight;
        }

        public String getNodeName() {
            return nodeName;
        }

        public double getWeight() {
            return weight;
        }
    }

    NodeCounter    nodeCounter;
    double         weight;
    private String CalcWeightMethod;

    public TextNodeScore(NodeCounter nodeCounter) {
        this.nodeCounter = nodeCounter;
    }

    private final static double BalanceFactor = 1.0 / 5;

    /**
     * <pre>
     * 文本算法这里有2种：
     *  第一种：大文本容器优先算法；
     *  第二种：高文本率优先算法；
     *  实际本运用算法是分别按2种算法算，然后取2种算法的平均值
     * </pre>
     *
     * @return
     */
    public strictfp double calcWeight() {
        // 链接+按钮超过5个则按高文本率优先算法
        if ((this.nodeCounter.htmlANodeCount + this.nodeCounter.htmlInputNodeCount) >= 5) {
            this.weight = calcWeight_2();
            CalcWeightMethod = "calcWeight_2(高文本率优先算法)";
        }
        // 如果绝对文本高于50字符则按大文本容器优先算法
        else if ((this.nodeCounter.htmlTextNodeCount - this.nodeCounter.htmlATextNodeCount) >= 50) {
            this.weight = calcWeight_1();
            CalcWeightMethod = "calcWeight_2(大文本容器优先算法)";
        }
        // 其他情况取 平均值
        else {
            this.weight = (calcWeight_1() + calcWeight_2()) / 2;
            CalcWeightMethod = "calcWeight(平均值算法)";
        }
        // 平滑曲线
        weight = (weight >= 0) ? Math.pow(weight, BalanceFactor) : -Math.pow(Math.abs(weight), BalanceFactor);

        return weight;
    }

    /**
     * 第一种：大文本容器优先算法；
     *
     * @return
     */
    private strictfp double calcWeight_1() {
        double positiveWeight = 0f;
        double negativeWeight = 0f;
        for (ScoreWeight scoreWeight : ScoreWeight.values()) {
            double weight = scoreWeight.weight;
            switch (scoreWeight) {
                case A:
                    weight *= nodeCounter.htmlANodeCount;
                    break;
                case Text:
                    // 大文本容器优先算法
                    weight *= (nodeCounter.htmlTextNodeCount - nodeCounter.htmlATextNodeCount);
                    break;
                case Img:
                    weight *= nodeCounter.htmlImgNodeCount;
                    break;
                case Input:
                    weight *= nodeCounter.htmlInputNodeCount;
                    break;
                default:
                    break;
            }
            if (weight > 0) {
                positiveWeight += weight;
            } else if (weight < 0) {
                negativeWeight += weight;
            }
        }

        if (0 == negativeWeight) {
            weight = positiveWeight;
        } else {
            weight = Math.abs(positiveWeight / negativeWeight);
        }
        // control direction.
        weight = (negativeWeight + positiveWeight) < 0 ? -weight : weight;
        // balance weight value.
        // weight = (weight >= 0) ? Math.pow(weight, BalanceFactor) : -Math.pow(Math.abs(weight), BalanceFactor);

        return weight;
    }

    /**
     * 第二种：高文本率优先算法；
     *
     * @return
     */
    private strictfp double calcWeight_2() {
        double positiveWeight = 0f;
        double negativeWeight = 0f;
        for (ScoreWeight scoreWeight : ScoreWeight.values()) {
            double weight = scoreWeight.weight;
            switch (scoreWeight) {
                case A:
                    weight *= nodeCounter.htmlANodeCount;
                    break;
                case Text:
                    // 高文本率优先算法
                    positiveWeight += weight * (nodeCounter.htmlTextNodeCount - nodeCounter.htmlATextNodeCount);
                    negativeWeight -= weight * nodeCounter.htmlATextNodeCount;
                    continue;
                case Img:
                    weight *= nodeCounter.htmlImgNodeCount;
                    break;
                case Input:
                    weight *= nodeCounter.htmlInputNodeCount;
                    break;
                default:
                    break;
            }
            if (weight > 0) {
                positiveWeight += weight;
            } else if (weight < 0) {
                negativeWeight += weight;
            }
        }

        if (0 == negativeWeight) {
            weight = positiveWeight;
        } else {
            weight = Math.abs(positiveWeight / negativeWeight);
        }
        // control direction.
        weight = (negativeWeight + positiveWeight) < 0 ? -weight : weight;
        // balance weight value.
        // weight = (weight >= 0) ? Math.pow(weight, BalanceFactor) : -Math.pow(Math.abs(weight), BalanceFactor);

        return weight;
    }

    @Override
    public int compareTo(TextNodeScore o) {
        return Double.valueOf(calcWeight()).compareTo(o.calcWeight());
    }

    @Override
    public int compare(TextNodeScore o1, TextNodeScore o2) {
        return compare(o1, o2);
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GeneralNodeScore [nodeCounter=");
        builder.append(nodeCounter);
        builder.append(", weight=");
        builder.append(weight);
        builder.append(", CalcWeightMethod=");
        builder.append(CalcWeightMethod);
        builder.append("]");
        return builder.toString();
    }

}
