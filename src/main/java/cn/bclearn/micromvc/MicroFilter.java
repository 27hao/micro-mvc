package cn.bclearn.micromvc;

import cn.bclearn.micromvc.controller.ControllerAdapter;
import cn.bclearn.micromvc.controller.Route;
import cn.bclearn.micromvc.controller.RouteMapping;
import cn.bclearn.micromvc.controller.config.BootConfig;
import cn.bclearn.micromvc.controller.config.DefaultBootConfig;
import cn.bclearn.micromvc.util.Constants;
import cn.bclearn.micromvc.view.DefaultViewHandler;
import cn.bclearn.micromvc.view.ViewHandler;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Logger;

public class MicroFilter implements Filter{

    private RouteMapping routeMapping=null;
    private BootConfig bootConfig=null;
    private ControllerAdapter adapter=null;
    private ViewHandler viewHandler=null;
    public void init(FilterConfig filterConfig) throws ServletException {
        String configClassName=filterConfig.getInitParameter("config-class");

        routeMapping=new RouteMapping();
        adapter = new ControllerAdapter();

        if(configClassName!=null) {
            try {
                Class clazz = Class.forName(configClassName);
                bootConfig = (BootConfig) clazz.newInstance();
                bootConfig.init();
                Logger.getLogger(MicroFilter.class.getName()).
                        info("---已加载自定义配置类:"+configClassName);
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
            Logger.getLogger(MicroFilter.class.getName()).
                    info("---已加载默认配置类:"+bootConfig.getClass().getName());
        }
        viewHandler=new DefaultViewHandler(bootConfig);

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        servletRequest.setCharacterEncoding(Constants.DEFAULT_CHARACTER);
        servletResponse.setCharacterEncoding(Constants.DEFAULT_CHARACTER);
        String uri=getRealUri(servletRequest);
        Logger.getLogger(MicroFilter.class.getName()).
                info("---得到请求:"+uri);
        Route route=routeMapping.findRoute(uri,servletRequest,servletResponse);
        if(route!=null) {
            Object result=adapter.invoke(route);
            if(result!=null) {
                viewHandler.resolve(result, servletRequest, servletResponse);
            }
        }else {
            chain.doFilter(request,response);
        }
    }

    private String getRealUri(HttpServletRequest request) {
        String uri=request.getRequestURI();
        String appPath=request.getContextPath();
        String realUri=uri.substring(appPath.length(),uri.length());
        char[] temp=new char[realUri.length()];
        temp[0]='/';
        for(int i=1,j=1;i<realUri.length();i++){
            if(realUri.charAt(i)!='/'||realUri.charAt(i-1)!='/'){
                temp[j++]=realUri.charAt(i);
            }
        }

        realUri= new String(temp).trim();
        try {
            realUri=URLDecoder.decode(realUri,Constants.DEFAULT_CHARACTER);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return realUri;
    }

    public void destroy() {

    }
}
