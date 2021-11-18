package com.hg.hyy.filters;

import com.hg.hyy.entity.IpInfo;
import com.hg.hyy.service.IpAddressService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class UrlFilter implements Filter {
  private static final Logger log = LoggerFactory.getLogger(UrlFilter.class);

  public void destroy() {}

  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    String curOrigin = request.getHeader("Origin");
    HttpServletResponse response = (HttpServletResponse) servletResponse;

    response.setCharacterEncoding("UTF-8");
    response.setHeader("Access-Control-Allow-Origin", curOrigin == null ? "true" : curOrigin);
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
    response.setHeader(
        "Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    String requestStr = getRequestString(request);
    log.error("请求的地址：" + request.getRequestURL().toString());
    log.error("请求的方式：" + request.getMethod());

    IpInfo ipInfo = getIp(request); // 获取用户IP信息
    log.info("请求的IP地址：" + ipInfo.getIp() + ",请求的浏览器：" + ipInfo.getUA());
    if ("bingo".equals(urlFilter2(requestStr))
        || "bingo".equals(urlFilter2(request.getRequestURL().toString()))) {
      log.error("访问地址发现非法字符，已拦截非法地址：" + request.getRequestURL().toString());
      PrintWriter writer = response.getWriter();
      response.setCharacterEncoding("utf-8");
      response.setContentType("text/html; charset=utf-8");
      writer.print("访问地址发现非法字符");
      writer.close();
      return;
    }
    // 允许以下IP、域名和端口访问
    String allowHosts =
        request.getHeader("host") != null ? request.getHeader("host") : "localhost:8080";
    if (!StringUtils.equals(allowHosts, "localhost:8080")) {
      log.error("访问host非法，已拦截。非法host:" + allowHosts);
      PrintWriter writer = response.getWriter();
      response.setCharacterEncoding("utf-8");
      response.setContentType("text/html; charset=utf-8");
      writer.print("访问host非法，已拦截");
      writer.close();
      return;
    }

    // 过滤请求特殊字符，扫描跨站式漏洞
    Map<String, String[]> parameters = request.getParameterMap();
    Map<String, String> paramsMap = new HashMap<>();

    if (parameters != null && parameters.size() > 0) {
      for (String key : parameters.keySet()) {
        String[] values = parameters.get(key);
        for (int i = 0; i < values.length; i++) {
          values[i] = urlFilter1(values[i]);
          // log.error("参数：{}——>值：{}", key, values[i]);
          paramsMap.put(key, values[i]);
        }
      }
      log.error("请求的参数：" + paramsMap);
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }

  public void init(FilterConfig filterConfig) throws ServletException {}

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
    String requestPath = req.getServletPath();
    String queryString = req.getQueryString();
    if (queryString != null) return requestPath + "?" + queryString;
    else return requestPath;
  }

  public String urlFilter2(String a) {
    if (StringUtils.isNotEmpty(a)) {
      if (a.contains("%22")
          || a.contains("%3E")
          || a.contains("%3e")
          || a.contains("%3C")
          || a.contains("%3c")
          || a.contains("<")
          || a.contains(">")
          || a.contains("\"")
          || a.contains("'")
          || a.contains("+")
          || /*
              * a.contains("%27") ||
              */ a.contains(" and ")
          || a.contains(" or ")
          || a.contains("1=1")
          || a.contains("(")
          || a.contains(")")) {
        return "bingo";
      }
    }
    return a;
  }

  public IpInfo getIp(HttpServletRequest request) {
    IpInfo ipInfo = new IpInfo();
    String browser = request.getHeader("User-Agent"); // 浏览器信息
    String ip = request.getHeader("x-forwarded-for"); // IP地址
    if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
      // 多次反向代理后会有多个ip值，第一个ip才是真实ip
      if (ip.contains(",")) {
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
    ipInfo.setCity(new IpAddressService().getCityName(ip)); // 根据IP地址获取城市
    return ipInfo;
  }
}
