package cn.bclearn.controller.config;

import cn.bclearn.controller.RouteManager;
import cn.bclearn.controller.annotation.Controller;
import cn.bclearn.controller.annotation.Route;
import cn.bclearn.util.ClasspathPackageScanner;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class DefaultBootConfig extends BootConfig {


    @Override
    public void routeConfig(RouteManager manager) {
//        default nothing
    }

    @Override
    public void otherConfig() {
        this.basePackage="";
    }
}
