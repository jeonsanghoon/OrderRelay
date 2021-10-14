package com.them.orderrelay.framework.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * 숫자 클래스
 */
@Slf4j
@Component
public class NumberInfo {
    /**
     * 내림 처리 digit이 -일경우 정수
     * @param val
     * @param digitNum
     * @return
     */
    public double floor(double val, int digitNum)
    {
        if(digitNum < 0)
        {
            double num = Math.pow(10, (-1 * digitNum));
            return Math.floor(val / num) * num;
        }
        else
        {
            double num = Math.pow(10, digitNum);
            return Math.floor(val * num) / num;
        }
    }

    /**
     * 올림처리 digitNum이 -일경우 정수
     * @param val
     * @param digitNum
     * @return
     */
    public double ceil(double val, int digitNum)
    {
        if(digitNum < 0)
        {
            double num = Math.pow(10, (-1 * digitNum));
            return Math.ceil(val / num) * num;
        }
        else
        {
            double num = Math.pow(10, digitNum);
            return Math.ceil(val * num) / num;
        }
    }

    /**
     * 반올림처리 digitNum이 -일경우 정수
     * @param val
     * @param digitNum
     * @return
     */
    public double round(double val, int digitNum)
    {
        if(digitNum < 0)
        {
            double num = Math.pow(10, (-1 * digitNum));
            return Math.round(val / num) * num;
        }
        else
        {
            double num = Math.pow(10, digitNum);
            return Math.round(val * num) / num;
        }
    }

    /**
     * 난수 자동생성
     * @param numLen
     * @return
     */
    public int getRandomNumber(int numLen) {
        if(numLen <= 0 ) return 0;

        Random random = new Random(System.currentTimeMillis());

        int range = (int) Math.pow(10, numLen);
        int trim = (int) Math.pow(10, numLen - 1);
        int result = random.nextInt(range) + trim;

        if (result > range) {
            result = result - trim;
        }
        return result;
    }

    /**
     * 난수 자동생성 Max 값 1부터 시작
     * @param maxNum
     * @return
     */
    public int getRandomLimit(int maxNum)
    {
        try {
            if (maxNum <= 0) return 0;
            Random random = new Random(System.currentTimeMillis());
            return random.nextInt(maxNum) + 1;
        }catch(Exception ex)
        {
            return 0;
        }
    }

    public Double parseDouble(String val)
    {
        Double rtn = 0d;

        if(this.isNumeric(val))
        {
            rtn = Double.parseDouble(val);
        }
        return rtn;
    }

    public Integer parseInt(String val)
    {
        Integer rtn = 0;

        if(this.isNumeric(val))
        {
            rtn = Integer.parseInt(val);
        }
        return rtn;
    }

    public Long parseLong(String val)
    {
        Long rtn = 0l;

        if(this.isNumeric(val))
        {
            rtn = Long.parseLong(val);
        }
        return rtn;
    }

    public boolean isNumeric(String val)
    {
        if(val == null) return false;
        Pattern DOUBLE_PATTERN = Pattern.compile(
                "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
                        "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
                        "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
                        "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");

        return DOUBLE_PATTERN.matcher(val).matches();
    }

    public String getMeterToKiloMeter(String distance) {
        return Double.toString (Global.getNumberInfo().parseDouble(distance) / 1000.00);
    }

    public Double getMeterToKiloMeter(Double distance) {
        if(distance == null) return 0d;
        return distance / 1000.00;
    }
}
