package com.them.orderrelay.framework.filter;


import com.them.orderrelay.framework.base.dto.ResCode;
import com.them.orderrelay.framework.base.dto.ResDto;
import com.them.orderrelay.framework.config.async.SpringAsyncConfig;
import com.them.orderrelay.framework.util.Global;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class AuthAccessFilter implements Filter {

    @Autowired
    private SpringAsyncConfig asyncConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(!asyncConfig.isPossibleTask())
        {
            this.invalidAccess(response);
        }
        chain.doFilter(request, response);
    }

    private void invalidAccess(ServletResponse response) throws IOException {
        HashMap<String, String> map = new HashMap<>();
        String msg = Global.getMessageInfo().getMessage("sm0001");
        ResDto res = new ResDto(ResCode.fail.toString(), msg);
        HttpServletResponse httpResp = (HttpServletResponse) response;
        httpResp.reset();
        httpResp.setHeader("Content-Type", "application/json;charset=UTF-8");
        httpResp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter out = httpResp.getWriter();
        out.print(Global.getDataInfo().convertToString(res));
        out.flush();
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
