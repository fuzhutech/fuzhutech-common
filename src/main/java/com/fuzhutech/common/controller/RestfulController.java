package com.fuzhutech.common.controller;

import com.fuzhutech.common.DataTableResult;
import com.fuzhutech.common.PageInfo;
import com.fuzhutech.common.ResponseResult;
import com.fuzhutech.common.entity.BaseEntity;
import com.fuzhutech.common.service.BaseService;
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
public abstract class RestfulController<T extends BaseEntity> extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(RestfulController.class);

    @Autowired
    protected BaseService<T> service;

    //获取列表.
    @RequestMapping(method = RequestMethod.GET)
    public List<T> getList(HttpServletRequest request, HttpServletResponse response, T model) {
        return getListInternal(request, response, model);
    }

    //获取列表-分页
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public DataTableResult getListByPageInfo(HttpServletRequest request, HttpServletResponse response, PageInfo pageInfo) {
        return getListByPageInfoInternal(request, response, pageInfo);
    }

    //获取单条记录
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public T getSingle(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Integer id) {
        return getSingleInternal(request, response, id);
    }

    //响应新增请求
    @RequestMapping(method = RequestMethod.POST)
    public ResponseResult add(HttpServletRequest request, HttpServletResponse response, @RequestBody T model) {
        return addInternal(request, response, model);
    }

    //更新
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseResult edit(HttpServletRequest request, HttpServletResponse response, @RequestBody T model) {
        return editInternal(request, response, model);
    }

    //响应删除请求.
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseResult delete(HttpServletRequest request, HttpServletResponse response,
                                 @PathVariable("id") Integer id) {
        return deleteInternal(request, response, id);
    }

    protected List<T> getListInternal(HttpServletRequest request, HttpServletResponse response, T model) {
        return service.queryListByWhere(model);
    }

    protected DataTableResult getListByPageInfoInternal(HttpServletRequest request, HttpServletResponse response, PageInfo pageInfo) {
        return service.queryByPageInfo(pageInfo);
    }

    protected T getSingleInternal(HttpServletRequest request, HttpServletResponse response, Integer id) {
        return service.queryById(id);
    }

    protected ResponseResult addInternal(HttpServletRequest request, HttpServletResponse response, T model) {
        try {
            service.add(model);

            //将id返回给请求
            return new ResponseResult(ResponseResult.SUCCESS, model.getId());
        } catch (RuntimeException ex) {
            logger.error("新增失败：{}", ex);
            return new ResponseResult(ResponseResult.FAILURE, model.getId(), ex.getMessage());
        }

    }

    protected ResponseResult editInternal(HttpServletRequest request, HttpServletResponse response, T model) {
        try {
            service.update(model);
            return new ResponseResult(ResponseResult.SUCCESS);
        } catch (RuntimeException ex) {
            logger.error("编辑失败：{}", ex);
            return new ResponseResult(ResponseResult.FAILURE, null, ex.getMessage());
        }
    }

    protected ResponseResult deleteInternal(HttpServletRequest request, HttpServletResponse response, Integer id) {
        try {
            service.deleteById(id);

            return new ResponseResult(ResponseResult.SUCCESS);
        } catch (RuntimeException ex) {
            logger.error("删除失败：{}", ex);
            return new ResponseResult(ResponseResult.FAILURE, null, ex.getMessage());
        }

    }

}
