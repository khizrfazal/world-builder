package com.my.worldbuilder.common.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
public class RequestLoggingAspect {

    @Around("execution(* com.my.worldbuilder..*Controller.*(..))")
    public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest req = attrs != null ? attrs.getRequest() : null;

        String method = req != null ? req.getMethod() : "UNKNOWN";
        String path = req != null ? req.getRequestURI() : "UNKNOWN";

        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - start;
        int status = attrs != null ? attrs.getResponse().getStatus() : -1;

        String outcome = status >= 200 && status < 300
                ? "SUCCESS"
                : "ERROR";

        log.info("{} {} {} ({} in {}ms)",
                method,
                path,
                outcome,
                status,
                duration
        );

        return result;
    }
}