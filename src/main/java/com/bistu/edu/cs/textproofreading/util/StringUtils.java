package com.bistu.edu.cs.textproofreading.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

public class StringUtils {

    public static final String JSON_EMPTY_LIST = "[]";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String list2Str(List<String> list){
        if(list==null||list.isEmpty()){
            return JSON_EMPTY_LIST;
        }
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return JSON_EMPTY_LIST;
        }
    }

    public static List<String> str2List(String str) {
        if (isEmpty(str)) {
            return Collections.emptyList();
        }
        if (JSON_EMPTY_LIST.equals(str)) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(str, new TypeReference<List<String>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }



    /**
     * 判断字符串是否为空（包括null或trim后为""的字符串）
     *
     * @param str 待判断的字符串
     * @return 若str为null或者trim后为""，返回true，否则返回false
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equalsIgnoreCase(str.trim());
    }

}
