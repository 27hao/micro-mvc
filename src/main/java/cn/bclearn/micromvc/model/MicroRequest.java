package cn.bclearn.micromvc.model;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Map;

public class MicroRequest extends HttpServletRequestWrapper{
    private HttpServletRequest request;
    public MicroRequest(HttpServletRequest request){
        super(request);
        this.request=request;
    }

    @SuppressWarnings("unchecked")
    public <T> T attr(String param){
        return (T)request.getAttribute(param);
    }

    public void attr(String param,Object value){
        request.setAttribute(param,value);
    }

    public String param(String name){
        return request.getParameter(name);
    }

    public Map<String,String[]> getAllParam(){
        return request.getParameterMap();
    }

}
