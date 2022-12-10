package com.lotto.lotto_simulator.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {
//    @Configuration : Spring 설정 관련 Class로 @Component
//    등록되어 Scanning 될 수 있다.
//    @EnableAsync : Spring method에서 비동기 기능을 사용가능하게 활성화 한다.
//    CorePoolSize : 기본 실행 대기하는 Thread의 수**
//    MaxPoolSize : 동시 동작하는 최대 Thread의 수
//    QueueCapacity : MaxPoolSize 초과 요청에서 Thread 생성 요청시,
//    해당 요청을 Queue에 저장하는데 이때 최대 수용 가능한 Queue의 수,
//    Queue에 저장되어있다가 Thread에 자리가 생기면 하나씩 빠져나가 동작
//    ThreadNamePrefix : 생성되는 Thread 접두사 지정
//    크기가 제한된 큐에 작업이 가득 차면 집중 대응 정책 saturation policy 가 동작한다.
//    1.집중 대응 정책은 ThreadPoolExecutor 에 setRejectedExecutionHandler 메소드를 사용해 설정할 수 있다.
//    중단 정책(abort)
//    기본적으로 사용하는 집중 대응 정책이며 execute 메소드에서 RejectedExecutionException을 던진다.
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-tread-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.initialize();
        return executor;
    }

}
