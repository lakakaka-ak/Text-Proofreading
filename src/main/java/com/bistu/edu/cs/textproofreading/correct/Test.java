package com.bistu.edu.cs.textproofreading.correct;


import com.bistu.edu.cs.textproofreading.correct.chinese.PinyinCorrect;
import com.bistu.edu.cs.textproofreading.correct.english.SpellCorrect;
import com.bistu.edu.cs.textproofreading.pojo.ErrorForm;
import com.bistu.edu.cs.textproofreading.util.BooleanEN;
import com.bistu.edu.cs.textproofreading.util.StringUtils;
import com.github.houbb.pinyin.api.impl.PinyinContext;
import com.github.houbb.pinyin.util.PinyinHelper;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 此类用于整体算法测试
 */

public class Test {
    public static void main(String[] args) throws Exception {
//        //TODO 对英文算法进行测试
//        SpellCorrect sc = new SpellCorrect();
//        AhoCorasickDoubleArrayTrie<String> acdat = sc.buildDAT();
//        String textInfo = "have to";//"I have $100 millions,but i don't konow how to speend."
//        String text=textInfo.replaceAll("[,.!?:']"," ");
//        System.out.println(text);
//        List<AhoCorasickDoubleArrayTrie.Hit<String>> hits = acdat.parseText(text);
//        /*hits.sort(new Comparator<AhoCorasickDoubleArrayTrie.Hit<String>>() {
//            @Override
//            public int compare(AhoCorasickDoubleArrayTrie.Hit<String> o1, AhoCorasickDoubleArrayTrie.Hit<String> o2) {
//                if(o1.end==o2.end){
//                    if(o1.begin<o2.begin)
//                        return 1;
//                }
//                return 0;
//            }
//        });*/
//        List<ErrorForm> result = sc.findWrongWords(text, hits);
//        System.out.println(result);
        String words = "你love is not..!=";
        String re= StringUtils.clearCharacter(words);
        System.out.println(re);
//        if (BooleanEN.isContainChinese(words))
//            System.out.println("yes");
//        String text = "抗战末妻";//抗战末妻
//        if (BooleanEN.isEnglishStr(text)) {
//            //TODO 对中文算法进行测试
//            PinyinCorrect pc = new PinyinCorrect();
//            AhoCorasickDoubleArrayTrie<String> acdat = pc.buildChDAT();
////            String text = "抗战末妻";//抗战末妻
//            //TODO 将文本转拼音
//            String pinyin = PinyinHelper.toPinyin(text);
//            List<AhoCorasickDoubleArrayTrie.Hit<String>> hits = acdat.parseText(pinyin);
//            Collections.reverse(hits);
//            List<ErrorForm> result = pc.findWrongPinyin(text, hits);
//            System.out.println(result);
//        }
    }
}
