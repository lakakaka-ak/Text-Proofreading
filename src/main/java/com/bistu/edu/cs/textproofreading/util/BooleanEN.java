package com.bistu.edu.cs.textproofreading.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 此类用于判断文本信息是否为英文文本或者是中文文本
 */

public class BooleanEN {

    /**
     * 是否是英文字符串（包含英文）
     */
    public static boolean isEnglishStr(String charaString){
        return charaString.matches(".*[a-zA-z].*");
    }

    /**
     *  是否是中文字符串（包含中文）
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }
}
