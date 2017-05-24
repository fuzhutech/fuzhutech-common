package com.fuzhutech.common.dao;

import com.fuzhutech.common.PageInfo;

import java.util.List;

public interface BaseMapper<T> {

  //MyBatis-generator默认生成方法
  int deleteByPrimaryKey(Integer id);

  int insert(T record);

  int insertSelective(T record);

  T selectByPrimaryKey(Integer id);

  int updateByPrimaryKeySelective(T record);

  int updateByPrimaryKey(T record);

  // 需要手工在mapper.xml添加的方法
  List<?> selectByPageInfo(PageInfo pageInfo);

  int countByPageInfo(PageInfo pageInfo);

  List<T> selectByWhere(T record);

  int deleteByWhere(T record);
  
  List<T> selectAll();

}
