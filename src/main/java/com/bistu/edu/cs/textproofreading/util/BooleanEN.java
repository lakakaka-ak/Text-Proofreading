package com.bistu.edu.cs.textproofreading.util;

import com.sun.org.apache.xerces.internal.impl.xs.identity.Selector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BooleanEN {
    /*
     * 是否是英文字符串
     */

    public static boolean isEnglishStr(String charaString){
        return charaString.matches(".*[a-zA-z].*");
    }

    /*
     *  是否是中文字符串
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }
}
