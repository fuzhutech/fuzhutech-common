package com.fuzhutech.common.controller;

import com.fuzhutech.common.entity.BaseEntity;
import com.fuzhutech.common.service.BaseService;
import com.fuzhutech.common.DataTableResult;
import com.fuzhutech.common.PageInfo;
import com.fuzhutech.common.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


//权限管理
//@RestController
//@RequestMapping("/api")
public abstract class RestfulController<T extends BaseEntity> {

    private static Logger logger = LoggerFactory.getLogger(RestfulController.class);

    @Autowired
    protected BaseService<T> service;

    //获取记录id
    protected Integer getModelId(T model){
        return model.getId();
    };

    //获取列表.
    @RequestMapping(method = RequestMethod.GET)
    private List<T> getList(HttpServletRequest request, HttpServletResponse response, T model) {
        //logger.info(this.getModelId());
        return service.queryListByWhere(model);
    }

    //获取列表-分页
    @RequestMapping(value="/page",method = RequestMethod.GET)
    private DataTableResult getListByPageInfo(HttpServletRequest request, HttpServletResponse response, PageInfo pageInfo) {
        return service.queryByPageInfo(pageInfo);
    }

    //获取单条记录
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    private T getSingle(@PathVariable("id") Integer id) {
        return service.queryById(id);
    }

    //响应新增请求
    @RequestMapping(method = RequestMethod.POST)
    private ResponseResult add(@RequestBody T model) {
        ResponseResult responseResult = new ResponseResult();
        try {
            service.add(model);
            responseResult.setData(getModelId(model));
            return new ResponseResult(ResponseResult.SUCCESS,getModelId(model));
        } catch (RuntimeException ex) {
            //logger.error("编辑失败：{}", ex);
            return new ResponseResult(ResponseResult.FAILURE,getModelId(model),ex.getMessage());
        }
    }

    //更新
    @RequestMapping(method = RequestMethod.PUT)
    private ResponseResult edit(@RequestBody T model/*,HttpServletRequest request*/) {
        ResponseResult responseResult = new ResponseResult();
        try {
            service.update(model);
            return new ResponseResult(ResponseResult.SUCCESS);
        } catch (RuntimeException ex) {
            //logger.error("编辑失败：{}", ex);
            return new ResponseResult(ResponseResult.FAILURE,null,ex.getMessage());
        }
    }

    //响应删除请求.
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    private ResponseResult delete(@PathVariable("id") Integer id) {
        ResponseResult responseResult = new ResponseResult();
        try {
            service.deleteById(id);
            return new ResponseResult(ResponseResult.SUCCESS);
        } catch (RuntimeException ex) {
            //logger.error("删除失败：{}", ex);
            return new ResponseResult(ResponseResult.FAILURE,null,ex.getMessage());
        }
    }

}
