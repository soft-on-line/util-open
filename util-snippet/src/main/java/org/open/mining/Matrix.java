package org.open.mining;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import org.open.chinese.Chinese;

public class Matrix
{
	private List<Chinese> wordLib = new ArrayList<Chinese>();
	
	private List<Article> articleLib = new ArrayList<Article>();
	
	public List<Chinese> getWordLib() {
		return wordLib;
	}

	public void setWordLib(List<Chinese> wordLib) {
		this.wordLib = wordLib;
	}
	
	public List<Article> getArticleLib() {
		return articleLib;
	}

	public void setArticleLib(List<Article> articleLib) {
		this.articleLib = articleLib;
	}

	private double[][] tf_idf;
	
	public void addArticle(String digest,Chinese[] words)
	{
		for(int i=0;i<words.length;i++)
		{
			if(!wordLib.contains(words[i])){
				wordLib.add(words[i]);
			}
		}
		
		articleLib.add(new Article(digest,words));
	}
	
	public double[][] getTfIdf()
	{
		if(tf_idf == null){
			run();
		}
		return tf_idf;
	}
	
	public double[] getTfIdf(int articleIndex)
	{
		double[] buf = new double[wordLib.size()];
		for(int j=0;j<wordLib.size();j++)
		{
			buf[j] = _tf_idf(articleLib.get(articleIndex),wordLib.get(j));
		}
		return buf;
	}
	
	public double[] getTf(int articleIndex)
	{
		double[] buf = new double[wordLib.size()];
		for(int j=0;j<wordLib.size();j++)
		{
			buf[j] = _tf(articleLib.get(articleIndex),wordLib.get(j));
		}
		return buf;
	}
	
	private void run()
	{
		double[][] _tf_idf = new double[articleLib.size()][wordLib.size()];
		
		for(int i=0;i<articleLib.size();i++)
		{
			for(int j=0;j<wordLib.size();j++)
			{
				_tf_idf[i][j] = _tf_idf(articleLib.get(i),wordLib.get(j));
			}
		}
		
		this.tf_idf = _tf_idf;
	}
	
	private double _tf(Article article,Chinese chinese)
	{
		int index = article.getWordLib().indexOf(chinese);
		if(-1 == index){
			return 0;
		}
		
		float f = article.getWordLib().get(index).getFrequency();
		return f / article.getWordLib().size();
	}
	
	private double _idf(Article article,Chinese chinese)
	{
		int wordFrq = 0;
		for(int i=0;i<articleLib.size();i++)
		{
			Article tmp = articleLib.get(i);
			int article_wordLib_index = tmp.getWordLib().indexOf(chinese); 
			if(-1 != article_wordLib_index){
				wordFrq += 1;
			}
		}

		double idf = Math.log(1.0f*articleLib.size()/wordFrq);
		return idf;
	}
	
	private double _tf_idf(Article article,Chinese chinese)
	{
		int index = article.getWordLib().indexOf(chinese);
		if(-1 == index){
			return 0;
		}
		
		Chinese _chinese = article.getWordLib().get(index);
		_chinese.setWeight(_tf(article,chinese) * _idf(article,chinese));
		return _chinese.getWeight();
	}
	
	public Set<Chinese> getTiptopChinese(Set<Integer> index)
	{
		if(tf_idf == null){
			run();
		}
		
		double max_tf_idf = Double.MIN_VALUE;
		Set<Chinese> area_wordLib = new HashSet<Chinese>(); 
		Iterator<Integer> e = index.iterator();
		while(e.hasNext())
//		for(int i=0;i<index.size();i++)
		{
			//double[] buf = tf_idf[index.get(i)];
			int dataIndex = e.next();
			double[] buf = tf_idf[dataIndex];
			double _max_tf_idf = Double.MIN_VALUE;
			Set<Chinese> article_wordLib = new HashSet<Chinese>(); 
			for(int j=0;j<buf.length;j++)
			{
				if(buf[j] > _max_tf_idf){
					_max_tf_idf = buf[j];
					article_wordLib.clear();
					article_wordLib.add(wordLib.get(j));
				}else if(buf[j] == _max_tf_idf){
					article_wordLib.add(wordLib.get(j));
				}
			}
			
			if(_max_tf_idf > max_tf_idf){
				max_tf_idf = _max_tf_idf;
				area_wordLib = article_wordLib;
			}
		}
		return area_wordLib;
	}
}