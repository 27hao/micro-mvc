package cn.bclearn.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * 管理路由Route
 *单例，全局只存在一个实例
 */
public class RouteManager {

        private static final RouteManager routeManager=new RouteManager();
        private List<Route> routes;
        private int size;

        private RouteManager(){
            routes=new ArrayList<Route>();
        }

        public static RouteManager getInstance(){
                return routeManager;
        }

        public void addRoute(Route route){
                this.routes.add(route);
        }

        public void addRoute(String uri, String method, Class controller){
                Method[] methods=controller.getMethods();
                int count=0;
                for(Method m:methods){
                        if(m.getName().equals(method)){
                                Route route=new Route();
                                route.setUri(uri);
                                route.setMethod(m);
                                route.setCotroller(controller);
                                this.routes.add(route);
                                count++;
                        }
                }
                if (count==0){
                        Logger.getLogger(RouteManager.class.getName()).
                                warning("在"+ controller.getSimpleName()+"中没有找到名为"+method+"的方法");
                }
        }

        public void addRoute(String uri, String method, Class controller,Class...methodArgs){
                Route route=new Route();
                route.setUri(uri);
                route.setCotroller(controller);
                try {
                        Method m=controller.getMethod(method,methodArgs);
                } catch (NoSuchMethodException e) {
                        Logger.getLogger(RouteManager.class.getName()).
                                warning("在"+ controller.getSimpleName()+"中没有找到名为"+method+"且参数相符的方法");
                        e.printStackTrace();
                }
                this.routes.add(route);
        }

        public void addRoutes(List<Route> routes){
                this.routes.addAll(routes);
        }

        public int getSize(){
                return this.routes.size();
        }

        public void removeRoute(Route route){
                Iterator ite= this.routes.iterator();
                while (ite.hasNext()){
                        Route r= (Route) ite.next();
                        if (r.equals(route)){
                                ite.remove();
                        }
                }
        }

        public void removeRoute(String uri){
                Iterator ite= this.routes.iterator();
                while( ite.hasNext() ) {
                        Route r = (Route) ite.next();
                        if(r.getUri().equals(uri)){
                                ite.remove();
                        }
                }
        }

        public List<Route> getRoutes(){
                return this.routes;
        }

        public void setRoutes(List<Route> routes){
                this.routes=routes;
        }
}
