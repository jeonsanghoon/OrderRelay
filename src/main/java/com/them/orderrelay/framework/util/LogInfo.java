package com.them.orderrelay.framework.util;

import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class LogInfo<T> {
    @Async
    public void info(Logger log, String key, T value){
        log.info(String.format("======================================================================"));
        log.info(String.format( "Info >> %s : %s ", key, Global.getDataInfo().convertToString(value)));
        log.info(String.format("======================================================================"));
    }

    @Async
    public void error(Logger log, String key, T value){
        log.error(String.format("======================================================================"));
        log.error(String.format( "Error >> %s : %s ", key, Global.getDataInfo().convertToString(value)));
        log.error(String.format("======================================================================"));
    }

    @Async
    public void info(Logger log, String key, String value){

        log.info(String.format("======================================================================"));
        log.info(String.format( "Info >> %s : %s ", key, value));
        log.info(String.format("======================================================================"));
    }

    @Async
    public void warn(Logger log, String key, String value){
        log.error(String.format("======================================================================"));
        log.error(String.format( "Warn >> %s : %s ", key, value));
        log.error(String.format("======================================================================"));
    }

    @Async
    public void error(Logger log, String key, String value){
        log.error(String.format("======================================================================"));
        log.error(String.format( "Error >> key : %s ", key, value));
        log.error(String.format("======================================================================"));
    }

    @Async
    public void debug(Logger log, String key, String value){
        log.debug(String.format("======================================================================"));
        log.debug(String.format( "Warn >> %s : %s ", key, value));
        log.debug(String.format("======================================================================"));
    }
}
