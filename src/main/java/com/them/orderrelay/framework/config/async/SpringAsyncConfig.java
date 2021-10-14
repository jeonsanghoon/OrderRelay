package com.them.orderrelay.framework.config.async;

import com.them.orderrelay.framework.util.Global;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@Slf4j
public class SpringAsyncConfig extends AsyncConfigurerSupport {

    private final ThreadPoolTaskExecutor executor1 ;

    public SpringAsyncConfig(ThreadPoolTaskExecutor executor1) {
        this.executor1 = executor1;
    }

    public static class ConfigSettingData{
        //기본 Thread 수
        public static int TASK_CORE_POOL_SIZE = 3;
        //최대 Thread 수
        public static int TASK_MAX_POOL_SIZE = 30;
        //QUEUE 수
        public static int TASK_QUEUE_CAPACITY = 500;
    }

    @Override
    public Executor getAsyncExecutor() {

        executor1.setCorePoolSize(ConfigSettingData.TASK_CORE_POOL_SIZE);
        executor1.setMaxPoolSize(ConfigSettingData.TASK_MAX_POOL_SIZE);
        executor1.setQueueCapacity(ConfigSettingData.TASK_QUEUE_CAPACITY);
        executor1.setThreadNamePrefix("Executor-");
        executor1.initialize();
        return executor1;
    }

    /*
     * task 생성전에 pool이 찼는지를 체크
     */
    public boolean isPossibleTask() {
        boolean result = true;

        Global.getLogInfo().debug(log,"활성 Task 수", Integer.toString(executor1.getActiveCount()));

        if(executor1.getActiveCount() >= (ConfigSettingData.TASK_MAX_POOL_SIZE + ConfigSettingData.TASK_QUEUE_CAPACITY)) {
            result = false;
        }
        return result;
    }
}
