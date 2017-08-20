package cn.bclearn.controller;

import cn.bclearn.model.MicroRequest;
import cn.bclearn.model.MicroResponse;
import cn.bclearn.model.annotation.RequestParam;
import cn.bclearn.model.annotation.RequestParamAnnotationHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteMapping {
    private RouteManager manager=null;
    private MicroRequest microRequest=null;
    private MicroResponse microResponse=null;

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

    private void argsHandler(List<Route> routes){
        Map<String, String[]> reqParams = microRequest.getAllParam();  // reqParams 所有请求
        Object[] args = null;         //args 准备传入方法中的所有值
        for (Route route : routes) {
            Class<?>[] params = route.getMethod().getParameterTypes();    // params 该路由中方法的所有参数
            args = new Object[params.length];
            boolean flag = true;
            Map<Integer,String> annotation=new RequestParamAnnotationHandler(route.getMethod()).getRequestParamValueAndIndex();
            for (int i = 0; i < params.length; i++) {
                if (annotation.containsKey(i)&&reqParams.containsKey(annotation.get(i))) {
                    if(params[i].isArray()) {
                        args[i] = reqParams.get(annotation.get(i));
                    }else {
                        args[i]=reqParams.get(annotation.get(i))[0];
                    }
                } else if (params[i].getName().equals(MicroRequest.class.getName())) {
                    args[i] = this.microRequest;
                } else if (params[i].getName().equals(MicroResponse.class.getName())) {
                    args[i] = this.microResponse;
                } else {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                route.setArgs(args);
            }else {
                routes.remove(route);
            }
        }
    }
    public Route findRoute(String uri,HttpServletRequest request, HttpServletResponse response){
        this.microRequest=new MicroRequest(request);
        this.microResponse=new MicroResponse(response);
        List<Route> routes=findRoutes(uri);
        System.out.println(routes.get(0));
        if(routes!=null) {

            argsHandler(routes);

            int index = 0;

            for (int i = 1; i < routes.size(); i++) {
                Route route = routes.get(i);
                if (route.getArgs().length > routes.get(index).getArgs().length) {
                    index = i;
                }
            }
            return routes.get(index);
        }
        return null;
    }
}
