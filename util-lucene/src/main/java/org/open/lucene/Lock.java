package org.open.lucene;

import java.io.File;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

/**
 * [单态设计类] lucene库更新锁类（lucene自己更新锁机制不是很稳定，特写此类）
 * 
 * @author 覃芝鹏
 * @version $Id: Lock.java,v 1.2 2009/07/07 09:00:00 moon Exp $
 */
public class Lock {

    private static final Log  log      = LogFactory.getLog(Lock.class);

    /**
     * 存储 各个lucene库路径 连接实例
     */
    private static Properties lockPool = new Properties();

    /**
     * 获得类实例
     */
    public static Lock        instance = new Lock();

    private Lock() {

    }

    /**
     * 从池子中打开一个写连接
     * 
     * @param lucenePath 更定lucene路径连接
     * @return org.apache.lucene.index.IndexWriter
     * @see org.apache.lucene.index.IndexWriter
     */
    public IndexWriter openIndexWriter(String lucenePath) {
        try {
            Object obj = lockPool.get(lucenePath);
            if (obj == null) {
                IndexWriter writer = null;
                File file = new File(lucenePath);
                FSDirectory dir = FSDirectory.open(file);
                if (file.exists()) {
                    // 给定lucene路径 存在lucene索引则直接打开
                    try {
                        writer = new IndexWriter(dir, new ChineseAnalyzer(), false,
                                                 IndexWriter.MaxFieldLength.UNLIMITED);
                    } catch (Exception e) {
                        log.error("openIndexWriter(Path:" + lucenePath, e);
                    }
                } else {
                    // 没有 则 新建一个
                    writer = new IndexWriter(dir, new ChineseAnalyzer(), true, IndexWriter.MaxFieldLength.UNLIMITED);
                }
                lockPool.put(lucenePath, writer);

                return writer;
            } else {
                return (IndexWriter) obj;
            }
        } catch (Exception e) {
            log.error("LucenePathUpdateLock openIndexWriter(" + lucenePath + ") error!\n", e);
            return null;
        }
    }

    /**
     * 关闭给定的lucene库路径所有操作
     * 
     * @param lucenePath lucene库路径
     */
    public void closeLucenePath(String lucenePath) {
        try {
            IndexWriter writer = (IndexWriter) lockPool.get(lucenePath);

            if (writer != null) {
                writer.optimize();
                writer.close();
            }

            lockPool.remove(lucenePath);
        } catch (Exception e) {
            log.error("LucenePathUpdateLock closeLucenePath(" + lucenePath + ") error!\n", e);
        }
    }

}
