package com.demo.hlAdapter.security.interceptor;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MultiTenancyInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                    Object handler) throws Exception {
        String method = request.getMethod();
        log.info(":::::Method {}", method);
        Map attribute = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String localAdd = request.getLocalAddr();
        String remoteAdd = request.getRemoteAddr();

        String authorization = request.getHeader("authorization");
        log.info(":::::localAdd {}, remoteAdd {}", localAdd, remoteAdd);
        log.info("::::authorization {}", authorization);
        return true;
    }
}
