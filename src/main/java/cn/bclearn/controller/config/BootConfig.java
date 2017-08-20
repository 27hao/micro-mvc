package cn.bclearn.controller.config;

import cn.bclearn.controller.Route;
import cn.bclearn.controller.RouteManager;
import cn.bclearn.controller.annotation.Controller;
import cn.bclearn.util.ClasspathPackageScanner;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Logger;

public abstract class BootConfig implements Config{
    protected String basePackage=null;

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
                        if(method.isAnnotationPresent(cn.bclearn.controller.annotation.Route.class)){
                            String uri=((Controller)clazz.getAnnotation(Controller.class)).value()+
                                    method.getAnnotation(cn.bclearn.controller.annotation.Route.class).value();
                            Route route=new Route();
                            route.setUri(uri);
                            route.setCotroller(clazz);
                            route.setMethod(method);
                            manager.addRoute(uri,method.getName(),clazz);
                            Logger.getLogger(BootConfig.class.getName()).
                                    info("---通过注解添加了路由:"+route);
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
