package cn.bclearn.model.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RequestParamAnnotationHandler {
    private Method method=null;
    public RequestParamAnnotationHandler(Method method){
        this.method=method;
    }
    public Map<Integer,String> getRequestParamValueAndIndex(){
        Map<Integer,String> result=new HashMap<Integer,String>();
        Annotation[][] parameterAnnotations=method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return null;
        }
        String[] parameterNames = new String[parameterAnnotations.length];
        int i = 0;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof RequestParam) {
                    RequestParam param = (RequestParam) annotation;
                    result.put(i,param.value());
                }
            }
            i++;
        }
        return result;
    }
}
