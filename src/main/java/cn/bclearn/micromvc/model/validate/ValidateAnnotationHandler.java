package cn.bclearn.micromvc.model.validate;

import cn.bclearn.micromvc.controller.Route;
import cn.bclearn.micromvc.model.annotation.*;
import cn.bclearn.micromvc.model.annotation.Number;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateAnnotationHandler {
    ErrorMessage errorMessage=new ErrorMessage();

    public ErrorMessage validate(Route route){
        Object[] args=route.getArgs();
        for(Object o:args){
            validateModel(o);
        }
        Method method=route.getMethod();
        return errorMessage;
    }

    private void validateModel(Object model) {
        if(model.getClass().isAnnotationPresent(Validation.class)){
            Field[] fields=model.getClass().getDeclaredFields();
            for(Field field:fields){
                String data= null;
                try {
                    data = (String) field.get(model);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if(field.isAnnotationPresent(Max.class)){
                    Annotation annotation=field.getAnnotation(Max.class);
                    String message=((Max)annotation).msg();
                    int max=Integer.parseInt(((Max) annotation).value());
                    if(data.length()>max)
                        errorMessage.put(field.getName(),message);
                }
                if(field.isAnnotationPresent(Min.class)){
                    Annotation annotation=field.getAnnotation(Min.class);
                    String message=((Min)annotation).msg();
                    int min=Integer.parseInt(((Min) annotation).value());
                    if(data.length()<min)
                        errorMessage.put(field.getName(),message);
                }
                if(field.isAnnotationPresent(NotNull.class)){
                    Annotation annotation=field.getAnnotation(NotNull.class);
                    String message=((NotNull)annotation).msg();

                    if(data==null||data.equals(""))
                        errorMessage.put(field.getName(),message);
                }
                if(field.isAnnotationPresent(Number.class)){
                    Annotation annotation=field.getAnnotation(Number.class);
                    String message=((Number)annotation).msg();
                    Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
                    Matcher isNum = pattern.matcher(data);
                    if(!isNum.matches()){
                        errorMessage.put(field.getName(),message);
                    }
                }
                if(field.isAnnotationPresent(Reg.class)){
                    Annotation annotation=field.getAnnotation(Reg.class);
                    String message=((Reg)annotation).msg();
                    String reg=((Reg)annotation).value();
                    Pattern pattern = Pattern.compile(reg);
                    Matcher isNum = pattern.matcher(data);
                    if(!isNum.matches()){
                        errorMessage.put(field.getName(),message);
                    }
                }
            }
        }
    }
}
