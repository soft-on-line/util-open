package org.open.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.open.PageModel;

/**
 * 操作自定义Lucene数据库主类
 * 
 * @author moon
 * @version $Id: Engine.java,v 1.11 2008/11/26 07:38:39 moon Exp $
 */
public class Engine {

    // 记录日志
    private static final Log  log      = LogFactory.getLog(Engine.class);

    /**
     * lucene分析器 默认中文分析器
     * 
     * @see org.apache.lucene.analysis.Analyzer
     */
    private static Analyzer   analyzer = new ChineseAnalyzer();

    /**
     * 定义bean的所有lucene字段信息，包括是否存储，是否加权等等。
     */
    private static Fields     Fields;

    /**
     * 当前实例存储 lucene 索引的路径
     */
    private String            lucenePath;

    /**
     * 存储 以 lucenePath 为唯一实例的 Engine
     */
    private static Properties pool     = new Properties();

    private Engine(String path) {
        this.lucenePath = path;
    }

    /**
     * 同步方法体，用来克制lucene写 和 更新操作同时进行
     * 
     * @param writer IndexWriter
     * @param terms Term[]
     * @param docs Document[]
     */
    // private synchronized int writerLucene(Term[] terms,Document[] docs)
    private synchronized int writerLucene(Term[] terms, Bean[] beans) {
        try {
            IndexWriter writer = Lock.instance.openIndexWriter(lucenePath);

            if (terms != null) {
                for (int i = 0; i < terms.length; i++) {
                    if (terms[i] != null) {
                        writer.deleteDocuments(terms[i]);
                    }
                }
            }
            //			
            // if(docs != null)
            // {
            // for(int i=0;i<docs.length;i++)
            // {
            // writer.deleteDocuments();
            // writer.addDocument(docs[i], analyzer);
            // }
            // }
            for (int i = 0; i < beans.length; i++) {
                writer.deleteDocuments(buildTerm(beans[i]));
                writer.addDocument(buildDocument(beans[i]), analyzer);
            }

        } catch (Exception e) {
            log.error("LuceneEngine writerLucene(...) error!", e);
            return -1;
        } finally {
            // 最后关闭 lucene 写索引
            Lock.instance.closeLucenePath(lucenePath);
        }

        return beans.length;
    }

    /**
     * 得到Engine实例类入口
     * 
     * @param path lucene路径
     * @param fields Fields类定义
     * @return Engine实例
     */
    public static Engine getInstance(String path, Class<?> fields) {
        Engine instance = (Engine) pool.get(path);

        if (instance == null) {
            instance = new Engine(path);
            instance.init();
            pool.put(path, instance);
        }

        try {
            Fields = (Fields) fields.newInstance();
        } catch (Exception e) {
            log.error("Engine getInstance(" + path + "," + fields.getName() + ") error!", e);
        }

        return instance;
    }

    /**
     * 初始化时候 优化索引库
     */
    public void init() {
        try {
            IndexWriter writer = Lock.instance.openIndexWriter(lucenePath);

            writer.optimize();
        } catch (Exception e) {
            log.error("init() error!", e);
        } finally {
            // 最后关闭 lucene 写索引
            Lock.instance.closeLucenePath(lucenePath);
        }
    }

    /**
     * 根据传入过来的Bean对象计算其bean的lucene索引权重值
     * 
     * @param bean Bean
     * @return float 权重值
     */
    private float calcBeanWeight(Bean bean) {
        /*
         * Fields[] fields = Fields.getFields(); double score = 0; for(int i=0;i<fields.length;i++) { Fields _fileds =
         * fields[i]; float weight = fields[i].getWeight(); weight = (weight<(float)1.0)? (float)1.0 : weight; String
         * value = bean.get(_fileds); if(value==null){ score -= (weight - 1.0); }else{ score += (weight - 1.0); } score +=
         * 1.0; } return (float)score/fields.length;
         */

        Fields[] fields = Fields.getFields();
        float score = 0;
        for (int i = 0; i < fields.length; i++) {
            String value = bean.get(fields[i]);
            if (value != null) {
                score += 0.1;
            }
        }

        // 如果bean数据对象自己定义了权重则加上其权重值
        if (bean.getWeight() != null) {
            score += bean.getWeight().floatValue();
        }

        return score;
    }

    /**
     * Bean数组对象 构建 Term数组对象
     * 
     * @param beans Bean数组
     * @return Term数组对象
     */
    // private Term[] buildTerm(Bean[] beans)
    // {
    // Term[] terms = new Term[beans.length];
    // for(int i=0;i<terms.length;i++){
    // terms[i] = new Term(Fields.getTermFields().getLabel(),beans[i].get(Fields.getTermFields()));
    // }
    // return terms;
    // }
    /**
     * Bean对象 构建 Term对象
     * 
     * @param bean Bean数组
     * @return Term对象
     */
    private Term buildTerm(Bean bean) {
        return new Term(Fields.getTermFields().getLabel(), bean.get(Fields.getTermFields()));
    }

    /**
     * Bean数组对象 构建 Document数组对象
     * 
     * @param beans Bean数组
     * @return Document数组对象
     */
    // private Document[] buildDocument(Bean[] beans)
    // {
    // Document[] docs = new Document[beans.length];
    // for(int i=0;i<beans.length;i++)
    // {
    // docs[i] = new Document();
    //			
    // Fields[] _fields = Fields.getFields();
    // for(int j=0;j<_fields.length;j++)
    // {
    // Fields field = _fields[j];
    // String value = beans[i].get(field);
    // if(value!=null)
    // {
    // Field.Store fs = (field.getStore())? Field.Store.YES : Field.Store.NO;
    // Field.Index fi = (field.getIndex())? Field.Index.TOKENIZED : Field.Index.UN_TOKENIZED;
    //					
    // Field _field = new Field(field.getLabel(),beans[i].get(field),fs, fi);
    // _field.setBoost(field.getWeight());
    //					
    // docs[i].add(_field);
    // }
    // }
    //			
    // //计算文档的权重值（根据字段的完整度等相关信息进行计算）
    // docs[i].setBoost(docs[i].getBoost()+calcBeanWeight(beans[i]));
    // }
    //		
    // return docs;
    // }
    /**
     * Bean对象 构建 Document对象
     * 
     * @param beans Bean
     * @return Document对象
     */
    private Document buildDocument(Bean bean) {
        Document document = new Document();

        Fields[] _fields = Fields.getFields();
        for (int j = 0; j < _fields.length; j++) {
            Fields field = _fields[j];
            String value = bean.get(field);
            if (value != null) {
                Field.Store fs = (field.getStore()) ? Field.Store.YES : Field.Store.NO;
                Field.Index fi = (field.getIndex()) ? Field.Index.ANALYZED : Field.Index.ANALYZED_NO_NORMS;

                Field _field = new Field(field.getLabel(), bean.get(field), fs, fi);
                _field.setBoost(field.getWeight());

                document.add(_field);
            }
        }

        // 计算文档的权重值（根据字段的完整度等相关信息进行计算）
        document.setBoost(document.getBoost() + calcBeanWeight(bean));

        return document;
    }

    /**
     * 保存LuceneBean 到lucene数据库
     * 
     * @param lucenePath lucene库路径
     * @param obj LuceneBean
     * @return 保存后的状态 -1 失败
     */
    public int save(Bean bean) {
        // try{
        // 调用同步写lucene
        // writerLucene(buildTerm(new Bean[]{bean}),buildDocument(new Bean[]{bean}));
        return writerLucene(null, new Bean[] { bean });
        //			
        // return 1;
        // }catch(Exception e){
        // log.error("LuceneDB save("+lucenePath+",...) error!", e);
        // return -1;
        // }finally{
        // //最后关闭 lucene 写索引
        // Lock.instance.closeLucenePath(lucenePath);
        // }
    }

    /**
     * 保存LuceneBean 到lucene数据库
     * 
     * @param lucenePath lucene库路径
     * @param obj LuceneBean
     * @return 保存后的状态 -1 失败
     */
    public int save(Bean[] beans) {
        // try{
        // 调用同步写lucene
        // writerLucene(buildTerm(beans),buildDocument(beans));
        return writerLucene(null, beans);
        //			
        // return 1;
        // }catch(Exception e){
        // log.error("LuceneDB save("+lucenePath+",...) error!", e);
        // return -1;
        // }finally{
        // //最后关闭 lucene 写索引
        // Lock.instance.closeLucenePath(lucenePath);
        // }
    }

    /**
     * 从lucene库中删除 记录
     * 
     * @param obj LuceneBean
     * @return 成功操作的条数，-1有错误。
     */
    public int delete(Bean obj) {
        return delete(obj.get(Fields.getTermFields()));
    }

    /**
     * 从lucene库中删除 记录
     * 
     * @param sid 字符串类型的唯一标识主键
     * @return 成功操作的条数，-1有错误。
     */
    public int delete(String sid) {
        Term term = new Term(Fields.getTermFields().getLabel(), sid);

        return writerLucene(new Term[] { term }, null);
    }

    /**
     * 从lucene库中删除 记录
     * 
     * @param sid 字符串数组 主键ID数组
     * @return 成功操作的条数，-1有错误。
     */
    public int delete(String[] sid) {
        Term[] terms = new Term[sid.length];

        for (int i = 0; i < terms.length; i++) {
            terms[i] = new Term(Fields.getTermFields().getLabel(), sid[i]);
        }

        return writerLucene(terms, null);
    }

    /**
     * @param bean LuceneBean实例化对象
     * @param doc lucene Documen对象
     * @return 填充数据后的 LuceneBean实例化对象
     */
    private static Bean DocumentToLuceneBean(Document doc) {
        Bean bean = new Bean();
        Fields[] _fields = Fields.getFields();
        for (int i = 0; i < _fields.length; i++) {
            bean.put(_fields[i], doc.get(_fields[i].getLabel()));
        }

        return bean;
    }

    private TopDocs _query(IndexSearcher searcher, String keyword, int maxIndex) throws ParseException, IOException {
        // 主查询段开始
        BooleanQuery query = new BooleanQuery();

        Fields[] _fields = Fields.getFields();
        for (int i = 0; i < _fields.length; i++) {
            // 需要索引的字段类型中 去查询
            if (_fields[i].getIndex()) {
                QueryParser qp = new QueryParser(Version.LUCENE_30, _fields[i].getLabel(), analyzer);
                query.add(qp.parse(keyword), BooleanClause.Occur.SHOULD);
            }
        }

        return searcher.search(query, maxIndex);
    }

    private List<Bean> hitsToList(IndexSearcher IndexSearcher, TopDocs hits, String keyword, int page, int pageSize)
                                                                                                                    throws IOException {
        List<Bean> list = new ArrayList<Bean>();

        int beginIndex = (page - 1) * pageSize;
        int endIndex = page * pageSize;

        // 当指定分页大小不符合正常逻辑的时候 返回查询全部结果集
        if (pageSize <= 0) {
            beginIndex = 0;
            endIndex = hits.totalHits;
        }

        for (int i = beginIndex; i < hits.totalHits && i < endIndex; i++) {
            Bean bean = DocumentToLuceneBean(IndexSearcher.doc(hits.scoreDocs[i].doc));

            bean.setKeyword(keyword);

            list.add(bean);
        }

        return list;
    }

    /**
     * 在lucene库中按SID查询记录（SID是一条记录的md5后的数字指纹，具有唯一性）
     * 
     * @param sid SID是一条记录的md5后的数字指纹，具有唯一性
     * @return Bean
     */
    public Bean findBySid(String sid) {
        // //lucene连接
        // Connection conn = new Connection();
        //		
        // try{
        // //主查询段开始
        // Query query = new TermQuery(new Term(Fields.getTermFields().getLabel(),sid));
        //			
        // TopDocs hits = conn.connection(lucenePath).search(query);
        // if(hits.length()<=0){
        // return null;
        // }else{
        // return DocumentToLuceneBean(hits.doc(0));
        // }
        // }catch(Exception e){
        // log.error("Engine findBySid("+sid+") error!", e);
        // return null;
        // }finally{
        // if(conn != null){
        // conn.close();
        // }
        // }
        return findByField(Fields.getTermFields().getLabel(), sid);
    }

    /**
     * @param fieldName 查询索引字段名称
     * @param fieldValue 查询索引字段值
     * @return 数据Bean类
     */
    public Bean findByField(String fieldName, String fieldValue) {
        // lucene连接
        Connection conn = new Connection();

        try {
            // 主查询段开始
            Query query = new TermQuery(new Term(fieldName, fieldValue));

            IndexSearcher searcher = conn.connection(lucenePath);
            TopDocs hits = searcher.search(query, 1);
            if (hits.totalHits <= 0) {
                return null;
            } else {
                return DocumentToLuceneBean(searcher.doc(hits.scoreDocs[0].doc));
            }
        } catch (Exception e) {
            log.error("findByField(" + fieldName + "," + fieldValue + ") error!", e);
            return null;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * 根据关键字 在lucene数据库中查找结果集
     * 
     * @param keyword 需要查找的关键字
     * @param pageNo 当前页
     * @param pageSize 每页大小,当pageSize为-1时，返回所有结果集
     * @return List<Bean>
     */
    public List<Bean> query(String keyword, int pageNo, int pageSize) {
        Connection conn = new Connection();
        IndexSearcher searcher = conn.connection(lucenePath);

        try {
            return hitsToList(searcher, _query(searcher, keyword, pageNo * pageSize), keyword, pageNo, pageSize);
        } catch (Exception e) {
            log.error("Engine query(" + keyword + ",PageModel) error!", e);
            return new ArrayList<Bean>();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * 根据关键字 在lucene数据库中查找结果集
     * 
     * @param keyword 需要查找的关键字
     * @param pageModel 包含查询结果集滚动页数，结果集条数等信息
     * @return List<Bean>
     */
    public List<Bean> query(String keyword, PageModel pageModel) {
        Connection conn = new Connection();
        IndexSearcher searcher = conn.connection(lucenePath);

        try {
            TopDocs hits = _query(searcher, keyword, pageModel.getPageSize() * pageModel.getPage());

            // 设置结果集总数
            pageModel.setItemCount(hits.totalHits);

            return hitsToList(searcher, hits, keyword, pageModel.getPage(), pageModel.getPageSize());
        } catch (Exception e) {
            log.error("Engine query(" + keyword + ",PageModel) error!", e);
            return new ArrayList<Bean>();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * 得到查询关键字的总数统计
     * 
     * @param keyword 查询关键字
     * @return 查询关键字结果集总数
     */
    public int queryCount(String keyword) {
        Connection conn = new Connection();
        IndexSearcher searcher = conn.connection(lucenePath);

        try {
            return _query(searcher, keyword, 0).totalHits;
        } catch (Exception e) {
            log.error("Engine query(" + keyword + ",PageModel) error!", e);
            return -1;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

}
