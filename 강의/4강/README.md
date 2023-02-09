# MVC 프레임워크 만들기
## 프론트 컨트롤러 패턴 소개
### 프론트 컨트롤러 도입 전
![img.png](img.png)

### 프론트 컨트롤러 도입 후
![img_1.png](img_1.png)

### Front Controller 패턴 특징
* 프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음
* 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출
* 입구를 하나로!
* 공통 처리 가능
* 프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 됨

### 스프링 웹 MVC와 프론트 컨트롤러
* **스프링 웹 MVC의 핵심**도 바로 FrontController
* 스프링 웹 MVC의 **DispatcherServlet**이 FrontContoller 패턴으로 구현되어 있음.

## 프론트 컨트롤러 도입 - v1
프론트 컨트롤러를 단계적으로 도입해보자.
이번 목표는 기존 코드를 최대한 유지하면서, 프론트 컨트롤러를 도입하는 것이다.
먼저 구조를 맞추어두고 점진적으로 리펙터링 해보자.

### V1 구조
![img_2.png](img_2.png)

### Controller V1
```java
public interface ControllerV1 {
    void process(
            HttpServletRequest req, 
            HttpServletResponse resp
    ) throws ServletException, IOException;
}
```
서블릿과 비슷한 모양의 컨트롤러 인터페이스를 도입한다.
각 컨트롤러들은 이 인터페이스를 구현하면 된다.
프론트 컨트롤러는 이 인터페이스를 호출해서 구현과 관계없이 로직의 일관성을 가져갈 수 있다.

이제 이 인터페이스를 구현한 컨트롤러를 만들어보자.
지금 단계에서는 기존 로직을 최대한 유지하는게 핵심이다.

### MemberFormController V1 - 회원 등록 컨트롤러
```java
/**
 * 회원 등록 컨트롤러 V1
 */
public class MemberFormControllerV1 implements ControllerV1 {
    @Override
    public void process(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {
        String viewPath = "/WEB-INF/views/new-form.jsp";
        RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
        dispatcher.forward(req, resp);
    }
}
```

### MemberSaveController V1 - 회원 저장 컨트롤러
```java
/**
 * 회원 저장 컨트롤러 V1
 */
public class MemberSaveControllerV1 implements ControllerV1 {
    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public void process(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {
        String username = req.getParameter("username");
        int age = Integer.parseInt(req.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        req.setAttribute("member", member);

        String viewPath = "/WEB-INF/views/save-result.jsp";
        RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
        dispatcher.forward(req, resp);
    }
}
```

### MemberListController V1 - 회원 목록 컨트롤러
```java
/**
 * 회원 목록 컨트롤러 V1
 */
public class MemberListControllerV1 implements ControllerV1 {
    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public void process(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {
        List<Member> members = memberRepository.findAll();
        req.setAttribute("members", members);

        String viewPath = "/WEB-INF/views/members.jsp";
        RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
        dispatcher.forward(req, resp);
    }
}
```

### FrontControllerServlet V1 - 프론트 컨트롤러
```java
@WebServlet(
        name = "frontControllerServletV1",
        urlPatterns = "/front-controller/v1/*"
)
public class FrontControllerServletV1 extends HttpServlet {
    private Map<String, ControllerV1> controllerMap = new HashMap<>();

    public FrontControllerServletV1() {
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());
    }

    @Override
    protected void service(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {
        System.out.println("FrontControllerServletV1.service");
        String requestURI = req.getRequestURI();

        ControllerV1 cont = controllerMap.get(requestURI);
        if(cont == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ;
        }

        cont.process(req, resp);
    }
}
```

### 프론트 컨트롤러 분석
#### urlPatterns
* `urlPatterns = "/front-controller/v1/*"`
  * `/front-controller/v1`를 포함한 하위 모든 요청은 이 서블릿에서 받아들인다.

#### controllerMap
* key: 매핑 URL
* value: 호출될 컨트롤러

#### service()
먼저 `requestURI`를 조회해서 실제 호출할 컨트롤러를 `controllerMap`에서 찾는다.
만약 없다면 404(SC_NOT_FOUND) 상태 코드를 반환한다.
컨트롤러를 찾고 `controller.process(req, resp);`을 호출해서 해당 컨트롤러를 실행한다.

> 참고
> * `req.requestURI()`: URL을 String으로 반환한다.
> * `req.requestURL()`: URL을 StringBuffer로 반환한다.

#### JSP
JSP는 이전 MVC에서 사용했던 것을 그대로 사용한다.

## View 분리 - v2

## Model 추가 - v3

## 단순하고 실용적인 컨트롤러 - v4

## 유연한 컨트롤러1 - v5

## 유연한 컨트롤러2 - v5

## 정리
