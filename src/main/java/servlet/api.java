package servlet;

import util.CallApi;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class api extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tym=req.getParameter("tym");
        tym=tym.trim();
        CallApi callApi=new CallApi();
        String result=null;
        try {
            result=callApi.test(tym);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String tym=req.getParameter("tym");
        tym=tym.trim();
        CallApi callApi=new CallApi();
        String result=null;
        try {
            result=callApi.test(tym);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
        resp.getWriter().write(result);
    }
}
