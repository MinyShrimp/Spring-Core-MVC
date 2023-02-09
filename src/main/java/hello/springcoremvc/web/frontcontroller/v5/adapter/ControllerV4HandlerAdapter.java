package hello.springcoremvc.web.frontcontroller.v5.adapter;

import hello.springcoremvc.web.frontcontroller.ModelView;
import hello.springcoremvc.web.frontcontroller.v4.ControllerV4;
import hello.springcoremvc.web.frontcontroller.v5.MyHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV4HandlerAdapter implements MyHandlerAdapter {
    /**
     * handler가 ControllerV4 인 경우에만 처리하는 어댑터이다.
     */
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof ControllerV4);
    }

    @Override
    public ModelView handle(
            HttpServletRequest req,
            HttpServletResponse resp,
            Object handler
    ) throws ServletException, IOException {
        // handler를 ControllerV4로 캐스팅
        ControllerV4 controller = (ControllerV4) handler;

        // paramMap, model 생성
        Map<String, String> paramMap = createParamMap(req);
        Map<String, Object> model = new HashMap<>();

        // 컨트롤러 호출, viewName 반환
        String viewName = controller.process(paramMap, model);

        /**
         * 어댑터 변환
         * 반환받은 viewName으로 ModelView를 생성해서 반환.
         */
        ModelView mv = new ModelView(viewName);
        mv.setModel(model);
        return mv;
    }

    private Map<String, String> createParamMap(HttpServletRequest req) {
        Map<String, String> paramMap = new HashMap<>();
        req.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, req.getParameter(paramName)));
        return paramMap;
    }
}
