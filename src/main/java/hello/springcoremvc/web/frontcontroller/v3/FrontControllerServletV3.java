package hello.springcoremvc.web.frontcontroller.v3;

import hello.springcoremvc.web.frontcontroller.ModelView;
import hello.springcoremvc.web.frontcontroller.MyView;
import hello.springcoremvc.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.springcoremvc.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.springcoremvc.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(
        name = "frontControllerServletV3",
        urlPatterns = "/front-controller/v3/*"
)
public class FrontControllerServletV3 extends HttpServlet {
    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {
        String requestURI = req.getRequestURI();

        ControllerV3 controller = controllerMap.get(requestURI);
        if(controller == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ;
        }

        Map<String, String> paramMap = createParamMap(req);
        ModelView mv = controller.process(paramMap);

        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);
        view.render(mv.getModel(), req, resp);
    }

    /**
     * HttpServletRequest에서 파라미터 정보를 꺼내서 Map으로 반환한다.
     * 그리고 해당 Map을 컨트롤러에 전달하면서 호출한다.
     */
    private Map<String, String> createParamMap(HttpServletRequest req) {
        Map<String, String> paramMap = new HashMap<>();

        req.getParameterNames().asIterator()
                .forEachRemaining(
                        paramName -> paramMap.put(paramName, req.getParameter(paramName))
                );

        return paramMap;
    }

    /**
     * 컨트롤러가 반환한 논리 뷰 이름을 실제 물리 뷰 경로로 변경한다.
     * 그리고 실제 물리 경로가 있는 MyView 객체를 반환한다.
     * @param viewName
     * @return "/WEB-INF/views/" + viewName + ".jsp"
     */
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
