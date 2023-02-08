package hello.springcoremvc.basic;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
    @Override
    protected void service(
            HttpServletRequest req, HttpServletResponse resp
    ) throws ServletException, IOException {
        super.service(req, resp);
        System.out.println("HelloServlet.service");
        System.out.println("req = " + req);
        System.out.println("resp = " + resp);
    }

    @Override
    protected void doGet(
            HttpServletRequest req, HttpServletResponse resp
    ) throws IOException {
        // 파라미터 획득
        String userName = req.getParameter("username");
        System.out.println("userName = " + userName);

        // Response Header 설정
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");

        // Response
        resp.getWriter().write("hello " + userName);
    }
}
