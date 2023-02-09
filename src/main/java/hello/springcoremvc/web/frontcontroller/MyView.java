package hello.springcoremvc.web.frontcontroller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class MyView {
    private String viewPath;

    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }

    /**
     * V2 용 Render
     */
    public void render(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
        dispatcher.forward(req, resp);
    }

    /**
     * V3 용 Render
     */
    public void render(
            Map<String, Object> model,
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws ServletException, IOException {
        modelToRequestAttribute(model, req);
        RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
        dispatcher.forward(req, resp);
    }

    /**
     * 모델의 데이터를 꺼내서 req.setAttribute()로 담아둔다.
     */
    private void modelToRequestAttribute(
            Map<String, Object> model,
            HttpServletRequest req
    ) {
        // model.forEach(req::setAttribute);
        model.forEach((key, value) -> req.setAttribute(key, value));
    }
}
