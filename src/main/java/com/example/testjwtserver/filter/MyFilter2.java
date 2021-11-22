package com.example.testjwtserver.filter;

import javax.servlet.*;
import java.io.IOException;

public class MyFilter2 implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터2");

        // 이렇게하면 그냥 끝남 다음것 진행 안함
//        PrintWriter out = response.getWriter();
//        out.print("");

        // 끝나지 말고 Process 진행하려면 체인에다가 넘겨줘야함
        chain.doFilter(request, response);
    }
}
