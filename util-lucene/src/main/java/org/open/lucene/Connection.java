package org.open.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

/**
 * 获得lucene查询连接
 * 
 * @author 覃芝鹏
 * @version $Id: Connection.java,v 1.1 2008/04/08 06:00:34 moon Exp $
 */
public class Connection {

    private static final Log log      = LogFactory.getLog(Connection.class);

    /**
     * @see org.apache.lucene.search.IndexSearcher
     */
    // private MultiSearcher searcher = null;
    private IndexSearcher    searcher = null;

    /**
     * 获得一个lucene查询连接
     * 
     * @see org.apache.lucene.search.IndexSearcher
     * @return org.apache.lucene.search.IndexSearcher
     */
    public IndexSearcher connection(String lucenePath) {
        try {
            /*
             * if(searcher == null) { IndexSearcher[] searchers = new IndexSearcher[FinalDefine.LUCENE_PATH_AMOUNT];
             * for(int i=0;i<FinalDefine.LUCENE_PATH_AMOUNT;i++) { searchers[i] = new
             * IndexSearcher(GlobalParameter.getLuceneCorpPath()+i); } searcher = new MultiSearcher(searchers); } return
             * searcher;
             */

            if (searcher == null) {
                searcher = new IndexSearcher(IndexReader.open(FSDirectory.open(new File(lucenePath)), true));
            }

            return searcher;
        } catch (IOException e) {
            log.error("LuceneConn open IndexSearcher(" + lucenePath + ") error!=>", e);
            return null;
        }
    }

    /**
     * 关闭查询连接
     */
    public void close() {
        try {
            if (searcher != null) {
                searcher.close();
            }
            searcher = null;
        } catch (IOException e) {
            log.error("CorpLuceneConnection close MultiSearcher error!=>", e);
        }
    }
}
