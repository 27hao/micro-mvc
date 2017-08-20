package cn.bclearn.controller;

import cn.bclearn.MicroFilter;
import cn.bclearn.model.MicroRequest;
import cn.bclearn.model.MicroResponse;
import cn.bclearn.model.ParamterHandler;
import cn.bclearn.model.annotation.RequestParam;
import cn.bclearn.model.annotation.RequestParamAnnotationHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class RouteMapping {
    private RouteManager manager=null;
    private ParamterHandler handler=null;
    public RouteMapping(){
        manager=RouteManager.getInstance();
    }
    private List<Route> findRoutes(String uri){
        List<Route> routes=new ArrayList<Route>();
        for(Route route:manager.getRoutes()){
            if (uri.equals(route.getUri())){
                routes.add(route);
            }
        }
        return routes;
    }


    public Route findRoute(String uri,HttpServletRequest request, HttpServletResponse response){
        List<Route> routes=findRoutes(uri);
        if(routes!=null) {
            handler=new ParamterHandler(routes,request,response);
            handler.argsHandler();

            int index = 0;
            for (int i = 1; i < routes.size(); i++) {
                Route route = routes.get(i);
                if (route.getArgs().length > routes.get(index).getArgs().length) {
                    index = i;
                }
            }
            Logger.getLogger(RouteMapping.class.getName()).
                    info("---已匹配到路由:"+routes.get(index));
            return routes.get(index);
        }
        return null;
    }
}
