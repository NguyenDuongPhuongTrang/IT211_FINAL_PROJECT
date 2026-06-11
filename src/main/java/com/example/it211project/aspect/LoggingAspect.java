package com.example.it211project.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around(
            "execution(* com.example.it211project.service.impl.*.*(..))"
    )
    public Object logExecutionTime(
            ProceedingJoinPoint joinPoint
    ) throws Throwable {

        long startTime = System.currentTimeMillis();

        String className =
                joinPoint.getTarget()
                        .getClass()
                        .getSimpleName();

        String methodName =
                joinPoint.getSignature()
                        .getName();

        log.info(
                "START -> {}.{}",
                className,
                methodName
        );

        Object result = joinPoint.proceed();

        long executionTime =
                System.currentTimeMillis()
                        - startTime;

        log.info(
                "END -> {}.{} | {} ms",
                className,
                methodName,
                executionTime
        );

        return result;
    }
}