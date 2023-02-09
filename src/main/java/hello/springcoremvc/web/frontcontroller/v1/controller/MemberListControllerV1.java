package hello.springcoremvc.web.frontcontroller.v1.controller;

import hello.springcoremvc.model.Member;
import hello.springcoremvc.model.MemberRepository;
import hello.springcoremvc.web.frontcontroller.v1.ControllerV1;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

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
