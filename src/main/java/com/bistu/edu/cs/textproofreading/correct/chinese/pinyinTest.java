package com.bistu.edu.cs.textproofreading.correct.chinese;


import com.bistu.edu.cs.textproofreading.constant.DicConstant;
import com.bistu.edu.cs.textproofreading.pojo.ch.SegmentForm;
import com.bistu.edu.cs.textproofreading.pojo.mysql.PinyinData;
import com.bistu.edu.cs.textproofreading.service.PinyinToneService;
import com.bistu.edu.cs.textproofreading.util.StringUtils;
import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum;
import com.github.houbb.pinyin.util.PinyinHelper;

import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.collection.trie.DoubleArrayTrie;
import com.hankcs.hanlp.corpus.document.sentence.Sentence;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 此类用于中文分词测试
 */

public class pinyinTest {

    public static void main(String[] args) throws Exception {
        PinyinDictionary pd = new PinyinDictionary();
        //AhoCorasickDoubleArrayTrie<String> dat = p.PinyinList();
        AhoCorasickDoubleArrayTrie<String> dat = pd.buildPinyinDAT();
        String text="作为一名大学生村官，既要胸坏远大丽想，凌云壮至，还必须脚踏实地，立足岗位，扎根基层，勇于担当，甘于风献，守得住清频，耐得住季寞。";
        HanLP.Config.ShowTermNature = false;//不显示词性

//        long time1 = System.currentTimeMillis();
//        List<Term> seg = HanLP.segment(text);//分词
//        long time2 = System.currentTimeMillis();
//        List<Term> s2 = IndexTokenizer.segment(text);
        long time3 = System.currentTimeMillis();
        List<Term> s3 = SpeedTokenizer.segment(text);
        long time4 = System.currentTimeMillis();
        //String pinyin= PinyinHelper.toPinyin(text, PinyinStyleEnum.NORMAL);
//        System.out.println("标准分词："+(time2-time1)+"ms");
//        System.out.println(seg);
//        System.out.println("索引分词："+(time3-time2)+"ms");
//        System.out.println(s2);
        System.out.println("极速分词："+(time4-time3)+"ms");
        System.out.println(s3);


    }
}