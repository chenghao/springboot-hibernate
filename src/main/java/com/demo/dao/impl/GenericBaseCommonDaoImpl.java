package com.demo.dao.impl;

import com.demo.annotation.EntityTitle;
import com.demo.dao.GenericBaseCommonDao;
import com.demo.dao.jdbc.JdbcDao;
import com.demo.dao.model.DBTable;
import com.demo.util.ConvertUtils;
import com.demo.util.MyBeanUtils;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class GenericBaseCommonDaoImpl<T, PK extends Serializable> implements GenericBaseCommonDao {
    private static final Logger logger = Logger.getLogger(GenericBaseCommonDaoImpl.class);

    /**
     * 注入一个sessionFactory属性,并注入到父类(HibernateDaoSupport)
     * **/
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("namedParameterJdbcTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<DBTable> getAllDbTableName() {
        List<DBTable> resultList = new ArrayList<>();
        SessionFactory factory = getSession().getSessionFactory();
        Map<String, ClassMetadata> metaMap = factory.getAllClassMetadata();
        for (String key : metaMap.keySet()) {
            DBTable dbTable = new DBTable();
            AbstractEntityPersister classMetadata = (AbstractEntityPersister) metaMap.get(key);
            dbTable.setTableName(classMetadata.getTableName());
            dbTable.setEntityName(classMetadata.getEntityName());
            Class<?> c;
            try {
                c = Class.forName(key);
                EntityTitle t = c.getAnnotation(EntityTitle.class);
                dbTable.setTableTitle(t != null ? t.name() : "");
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage(), e);
            }
            resultList.add(dbTable);
        }
        return resultList;
    }

    @Override
    public Integer getAllDbTableSize() {
        SessionFactory factory = getSession().getSessionFactory();
        Map<String, ClassMetadata> metaMap = factory.getAllClassMetadata();
        return metaMap.size();
    }

    @Override
    public <T> List<T> loadAll(Class<T> entityClass) {
        Criteria criteria = createCriteria(entityClass);
        return criteria.list();
    }

    @Override
    public int updateBySqlString(String sql) {
        Query query = getSession().createSQLQuery(sql);
        return query.executeUpdate();
    }

    @Override
    public <T> List<T> findListBySql(String sql) {
        Query query = getSession().createSQLQuery(sql);
        return query.list();
    }

    @Override
    public <T> List<T> findByPropertyIsOrder(Class<T> entityClass, String propertyName, Object value,
                                             String orderName, boolean isAsc) {
        Assert.hasText(propertyName, "字段名称不能为空");
        return createCriteria(entityClass, orderName, isAsc, Restrictions.eq(propertyName, value)).list();
    }

    @Override
    public <T> List<T> pageList(DetachedCriteria dc, int firstResult, int maxResult) {
        Criteria criteria = dc.getExecutableCriteria(getSession());
        criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        criteria.setFirstResult(firstResult);
        criteria.setMaxResults(maxResult);
        return criteria.list();
    }

    @Override
    public Long pageListCount(DetachedCriteria dc) {
        dc.setProjection(Projections.rowCount());
        Criteria criteria = dc.getExecutableCriteria(getSession());
        criteria.setFirstResult(0);
        Long totalNum = (Long) criteria.uniqueResult();
        return totalNum;
    }

    @Override
    public <T> List<T> findByDetached(DetachedCriteria dc) {
        return dc.getExecutableCriteria(getSession()).list();
    }

    public Session getSession() {
        // 事务必须是开启的(Required)，否则获取不到
        return sessionFactory.getCurrentSession();
    }

    /**
     * 根据传入的实体持久化对象
     */
    @Override
    public <T> Serializable save(T entity) {
        try {
            Serializable id = getSession().save(entity);
            //getSession().flush();
            if (logger.isDebugEnabled()) {
                logger.debug("保存实体成功," + entity.getClass().getName());
            }
            return id;
        } catch (RuntimeException e) {
            logger.error("保存实体异常", e);
            throw e;
        }
    }

    /**
     * 批量保存数据
     * @param entitys 要持久化的临时实体对象集合
     */
    @Override
    public <T> void batchSave(List<T> entitys) {
        for (int i = 0; i < entitys.size(); i++) {
            getSession().save(entitys.get(i));
            if (i % 1000 == 0) {
                // 1000个对象批量写入数据库，后才清理缓存
                getSession().flush();
                getSession().clear();
            }
        }
        //最后页面的数据，进行提交手工清理
        getSession().flush();
        getSession().clear();
    }

    /**
     * 根据传入的实体添加或更新对象
     * @param entity
     */
    @Override
    public <T> void saveOrUpdate(T entity) {
        try {
            getSession().saveOrUpdate(entity);
            //getSession().flush();
            if (logger.isDebugEnabled()) {
                logger.debug("添加或更新成功," + entity.getClass().getName());
            }
        } catch (RuntimeException e) {
            logger.error("添加或更新异常", e);
            throw e;
        }
    }

    /**
     * 根据传入的实体删除对象
     */
    @Override
    public <T> void delete(T entity) {
        try {
            getSession().delete(entity);
            //getSession().flush();
            if (logger.isDebugEnabled()) {
                logger.debug("删除成功," + entity.getClass().getName());
            }
        } catch (RuntimeException e) {
            logger.error("删除异常", e);
            throw e;
        }
    }

    /**
     * 根据主键删除指定的实体
     * @param entityName
     * @param id
     */
    @Override
    public <T> void deleteEntityById(Class entityName, Serializable id) {
        delete(get(entityName, id));
        //getSession().flush();
    }

    /**
     * 根据Id获取对象。
     */
    @Override
    public <T> T get(Class<T> entityClass, final Serializable id) {
        return getSession().get(entityClass, id);
    }

    /**
     * 根据主键获取实体并加锁。
     * @param entityName
     * @param id
     */
    @Override
    public <T> T getEntity(Class entityName, Serializable id) {
        T t = (T) getSession().get(entityName, id);
        if (t != null) {
            //getSession().flush();
        }
        return t;
    }

    /**
     * 更新指定的实体
     * @param entity
     */
    @Override
    public <T> void updateEntity(T entity) {
        getSession().update(entity);
        //getSession().flush();
    }

    /**
     * 根据主键更新实体
     */
    @Override
    public <T> void updateEntityById(Class entityName, Serializable id) {
        updateEntity(get(entityName, id));
    }

    /**
     * 通过hql 查询语句查找对象
     * @param hql
     */
    @Override
    public List<T> findByQueryString(final String hql) {
        Query queryObject = getSession().createQuery(hql);
        List<T> list = queryObject.list();
		if (list.size() > 0) {
            //getSession().flush();
		}
        return list;
    }

    /**
     * 通过hql查询唯一对象
     * @param hql
     */
    @Override
    public <T> T singleResult(String hql) {
        T t = null;
        List<T> list = (List<T>) findByQueryString(hql);
        if (list.size() == 1) {
            //getSession().flush();
            t = list.get(0);
        } else if (list.size() > 0) {
            throw new RuntimeException("查询结果数:" + list.size() + "大于1");
        }
        return t;
    }

    /**
     * 根据实体名字获取唯一记录
     * @param propertyName
     * @param value
     * @return
     */
    @Override
    public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value) {
        Assert.hasText(propertyName, "字段名称不能为空");
        return (T) createCriteria(entityClass, Restrictions.eq(propertyName, value)).uniqueResult();
    }

    /**
     * 按属性查找对象列表.
     */
    @Override
    public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value) {
        Assert.hasText(propertyName, "字段名称不能为空");
        return (List<T>) createCriteria(entityClass, Restrictions.eq(propertyName, value)).list();
    }

    @Override
    public Integer executeSql(String sql, List<Object> param) {
        return this.jdbcTemplate.update(sql, param);
    }

    @Override
    public Integer executeSql(String sql, Object... param) {
        return this.jdbcTemplate.update(sql, param);
    }

    @Override
    public Integer executeSql(String sql, Map<String, Object> param) {
        return this.namedParameterJdbcTemplate.update(sql, param);
    }

    @Override
    public Object executeSqlReturnKey(final String sql, Map<String, Object> param) {
        Object keyValue = null;
        KeyHolder keyHolder = null;
        SqlParameterSource sqlp  = new MapSqlParameterSource(param);

        if (!StringUtils.isEmpty(param.get("id"))) {//表示已经生成过id(UUID),则表示是非序列或数据库自增的形式
            this.namedParameterJdbcTemplate.update(sql, sqlp);
        }else if (!StringUtils.isEmpty(param.get("ID"))) {//表示已经生成过id(UUID),则表示是非序列或数据库自增的形式
            this.namedParameterJdbcTemplate.update(sql, sqlp);
        }else{//NATIVE or SEQUENCE
            keyHolder = new GeneratedKeyHolder();
            this.namedParameterJdbcTemplate.update(sql, sqlp, keyHolder, new String[]{"id"});
            Number number = keyHolder.getKey();
            if(ConvertUtils.isNotEmpty(number)){
                keyValue = keyHolder.getKey().longValue();
            }
        }

        return keyValue;
    }

    @Override
    public Map<String, Object> findOneForJdbc(String sql, Object... objs) {
        try {
            return this.jdbcTemplate.queryForMap(sql, objs);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    /**
     * 使用指定的检索标准检索数据并分页返回数据-采用预处理方式
     * @param sql
     * @param page
     * @param rows
     * @param objs
     */
    @Override
    public List<Map<String, Object>> findForJdbcParam(String sql, int page, int rows, Object... objs) {
        // 封装分页SQL
        sql = JdbcDao.createPageSql(sql, page, rows);
        return this.jdbcTemplate.queryForList(sql, objs);
    }

    /**
     * 通过hql 查询语句查找对象
     * @param hql
     * @param param
     */
    @Override
    public <T> List<T> findHql(String hql, Object... param) {
        Query q = getSession().createQuery(hql);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                q.setParameter(i, param[i]);
            }
        }
        return q.list();
    }

    /**
     * 执行HQL语句操作更新
     * @param hql
     */
    @Override
    public Integer executeHql(String hql) {
        Query q = getSession().createQuery(hql);
        return q.executeUpdate();
    }

    /**
     * 使用指定的检索标准检索数据并分页返回数据
	 */
    @Override
    public List<Map<String, Object>> findForJdbc(String sql, int page, int rows) {
        // 封装分页SQL
        sql = JdbcDao.createPageSql(sql, page, rows);
        return this.jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> findForJdbc(String sql, Object... objs) {
        return this.jdbcTemplate.queryForList(sql, objs);
    }

    /**
     * 使用指定的检索标准检索数据并分页返回数据
     */
    @Override
    public <T> List<T> findObjForJdbc(String sql, int page, int rows, Class<T> clazz) {
        List<T> rsList = new ArrayList<>();
        // 封装分页SQL
        sql = JdbcDao.createPageSql(sql, page, rows);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);

        T po = null;
        for (Map<String, Object> m : mapList) {
            try {
                po = clazz.newInstance();
                MyBeanUtils.copyMap2Bean_Nobig(po, m);
                rsList.add(po);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return rsList;
    }

    /**
     * 调用存储过程
     */
    @Override
    public <T> List<T> executeProcedure(String executeSql, Object... params) {
        SQLQuery sqlQuery = getSession().createSQLQuery(executeSql);

        for(int i=0;i<params.length;i++){
            sqlQuery.setParameter(i, params[i]);
        }

        return sqlQuery.list();
    }

    /**
     * 创建Criteria对象，有排序功能。
     * @param entityClass
     * @param isAsc
     * @param criterions
     */
    private <T> Criteria createCriteria(Class<T> entityClass, String orderName, boolean isAsc, Criterion... criterions){
        Criteria criteria = createCriteria(entityClass, criterions);
        if (isAsc) {
            criteria.addOrder(Order.asc(orderName));
        } else {
            criteria.addOrder(Order.desc(orderName));
        }
        return criteria;
    }
    /**
     * 创建Criteria对象带属性比较
     * @param entityClass
     * @param criterions
     */
    private <T> Criteria createCriteria(Class<T> entityClass, Criterion... criterions) {
        Criteria criteria = getSession().createCriteria(entityClass);
        for (Criterion c : criterions) {
            criteria.add(c);
        }
        return criteria;
    }
}
