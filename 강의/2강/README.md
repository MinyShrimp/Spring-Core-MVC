# 서블릿
## 프로젝트 생성
![img.png](img.png)
* https://start.spring.io/
* Packaging은 Jar가 아닌 War로 선택해 주어야 한다.
  * 이는 JSP를 실행하기 위해서이다.

## Hello Servlet
스프링 부트 환경에서 서블릿 등록하고 사용해보자.
> 참고<br>
> 서블릿은 톰캣 같은 웹 애플리케이션 서버를 직접 설치하고, 
> 그 위에 서블릿 코드를 클래스 파일로 빌드해서 올린 다음, 톰캣 서버를 실행하면 된다. 하지만 이 과정은 너무 번거롭다.<br>
> 스프링 부트는 톰캣 서버를 내장하고 있으므로, 톰캣 서버 설치 없이 편리하게 서블릿 코드를 실행할 수 있다.

### 스프링 부트 서블릿 환경 구성
스프링 부트는 서블릿을 직접 등록해서 사용할 수 있도록, `@ServletComponentScan`을 지원한다. 다음과 같이 추가하자. 

`hello.springcoremvc.ServletApplication`
```java
@ServletComponentScan // 서블릿 자동 등록
@SpringBootApplication
public class ServletApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServletApplication.class, args);
    }
}
```

### 서블릿 등록하기
처음으로 실제로 동작하는 서블릿 코드를 등록해보자.

`hello.springcoremvc.ServletApplication`
```java
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {
  @Override
  protected void service(
          HttpServletRequest req, HttpServletResponse resp
  ) {
    System.out.println("HelloServlet.service");
    System.out.println("req = " + req);
    System.out.println("resp = " + resp);

    // 파라미터 획득
    String userName = req.getParameter("username");
    System.out.println("userName = " + userName);

    // Response Header 설정
    resp.setContentType("txt/plain");
    resp.setCharacterEncoding("utf-8");

    // Response
    resp.getWriter().write("hello " + userName);
  }
}
```
* `@WebServlet` 서블릿 애노테이션
  * name: 서블릿 이름
  * urlPatterns: URL 매핑
  * HTTP 요청을 통해 매핑된 URL이 호출되면 서블릿 컨테이너는 다음 메서드를 실행한다.
    * `protected void service(HttpServletRequest req, HttpServletResponse resp)`
* 웹 브라우저 실행
  * http://localhost:8080/hello?username=world
  * 결과: Hello world
* 콘솔 실행 결과
```
HelloServlet.service
req = org.apache.catalina.connector.RequestFacade@447f0797
resp = org.apache.catalina.connector.ResponseFacade@3d5e59f1
userName = world
```

### HTTP 요청 메시지 로그로 확인하기
`application.properties`
```properties
logging.level.org.apache.coyote.http11=debug
```
> **참고**<br>
> 운영서버에 이렇게 모든 요청 정보를 다 남기면 성능저하가 발생할 수 있다.
> 개발 단계에서만 사용하자.

### 서블릿 컨테이너 동작 방식 설명
**내장 톰캣 서버 생성**
![img_1.png](img_1.png)

**HTTP 요청, HTTP 응답 메시지**
![img_2.png](img_2.png)

**웹 애플리케이션 서버의 요청 응답 구조**
![img_3.png](img_3.png)

> 참고<br>
> HTTP 응답에서 Content-Length는 웹 애플리케이션 서버가 자동으로 생성해준다.

### Welcome 페이지 추가
![img_4.png](img_4.png)
![img_5.png](img_5.png)

## HttpServletRequest - 개요
### HttpServletRequest의 역할
HTTP 요청 메시지를 개발자가 직접 파싱해서 사용해도 되지만, 매우 불편할 것이다.
서블릿은 개발자가 HTTP 요청 메시지를 편리하게 사용할 수 있도록 개발자 대신에 HTTP 요청 메시지를 파싱한다.
그리고 그 결과를 `HttpServletRequest` 객체에 담아서 제공한다.

**HTTP 요청 메시지**
```
POST /save HTTP/1.1
Host: localhost:8080
Content-Type: application/x-www-form-urlencoded

username=kim&age=20
```
* START LINE
  * HTTP Method
  * URL
  * Query String
  * Schema, Protocol
* HEADER
  * 헤더 조회
* BODY
  * form 파라미터 형식 조회
  * message body 데이터 직접 조회

### 여러가지 부가 기능
* 임시 저장소 기능
  * 해당 HTTP 요청이 시작부터 끝날 때까지 유지되는 임시 저장소 기능
    * 저장: `req.setAttribute(key, value);`
    * 조회: `req.getAttribute(key);`
* 세션 관리 기능
  * `req.getSession(create: true);`

> **중요**<br>
> HttpServletRequest, HttpServletResponse를 사용할 때 가장 중요한 점은 
> 이 객체들이 HTTP 메시지, HTTP 응답 메시지를 **편리하게 사용하도록 도와주는 객체**라는 점이다.
> 따라서 이 기능에 대해서 깊이있는 이해를 하려면 **HTTP 스펙이 제공하는 요청, 응답 메시지 자체를 이해**해야 한다.

## HttpServletRequest - 기본 사용법
`hello.springcoremvc.basic.request.RequestHeaderServlet`
```java
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
}
```

### Start Line 정보
```java
public class RequestHeaderServlet extends HttpServlet {
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
}
```
```
--- REQUEST LINE -- START ---
req.getMethod() = GET
req.getProtocol() = HTTP/1.1
req.getScheme() = http
req.getRequestURL() = http://localhost:8080/request-header
req.getRequestURI() = /request-header
req.getQueryString() = null
req.isSecure() = false
--- REQUEST LINE -- END ---
```

### Header 정보
```java
public class RequestHeaderServlet extends HttpServlet {
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
}
```
```
--- HEADERS -- START ---
host: localhost:8080
connection: keep-alive
cache-control: max-age=0
sec-ch-ua: "Not_A Brand";v="99", "Google Chrome";v="109", "Chromium";v="109"
sec-ch-ua-mobile: ?0
sec-ch-ua-platform: "macOS"
upgrade-insecure-requests: 1
user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36
accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
sec-fetch-site: same-origin
sec-fetch-mode: navigate
sec-fetch-user: ?1
sec-fetch-dest: document
referer: http://localhost:8080/basic.html
accept-encoding: gzip, deflate, br
accept-language: ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7
--- HEADERS -- END ---
```

### Header Utils 정보
```java
public class RequestHeaderServlet extends HttpServlet {
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
}
```
```
--- HEADER UTILS -- START ---
[Host 편의 조회]
request.getServerName() = localhost
request.getServerPort() = 8080

[Accept-Language 편의 조회]
locale = ko_KR
locale = ko
locale = en_US
locale = en
request.getLocale() = ko_KR

[cookie 편의 조회]

[Content 편의 조회]
request.getContentType() = null
request.getContentLength() = -1
request.getCharacterEncoding() = UTF-8
--- HEADER UTILS -- END ---
```

### Etc 정보
```java
public class RequestHeaderServlet extends HttpServlet {
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
```
```
--- ETC -- START ---
[Remote 정보]
request.getRemoteHost() = 0:0:0:0:0:0:0:1
request.getRemoteAddr() = 0:0:0:0:0:0:0:1
request.getRemotePort() = 58633

[Local 정보]
request.getLocalName() = localhost
request.getLocalAddr() = 0:0:0:0:0:0:0:1
request.getLocalPort() = 8080
--- ETC -- END ---
```

> 참고<br>
> 로컬에서 테스트하면 IPv6 정보가 나오는데, IPv4 정보를 보고 싶으면 다음 옵션을 VM options에 넣어주면 된다.<br>
> `-Djava.net.preferIPv4Stack=true`

## HTTP 요청 데이터 - 개요


## HTTP 요청 데이터 - GET 쿼리 파라미터


## HTTP 요청 데이터 - POST HTML Form


## HTTP 요청 데이터 - API 메시지 바디 - 단순 텍스트


## HTTP 요청 데이터 - API 메시지 바디 - JSON


## HttpServletResponse - 기본 사용법


## HTTP 응답 데이터 - 단순 텍스트, HTML


## HTTP 응답 데이터 - API JSON


## 정리

