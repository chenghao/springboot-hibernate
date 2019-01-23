package com.demo.service;

import com.demo.dao.model.DBTable;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface CommonService {

    /**
     * 获取所有数据库表
     * @return
     */
    List<DBTable> getAllDbTableName();

    Integer getAllDbTableSize();

    /**
     * 加载全部实体
     * @param <T>
     * @param entityClass
     * @return
     */
    <T> List<T> loadAll(final Class<T> entityClass);

    /**
     * 根据sql更新
     * @param sql
     * @return
     */
    int updateBySqlString(String sql);

    /**
     * 根据sql查找List
     * @param query
     * @return
     */
    <T> List<T> findListBySql(String query);


    <T> Serializable save(T entity);

    <T> void saveOrUpdate(T entity);

    <T> void delete(T entity);

    <T> void batchSave(List<T> entitys);

    /**
     * 根据实体名称和主键获取实体
     * @param entityName
     * @param id
     */
    <T> T get(Class<T> entityName, Serializable id);

    /**
     * 根据实体名称和主键获取实体
     * @param entityName
     * @param id
     */
    <T> T getEntity(Class entityName, Serializable id);

    /**
     * 根据实体名称和字段名称和字段值获取唯一记录
     * @param entityClass
     * @param propertyName
     * @param value
     */
    <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value);

    /**
     * 按属性查找对象列表.
     */
    <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value);

    /**
     * 删除实体主键删除
     * @param entityName
     * @param id
     */
    <T> void deleteEntityById(Class entityName, Serializable id);

    /**
     * 更新指定的实体
     * @param entity
     */
    <T> void updateEntity(T entity);

    /**
     * 通过hql 查询语句查找对象
     * @param <T>
     */
    <T> List<T> findByQueryString(String hql);

    /**
     * 通过属性称获取实体带排序
     * @param entityClass
     * @param propertyName
     * @param value
     * @param isAsc
     */
    <T> List<T> findByPropertyIsOrder(Class<T> entityClass, String propertyName, Object value,
                                      String orderName, boolean isAsc);

    <T> List<T> getList(Class clas);

    <T> T singleResult(String hql);

    /**
     * 执行SQL
     */
    Integer executeSql(String sql, List<Object> param);

    /**
     * 执行SQL
     */
    Integer executeSql(String sql, Object... param);

    /**
     * 执行SQL 使用:name占位符
     */
    Integer executeSql(String sql, Map<String, Object> param);

    /**
     * 执行SQL 使用:name占位符,并返回执行后的主键值
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
     * @param sql
     * @param page
     * @param rows
     * @param objs
     */
    List<Map<String, Object>> findForJdbcParam(String sql, int page, int rows, Object... objs);

    /**
     * 通过hql 查询语句查找对象
     * @param hql
     * @param param
     */
    <T> List<T> findHql(String hql, Object... param);

    <T> List<T> pageList(DetachedCriteria dc, int firstResult, int maxResult);
    Long pageListCount(DetachedCriteria dc);

    <T> List<T> findByDetached(DetachedCriteria dc);

    /**
     * 执行存储过程
     * @param procedureSql
     * @param params
     */
    <T> List<T> executeProcedure(String procedureSql, Object... params);

    Session getSession();
}
