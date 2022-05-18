package com.bistu.edu.cs.textproofreading.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.houbb.heaven.util.lang.CharUtil;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 此类实现文本处理
 */

public class StringUtils {

    public static final String EMPTY = "";
    public static final String EMPTY_JSON = "{}";
    public static final String BLANK = " ";
    public static final String NEW_LINE = "";
    public static final String COLON = ":";
    public static final String COMMA = ",";

    /**
     * 实现 List <--> String 相互转换
     */
    public static final String JSON_EMPTY_LIST = "[]";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String list2Str(List<String> list) {
        if (list == null || list.isEmpty()) {
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

    /**
     * 清除文本中的标点符号
     * 不需要用空格进行代替
     *
     * @param text 文本
     * @return 清除后的文本
     */
    public static String clearCharacter(String text) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(text);
        return m.replaceAll("").trim();
    }

    /**
     * 清除文本中的标点符号
     * 需要用空格进行代替
     *
     * @param text 文本
     * @return 清除后的文本
     */
    public static String replaceCharacter(String text) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\-\\[\\].<>/?~！@#￥%……&*（）——\\-+|{}【】‘；：”“。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(text);
        return m.replaceAll(" ").trim();
    }

    /**
     * 判断文本是否是英文
     */
    public static boolean isEnglish(String text) {
        if (isEmpty(text)) {
            return false;
        } else {
            char[] chars = text.toCharArray();
            char[] var2 = chars;
            int var3 = chars.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                char c = var2[var4];
                if (!CharUtil.isEnglish(c)) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * 判断文本是否为中文
     */
    public static boolean isChinese(String text) {
        if (isEmpty(text)) {
            return false;
        } else {
            char[] chars = text.toCharArray();
            char[] var2 = chars;
            int var3 = chars.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                char c = var2[var4];
                if (!CharUtil.isChinese(c)) {
                    return false;
                }
            }
            return true;
        }
    }


}
