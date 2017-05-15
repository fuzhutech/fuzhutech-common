package com.fuzhutech.common.datasource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSourceChange {
  
  /**
   * 切换当前数据库名
   * @return 数据库名称
   */
  String name() default DataSourceChange.master;

  public static String master = "master";
}
