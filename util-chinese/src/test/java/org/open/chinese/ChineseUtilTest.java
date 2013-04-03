package org.open.chinese;

import junit.framework.TestCase;


public class ChineseUtilTest extends TestCase {

    private ChineseUtil cu = ChineseUtil.instance(ChineseUtil.InstanceModel.ExactModel);
    
    public void testParserMaxFirstString() {
        System.out.println("Begin.");
        
        Chinese[] chineseArray = cu.parserMaxFirst("我爱足球");
        for(Chinese chinese : chineseArray){
            System.out.println(chinese.toString());
        }
        
        System.out.println("End.");
    }

    public void testParserMaxFirstStringBoolean() {
        
    }

    public void testParserMaxFirstStringBooleanCollectionOfCharacter() {
        
    }

    public void testParserMaxFirstBySeparatorString() {
        
    }

    public void testParserMaxFirstBySeparatorStringString() {
        
    }

    public void testParserMinFirstString() {
        
    }

    public void testParserMinFirstStringBoolean() {
        
    }

    public void testParserMinFirstStringBooleanCollectionOfCharacter() {
        
    }

    public void testParserMinFirstBySeparatorString() {
        
    }

    public void testParserMinFirstBySeparatorStringString() {
        
    }

    public void testParserMinToMaxString() {
        
    }

    public void testParserMinToMaxStringBoolean() {
        
    }

    public void testParserMinToMaxStringBooleanCollectionOfCharacter() {
        
    }

}
