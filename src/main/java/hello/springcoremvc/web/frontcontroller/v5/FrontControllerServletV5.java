package hello.springcoremvc.web.frontcontroller.v5;

import hello.springcoremvc.web.frontcontroller.ModelView;
import hello.springcoremvc.web.frontcontroller.MyView;
import hello.springcoremvc.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.springcoremvc.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.springcoremvc.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.springcoremvc.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.springcoremvc.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.springcoremvc.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.springcoremvc.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import hello.springcoremvc.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(
        name = "frontControllerServletV5",
        urlPatterns = "/front-controller/v5/*"
)
public class FrontControllerServletV5 extends HttpServlet {
    /**
     * Controller -> Handler
     * 이전에는 컨트롤러를 직접 매핑해서 사용했다.
     * 그런데 이제는 어댑터를 사용하기 때문에, 컨트롤러 뿐만 아니라 어댑터가 지원하기만 하면, 어떤 것이라도 URL에 매핑해서 사용할 수 있다.
     * 그래서 이름을 컨트롤러에서 더 넓은 범위의 핸들러로 변경했다.
     */
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdapter();
    }

    /**
     * Handler 매핑 초기화
     */
    private void initHandlerMappingMap() {
        // V3 URL 매핑
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        // V4 URL 매핑
        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    /**
     * Adapter 초기화
     */
    private void initHandlerAdapter() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    @Override
    protected void service(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {
        // Handler 매핑
        Object handler = getHandler(req);
        if(handler == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ;
        }

        // Handler를 처리할 수 있는 어댑터 조회
        MyHandlerAdapter adapter = getHandlerAdapter(handler);
        /**
         * Adapter 호출
         * 어댑터의 handle() 메서드를 통해 실제 어댑터가 호출된다.
         * 어댑터는 handler를 호출하고 그 결과를 어댑터에 맞추어 반환한다.
         * ControllerV3HandlerAdapter의 경우 어댑터의 모양과 컨트롤러의 모양이 유사해서 변환 로직이 단순하다.
         */
        ModelView mv = adapter.handle(req, resp, handler);

        MyView view = viewResolver(mv.getViewName());
        view.render(mv.getModel(), req, resp);
    }

    /**
     * Handler 매핑
     * 핸들러 매핑 정보인 handlerMappingMap에서 URL에 매핑된 핸들러 객체를 찾아서 반환한다.
     */
    private Object getHandler(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }

    /**
     * Handler를 처리할 수 있는 어댑터 조회
     * 핸들러를 처리할 수 있는 어댑터를 adapter.supports(handler)를 통해서 찾는다.
     * 핸들러가 ControllerV3 인터페이스를 구현했다면, ControllerV3HandlerAdapter 객체가 반환된다.
     */
    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for(MyHandlerAdapter adapter : handlerAdapters) {
            if(adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. handler = " + handler);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
