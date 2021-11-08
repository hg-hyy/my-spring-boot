package com.hg.hyy.filters;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.hg.hyy.entity.IpInfo;
import com.hg.hyy.service.IpAddressService;

import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UrlFilter implements Filter {
    private static Logger log = LoggerFactory.getLogger(UrlFilter.class);
    @Autowired
    IpAddressService ipAddressService;// IP服务类

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String curOrigin = request.getHeader("Origin");
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", curOrigin == null ? "true" : curOrigin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        String requestStr = getRequestString(request);
        log.error("请求的地址：" + request.getRequestURL().toString());
        log.error("请求的方式：" + request.getMethod());

        IpInfo ipInfo = getIp(request); // 获取用户IP信息
        log.error("请求的IP地址：" + ipInfo.getIp() + ",请求的浏览器：" + ipInfo.getUA());
        if ("bingo".equals(urlFilter2(requestStr)) || "bingo".equals(urlFilter2(request.getRequestURL().toString()))) {
            log.error("访问地址发现非法字符，已拦截非法地址：" + request.getRequestURL().toString());
            PrintWriter writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html; charset=utf-8");
            writer.print("访问地址发现非法字符");
            writer.close();
            return;
        }
        // 允许以下主机ip或域名和端口允许访问
        String myhosts = request.getHeader("host");
        if (!StringUtils.equals(myhosts, "127.0.0.1")// 特定ip
                && !StringUtils.equals(myhosts, "127.0.0.1:8080")// 特定ip和端口
                && !StringUtils.equals(myhosts, "localhost:8080")) {
            log.error("访问host非法，已拦截。非法host:" + myhosts);
            PrintWriter writer = response.getWriter();
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html; charset=utf-8");

            writer.print("访问host非法，已拦截");
            writer.close();
            return;
        }

        // 过滤请求特殊字符，扫描跨站式漏洞
        Map<String, String[]> parameters = request.getParameterMap();
        Map<String, String> paramsmap = new HashMap<String, String>();

        if (parameters != null && parameters.size() > 0) {
            for (Iterator<String> iter = parameters.keySet().iterator(); iter.hasNext();) {
                String key = (String) iter.next();
                String[] values = (String[]) parameters.get(key);
                for (int i = 0; i < values.length; i++) {
                    values[i] = urlFilter1(values[i]);
                    // log.error("参数：{}——>值：{}", key, values[i]);
                    paramsmap.put(key, values[i]);
                }
            }
            log.error("请求的参数：" + paramsmap.toString());
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public static String urlFilter1(String a) {
        a = a.replaceAll("%22", "");
        a = a.replaceAll("%27", "");
        a = a.replaceAll("%3E", "");
        a = a.replaceAll("%3e", "");
        a = a.replaceAll("%3C", "");
        a = a.replaceAll("%3c", "");
        a = a.replaceAll("<", "");
        a = a.replaceAll(">", "");
        a = a.replaceAll("\"", "");
        a = a.replaceAll("'", "");
        a = a.replaceAll("\\+", "");
        a = a.replaceAll("\\(", "");
        a = a.replaceAll("\\)", "");
        a = a.replaceAll(" and ", "");
        a = a.replaceAll(" or ", "");
        a = a.replaceAll(" 1=1 ", "");
        return a;
    }

    private String getRequestString(HttpServletRequest req) {
        String requestPath = req.getServletPath().toString();
        String queryString = req.getQueryString();
        if (queryString != null)
            return requestPath + "?" + queryString;
        else
            return requestPath;
    }

    public String urlFilter2(String a) {
        if (StringUtils.isNotEmpty(a)) {
            if (a.contains("%22") || a.contains("%3E") || a.contains("%3e") || a.contains("%3C") || a.contains("%3c")
                    || a.contains("<") || a.contains(">") || a.contains("\"") || a.contains("'") || a.contains("+")
                    || /*
                        * a.contains("%27") ||
                        */
                    a.contains(" and ") || a.contains(" or ") || a.contains("1=1") || a.contains("(")
                    || a.contains(")")) {
                return "bingo";
            }
        }
        return a;
    }

    public IpInfo getIp(HttpServletRequest request) {
        IpInfo ipInfo = new IpInfo();
        String browser = request.getHeader("User-Agent");// 浏览器信息
        String ip = request.getHeader("x-forwarded-for");// IP地址
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        ipInfo.setIp(ip);
        ipInfo.setUA(browser);
        ipInfo.setCity(new IpAddressService().getCityName(ip));// 根据IP地址获取城市
        return ipInfo;
    }
}