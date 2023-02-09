package hello.springcoremvc.basic.request;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Enumeration;

@WebServlet(
        name = "requestParamServlet",
        urlPatterns = "/request-param"
)
public class RequestParamServlet extends HttpServlet {
    @Override
    protected void service(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        System.out.println("전체 파라미터 조회 - START");

        /*
        Enumeration<String> parameterNames = req.getParameterNames();
        while(parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            System.out.println(paramName + ": " + req.getParameter(paramName));
        }
        */
        req.getParameterNames().asIterator()
                .forEachRemaining(paramName -> System.out.println(paramName + ": " + req.getParameter(paramName)));

        System.out.println("전체 파라미터 조회 - END");
        System.out.println();

        System.out.println("단일 파라미터 조회 - START");

        String userName = req.getParameter("username");
        System.out.println("req.getParameter(\"username\") = " + userName);
        
        String age = req.getParameter("age");
        System.out.println("req.getParameter(\"age\") = " + age);

        System.out.println("단일 파라미터 조회 - END");
        System.out.println();

        System.out.println("이름이 같은 복수 파라미터 조회 - START");

        String[] userNames = req.getParameterValues("username");
        for (String name : userNames) {
            System.out.println("name = " + name);
        }

        System.out.println("이름이 같은 복수 파라미터 조회 - END");

        resp.getWriter().write("ok");
    }
}
