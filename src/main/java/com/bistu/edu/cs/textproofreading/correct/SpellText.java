package com.bistu.edu.cs.textproofreading.correct;


import com.bistu.edu.cs.textproofreading.correct.english.SpellCorrect;
import com.bistu.edu.cs.textproofreading.pojo.ErrorForm;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 此类用于整体算法测试
 */

public class SpellText {
    public static void main(String[] args) throws Exception {
        //TODO 对英文算法进行测试
        SpellCorrect sc = new SpellCorrect();
        AhoCorasickDoubleArrayTrie<String> acdat = sc.buildDAT();
        String textInfo = "have to";//"I have $100 millions,but i don't konow how to speend."
        String text=textInfo.replaceAll("[,.!?:']"," ");
        System.out.println(text);
        List<AhoCorasickDoubleArrayTrie.Hit<String>> hits = acdat.parseText(text);
        /*hits.sort(new Comparator<AhoCorasickDoubleArrayTrie.Hit<String>>() {
            @Override
            public int compare(AhoCorasickDoubleArrayTrie.Hit<String> o1, AhoCorasickDoubleArrayTrie.Hit<String> o2) {
                if(o1.end==o2.end){
                    if(o1.begin<o2.begin)
                        return 1;
                }
                return 0;
            }
        });*/
        //TODO 对hits按照end降序，begin升序进行排序
        for(AhoCorasickDoubleArrayTrie.Hit hit:hits) {
            System.out.println(hit);
        }
       /* List<ErrorForm> result = sc.findWrongWords(text, hits);
        System.out.println(result);*/
    }
}
