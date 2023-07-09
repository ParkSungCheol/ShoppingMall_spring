package com.example.shoppingmall.config;


import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	@Bean(name = "mailExecutor")
	// ThreadPool을 만들어놓고 추후 이메일 전송 시 Async 어노테이션을 이용해 비동기처리
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(4);
		executor.setThreadNamePrefix("email-thread");
		executor.initialize();
		return executor;
	}
	
	@Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) ->
          logger.error("Exception handler for async method '" + method.toGenericString()
            + "' threw unexpected exception itself", ex);
    }
}
