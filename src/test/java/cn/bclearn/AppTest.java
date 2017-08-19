package cn.bclearn;

import cn.bclearn.controller.Route;
import cn.bclearn.controller.RouteManager;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() throws NoSuchMethodException {
        RouteManager manager=RouteManager.getInstance();
        manager.addRoute("/hello","addRoute",RouteManager.class);
        manager.addRoute("/test","addRoutes",RouteManager.class);
//        RouteManager.class.ge
        System.out.println(manager.getSize());
        for (Route route:manager.getRoutes()){
            System.out.println(route);
            System.out.println(route.getMethod().getName());
        }
        RouteManager rm=RouteManager.getInstance();
        rm.removeRoute("/hello");
        System.out.println(manager.getSize());
        for (Route rou:manager.getRoutes()){
            System.out.println(rou);
        }
    }
}
