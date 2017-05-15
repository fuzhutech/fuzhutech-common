package com.fuzhutech.common;

import java.util.List;

public class DataTableResult {

  private int total; // 总记录数

  private List<?> rows; // 显示的记录

  public DataTableResult(List<?> rows, int total) {
    this.rows = rows;
    this.total = total;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public List<?> getRows() {
    return rows;
  }

  public void setRows(List<?> rows) {
    this.rows = rows;
  }

}
