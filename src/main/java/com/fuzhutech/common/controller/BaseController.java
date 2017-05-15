package com.fuzhutech.common.controller;

//import com.fuzhutech.utils.StringEscapeEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseController {

  /**
   * initBinder.
   * @param binder ServletRequestDataBinder
   */
  @InitBinder
  public void initBinder(ServletRequestDataBinder binder) {
    /**
     * 自动转换日期类型的字段格式
     */
    binder.registerCustomEditor(Date.class,
        new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));

    /**
     * 防止XSS攻击
     */
    //binder.registerCustomEditor(String.class, new StringEscapeEditor(true, false));
  }

}
