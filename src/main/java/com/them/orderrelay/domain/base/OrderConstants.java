package com.them.orderrelay.domain.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderConstants {
    public static String storeKeyGubun;
    @Value("${service.base.storeKey-gubun}")
    public void setStoreKeyGubun(String val)
    {
        storeKeyGubun = val;
    }

    public static String orderGubun;
    @Value("${service.base.order-gubun}")
    public void setOrderGubun(String val)
    {
        orderGubun = val;
    }

    public static long timeZone;
    @Value("${spring.data.mongodb.timezone}")
    public void setTimeZone(long val)
    {
        timeZone = val;
    }
}
