package com.fuzhutech.common;

import java.util.Map;

// 分页实体类
public class PageInfo {

    private int offset; //First row offset

    private int rows; // Number of rows per page

    private int total = -1; //默认值-1表示请求未赋值

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    //private String sort = "id";// 排序字段

    //private String order = "asc";// asc，desc mybatis Order 关键字
}
