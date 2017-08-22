package cn.bclearn.micromvc.controller;

import java.lang.reflect.Method;

public class Route {
    private String uri;
    private Method method;
    private Class cotroller;
    private Object[] args;

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public java.lang.reflect.Method getMethod() {
        return method;
    }

    public void setMethod(java.lang.reflect.Method method) {
        this.method = method;
    }

    public Class getCotroller() {
        return cotroller;
    }

    public void setCotroller(Class cotroller) {
        this.cotroller = cotroller;
    }

    @Override
    public boolean equals(Object obj) {
        Route route=(Route)obj;
        if( this.uri.equals(route.uri) &&
                this.method.getName().equals(route.method.getName()) &&
                this.cotroller.getName().equals(route.cotroller.getName())){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Route{" +
                "uri='" + uri + '\'' +
                ", Method=" + method +
                ", Cotroller=" + cotroller +
                '}';
    }
}
