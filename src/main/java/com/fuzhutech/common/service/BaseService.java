package com.fuzhutech.common.service;

import com.fuzhutech.common.DataTableResult;
import com.fuzhutech.common.PageInfo;

import java.util.List;

public interface BaseService<T> {

  T queryById(Integer id);

  DataTableResult queryByPageInfo(PageInfo pageInfo);

  List<T> queryListByWhere(T record);

  Integer add(T record);

  Integer update(T record);
  
  Integer updateSelective(T record);

  Integer deleteById(Integer id);

  Integer deleteByWhere(T record);

}
