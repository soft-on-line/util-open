package org.open.mining;


import java.io.File;
import java.io.IOException;

import org.open.chinese.ChineseUtil;
import org.open.util.ReaderUtil;

/**
 * 用于数据聚类
 * @author 覃芝鹏
 * @version $Id: Cluster.java,v 1.11 2008/08/14 05:51:07 moon Exp $
 */
public class Cluster {

	private Matrix             matrix = new Matrix();

	private static ChineseUtil cu     = ChineseUtil.instance(ChineseUtil.InstanceModel.MiningModel);

	public Cluster(File file) {
		String[] articles = null;
		try {
			articles = ReaderUtil.read(file);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < articles.length; i++) {
			putArticleToMatrix(String.valueOf(i), articles[i]);
		}
	}

	public Cluster(String[] articles) {
		for (int i = 0; i < articles.length; i++) {
			putArticleToMatrix(String.valueOf(i), articles[i]);
		}
	}

	private void putArticleToMatrix(String id, String article) {
		matrix.addArticle(id, cu.parserMaxFirst(article, true));
		//		matrix.addArticle(id,cu.parserMinToMax(article,true));
	}

	public Matrix getMatrix() {
		return matrix;
	}

	public Clan[] algorithmKMeans(int k) {
		return new KMeans(getMatrix().getTfIdf(), k).run();
	}

	public Clan[] algorithmKMeans(double[][] data, int k) {
		return new KMeans(data, k).run();
	}

	public Clan[] algorithmKSector(int k) {
		return new KSector(getMatrix().getTfIdf(), k).run();
	}

	public Clan[] algorithmKSector(double[][] data, int k) {
		return new KSector(data, k).run();
	}

}
