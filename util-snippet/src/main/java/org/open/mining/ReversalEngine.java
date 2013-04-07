package org.open.mining;


import java.io.File;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.open.chinese.Chinese;
import org.open.chinese.ChineseUtil;
import org.open.db.ResultSet;
import org.open.db.Transaction;
import org.open.util.CodeUtil;
import org.open.util.ReaderUtil;

public class ReversalEngine {

	//写日志.
	private static final Log   log = LogFactory.getLog(ReversalEngine.class);

	private static ChineseUtil cu  = ChineseUtil.instance(ChineseUtil.InstanceModel.MiningModel);

	private Mining             mining;

	public ReversalEngine(Mining mining) {
		this.mining = mining;
	}

	private int studyArticle(String article, String digest) {
		long st = new Date().getTime();

		Transaction transaction = mining.getTransaction();

		try {
			Chinese[] words = cu.parserMaxFirst(article, true);
			for (int i = 0; i < words.length; i++) {
				String query_word = "select count(*) from word where name = ?;";
				String update_word = "insert into word(name) values(?);";

				if (!transaction.prepareExecuteQueryCountHave(query_word, new String[] { words[i].getWord() })) {
					transaction.prepareExecuteUpdate(update_word, new String[] { words[i].getWord() });
				}

				String query_article = "select count(*) from article where digest = ?;";
				String update_article = "insert into article(digest,content) values(?,?);";

				if (!transaction.prepareExecuteQueryCountHave(query_article, new String[] { digest })) {
					transaction.prepareExecuteUpdate(update_article, new String[] { digest, article });
				}

				String query_reversal = "select count(*) from reversal r,word w,article a where w.name = ? and a.digest = ? and r.word = w.id and r.article = a.id;";
				String update_reversal = "insert into reversal(word,article,frequency) select w.id,a.id,? from word w,article a where w.name=? and a.digest=?;";

				if (!transaction.prepareExecuteQueryCountHave(query_reversal, new String[] { words[i].getWord(), digest })) {
					transaction.prepareExecuteUpdate(update_reversal, new String[] { String.valueOf(words[i].getFrequency()), words[i].getWord(), digest });
				}
			}

			transaction.commit();
		}
		catch (Exception e) {
			log.error("ReverseUtil insertWordArticle(" + article + ") error!", e);
			transaction.rollback();
			return -1;
		}
		finally {
			transaction.close();
		}

		log.info("Mining studyArticle() " + digest);
		log.info("studyArticle used time:" + (new Date().getTime() - st) / 1000.0f + "秒");

		return 1;
	}

	private boolean isArticleExist(String digest) {
		String query_sql = "select count(*) from article where digest = ?";
		return mining.queryResultSetHave(query_sql, new String[] { digest });
	}

	public int studyFromText(String article) {
		String digest = CodeUtil.md5string2string(article);

		if (!isArticleExist(digest)) {
			log.info("Article md5 is " + digest);

			return studyArticle(article, digest);
		} else {
			log.info("Exist Article md5 is " + digest);

			return 1;
		}
	}

	public int studyFromFile(String file) {
		return studyFromText(ReaderUtil.read(file));
	}

	public int studyFromFile(File filePath) {
		int count = 0;
		File[] file = filePath.listFiles();

		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				int status = studyFromFile(file[i].toString());
				if (1 == status) {
					count += 1;
					file[i].delete();
				} else {
					File newFile = new File(file[i].getParent() + "\\error\\" + file[i].getName());
					//					WriterUtil.mkdirs(newFile);
					file[i].renameTo(newFile);
				}
			} else if (file[i].isDirectory()) {
				//如果是目录
				count += studyFromFile(file[i]);
			}
		}

		return count;
	}

	public double getScoreForWord(String word1, String word2) {
		try {
			if (word1.equalsIgnoreCase(word2)) {
				return 1.0f;
			}

			//String query_word1_word2 = "select count(*) from reversal where word in (select id from word where name = ?) and article in (select r.article from reversal r,word w where w.name = ? and r.word = w.id)";
			String query_word1_word2 = "select count(tmp.article) from ( select distinct(r.article) as article from reversal r,word w where w.name = ? and r.word = w.id ) tmp where tmp.article in ( select distinct(r.article) from reversal r,word w where w.name = ? and r.word = w.id )";
			String query_count_article = "select count(*) from article";

			float count_word1_word2 = 0;
			float count_article = 0;

			ResultSet rs = null;
			try {
				log.debug("sql_word1_word2:" + query_word1_word2);

				rs = mining.queryResultSet(query_word1_word2, new String[] { word1, word2 });
				if (rs.next()) {
					count_word1_word2 = Integer.valueOf(rs.getString(1));

					log.debug("query_word1_word2:" + count_word1_word2);
				}
			}
			finally {
				rs.close();
			}

			try {
				rs = mining.queryResultSet(query_count_article);
				if (rs.next()) {
					count_article = Integer.valueOf(rs.getString(1));

					log.debug("count_article:" + count_article);
				}
			}
			finally {
				rs.close();
			}

			if (count_article == 0) {
				return 0;
			} else {
				return Math.pow(count_word1_word2 / count_article, 1d / 3);
			}
		}
		catch (Exception e) {
			log.error("ReverseUtil getScore(...) error!", e);
			return 0;
		}
	}

	public double getScoreForSentence(String sentence1, String sentence2) {
		try {
			if (sentence1.equalsIgnoreCase(sentence2)) {
				return 1.0f;
			}

			Chinese[] w1 = cu.parserMinToMax(sentence1, true);
			Chinese[] w2 = cu.parserMinToMax(sentence2, true);

			String w1_str = ChineseUtil.toWordsWithSeparator(w1, "','");
			String w2_str = ChineseUtil.toWordsWithSeparator(w2, "','");

			//String query_word1_word2 = "select count(*) from reversal where word in (select id from word where name in ('"+w1_str+"')) and article in (select r.article from reversal r,word w where w.name in ('"+w2_str+"') and r.word = w.id)";
			//String query_word1_word2 = "select count(*) from reversal where article in (select r.article from reversal r,word w where w.name in ('"+w1_str+"') and r.word = w.id) and article in (select r.article from reversal r,word w where w.name in ('"+w2_str+"') and r.word = w.id)";
			String query_word1_word2 = "select count(tmp.article) from ( select distinct(r.article) as article from reversal r,word w where w.name in ('" + w1_str + "') and r.word = w.id ) tmp where tmp.article in ( select distinct(r.article) from reversal r,word w where w.name in ('" + w2_str
			        + "') and r.word = w.id )";
			String query_count_article = "select count(*) from article";

			float count_word1_word2 = 0;
			float count_article = 0;

			ResultSet rs = null;
			try {
				log.debug("sql_word1_word2:" + query_word1_word2);

				rs = mining.queryResultSet(query_word1_word2);
				if (rs.next()) {
					count_word1_word2 = Integer.valueOf(rs.getString(1));

					log.debug("query_word1_word2:" + count_word1_word2);
				}
			}
			finally {
				rs.close();
			}

			try {
				rs = mining.queryResultSet(query_count_article);
				if (rs.next()) {
					count_article = Integer.valueOf(rs.getString(1));

					log.debug("count_article:" + count_article);
				}
			}
			finally {
				rs.close();
			}

			if (count_article == 0) {
				return 0;
			} else {
				return Math.pow(count_word1_word2 / count_article, 1d / 3);
			}
		}
		catch (Exception e) {
			log.error("ReverseUtil getScore(...) error!", e);
			return 0;
		}
	}
}
