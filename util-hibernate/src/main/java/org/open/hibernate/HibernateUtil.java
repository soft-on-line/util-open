package org.open.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.open.PageModel;
import org.open.util.DateUtil;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

/**
 * 基于spring hibernate开源模块二次封装功能类
 *
 * @author 覃芝鹏
 * @version $Id: HibernateUtil.java,v 1.11 2009/06/09 09:15:31 moon Exp $
 */
public class HibernateUtil {

    private static final Log  log = LogFactory.getLog(HibernateUtil.class);
    private HibernateTemplate hibernateTemplate;

    public HibernateUtil() {

    }

    public HibernateUtil(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public HibernateTemplate getHibernateTemplate() {
        return this.hibernateTemplate;
    }

//    public List query(String Class clazz,String[] properties){
//
//
//        /////////////////
//        SessionFactory sessionFactory = getHibernateTemplate().getSessionFactory();
//        Session session = sessionFactory.openSession();
//        try {
//            Criteria   criteria=session.createCriteria(clazz);
//
//            ProjectionList   proList   =   Projections.projectionList();//设置投影集合
//                        proList.add(Projections.groupProperty( "userName "));
//                        proList.add(Projections.groupProperty( "password "));
//
//            criteria.setProjection(proList);
//
//            session.createQuery(arg0)
//        } catch (Exception e) {
//           log.error(e.getMessage(), e);
//
//            return new ArrayList<T>();
//        } finally {
//            SessionFactoryUtils.releaseSession(session, sessionFactory);
//        }
//    }

    public <T> List<T> queryByBuildHQL(String HQL, int amount, final String... paras) {
        for (String para : paras) {
            HQL = HQL.replaceFirst("\\?", para);
        }
        return queryByHQLWithLimit(HQL, amount);
    }

    public <T> List<T> queryByBuildHQL(String HQL, final String... paras) {
        for (String para : paras) {
            HQL = HQL.replaceFirst("\\?", para);
        }
        return queryByHQL(HQL);
    }

    public <T> List<T> queryByBuildHQLWithLimit(String HQL, int amount, final String... paras) {
        for (String para : paras) {
            HQL = HQL.replaceFirst("\\?", para);
        }
        return queryByHQLWithLimit(HQL, amount);
    }

    public <T> List<T> queryByBuildHQLWithPageModel(String HQLCount, String HQLQuery, final PageModel pageModel,
                                                    final String... paras) {
        for (String para : paras) {
            HQLCount = HQLCount.replaceFirst("\\?", para);
            HQLQuery = HQLQuery.replaceFirst("\\?", para);
        }
        return queryByHQLWithPageModel(HQLCount, HQLQuery, pageModel);
    }

    public <T> List<T> queryByBuildSQL(String SQL, Class<T> clazz, String... paras) {
        for (String para : paras) {
            SQL = SQL.replaceFirst("\\?", para);
        }
        return queryBySQL(SQL, clazz);
    }

    public <T> List<T> queryByBuildSQLWithLimit(String SQL, int amount, Class<T> clazz, String... paras) {
        for (String para : paras) {
            SQL = SQL.replaceFirst("\\?", para);
        }
        return queryBySQLWithPageModel(null, SQL, new PageModel(amount), clazz,
                                       (LinkedHashMap<String, org.hibernate.type.NullableType>) null);
    }

    public <T> List<T> queryByBuildSQLWithPageModel(String SQLCount, String SQLQuery, Class<T> clazz,
                                                    PageModel pageModel, String... paras) {
        for (String para : paras) {
            SQLCount = SQLCount.replaceFirst("\\?", para);
            SQLQuery = SQLQuery.replaceFirst("\\?", para);
        }
        return queryBySQLWithPageModel(SQLCount, SQLQuery, pageModel, clazz,
                                       (LinkedHashMap<String, org.hibernate.type.NullableType>) null);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> queryByHQL(String hql, Object... obj) {
        long st = new Date().getTime();
        try {
            return hibernateTemplate.find(hql, obj);
        } catch (Exception e) {
            log.error("queryByHQL(HQL:" + hql + "," + obj + ")", e);
            return new ArrayList<T>();
        } finally {
            log.debug("Used Time:" + DateUtil.convert(new Date().getTime() - st));
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> queryByHQLWithLimit(final String hql, final int amount, final Object... obj) {
        long st = new Date().getTime();
        try {
            return hibernateTemplate.executeFind(new HibernateCallback() {

                public Object doInHibernate(Session session) throws HibernateException, SQLException {
                    Query query = session.createQuery(hql);

                    for (int i = 0; i < obj.length; i++) {
                        query.setParameter(i, obj[i]);
                    }

                    query.setMaxResults(amount);

                    return query.list();
                }
            });
        } catch (Exception e) {
            log.error("queryByHQLWithLimit(HQL:" + hql + "," + obj + ")", e);
            return new ArrayList<T>();
        } finally {
            log.debug("Used Time:" + DateUtil.convert(new Date().getTime() - st));
        }
    }

    public <T> List<T> queryByHQLWithPageModel(final String HQLQuery, final PageModel pageModel, final Object... paras) {
        return queryByHQLWithPageModel(null, HQLQuery, pageModel, paras);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> queryByHQLWithPageModel(String HQLCount, final String HQLQuery, final PageModel pageModel,
                                               final Object... paras) {
        log.info("当前页面:" + pageModel.getPage());
        log.info("页面大小:" + pageModel.getPageSize());

        if (HQLCount != null) {
            long cnt = 0;

            Object _cnt = getHibernateTemplate().find(HQLCount, paras).get(0);
            if (_cnt instanceof Integer) {
                cnt = (Integer) _cnt;
            } else if (_cnt instanceof Long) {
                cnt = (Long) _cnt;
            }

            pageModel.setItemCount((int) cnt);
        }

        return getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(HQLQuery);
                for (int i = 0; i < paras.length; i++) {
                    query.setParameter(i, paras[i]);
                }
                query.setFirstResult((pageModel.getPage() - 1) * (pageModel.getPageSize()));
                query.setMaxResults(pageModel.getPageSize());
                return query.list();
            }
        });
    }

    public <T> List<T> queryBySQL(String SQL, Class<T> clazz, Object... paras) {
        return queryBySQLWithPageModel(null, SQL, (PageModel) null, clazz,
                                       (LinkedHashMap<String, org.hibernate.type.NullableType>) null, paras);
    }

    // public <T> List<T> queryBySQL(String SQL, Class<T> clazz, Object... paras) {
    // SessionFactory sessionFactory = getHibernateTemplate().getSessionFactory();
    // Session session = sessionFactory.openSession();
    // try {
    // SQLQuery q = session.createSQLQuery(SQL);
    //
    // int pc = 0;
    // for (Object o : paras) {
    // q.setParameter(pc++, o);
    // }
    //
    // if (clazz != null) {
    // q.addEntity(clazz);
    // }
    //
    // return q.list();
    // } catch (Exception e) {
    // log.error("queryBySQL(SQL:" + SQL + ",...)", e);
    // return new ArrayList<T>();
    // } finally {
    // SessionFactoryUtils.releaseSession(session, sessionFactory);
    // }
    // }

    public <T> List<T> queryBySQL(String SQL, int amount,
                                  LinkedHashMap<String, org.hibernate.type.NullableType> scalar, Object... paras) {
        return queryBySQLWithPageModel(null, SQL, new PageModel(amount), (Class<T>) null, scalar, paras);
    }

    public <T> List<T> queryBySQL(String SQL, LinkedHashMap<String, org.hibernate.type.NullableType> scalar,
                                  Object... paras) {
        return queryBySQLWithPageModel(null, SQL, (PageModel) null, (Class<T>) null, scalar, paras);
    }

    public <T> List<T> queryBySQL(String SQL, Object... paras) {
        return queryBySQLWithPageModel(null, SQL, (PageModel) null, (Class<T>) null,
                                       (LinkedHashMap<String, org.hibernate.type.NullableType>) null, paras);
    }

    public <T> List<T> queryBySQLWithLimit(String SQLQuery, int amount) {
        return queryBySQLWithPageModel(null, SQLQuery, new PageModel(amount), (Class<T>) null,
                                       (LinkedHashMap<String, org.hibernate.type.NullableType>) null);
    }

    public <T> List<T> queryBySQLWithLimit(String SQLQuery, int amount, Class<T> clazz, Object... paras) {
        return queryBySQLWithPageModel(null, SQLQuery, new PageModel(amount), clazz,
                                       (LinkedHashMap<String, org.hibernate.type.NullableType>) null, paras);
    }

    // public <T> List<T> queryBySQLWithLimit(String SQLQuery, int amount, Class<T> clazz, Object... paras) {
    // SessionFactory sessionFactory = getHibernateTemplate().getSessionFactory();
    // Session session = sessionFactory.openSession();
    // try {
    // SQLQuery q = session.createSQLQuery(SQLQuery);
    //
    // if (clazz != null) {
    // q.addEntity(clazz);
    // }
    //
    // int pc = 0;
    // for (Object o : paras) {
    // q.setParameter(pc++, o);
    // }
    //
    // q.setMaxResults(amount);
    //
    // return q.list();
    // } catch (Exception e) {
    // log.error("queryBySQL(SQL:" + SQLQuery + ",...)", e);
    // return new ArrayList<T>();
    // } finally {
    // SessionFactoryUtils.releaseSession(session, sessionFactory);
    // }
    // }

    public <T> List<T> queryBySQLWithPageModel(String SQLQuery, PageModel pageModel, Class<T> clazz, Object... paras) {
        return queryBySQLWithPageModel(null, SQLQuery, pageModel, clazz,
                                       (LinkedHashMap<String, org.hibernate.type.NullableType>) null, paras);
    }

    public <T> List<T> queryBySQLWithPageModel(String SQLQuery, PageModel pageModel,
                                               LinkedHashMap<String, org.hibernate.type.NullableType> scalar,
                                               Object... paras) {
        return queryBySQLWithPageModel(null, SQLQuery, pageModel, (Class<T>) null, scalar, paras);
    }

    public <T> List<T> queryBySQLWithPageModel(String SQLQuery, PageModel pageModel, Object... paras) {
        return queryBySQLWithPageModel(null, SQLQuery, pageModel, (Class<T>) null,
                                       (LinkedHashMap<String, org.hibernate.type.NullableType>) null, paras);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> queryBySQLWithPageModel(String SQLCount, String SQLQuery, PageModel pageModel, Class<T> clazz,
                                               LinkedHashMap<String, org.hibernate.type.NullableType> scalar,
                                               Object... paras) {
        SessionFactory sessionFactory = getHibernateTemplate().getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            if (null != SQLCount && null != pageModel) {
                // ///////
                SQLQuery qc = session.createSQLQuery(SQLCount);

                if (null != paras) {
                    int pc = 0;
                    for (Object o : paras) {
                        qc.setParameter(pc++, o);
                    }
                }

                pageModel.setItemCount(Integer.valueOf(qc.list().get(0).toString()));
            }

            // //////////////////////
            SQLQuery q = session.createSQLQuery(SQLQuery);

            if (clazz != null) {
                q.addEntity(clazz);
            }

            if (null != paras) {
                int pc = 0;
                for (Object o : paras) {
                    q.setParameter(pc++, o);
                }
            }

            // 增加字段类型映射
            if (null != scalar) {
                Iterator<String> keys = scalar.keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    q.addScalar(key, scalar.get(key));
                }
            }

            if (null != pageModel) {
                q.setFirstResult((pageModel.getPage() - 1) * (pageModel.getPageSize()));
                q.setMaxResults(pageModel.getPageSize());
            }

            return q.list();
        } catch (Exception e) {
            log.error("queryBySQLWithPageModel(SQL:" + SQLQuery + ",PageModel:" + pageModel + "++,Class:" + clazz
                      + ",paras:" + paras + ")", e);
            return new ArrayList<T>();
        } finally {
            SessionFactoryUtils.releaseSession(session, sessionFactory);
        }
    }

    public <T> List<T> queryBySQLWithPageModel(String SQLCount, String SQLQuery, PageModel pageModel, Class<T> clazz,
                                               Object... paras) {
        return queryBySQLWithPageModel(SQLCount, SQLQuery, pageModel, clazz,
                                       (LinkedHashMap<String, org.hibernate.type.NullableType>) null, paras);
    }

    public int queryHQLCount(String HQLCount, Object... paras) {
        long cnt = 0;

        Object _cnt = getHibernateTemplate().find(HQLCount, paras).get(0);
        if (_cnt instanceof Integer) {
            cnt = (Integer) _cnt;
        } else if (_cnt instanceof Long) {
            cnt = (Long) _cnt;
        }

        return (int) cnt;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public int updateByBuildHQL(String hql, String... paras) {
        if (null != paras) {
            for (String para : paras) {
                hql = hql.replaceFirst("\\?", para);
            }
        }

        log.info(hql);

        return updateByHQL(hql);
    }

    public int updateByBuildSQL(String SQL, String... paras) {
        if (null != paras) {
            for (String para : paras) {
                SQL = SQL.replaceFirst("\\?", para);
            }
        }

        log.info("SQL:" + SQL);

        return updateBySQL(SQL);
    }

    public int updateByHQL(String hql, Object... paras) {
        long st = new Date().getTime();
        try {
            return getHibernateTemplate().bulkUpdate(hql, paras);
        } catch (Exception e) {
            log.error("updateByHQL(HQL:" + hql + ",...)", e);
            return -1;
        } finally {
            log.info("Used Time:" + DateUtil.convert(new Date().getTime() - st));
        }
    }

    public int updateBySQL(String SQL, Object... paras) {
        SessionFactory sessionFactory = getHibernateTemplate().getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();

            // //////////////////////
            SQLQuery q = session.createSQLQuery(SQL);

            if (null != paras) {
                int pc = 0;
                for (Object o : paras) {
                    q.setParameter(pc++, o);
                }
            }

            int rs = q.executeUpdate();

            session.getTransaction().commit();

            return rs;
        } catch (Exception e) {
            log.error("updateBySQL(SQL:" + SQL + ",...)", e);
            if (session != null) {
                session.getTransaction().rollback();
            }
            return -1;
        } finally {
            SessionFactoryUtils.releaseSession(session, sessionFactory);
        }
    }

}
