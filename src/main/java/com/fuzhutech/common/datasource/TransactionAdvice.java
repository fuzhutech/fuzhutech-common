package com.fuzhutech.common.datasource;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.lang.reflect.Method;

@SuppressWarnings("serial")
public class TransactionAdvice extends TransactionInterceptor {

  private static Logger logger = LoggerFactory.getLogger(TransactionAdvice.class);

  @Override
  public Object invoke(final MethodInvocation invocation) throws Throwable {

    // logger.info("TransactionAdvice.invoke:" + invocation);

    // 如果为接口，咋不进行切换数据源和数据库事务操作
    Method method = invocation.getMethod();
    if (method == null || method.getDeclaringClass().isInterface()) {
      return invocation.proceed();
    }

    DataSourceChange annotation = invocation.getMethod().getAnnotation(DataSourceChange.class);
    if (null != annotation) {
      // 根据注解切换数据源
      DynamicDataSourceHolder.setDataSource(annotation.name());
      if (logger.isDebugEnabled()) {
        logger.debug("数据源切换为{}", annotation.name());
      }
    } else {
      // 根据方法名切换读库和写库
      if (isSlave(method.getName())) {
        // 标记为写库
        if (logger.isDebugEnabled()) {
          logger.debug("数据源切换为master");
        }
        DynamicDataSourceHolder.markMaster();
      } else {
        // 标记为读库
        if (logger.isDebugEnabled()) {
          logger.debug("数据源切换为Slave");
        }
        DynamicDataSourceHolder.markSlave();
      }
    }

    return super.invoke(invocation);
  }

  /**
   * 判断是否为读库.
   * 
   * @param methodName 方法名
   * @return .
   */
  private Boolean isSlave(String methodName) {
    // 方法名以query、find、get开头的方法名走从库
    return StringUtils.startsWithAny(methodName, "query", "find", "get");
  }
}
