package com.fuzhutech.common.service.impl;

import com.fuzhutech.common.dao.BaseMapper;
import com.fuzhutech.common.service.BaseService;
import com.fuzhutech.common.DataTableResult;
import com.fuzhutech.common.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

    //private static final Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

    @Autowired
    protected BaseMapper<T> mapper;

    /**
     * 根据id查询数据.
     *
     * @param id 主键ID
     * @return 返回T
     */
    public T queryById(Integer id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    /**
     * 根据id删除数据.
     *
     * @param id 主键ID
     * @return 删除了几条记录.
     */
    public Integer deleteById(Integer id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    /**
     * 新增数据.
     *
     * @param record 对象数据
     * @return 插入几条数据
     */
    public Integer add(T record) {
        // t.setCreated(new Date());
        // t.setUpdated(t.getCreated());
        return this.mapper.insert(record);
    }

    /**
     * 根据主键更新数据.
     *
     * @param record 数据对象
     * @return 更新了几条记录
     */
    public Integer update(T record) {
        // t.setUpdated(new Date());
        return this.mapper.updateByPrimaryKey(record);
    }

    /**
     * 有选择的更新，选择不为null的字段作为更新字段.
     *
     * @param record .
     * @return .
     */
    public Integer updateSelective(T record) {
        // t.setUpdated(new Date());
        return this.mapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 分页查询数据列表.
     *
     * @param pageInfo 分页信息
     */
    public DataTableResult queryByPageInfo(PageInfo pageInfo) {
        if (pageInfo.getTotal() == -1)
            return new DataTableResult(mapper.selectByPageInfo(pageInfo), mapper.countByPageInfo(pageInfo));
        else
            return new DataTableResult(mapper.selectByPageInfo(pageInfo), pageInfo.getTotal());
    }

    /**
     * 根据条件查询数据列表.
     *
     * @param record .
     * @return .
     */
    public List<T> queryListByWhere(T record) {
        return this.mapper.selectByWhere(record);
    }

    /**
     * 根据条件删除数据.
     *
     * @param record .
     * @return .
     */
    public Integer deleteByWhere(T record) {
        return this.mapper.deleteByWhere(record);
    }

    /**
     * 根据条件查询数据列表.
     *
     * @param record .
     * @return .
     */

    /** 下面的系列方法还未实现. */

    /**
     * 查询所有数据.
     *
     * @return .
     */
  /*
   * public List<T> queryAll() { // return this.mapper.select(null); return null; }
   */

    /**
     * 根据条件查询一条数据.
     *
     * @param record .
     * @return .
     */
    public T queryOne(T record) {
        // return this.mapper.selectOne(record);
        return null;
    }

    /**
     * 分页查询数据列表.
     *
     * @param page .
     * @param rows .
     * @param record .
     * @return .
     */
  /*
   * public PageInfo<T> queryPageListByWhere(Integer page, Integer rows, T record) { // 设置分页参数
   * PageHelper.startPage(page, rows); List<T> list = this.mapper.select(record); return new
   * PageInfo<T>(list); }
   */

    /**
     * 新增数据
     *
     * @param record .
     * @return .
     */
    public Integer save(T record) {
        // t.setCreated(new Date());
        // t.setUpdated(t.getCreated());
        return this.mapper.insert(record);
    }

    /**
     * 有选择的保存，选择不为null的字段作为插入字段.
     *
     * @param record .
     * @return .
     */
    public Integer saveSelective(T record) {
        // t.setCreated(new Date());
        // t.setUpdated(t.getCreated());
        return this.mapper.insertSelective(record);
    }


    // 批量删除
  /*
   * public Integer deleteByIds(Class<T> clazz, String property, List<Object> values) { Example
   * example = new Example(clazz); example.createCriteria().andIn(property, values); return
   * this.mapper.deleteByExample(example); }
   */


}
