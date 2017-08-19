package cn.bclearn;

import cn.bclearn.controller.config.BootConfig;

import javax.servlet.*;
import java.io.IOException;

public class MicroFilter implements Filter{

    private BootConfig bootConfig=null;
    public void init(FilterConfig filterConfig) throws ServletException {
        String configClassName=filterConfig.getInitParameter("config-class");
        try {
            Class clazz=Class.forName(configClassName);
            bootConfig=(BootConfig) clazz.newInstance();
            bootConfig.init();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    }

    public void destroy() {

    }
}
