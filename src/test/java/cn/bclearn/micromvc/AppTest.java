package cn.bclearn.micromvc;

import cn.bclearn.micromvc.model.annotation.RequestParam;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
    public int test(@RequestParam("/test") String test, int i, String test2, int j, AppTest t){
        return 1;
    }
    public void testApp() throws NoSuchMethodException, IllegalAccessException, InstantiationException, NoSuchFieldException {
//        RouteManager manager=RouteManager.getInstance();
//        manager.addRoute("/test","test",AppTest.class);
//                //获取方法的所有的参数的原始类型。
//        Annotation[][] annotations= manager.getRoutes().get(0).getMethod().getParameterAnnotations();
//        System.out.println(annotations);
        List<String> list=new ArrayList<>();
        try {
            Field field=String.class.getDeclaredField("value");
            System.out.println(list.getClass().getName());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }
}
