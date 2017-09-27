package cn.bclearn.micromvc.model.validate;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class ErrorMessage extends LinkedHashMap<String,String>{

    public ErrorMessage(){
        super();
    }
    public ErrorMessage(int cap){
        super(cap);
    }

    public String getErrorMessage(int i){
        Iterator iterator=this.values().iterator();
        String message="";
        int j=0;
        while(iterator.hasNext()){
            if(i==j) {
                message = (String) iterator.next();
                return message;
            }
            iterator.next();
            j++;
        }
        return message;
    }
    public String getErrorMessage(String field){
        if(this.containsKey(field)){
            return this.get(field);
        }
        return "";
    }
}
