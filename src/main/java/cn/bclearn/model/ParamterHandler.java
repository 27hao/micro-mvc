package cn.bclearn.model;

import cn.bclearn.controller.Route;
import cn.bclearn.model.annotation.RequestParamAnnotationHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ParamterHandler {
    private MicroRequest microRequest=null;
    private MicroResponse microResponse=null;
    private List<Route> routes=null;
    public ParamterHandler(List<Route> routes, HttpServletRequest request, HttpServletResponse response){
        microRequest=new MicroRequest(request);
        microResponse=new MicroResponse(response);
        this.routes=routes;
    }
    public void argsHandler(){
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
}
