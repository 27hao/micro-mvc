package cn.bclearn;

import cn.bclearn.controller.Route;
import cn.bclearn.controller.RouteManager;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.lang.reflect.Type;
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
    public int test(String test,int i,String test2,int j,AppTest t){
        return 1;
    }
    public void testApp() throws NoSuchMethodException {
        RouteManager manager=RouteManager.getInstance();
        manager.addRoute("/test","test",AppTest.class);

        Class<?>pType  = manager.getRoutes().get(0).getMethod().getReturnType();
        //获取方法的所有的参数的原始类型。
        Class[] gpType = manager.getRoutes().get(0).getMethod().getParameterTypes();
        for(Class t:gpType){
            System.out.println(t.getTypeName());
            try {
                t.
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
