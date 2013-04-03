package org.open.crawler.html.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * 友好类，仅包内可见
 *
 * @author Qin Zhipeng 2010-09-17 22:13
 */
class NodeScoreFactory {

    static List<TextNodeScore> buildGeneralNodeScore(List<NodeCounter> listNodeCounter) {
        List<TextNodeScore> listGeneralNodeScore = new ArrayList<TextNodeScore>(listNodeCounter.size());
        for (NodeCounter nodeCounter : listNodeCounter) {
            listGeneralNodeScore.add(new TextNodeScore(nodeCounter));
        }
        return listGeneralNodeScore;
    }

}
