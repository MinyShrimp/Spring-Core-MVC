package hello.springcoremvc.basic.request;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Enumeration;

@WebServlet(
        name = "requestHeaderServlet",
        urlPatterns = "/request-header"
)
public class RequestHeaderServlet extends HttpServlet {
    @Override
    protected void service(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {
        printStartLine(req);
        printHeaders(req);
        printHeaderUtils(req);
        printEtc(req);

        resp.getWriter().write("ok");
    }

    private void printStartLine(HttpServletRequest req) {
        System.out.println("--- REQUEST LINE -- START ---");

        System.out.println("req.getMethod() = " + req.getMethod());
        System.out.println("req.getProtocol() = " + req.getProtocol());
        System.out.println("req.getScheme() = " + req.getScheme());
        System.out.println("req.getRequestURL() = " + req.getRequestURL());
        System.out.println("req.getRequestURI() = " + req.getRequestURI());
        System.out.println("req.getQueryString() = " + req.getQueryString());
        System.out.println("req.isSecure() = " + req.isSecure());

        System.out.println("--- REQUEST LINE -- END ---");
    }

    private void printHeaders(HttpServletRequest req) {
        System.out.println("--- HEADERS -- START ---");

        /*
        Enumeration<String> headerNames = req.getHeaderNames();
        while( headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ": " + req.getHeader(headerName));
        }
        */

        req.getHeaderNames().asIterator()
                .forEachRemaining(
                        headerName -> System.out.println(headerName + ": " + req.getHeader(headerName))
                );

        System.out.println("--- HEADERS -- END ---");
    }

    private void printHeaderUtils(HttpServletRequest req) {
        System.out.println("--- HEADER UTILS -- START ---");

        System.out.println("[Host 편의 조회]");
        System.out.println("request.getServerName() = " + req.getServerName()); //Host 헤더
        System.out.println("request.getServerPort() = " + req.getServerPort()); //Host 헤더
        System.out.println();

        System.out.println("[Accept-Language 편의 조회]");
        req.getLocales().asIterator()
                .forEachRemaining(locale -> System.out.println("locale = " + locale));
        System.out.println("request.getLocale() = " + req.getLocale());
        System.out.println();

        System.out.println("[cookie 편의 조회]");
        if (req.getCookies() != null) {
            for (Cookie cookie : req.getCookies()) {
                System.out.println(cookie.getName() + ": " + cookie.getValue());
            }
        }
        System.out.println();

        System.out.println("[Content 편의 조회]");
        System.out.println("request.getContentType() = " + req.getContentType());
        System.out.println("request.getContentLength() = " + req.getContentLength());
        System.out.println("request.getCharacterEncoding() = " + req.getCharacterEncoding());

        System.out.println("--- HEADER UTILS -- END ---");
    }

    private void printEtc(HttpServletRequest req) {
        System.out.println("--- ETC -- START ---");

        System.out.println("[Remote 정보]");
        System.out.println("request.getRemoteHost() = " + req.getRemoteHost()); //
        System.out.println("request.getRemoteAddr() = " + req.getRemoteAddr()); //
        System.out.println("request.getRemotePort() = " + req.getRemotePort()); //
        System.out.println();

        System.out.println("[Local 정보]");
        System.out.println("request.getLocalName() = " + req.getLocalName()); //
        System.out.println("request.getLocalAddr() = " + req.getLocalAddr()); //
        System.out.println("request.getLocalPort() = " + req.getLocalPort()); //

        System.out.println("--- ETC -- END ---");
    }
}
