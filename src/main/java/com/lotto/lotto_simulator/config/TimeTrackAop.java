package com.lotto.lotto_simulator.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Slf4j
public class TimeTrackAop {

    // 조인포인트를 어노테이션으로 설정
    @Pointcut("@annotation(com.lotto.lotto_simulator.config.ExecutionTime)")
    private void timer(){};

    // 메서드 실행 전,후로 시간을 공유해야 하기 때문
    @Around("timer()")
    public void AssumeExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();

        // 시간 측정을 하는 메소드 이름을 알기 위해
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        stopWatch.start(methodName);
        joinPoint.proceed(); // 조인포인트의 메서드 실행
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());
    }


}
