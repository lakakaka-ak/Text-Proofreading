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
        String text="习近平总书记发表了系烈重要讲话，深课回答了新形势下党和国家事页发展的一系列重大里论和现是问题，提出许多富有闯见的新理念新思想新栈略，丰富发展了党的科雪理论，是新的历史条件下我们党值国理政的行动钢领，是坚持和发展中国特色社辉主义的最新理论成果，凝结了全党知灰。深入学习贯彻习近平总书记系列重要讲话精神，是当前全省教育系统的一项重要政治任务。";
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