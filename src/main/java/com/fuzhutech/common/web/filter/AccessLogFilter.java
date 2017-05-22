package com.fuzhutech.common.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AccessLogFilter extends OncePerRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(AccessLogFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("URI:{},Method:{},QueryString:{}", request.getRequestURI(), request.getMethod(), request.getQueryString());

        filterChain.doFilter(request, response);
    }

}

