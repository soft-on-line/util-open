package org.open.chinese;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.open.util.OperationUtil;

/**
 * 中文词 类定义
 * @author 覃芝鹏
 * @version $Id: Chinese.java,v 1.14 2008/08/16 03:04:16 moon Exp $
 */
public class Chinese implements Comparator<Chinese>,Comparable<Chinese>
{
	/**
	 * 定义词的类型
	 */
	public static final class Character implements Comparable<Character>
	{
		/**
		 * 词性映射表
		 */
		private static Map<String,Character> map = new HashMap<String,Character>();
		
		/**
		 * 词性标识名字
		 */
		private String name;
		
		/**
		 * 词性标识描述
		 */
		private String desc;
		
		/**
		 * @param name 词性标识名字
		 * @param desc 词性标识描述
		 */
		private Character(String name,String desc){
			this.name = name;
			this.desc = desc;
			map.put(name, this);
		}
		
		/**
		 * 名词
		 */
		public static final Character N = new Character("N","名词");
		
		/**
		 * 动词
		 */
		public static final Character V = new Character("V","动词");
		
		/**
		 * 形容词
		 */
		public static final Character ADJ = new Character("ADJ","形容词");
		
		/**
		 * 副词
		 */
		public static final Character ADV = new Character("ADV","副词");	
		
		/**
		 * 量词
		 */
		public static final Character CLAS = new Character("CLAS","量词");
		
		/**
		 * 拟声词
		 */
		public static final Character ECHO = new Character("ECHO","拟声词");			
		
		/**
		 * 结构助词
		 */
		public static final Character STRU = new Character("STRU","结构助词");
		
		/**
		 * 助词
		 */
		public static final Character AUX = new Character("AUX","助词");
		
		/**
		 * 并列连词
		 */
		public static final Character COOR = new Character("COOR","并列连词");
		
		/**
		 * 连词
		 */
		public static final Character CONJ = new Character("CONJ","连词");
		
		/**
		 * 前缀
		 */
		public static final Character SUFFIX = new Character("SUFFIX","前缀");
		
		/**
		 * 后缀
		 */
		public static final Character PREFIX = new Character("PREFIX","后缀");		
		
		/**
		 * 介词
		 */
		public static final Character PREP = new Character("PREP","介词");		
		
		/**
		 * 代词
		 */
		public static final Character PRON = new Character("PRON","代词");	
		
		/**
		 * 疑问词
		 */
		public static final Character QUES = new Character("QUES","疑问词");
		
		/**
		 * 数词
		 */
		public static final Character NUM = new Character("NUM","数词");
		
		/**
		 * 成语
		 */
		public static final Character IDIOM = new Character("IDIOM","成语");
		
		/**
		 * 未定义
		 */
		public static final Character NONE = new Character("NONE","未定义");
		
		public static Character getCharacter(String name){
			Character c = map.get(name);
			return (c==null)? NONE : c;
		}
		
		public String getName(){
			return this.name;
		}
		
		public String getDesc(){
			return this.desc;
		}
		
		public int compareTo(Character o) {
			return this.name.compareTo(o.getName());
		}
	
		public boolean equals(Object o){
			return this.name.equals(((Character)o).getName());
		}
		
		public String toString(){
			return this.name+"["+this.desc+"]";
		}
	}
	
	private String word;
	
	private int frequency;
	
	private double weight;
	
	private Character[] character;
	
	public Chinese(){
		
	}
	
	public Chinese(String word){
		this.word = word;
	}
	
	private boolean contains(Character character)
	{
		if(this.character==null){
			return false;
		}else{
			for(int i=0;i<this.character.length;i++){
				if(this.character[i].equals(character)){
					return true;
				}
			}
			return false;
		}
	}
	
	public void addCharacter(Character character){
		if(!contains(character)){
			this.character = OperationUtil.addAfter(this.character, character);
		}
	}
	
	public void setWord(String word){	this.word = word;	}
	public String getWord(){	return this.word;	}
	
	public void setFrequency(int frequency){	this.frequency = frequency;	}
	public int getFrequency(){	return this.frequency;	}
	
	public double getWeight() { return weight; }
	public void setWeight(double weight) { this.weight = weight; }

	public Character[] getCharacter() { return character; }
	public void setCharacter(Character[] character) { this.character = character; }
	
	public int compare(Chinese o1, Chinese o2) {
		return o2.frequency - o1.frequency;
	}
	
	public int compareTo(Chinese o) {
		//return (this.frequency<o.frequency)? 1 : 0;
		return this.word.compareTo(o.getWord());
	}
	
	public boolean equals(Object anObject)
	{
		if (this == anObject) {
		    return true;
		}
		if (anObject instanceof Chinese) {
		    return this.word.equals(((Chinese)anObject).getWord());
		}
		return false;
	}
	
	public int hashCode(){
		return this.word.hashCode();
	}
	
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("[word:");
		buf.append(this.word);
		buf.append(",weight:");
		buf.append(this.weight);
		buf.append(",frequency:");
		buf.append(this.frequency);
		buf.append(",character:");
		if(this.character!=null){
			for(int i=0;i<this.character.length;i++){
				buf.append(this.character[i]);
			}
		}
		buf.append("]");
		return buf.toString();
	}

}