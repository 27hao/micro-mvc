package cn.bclearn.micromvc.controller.config;

import cn.bclearn.micromvc.controller.RouteManager;

public interface Config {
    void otherConfig();
    void routeConfig(RouteManager manager);
}
