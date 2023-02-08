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


## HttpServletRequest - 기본 사용법


## HTTP 요청 데이터 - 개요


## HTTP 요청 데이터 - GET 쿼리 파라미터


## HTTP 요청 데이터 - POST HTML Form


## HTTP 요청 데이터 - API 메시지 바디 - 단순 텍스트


## HTTP 요청 데이터 - API 메시지 바디 - JSON


## HttpServletResponse - 기본 사용법


## HTTP 응답 데이터 - 단순 텍스트, HTML


## HTTP 응답 데이터 - API JSON


## 정리

