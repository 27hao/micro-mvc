package cn.bclearn.micromvc.controller;

import cn.bclearn.micromvc.model.ParamterHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
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
        //得到所有能匹配该uri的路由
        List<Route> routes=findRoutes(uri);

        handler=new ParamterHandler(routes,request,response);
        handler.argsHandler();

        if(routes.size()>0) {
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
        Logger.getLogger(RouteMapping.class.getName()).
                info("---没有匹配到路由:"+uri+"---请检查参数是否一致!");
        return null;
    }
}
