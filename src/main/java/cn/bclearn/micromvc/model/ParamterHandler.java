package cn.bclearn.micromvc.model;

import cn.bclearn.micromvc.controller.Route;
import cn.bclearn.micromvc.model.annotation.RequestParamAnnotationHandler;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.Iterator;
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

    /**
     * 去除没有请求参数匹配的路由
     * 绑定路由参数的值
     */
    public void argsHandler(){
        if(routes==null)
            return;
        Map<String, String[]> reqParams = microRequest.getAllParam();  // 所有请求参数
        Object[] args = null;         //args 准备传入方法中的所有值
        Iterator iterator=routes.iterator();
        /**
         * 遍历uri相匹配的所有routes
         */
        while (iterator.hasNext()){
            Route route= (Route) iterator.next();

            Class<?>[] params = route.getMethod().getParameterTypes();    // params 该路由中方法的所有参数
            args = new Object[params.length];   //储存参数的具体值
            boolean flag = true;    //判断该路由方法的每一个参数是否有匹配请求值
            //得到该路由方法参数上的所有注解，key为位置,value为注解的值
            Map<Integer,String> annotation=new RequestParamAnnotationHandler(route.getMethod()).getRequestParamValueAndIndex();
            /**
             * 遍历路由方法的每一个参数
             */
            for (int i = 0; i < params.length; i++) {

                if (annotation.containsKey(i)&&reqParams.containsKey(annotation.get(i))) {  //如果该参数上有注解并且请求参数中包含该注解值,必须为String型
                    if(params[i].isArray()) {
                        args[i] = reqParams.get(annotation.get(i));
                    }else {
                        args[i]=reqParams.get(annotation.get(i))[0];
                    }
                }else if (ServletRequest.class.isAssignableFrom(params[i])) {   //如果该参数实现了ServletRequest
                    args[i] = this.microRequest;
                } else if (ServletResponse.class.isAssignableFrom(params[i])) {     //如果该参数实现了ServletResponse
                    args[i] = this.microResponse;
                } else {
                    /**
                     * 最后如果该参数是一个自定义实体类
                     * 首先将该路由设置为没有匹配到参数
                     * 如果该实体类有一个字段名和请求参数匹配
                     * 将该路由设置匹配到参数
                     */
                    flag = false;
                    Field[] fields=params[i].getDeclaredFields();
                    Object value=null;
                    try {
                        value=params[i].newInstance();
                        /**
                         * 遍历该实体类的所有字段
                         *
                         */
                        for(Field field:fields){
                            if(reqParams.containsKey(field.getName())){
                                flag=true;
                                if(field.getClass().isArray()){
                                    field.set(value, reqParams.get(field.getName()));
                                }else {
                                    field.set(value, reqParams.get(field.getName())[0]);
                                }
                            }
                        }
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    if(flag) {
                        args[i]=value;
                    }else {
                        break;
                    }
                }
            }

            //最后通过flag移除route
            if (flag) {
                route.setArgs(args);
            }else {
                iterator.remove();
            }
        }
    }
}
