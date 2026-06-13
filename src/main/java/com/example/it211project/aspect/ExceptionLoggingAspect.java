package com.example.it211project.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExceptionLoggingAspect {
    @AfterThrowing(pointcut = "execution(* com.example.it211project.service.impl.*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        log.error(
                "ERROR -> {}.{} | {}",
                joinPoint.getTarget()
                        .getClass()
                        .getSimpleName(),
                joinPoint.getSignature()
                        .getName(),
                ex.getMessage()
        );
    }
}
