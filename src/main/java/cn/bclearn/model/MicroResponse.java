package cn.bclearn.model;

import javax.servlet.http.HttpServletResponse;

public class MicroResponse {
    private HttpServletResponse response;
    public MicroResponse(HttpServletResponse response){
        this.response=response;
    }
}
