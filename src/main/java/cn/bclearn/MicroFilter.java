package cn.bclearn;

import cn.bclearn.controller.Route;
import cn.bclearn.controller.RouteMapping;
import cn.bclearn.controller.config.BootConfig;
import cn.bclearn.controller.config.DefaultBootConfig;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MicroFilter implements Filter{

    private RouteMapping routeMapping=null;
    private BootConfig bootConfig=null;
    public void init(FilterConfig filterConfig) throws ServletException {
        String configClassName=filterConfig.getInitParameter("config-class");
        routeMapping=new RouteMapping();
        if(configClassName!=null) {
            try {
                Class clazz = Class.forName(configClassName);
                bootConfig = (BootConfig) clazz.newInstance();
                bootConfig.init();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }else {
            bootConfig=new DefaultBootConfig();
            bootConfig.init();
        }

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        String uri=servletRequest.getRequestURI();
        Route route=routeMapping.findRoute(uri,servletRequest,servletResponse);

    }

    public void destroy() {

    }
}
