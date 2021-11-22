package com.example.testjwtserver.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터3");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 토큰 : ID,PW정상적으로 들어와서 로그인이 완료되면 토큰을 만들어 주고 그걸 응답해 준다.
        // 요청할 떄 마다 header 에 Authorization에 value값으로 토큰을 가지고 오겠죠?
        // 그때 토근이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지만 검증만 하면됨. (RSA, HS256)

        if (req.getMethod().equals("POST")) {
            System.out.println("POST 요청됨");
//            String headerAuth = req.getHeader("Authorization");
            // TODO. 임시 에러나서
            String headerAuth = String.valueOf(req.getHeader("Authorization"));

            System.out.println("headerAuth : " + headerAuth);

            if (headerAuth.equals("cos")) {
                System.out.println("인증 OK");
                chain.doFilter(req, res);
            } else {
                System.out.println("인증 NO");
                PrintWriter out = res.getWriter();
                out.println("인증안됨");
            }
        }
    }
}
