package cn.bclearn.controller.config;

import cn.bclearn.controller.RouteManager;
import cn.bclearn.controller.annotation.Controller;
import cn.bclearn.controller.annotation.Route;
import cn.bclearn.util.ClasspathPackageScanner;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class DefaultBootConfig extends BootConfig {

    public void otherConfig() {
        basePackage="";
    }

    public void routeConfig(RouteManager manager) {

    }
}
