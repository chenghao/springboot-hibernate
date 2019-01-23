package com.demo.dao;

import com.demo.dao.model.DBTable;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface GenericBaseCommonDao {

    /**
     * 获取所有数据库表
     */
    List<DBTable> getAllDbTableName();

    Integer getAllDbTableSize();

    /**
     * 加载全部实体
     * @param entityClass
     */
    <T> List<T> loadAll(final Class<T> entityClass);

    /**
     * 根据sql更新
     * @param sql
     */
    int updateBySqlString(String sql);

    /**
     * 根据sql查找List
     * @param sql
     */
    <T> List<T> findListBySql(String sql);

    /**
     * 通过属性称获取实体带排序
     * @param entityClass
     * @param propertyName
     * @param value
     * @param isAsc
     */
    <T> List<T> findByPropertyIsOrder(Class<T> entityClass, String propertyName, Object value,
                                      String orderName, boolean isAsc);

    <T> List<T> pageList(DetachedCriteria dc, int firstResult, int maxResult);
    Long pageListCount(DetachedCriteria dc);

    <T> List<T> findByDetached(DetachedCriteria dc);

    Session getSession();

    <T> Serializable save(T entity);

    <T> void batchSave(List<T> entitys);

    <T> void saveOrUpdate(T entity);

    <T> void delete(T entity);

    <T> T get(Class<T> entityName, Serializable id);

    <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value);

    <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value);

    <T> T getEntity(Class entityName, Serializable id);

    <T> void deleteEntityById(Class entityName, Serializable id);

    <T> void updateEntity(T entity);

    <T> void updateEntityById(Class entityName, Serializable id);

    <T> List<T> findByQueryString(String hql);

    <T> T singleResult(String hql);

    Integer executeSql(String sql, List<Object> param);

    Integer executeSql(String sql, Object... param);

    /**
     * 执行SQL 使用:name占位符
     */
    Integer executeSql(String sql, Map<String, Object> param);

    /**
     * 执行SQL 使用:name占位符,并返回插入的主键值
     */
    Object executeSqlReturnKey(String sql, Map<String, Object> param);

    /**
     * 通过JDBC查找对象集合 使用指定的检索标准检索数据返回数据
     */
    List<Map<String, Object>> findForJdbc(String sql, Object... objs);

    /**
     * 通过JDBC查找对象集合 使用指定的检索标准检索数据返回数据
     */
    Map<String, Object> findOneForJdbc(String sql, Object... objs);

    /**
     * 通过JDBC查找对象集合,带分页 使用指定的检索标准检索数据并分页返回数据
     */
    List<Map<String, Object>> findForJdbc(String sql, int page, int rows);

    /**
     * 通过JDBC查找对象集合,带分页 使用指定的检索标准检索数据并分页返回数据
     */
    <T> List<T> findObjForJdbc(String sql, int page, int rows, Class<T> clazz);
    /**
     * 使用指定的检索标准检索数据并分页返回数据-采用预处理方式
     */
    List<Map<String, Object>> findForJdbcParam(String sql, int page, int rows, Object... objs);

    /**
     * 通过hql 查询语句查找对象
     */
    <T> List<T> findHql(String hql, Object... param);

    /**
     * 执行HQL语句操作更新
     */
    Integer executeHql(String hql);

    /**
     * 执行存储过程
     */
    <T> List<T> executeProcedure(String procedureSql, Object... params);
}
