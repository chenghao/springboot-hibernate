package com.demo.controller;

import com.demo.entity.SysLogEntity;
import com.demo.service.CommonService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sysLog")
public class SysLogController {
    @Autowired
    private CommonService commonService;

    @RequestMapping(value = "/get")
    @ResponseBody
    public Object get(Long id){
        SysLogEntity entity = commonService.get(SysLogEntity.class, id);
        return entity;
    }

    @RequestMapping(value = "/getAllDbTableName")
    @ResponseBody
    public Object getAllDbTableName(){
        return commonService.getAllDbTableName();
    }

    @RequestMapping(value = "/findByProperty")
    @ResponseBody
    public Object findByProperty(){
        return commonService.findByProperty(SysLogEntity.class, "operation", "登录");
    }

    @RequestMapping(value = "/findByPropertyisOrder")
    @ResponseBody
    public Object findByPropertyisOrder(){
        return commonService.findByPropertyIsOrder(SysLogEntity.class, "operation", "登录",
                "gmtCreate", false);
    }

    @RequestMapping(value = "/findForJdbc")
    @ResponseBody
    public Object findForJdbc(){
        String sql = "select * from sys_log where operation='登录' order by id asc";

        return commonService.findForJdbc(sql, 1, 10);
    }

    @RequestMapping(value = "/findForJdbcParam")
    @ResponseBody
    public Object findForJdbcParam(){
        String sql = "select * from sys_log where operation=? order by id desc";

        return commonService.findForJdbcParam(sql, 1, 10, new Object[]{"登录"});
    }

    @RequestMapping(value = "/pageList")
    @ResponseBody
    public Object pageList(int page, int limit){
        DetachedCriteria criteria = DetachedCriteria.forClass(SysLogEntity.class);

        criteria.add(Restrictions.eq("operation", "登录"));
        criteria.add(Restrictions.lt("id", 1000L));

        List<SysLogEntity> list = commonService.pageList(criteria, page, limit);
        Long count = commonService.pageListCount(criteria);

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("count", count.intValue());

        return map;
    }



}
