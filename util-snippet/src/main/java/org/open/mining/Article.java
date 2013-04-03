package org.open.mining;

import java.util.List;
import java.util.ArrayList;

import org.open.chinese.Chinese;

public class Article
{
	private String digest;
	
	private List<Chinese> wordLib = new ArrayList<Chinese>();
	
	public List<Chinese> getWordLib() {
		return wordLib;
	}

	public void setWordLib(List<Chinese> wordLib) {
		this.wordLib = wordLib;
	}

	public Article(String digest){
		this.digest = digest;
	}
	
	public Article(String digest,Chinese[] words)
	{
		this.digest = digest;
		for(int i=0;i<words.length;i++)
		{
			this.wordLib.add(words[i]);
		}
	}
	
	public boolean equals(Object anObject)
	{
		if (this == anObject) {
		    return true;
		}
		if (anObject instanceof Article) {
		    return this.digest.equals(((Article)anObject).digest);
		}
		return false;
	}
	
	public String toString()
	{
		return "[digest:"+this.digest+",wordLib:"+this.wordLib+"]";
	}
}