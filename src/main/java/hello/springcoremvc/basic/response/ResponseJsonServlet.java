package hello.springcoremvc.basic.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springcoremvc.basic.HelloData;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(
        name = "responseJsonServlet",
        urlPatterns = "/response-json"
)
public class ResponseJsonServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        HelloData data = new HelloData();
        data.setUsername("새우");
        data.setAge(27);

        // { "username": "새우", "age": 27 }
        String result = objectMapper.writeValueAsString(data);
        resp.getWriter().write(result);
    }
}
