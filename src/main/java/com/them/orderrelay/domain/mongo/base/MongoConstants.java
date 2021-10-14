package com.them.orderrelay.domain.mongo.base;

import com.them.orderrelay.framework.util.Global;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class MongoConstants {
    public static long timeZone;
    @Value("${spring.data.mongodb.timezone}")
    public void setTimeZone(long val)
    {
        timeZone = val;
    }
}
