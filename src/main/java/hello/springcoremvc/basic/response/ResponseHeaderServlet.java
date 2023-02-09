package hello.springcoremvc.basic.response;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(
        name = "responseHeaderServlet",
        urlPatterns = "/response-header"
)
public class ResponseHeaderServlet extends HttpServlet {
    @Override
    protected void service(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        // [ Status Line ]
        resp.setStatus(HttpServletResponse.SC_OK); // 200

        // [ Response Header ]
        resp.setHeader("Content-Type", "text/plain;charset=utf-8");
        // Cache 완전 무효화
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        // Custom Header
        resp.setHeader("My-Header", "hello");

        // [ Header 편의 메소드 ]
        content(resp);
        cookie(resp);
        redirect(resp);

        // [ Message Body ]
        PrintWriter writer = resp.getWriter();
        writer.println("ok");
    }

    /**
     * Content 편의 메소드
     * Content-Type: text/plain;utf-8
     * Content-Length: 2
     */
    private void content(HttpServletResponse resp) {
        // resp.setHeader("Content-Type", "text/plain;utf-8");
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("utf-8");
        resp.setContentLength(2); // 생략시 자동 생성
    }

    /**
     * Cookie 편의 메서드
     * Set-Cookie: myCookie=good; Max-Age=600;
     */
    private void cookie(HttpServletResponse resp) {
        // resp.setHeader("Set-Cookie", "myCookie=good; Max-Age=600");
        Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600);
        resp.addCookie(cookie);
    }

    /**
     * Redirect 편의 메서드
     * Status Code 302
     * Location: /basic/hello-form.html
     */
    private void redirect(HttpServletResponse resp) throws IOException {
        // resp.setStatus(HttpServletResponse.SC_FOUND); // 302
        // resp.setHeader("Location", "/basic/hello-form.html");
        resp.sendRedirect("/basic/hello-form.html");
    }
}
