package cn.bclearn.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControllerAdapter {
    Route route=null;

    public ControllerAdapter(Route route ){
        this.route=route;
    }

   public void invoke(){
       Method method=route.getMethod();
        Object controller= null;
        try {
            controller = route.getCotroller().newInstance();
            Object[] args=route.getArgs();
            Object invoke = method.invoke(controller, args);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
