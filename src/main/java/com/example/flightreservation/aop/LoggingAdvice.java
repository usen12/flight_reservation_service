package com.example.flightreservation.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAdvice {

    private final ObjectMapper objectMapper;

    @Pointcut("execution(* com.example.flightreservation.service.impl.*Impl.create*(..))")
    private void createPointCut() {}

    @Around("createPointCut()")
    public Object createLogging(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().toString();
        String className = pjp.getTarget().getClass().toString();
        Object[] args = pjp.getArgs();

        log.info("method invoked {} : {}. Arguments: {}", className, methodName, objectMapper.writeValueAsString(args));

        Object result = pjp.proceed();

        log.info("{} : {}. Response: {}", className, methodName, objectMapper.writeValueAsString(result));
        return result;
    }
}
