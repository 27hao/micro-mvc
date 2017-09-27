package cn.bclearn.micromvc;

import cn.bclearn.micromvc.controller.ControllerAdapter;
import cn.bclearn.micromvc.controller.Route;
import cn.bclearn.micromvc.controller.RouteMapping;
import cn.bclearn.micromvc.controller.config.BootConfig;
import cn.bclearn.micromvc.controller.config.DefaultBootConfig;
import cn.bclearn.micromvc.model.validate.ErrorMessage;
import cn.bclearn.micromvc.model.validate.ValidateAnnotationHandler;
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
        System.out.println("adpter:"+adapter.hashCode());
        System.out.println("view:"+viewHandler.hashCode());
        if(route!=null) {
            /**
             * 验证
             */
            ErrorMessage errorMessage=new ValidateAnnotationHandler().validate(route);
            if(errorMessage.size()>0){
                String ref=servletRequest.getHeader("Referer");
                if(ref!=null) {
                    servletRequest.setAttribute("micro-error",errorMessage);
                    String[] referer=ref.split("/");
                    servletRequest.getRequestDispatcher(referer[referer.length - 1]).forward(servletRequest, servletResponse);
                }else {
                    servletResponse.setStatus(500);
                    chain.doFilter(servletRequest,servletResponse);
                }
            }
            /**
             * 执行Controller
             */
            Object result=adapter.invoke(route);
            /**
             * 视图解析
             */
            if(result!=null) {
                viewHandler.resolve(result, servletRequest, servletResponse);
            }else {
                chain.doFilter(request,response);
            }
        }else {
            chain.doFilter(request,response);
        }
    }

    /**
     *
     * 处理uri
     * 去掉多余的'/'
     * 去掉appPath
     * @param request 从request中得到uri
     * @return 返回处理后的uri
     */
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
            /**
             * 尝试转化为utf-8编码
             */
            realUri=URLDecoder.decode(realUri,Constants.DEFAULT_CHARACTER);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return realUri;
    }

    public void destroy() {

    }
}
