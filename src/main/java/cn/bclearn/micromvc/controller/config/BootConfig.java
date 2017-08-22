package cn.bclearn.micromvc.controller.config;

import cn.bclearn.micromvc.controller.Route;
import cn.bclearn.micromvc.controller.RouteManager;
import cn.bclearn.micromvc.controller.annotation.Controller;
import cn.bclearn.micromvc.util.ClasspathPackageScanner;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Logger;

public abstract class BootConfig implements Config{
    protected String basePackage=null;
    protected String viewPrefix="";
    protected String viewSuffix=".jsp";

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
                        if(method.isAnnotationPresent(cn.bclearn.micromvc.controller.annotation.Route.class)){

                            //uri为控制类注解加方法的注解
                            String realUri=((Controller)clazz.getAnnotation(Controller.class)).value()+
                                    method.getAnnotation(cn.bclearn.micromvc.controller.annotation.Route.class).value();

                            /*
                                对uri做处理
                                去掉多余的“/”
                                去掉前后的空格
                                如果不是以“/”开头加上“/”
                             */
                            char[] temp=new char[realUri.length()];
                            temp[0]='/';
                            for(int i=1,j=1;i<realUri.length();i++){
                                if(realUri.charAt(i)!='/'||realUri.charAt(i-1)!='/'){
                                    temp[j++]=realUri.charAt(i);
                                }
                            }
                            realUri= new String(temp).trim();
                            if(!realUri.startsWith("/")){
                                realUri="/"+realUri;
                            }

                            Route route=new Route();
                            route.setUri(realUri);
                            route.setCotroller(clazz);
                            route.setMethod(method);
                            manager.addRoute(realUri,method.getName(),clazz);
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

    public String getBasePackage() {
        return basePackage;
    }

    public String getViewPrefix() {
        return viewPrefix;
    }

    public String getViewSuffix() {
        return viewSuffix;
    }
}
