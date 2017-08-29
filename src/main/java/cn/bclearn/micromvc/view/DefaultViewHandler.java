package cn.bclearn.micromvc.view;

import cn.bclearn.micromvc.controller.config.BootConfig;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DefaultViewHandler implements ViewHandler {
    private String prefix="";
    private String suffix=".jsp";
    public DefaultViewHandler(BootConfig bootConfig){
        prefix=bootConfig.getViewPrefix();
        suffix=bootConfig.getViewSuffix();
    }
    @Override
    public void resolve(Object result, HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        if(result.getClass().getName().equals(String.class.getName())){
            request.getRequestDispatcher(prefix+result+suffix).forward(request,response);
        }else{
            response.setContentType("application/json;charset=utf-8");
            Gson gson=new Gson();
            PrintWriter writer=response.getWriter();
            writer.print(gson.toJson(result));
        }
    }

}
