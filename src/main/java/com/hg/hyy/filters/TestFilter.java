package com.hg.hyy.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.hg.hyy.entity.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestFilter implements Filter {
    private static Logger log = LoggerFactory.getLogger(TestFilter.class);

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        log.error("filter url:" + request.getRequestURI());
        arg2.doFilter(arg0, arg1);
        try {

            Log.getLog(this).error("过滤成功！");
        } catch (Exception e) {
            Log.getLog(this).debug("过滤错误！" + e.getMessage());
        }

    }

}
