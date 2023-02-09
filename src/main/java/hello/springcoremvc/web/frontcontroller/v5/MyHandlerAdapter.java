package hello.springcoremvc.web.frontcontroller.v5;

import hello.springcoremvc.web.frontcontroller.ModelView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface MyHandlerAdapter {
    /**
     * 어댑터가 해당 컨트롤러를 처리할 수 있는지 판단하는 메소드
     *
     * @param handler
     * @return 어댑터가 해당 컨트롤러를 처리할 수 있는지 여부
     */
    boolean supports(Object handler);

    /**
     * 어댑터는 실제 컨트롤러를 호출하고, 그 결과로 ModelView 를 반환한다.
     * 실제 컨트롤러가 ModelView를 반환하지 못하면, 어댑터가 ModelView를 직접 생성해서라도 반환해야 한다.
     * 이전에는 프론트 컨트롤러가 실제 컨트롤러를 호출했지만, 이제는 이 어댑터를 통해서 실제 컨트롤러가 호출된다.
     *
     * @return ModelView
     */
    ModelView handle(
            HttpServletRequest req,
            HttpServletResponse resp,
            Object handler
    ) throws ServletException, IOException;
}
