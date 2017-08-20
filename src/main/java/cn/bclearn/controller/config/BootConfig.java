package cn.bclearn.controller.config;

import cn.bclearn.controller.RouteManager;
import cn.bclearn.controller.annotation.Controller;
import cn.bclearn.controller.annotation.Route;
import cn.bclearn.util.ClasspathPackageScanner;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Logger;

public abstract class BootConfig {
    protected String basePackage=null;

    abstract void routeConfig(RouteManager manager);

    abstract void otherConfig();

    private void scanAndAdd(RouteManager manager){
        ClasspathPackageScanner scanner=new ClasspathPackageScanner(this.basePackage);
        List<String> classNames=null;
        try {
            classNames=scanner.getFullyQualifiedClassNameList();
            for(String name:classNames){
                Class clazz=Class.forName(name);
                if(clazz.isAnnotationPresent(Controller.class)){
                    Method[] methods= clazz.getMethods();
                    for (Method method:methods){
                        if(method.isAnnotationPresent(Route.class)){
                            String uri=((Controller)clazz.getAnnotation(Controller.class)).value()+
                                    method.getAnnotation(Route.class).value();
                            manager.addRoute(uri,method.getName(),clazz);
                            System.out.println("添加了路由"+uri+" "+method.getName()+" "+clazz.getSimpleName());
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public final void init(){
        RouteManager routeManager=routeManager=RouteManager.getInstance();
        otherConfig();
        routeConfig(routeManager);
        if(basePackage!=null) {
            scanAndAdd(routeManager);
        }
    }
}
