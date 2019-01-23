package com.demo.service.impl;

import com.demo.dao.CommonDao;
import com.demo.dao.model.DBTable;
import com.demo.service.CommonService;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CommonServiceImpl implements CommonService {
    @Autowired
    private CommonDao commonDao;

    @Override
    @Transactional(readOnly = true)
    public List<DBTable> getAllDbTableName() {
        return commonDao.getAllDbTableName();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getAllDbTableSize() {
        return commonDao.getAllDbTableSize();
    }

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> loadAll(Class<T> entityClass) {
        return commonDao.loadAll(entityClass);
    }

    @Override
    public int updateBySqlString(String sql) {
        return commonDao.updateBySqlString(sql);
    }

    @Override
    public <T> List<T> findListBySql(String query) {
        return commonDao.findListBySql(query);
    }

    @Override
    public <T> Serializable save(T entity) {
        return commonDao.save(entity);
    }

    @Override
    public <T> void saveOrUpdate(T entity) {
        commonDao.saveOrUpdate(entity);
    }

    @Override
    public <T> void delete(T entity) {
        commonDao.delete(entity);
    }

    @Override
    public <T> void batchSave(List<T> entitys) {
        commonDao.batchSave(entitys);
    }

    @Override
    @Transactional(readOnly = true)
    public <T> T get(Class<T> entityName, Serializable id) {
        return commonDao.get(entityName, id);
    }

    @Override
    @Transactional(readOnly = true)
    public <T> T getEntity(Class entityName, Serializable id) {
        return commonDao.getEntity(entityName, id);
    }

    @Override
    @Transactional(readOnly = true)
    public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName, Object value) {
        return commonDao.findUniqueByProperty(entityClass, propertyName, value);
    }

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findByProperty(Class<T> entityClass, String propertyName, Object value) {
        return commonDao.findByProperty(entityClass, propertyName, value);
    }

    @Override
    public <T> void deleteEntityById(Class entityName, Serializable id) {
        commonDao.deleteEntityById(entityName, id);
    }

    @Override
    public <T> void updateEntity(T entity) {
        commonDao.updateEntity(entity);
    }

    @Override
    public <T> List<T> findByQueryString(String hql) {
        return commonDao.findByQueryString(hql);
    }

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findByPropertyIsOrder(Class<T> entityClass, String propertyName, Object value,
                                             String orderName, boolean isAsc) {
        return commonDao.findByPropertyIsOrder(entityClass, propertyName, value, orderName, isAsc);
    }

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> getList(Class clazz) {
        return commonDao.loadAll(clazz);
    }

    @Override
    @Transactional(readOnly = true)
    public <T> T singleResult(String hql) {
        return commonDao.singleResult(hql);
    }

    @Override
    public Integer executeSql(String sql, List<Object> param) {
        return commonDao.executeSql(sql, param);
    }

    @Override
    public Integer executeSql(String sql, Object... param) {
        return commonDao.executeSql(sql, param);
    }

    @Override
    public Integer executeSql(String sql, Map<String, Object> param) {
        return commonDao.executeSql(sql, param);
    }

    @Override
    public Object executeSqlReturnKey(String sql, Map<String, Object> param) {
        return commonDao.executeSqlReturnKey(sql, param);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> findForJdbc(String sql, Object... objs) {
        return commonDao.findForJdbc(sql, objs);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> findOneForJdbc(String sql, Object... objs) {
        return commonDao.findOneForJdbc(sql, objs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> findForJdbc(String sql, int page, int rows) {
        return commonDao.findForJdbc(sql, page, rows);
    }

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findObjForJdbc(String sql, int page, int rows, Class<T> clazz) {
        return commonDao.findObjForJdbc(sql, page, rows, clazz);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> findForJdbcParam(String sql, int page, int rows, Object... objs) {
        return commonDao.findForJdbcParam(sql, page, rows, objs);
    }

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findHql(String hql, Object... param) {
        return commonDao.findHql(hql, param);
    }

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> pageList(DetachedCriteria dc, int firstResult, int maxResult) {
        return commonDao.pageList(dc, firstResult, maxResult);
    }

    @Override
    public Long pageListCount(DetachedCriteria dc) {
        return commonDao.pageListCount(dc);
    }

    @Override
    @Transactional(readOnly = true)
    public <T> List<T> findByDetached(DetachedCriteria dc) {
        return commonDao.findByDetached(dc);
    }

    @Override
    public <T> List<T> executeProcedure(String procedureSql, Object... params) {
        return commonDao.executeProcedure(procedureSql, params);
    }

    @Override
    public Session getSession() {
        return commonDao.getSession();
    }
}
