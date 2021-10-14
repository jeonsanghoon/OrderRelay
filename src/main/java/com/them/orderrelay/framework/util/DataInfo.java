package com.them.orderrelay.framework.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.them.orderrelay.framework.base.dto.KoreanUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Component
public final class DataInfo {
    /**
     * lPad 함수
     *
     * @param str
     * @param iLen
     * @param strChar
     * @return
     */
    public String lPad(String str, int iLen, String strChar) {
        String strResult = "";
        StringBuilder sbAddChar = new StringBuilder();
        for (int i = str.length(); i < iLen; i++) {
            // iLen길이 만큼 strChar문자로 채운다.
            sbAddChar.append(strChar);
        }
        strResult = sbAddChar + str;
        return strResult;
    }

    /**
     * rPad 함수
     *
     * @param str
     * @param iLen
     * @param strChar
     * @return
     */
    public String rPad(String str, int iLen, String strChar) {
        String strResult = "";
        StringBuilder sbAddChar = new StringBuilder();
        for (int i = str.length(); i < iLen; i++) {
            // iLen길이 만큼 strChar문자로 채운다.
            sbAddChar.append(strChar);
        }
        strResult = str + sbAddChar;
        return strResult;
    }

    /**
     * 공백 여부
     *
     * @param str
     * @return
     */
    public Boolean isNullEmpty(String str) {
        return StringUtils.isBlank(str);
    }

    /**
     * 현재일자 문자로 변경
     *
     * @return
     */
    public String nowDateToString() {
        return this.nowDateToString(null);
    }

    public String nowDateToString(String format) {
        return dateToString(new Date(), format);
    }


    public String nowDateTimeToString() {
        return this.nowDateToString("yyyyMMddHHmmss");
    }

    public String nowDateTimeToString(String format) {
        return dateToString(new Date(), format);
    }

    /**
     * 랜덤 일자 생성
     *
     * @return
     */
    public String getRandomDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(2021, Calendar.JANUARY, 1);
        cal.add(Calendar.DATE, 7);

        return getRandomDate(cal.getTime(), new Date());
    }

    public String getRandomDate(Date startInclusive, Date endExclusive) {
        long startMillis = startInclusive.getTime();
        long endMillis = endExclusive.getTime();
        long randomMillisSinceEpoch = ThreadLocalRandom
                .current()
                .nextLong(startMillis, endMillis);

        return dateToString(new Date(randomMillisSinceEpoch));
    }

    /**
     * 날짜를 문자로 변경
     *
     * @param date
     * @return
     */

    public String dateToString(Date date) {
        return dateToString(date, null);
    }

    public String dateToString(Date date, String format) {
        format = (isNullEmpty(format)) ? "yyyyMMdd" : format;
        SimpleDateFormat transFormat = new SimpleDateFormat(format);

        return transFormat.format(date);
    }

    /**
     * 시간을 더하여 날짜를 리턴하는 함수
     *
     * @param date
     * @param hours
     * @return
     */
    public Date addHoursToDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    /**
     * 날짜를 더하여 TimeStamp를 리턴하는 함수
     *
     * @param date
     * @param hours
     * @return
     */
    public Timestamp addHoursToTimestamp(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        Timestamp rtn = new Timestamp(calendar.getTime().getTime());
        return rtn;
    }


    /**
     * 난수 자동생성
     *
     * @param numLen
     * @return
     */
    public String getGenerateNumber(int numLen) {
        if (numLen <= 0) return "0";

        Random random = new Random(System.currentTimeMillis());

        int range = (int) Math.pow(10, numLen);
        int trim = (int) Math.pow(10, numLen - 1);
        int result = random.nextInt(range) + trim;

        if (result > range) {
            result = result - trim;
        }
        return String.valueOf(result);
    }

    /**
     * 난수 자동생성 Max 값 1부터 시작
     *
     * @param maxNum
     * @return
     */
    public int getGenerateNumberLimit(int maxNum) {
        try {
            if (maxNum <= 0) return 0;
            Random random = new Random(System.currentTimeMillis());
            return random.nextInt(maxNum) + 1;
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * 오브젝트를 Json String 로 리턴하는 함수
     *
     * @param t
     * @param <T>
     * @return
     */
    public <T> String convertToString(T t) {
        Gson gson = new Gson();
        return gson.toJson(t);
    }

    public <T> String convertToObjectMapperString(T t) {

        try {
            return new ObjectMapper().writeValueAsString(t);
        } catch (
                JsonProcessingException e) {
            return "";
        }
    }

    /**
     * json 을 오브젝트로 리턴하는 함수
     * @param json
     * @param sClass
     * @param <T>
     * @return
     * @throws JsonProcessingException
     */
    public <T>  T convertToObjectMapperClass(String json, Class<T> sClass) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, sClass) ;
    }

    /**
     * String를 Class Object로 리턴하는 함수
     * @param json
     * @param t
     * @param <T>
     * @return
     */
    public <T> T convertToClass(String json, Class<T> t)
    {
        Gson gson = new Gson();
        return gson.fromJson(json, t);
    }

    /**
     * 다른 클래스로 변환
     * @param t
     * @param t1
     * @param <T>
     * @param <T1>
     * @return
     */
    public <T,T1> T1 changeToOtherClass(T t, Class<T1> t1)
    {
        Gson gson = new Gson();
        return gson.fromJson(gson.toJson(t), t1);
    }

    /**
     * 오브젝트 다른 클래스로 전환
     * @param t
     * @param t1
     * @param <T>
     * @param <T1>
     * @return
     */
    public <T,T1> T1 changeToOtherObjectClass(T t, Class<T1> t1)
    {
        String sJson = Global.getDataInfo().convertToObjectMapperString(t);
        return  Global.getDataInfo().convertToClass(sJson , t1);
    }

    /**
     * 리스트 중복제거 List Distinct
     * ex) Global.getDataInfo().distinctList(list, SignageBanner::getSignCode, SignageBanner::getPage);
     * @param list
     * @param keyExtractors
     * @param <T>
     * @return
     */
    public <T> List<T> distinctList(List<T> list, Function<? super T, ?>... keyExtractors) {
        return list.stream()
                .filter(distinctByKeys(keyExtractors))
                .collect(Collectors.toList());
    }

    private <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) {
        final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();
        return t -> {
            final List<?> keys = Arrays.stream(keyExtractors)
                    .map(ke -> ke.apply(t))
                    .collect(Collectors.toList());
            return seen.putIfAbsent(keys, Boolean.TRUE) == null;
        };
    }


    /**
     * List Ojbect를 Map로 변경
     * @param <T>
     * @param target
     * @return
     */
    public <T> List<LinkedHashMap<String, Object>> convertListToMap(List<T> target) {

        List<LinkedHashMap<String, Object>> resultList = new ArrayList<LinkedHashMap<String, Object>>();
        for (T element : target) {
            LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
            Field[] fieldList = element.getClass().getDeclaredFields();
            if (fieldList != null && fieldList.length > 0) {
                try {
                    for (int i = 0; i < fieldList.length; i++) {
                        String curInsName = fieldList[i].getName();
                        Field field = element.getClass().getDeclaredField(curInsName);
                        field.setAccessible(true);
                        Object targetValue = field.get(element);
                        resultMap.put(curInsName, targetValue);
                    }
                    resultList.add(resultMap);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return resultList;
    }



    /**
     * 클래스를 key value map로 가져오기
     * @param <T>
     * @param target
     * @return
     */
    public <T> LinkedHashMap<String, Object> convertClassToMap(T target)
    {
        LinkedHashMap<String, Object> resultMap = new LinkedHashMap<String, Object>();
        Field[] fieldList = target.getClass().getDeclaredFields();
        if (fieldList != null && fieldList.length > 0) {
            try {
                for (int i = 0; i < fieldList.length; i++) {
                    String curInsName = fieldList[i].getName();
                    Field field = target.getClass().getDeclaredField(curInsName);
                    field.setAccessible(true);
                    Object targetValue = field.get(target);
                    resultMap.put(curInsName, targetValue);
                }
                return resultMap;

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Map을 Class로 변경
     * @param map
     * @param toClassType
     * @param <T>
     * @return
     */
    public <T> T convertMapToClass(LinkedHashMap<String, Object> map, Class<T> toClassType){
        ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        return (T) mapper.convertValue(map, toClassType);
    }

    public <T> List<T> convertMapToList(List<LinkedHashMap> mapList, Class<T> toClassType){
        List<T> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        for(LinkedHashMap map : mapList)
        {
            T dto = (T) Global.getDataInfo().convertMapToClass(map,toClassType);

            list.add(dto);
        }

        return list;
    }


    public <T> T convertMapListToData(List<LinkedHashMap> mapList, Class<T> toClassType){
        List<T> list = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
        for(LinkedHashMap map : mapList)
        {
            T dto = (T) Global.getDataInfo().convertMapToClass(map,toClassType);

            list.add(dto);
        }
        T rtnDta = (T) toClassType.getClass();
        return list.size() == 0 ? rtnDta : list.get(0);
    }






    /**
     * 키워드 분할
     * @param text
     * @return
     */
    public KoreanUnit convertKoreanUnit(String text) {
        String chosung = "ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ";
        String jungsung = "ㅏㅐㅑㅒㅓㅔㅕㅖㅗㅘㅙㅚㅛㅜㅝㅞㅟㅠㅡㅢㅣ";
        String jongsung = " ㄱㄲㄳㄴㄵㄶㄷㄹㄺㄻㄼㄽㄾㄿㅀㅁㅂㅄㅅㅆㅇㅈㅊㅋㅌㅍㅎ";
        int HANGEUL_BASE = 0xAC00; // '가'
        int HANGEUL_END = 0xD7AF;

        // 이하 ja, mo는 단독으로 입력된 자모에 대해 적용
        int JA_BASE = 0x3131;
        int MO_BASE = 0x314F;

        KoreanUnit rtnData = new KoreanUnit();
        // 받침이 없을 수 있으므로 제일 첫부분은 띄워줘야 합니다.
        for (char c : text.toCharArray()) {
            if ((c <= 10 && c <= 13) || c == 32) {
                rtnData.setKeywordUnit(rtnData.getKeywordUnit().concat(Character.toString(c).trim()));
                continue;
            } else if (c >= JA_BASE && c <= JA_BASE + 36) {
                rtnData.setKeywordUnit(rtnData.getKeywordUnit().concat(Character.toString(c).trim()));
                continue;
            } else if (c >= MO_BASE && c <= MO_BASE + 58) {
                rtnData.setKeywordUnit(rtnData.getKeywordUnit().concat(Character.toString((char) 0).trim()));
                continue;
            } else if (c >= HANGEUL_BASE && c <= HANGEUL_END) {

                int Code =  c -HANGEUL_BASE;

                int JongsungCode = Code % 28; // 종성 코드 분리
                Code = (Code - JongsungCode) / 28;

                int JungsungCode = Code % 21; // 중성 코드 분리
                Code = (Code - JungsungCode) / 21;

                int ChosungCode = Code; // 남는 게 자동으로 초성이 됩니다.

                char Chosung = chosung.toCharArray()[ChosungCode]; // Chosung 목록 중에서 ChosungCode 번째 있는 글자
                char Jungsung = jungsung.toCharArray()[JungsungCode];
                char Jongsung = jongsung.toCharArray()[JongsungCode];

                rtnData.setChoSung(rtnData.getChoSung().concat(Character.toString(Chosung).trim()));
                rtnData.setJungSung(rtnData.getJungSung().concat(Character.toString(Jungsung).trim()));
                rtnData.setJongSung(rtnData.getJongSung().concat(Character.toString(Jongsung).trim()));
                rtnData.setKeywordUnit(rtnData.getKeywordUnit().concat(Character.toString(Chosung).trim())
                        .concat(Character.toString(Jungsung).trim())
                        .concat(Character.toString(Jongsung).trim()));
            } else {
                rtnData.setKeywordUnit(rtnData.getKeywordUnit() + Character.toString(c).trim());
            }
        }
        return rtnData;
    }

    public Integer getOnlyNumber(String val)
    {
        try {
            return Integer.parseInt(val.replaceAll("[^0-9]", ""));
        }catch(Exception e)
        {
            return 0;
        }
    }

    public String getOnlyAsteriskNumber(String val)
    {
        if(val == null) return "";
        return val.replaceAll("[^0-9*]","");
    }

    public void merge(Object obj, Object update){
        if(!obj.getClass().isAssignableFrom(update.getClass())){
            return;
        }

        Method[] methods = obj.getClass().getMethods();

        for(Method fromMethod: methods){
            if(fromMethod.getDeclaringClass().equals(obj.getClass())
                    && fromMethod.getName().startsWith("get")){

                String fromName = fromMethod.getName();
                String toName = fromName.replace("get", "set");

                try {
                    Method toMetod = obj.getClass().getMethod(toName, fromMethod.getReturnType());
                    Object value = fromMethod.invoke(update, (Object[])null);
                    if(value != null){
                        toMetod.invoke(obj, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //고유 아이디 만들기 (yyyyMMddHHmmssSSS_랜덤문자6개)
    public String getUniqueId() {
        String uniqueId = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Calendar dateTime = Calendar.getInstance();
        uniqueId = sdf.format(dateTime.getTime());

        //yyyymmddhh24missSSS_랜덤문자6개
        uniqueId = uniqueId +"_"+ RandomStringUtils.randomAlphanumeric(6);

        return uniqueId;
    }

    public List<String> splitByString(String exMsg, String splitData) {
        List<String> list = new ArrayList<>();
        int charIdx = 0;

        while (true) {
            int findIdx = exMsg.indexOf(splitData, charIdx);
            if (exMsg.indexOf(splitData, charIdx) > 0) {
                list.add(exMsg.substring(charIdx, findIdx));
                charIdx = findIdx + splitData.length();
            } else
                break;
        }

        if (charIdx < exMsg.length()) {
            list.add(exMsg.substring(charIdx));
        }
        return list;
    }

    public String splitByIndex(String exMsg, String splitData, int idx) {
        List<String> list = this.splitByString(exMsg, splitData);
        if(list.size() <= idx) return "";
        return list.get(idx);
    }
}
