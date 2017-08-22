package cn.bclearn.micromvc.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class ControllerAdapter {
    Route route=null;

    public ControllerAdapter(Route route ){
        this.route=route;
    }

    public ControllerAdapter(){

    }
   public Object invoke(Route route){
       Method method=route.getMethod();
        Object controller= null;
        try {
            controller = route.getCotroller().newInstance();
            Object[] args=route.getArgs();
            Object invoke = method.invoke(controller, args);
            return invoke;
        } catch (InstantiationException e) {
            Logger.getLogger(ControllerAdapter.class.getName()).warning("不能实例化"+route.getCotroller().getName());
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e){
            Logger.getLogger(ControllerAdapter.class.getName()).
                    warning("请检查"+method.getName()+"方法是否存在非法类型---请求参数类型必须为String或String数组");
            e.printStackTrace();
        }
        return null;
    }


}
