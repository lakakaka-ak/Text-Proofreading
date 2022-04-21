package com.bistu.edu.cs.textproofreading.correct.chinese;

import com.bistu.edu.cs.textproofreading.constant.DicConstant;
import com.bistu.edu.cs.textproofreading.pojo.en.Symbol;
import com.github.houbb.heaven.annotation.ThreadSafe;
import com.github.houbb.heaven.constant.PunctuationConst;
import com.github.houbb.heaven.util.guava.Guavas;
import com.github.houbb.heaven.util.io.StreamUtil;
import com.github.houbb.heaven.util.lang.ObjectUtil;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.pinyin.api.impl.PinyinContext;
import com.github.houbb.pinyin.constant.PinyinConst;
import com.github.houbb.pinyin.support.tone.DefaultPinyinTone;
import com.github.houbb.pinyin.util.PinyinHelper;
import com.github.houbb.word.checker.util.EnWordCheckers;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ChText {
    static int a = 0;

    public static void main(String[] args) throws Exception {
//        String text = "抗战末期，一群溃败下来的国民党士兵聚集在西南小镇的收容所里，他们被几年来国土渐次沦丧弄得毫无斗志，只想苟且偷生。" ;
        String text = "抗战末妻";//抗战末妻

        //TODO 将文本转拼音
        String pinyin = PinyinHelper.toPinyin(text);
        System.out.println(pinyin);
        //TODO 构建ACDAT树
        ChText ct = new ChText();
        AhoCorasickDoubleArrayTrie<String> acdat = ct.buildChDAT();
        //TODO 根据加入到ACDAT的词典进行文本解析
        List<AhoCorasickDoubleArrayTrie.Hit<String>> hits = acdat.parseText(pinyin);
        System.out.println(hits);
        //TODO 将hits倒序
        Collections.reverse(hits);
        //TODO 纠错与查错
        int start = 0;//begin值
        int length = text.length();//文本长度
        List<Integer> index = new ArrayList<>();
        for (int i = 0; i < hits.size(); ) {
            if (hits.get(i).end > start && i != 0) {
                i++;
                continue;
            }
            int curEnd = hits.get(i).end;//当前hit.end
            int curHits = i;//获取当前匹配正确的hits下标
            int minBegin = hits.get(i).begin;//记录最长匹配串的hits下标
            int k = i + 1;
            if (k < hits.size()) {
                while (curEnd == hits.get(k).end) {
                    if (hits.get(k).begin < minBegin) {
                        minBegin = hits.get(k).begin;
                        curHits = k;//更新下标
                    }
                    k++;
                }
            }
            index.add(curHits);
            start = minBegin;
            i = k;
            if (minBegin == 0) {
                break;
            }
        }
        for(int i=0;i<index.size();i++){
            String correct = hits.get(index.get(i)).value;//匹配到的正确串
            int correctLength = correct.length();//串长度
            String orgtext = text.substring(length-correctLength,length);//原本text的位置
            if(!orgtext.equals(correct)){
                String wrong = orgtext;//原text
                String position = "[" + (length - correctLength) + ":" + length + "]";
                String suggestion = correct;
                System.out.println(wrong);//原text
                System.out.println(position);
                System.out.println(suggestion);
            }
            length = length-correctLength;//更新text长度
        }

    }

    public Map<String, String> loadCh() throws IOException {
        Map<String, String> map = new TreeMap<>();
        final String path = DicConstant.Ch;
        BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            String[] word = line.split(":");
            List<String> pinyinList = StringUtil.splitToList(word[1]);
            map.put(pinyinList.get(0), word[0]);
        }
        br.close();
        return map;
    }

    public AhoCorasickDoubleArrayTrie<String> buildChDAT() throws Exception {
        // 加载词典
        Map<String, String> map = loadCh();
        // 建立AhoCorasickDoubleArrayTrie
        AhoCorasickDoubleArrayTrie<String> dat = new AhoCorasickDoubleArrayTrie<>();
        dat.build(map);
        return dat;
    }

}
//TODO 对文本进行分词处理
       /* Segment segment = HanLP.newSegment().enableNameRecognize(true); //识别人名 segment.seg
        HanLP.Config.ShowTermNature = false;//不显示词性
        //HanLP.segment()
        System.out.println("Hanlp.segment");
        List<Term> HanlpList = HanLP.segment(text);
        CoreStopWordDictionary.apply(HanlpList);
        System.out.println(HanlpList);*/

/*//TODO 返回同音字
        List<String> re=PinyinHelper.samePinyinList("zhong4");
        System.out.println(re);*/
