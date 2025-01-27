package com.team3.devinit_back.global.aop;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Objects;

@Component
@Aspect
@Slf4j
public class LogAspect {

    // 프로젝트의 모든 컨트롤러(WebSocket 제외)를 대상으로 지정
    @Pointcut("within(com.team3.devinit_back.*.controller..*) && !execution(* com.team3.devinit_back.websocket.controller.*.*(..))")
    public void controller() {}

    // 프로젝트의 모든 ResponseEntity 대상 지정
    @Pointcut("execution(org.springframework.http.ResponseEntity *.*(..))")
    private void responseEntityMethods() {}

    @Before("controller()")
    public void beforeRequest(JoinPoint joinPoint) {
        log.info("========== 클라이언트 요청 ==========");

        //WebSocket 요청인지 확인
        if (RequestContextHolder.getRequestAttributes() == null) {
            log.info("WebSocket 요청으로 HttpServletRequest가 없습니다.");
            return;
        }

        // 요청한 경로 로깅
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String requestUrl = request.getRequestURL().toString();
        log.info("요청한 경로: " + requestUrl);

        // 컨트롤러 이름 로깅
        String controllerName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        log.info("컨트롤러 이름: " + controllerName);

        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);
        log.info("메서드 이름: " + method.getName());

        /// 컨트롤러의 메서드 파라미터 로깅
        Object[] args = joinPoint.getArgs();
        if (args.length <= 0) {
            log.info("메서드에 전달된 인자가 없습니다.");
        } else {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String[] parameterNames = methodSignature.getParameterNames();

            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg != null) {
                    // 파라미터의 선언된 이름 로깅
                    if (parameterNames != null && parameterNames.length > i) {
                        String parameterName = parameterNames[i];
                        log.info("파라미터의 선언된 이름: {}", parameterName);
                    }
                    // 파라미터의 타입 로깅
                    log.info("파라미터의 타입: {}",  arg.getClass().getSimpleName());
                    // 파라미터의 값 로깅
                    log.info("파라미터의 값: {}",  arg);
                } else {
                    log.info("파라미터의 값: null");
                }
            }
        }
        log.info("===========     End     ===========");
    }

    // 컨트롤러 메서드 실행 후 반환값 로깅
    @AfterReturning(pointcut = "responseEntityMethods()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {
        log.info("==========    서버응답    ==========");
        // 컨트롤러 이름 받아오기
        String controllerName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        log.info("컨트롤러 이름: " + controllerName);

        // 메서드 정보 받아오기
        Method method = getMethod(joinPoint);
        log.info("메서드 이름: " + method.getName());

        log.info("반환 타입: " + returnObj.getClass().getSimpleName());
        log.info("반환 값: " + returnObj);
        log.info("===========     End     ===========");
    }

    // JoinPoint로 메서드 정보 가져오기
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}