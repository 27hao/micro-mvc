package cn.bclearn.micromvc.view;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ViewHandler {
    void resolve( Object result, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
