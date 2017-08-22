package cn.bclearn.micromvc.model;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class MicroResponse extends HttpServletResponseWrapper {
    private HttpServletResponse response;
    public MicroResponse(HttpServletResponse response){
        super(response);
        this.response=response;
    }

    public void text(String data){
        response.setContentType("text/html charset=utf-8");
        try {
            PrintWriter writer=response.getWriter();
            writer.print(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
