package com.fuzhutech.common.web.filter;

import com.fuzhutech.common.web.servlet.gzip.GZIPServletResponseWrapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GzipFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(GzipFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //性能监控
        long startTime = System.currentTimeMillis();

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //如果浏览器支持gzip压缩且满足gzip压缩样式
        if (isGzipAccepted(req) && isNeededGzip(req)) {
            // 创建装饰后的resposne对象
            GZIPServletResponseWrapper gzipResponseWrapper = new GZIPServletResponseWrapper(res);
            // 这里传递的是装饰后的Response对象，所以printwrite 使用的是压缩输出
            // 使用过滤器就是将所有的响应在到大浏览器之前将原来的Response 替换为装饰后的Response对象

            chain.doFilter(request, gzipResponseWrapper); //使用自定义的response

            gzipResponseWrapper.finishResponse();//输出压缩数据

        } else {
            //否则不压缩
            chain.doFilter(request, response);
        }

        long ss = System.currentTimeMillis() - startTime;
        logger.info("压缩{}耗时:{}",((HttpServletRequest) request).getRequestURL(),ss);
    }

    @Override
    public void destroy() {
        //
    }

    //判断浏览器是否支持gzip输出
    private boolean isGzipAccepted(HttpServletRequest request) {
        String value = request.getHeader("Accept-Encoding");
        return value != null && value.toLowerCase().contains("gzip");
    }

    private boolean isNeededGzip(HttpServletRequest request){
        String uri = request.getRequestURI();
        String ext = FilenameUtils.getExtension(uri);

        //需要过滤的扩展名：.htm,.html,.jsp,.js,.ajax,.css
        String gzippPattern=",.htm,.html,.jsp,.js,.ajax,.css,";
        return (StringUtils.indexOf(gzippPattern, ",."+ext+",") != -1);
    }

}
