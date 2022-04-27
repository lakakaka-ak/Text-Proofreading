package com.bistu.edu.cs.textproofreading.correct.english;

import com.bistu.edu.cs.textproofreading.correct.english.SpellCorrect;
import com.bistu.edu.cs.textproofreading.pojo.en.Symbol;
import com.github.houbb.word.checker.util.EnWordCheckers;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用于进行英文校对的细节测试
 */

public class englishTest {

    //TODO static 关键字是定义全局变量的
    //TODO final 表示在编译过程中永不改变的量
    static int a = 0;

    @SneakyThrows
    public static void main(String[] args) {
        long time1 = System.currentTimeMillis();
        SpellCorrect sc = new SpellCorrect();
        AhoCorasickDoubleArrayTrie<String> acdat = sc.buildDAT();
        long time2 = System.currentTimeMillis();

        String text = "I have to speend";
        List<AhoCorasickDoubleArrayTrie.Hit<String>> hits = acdat.parseText(text);
        long time3 = System.currentTimeMillis();

        //TODO 将hits倒序
        Collections.reverse(hits);
        long time4 = System.currentTimeMillis();

        /** hits.sort((o1, o2) -> {
         if(o1.end==o2.end){
         if(o1.begin<o2.begin)
         return 1;
         }
         return 0;
         });*/

        int start = 0;
        int j = 0;//获取递归时hits下表
        List<Symbol> index = new ArrayList<>();
        for (int i = 0; i < hits.size(); ) {
            if (hits.get(i).end > start && i != 0) {
                i++;
                continue;
            }
            int curEnd = hits.get(i).end;//当前hit.end
            int minBegin = hits.get(i).begin;//记录最长匹配串的hits下标
            int k = i + 1;
            if (k < hits.size()) {
                while (curEnd == hits.get(k).end) {
                    if (hits.get(k).begin < hits.get(i).begin) {
                        minBegin = hits.get(k).begin;
                    }
                    k++;
                }
            }

            //TODO 承接DFS的返回值 返回begin值
            start = DFSSearch(hits, minBegin, j);
            //TODO Symbol类型的集合 存放begin，end，递归次数num
            index.add(new Symbol(start, hits.get(i).end, a));
            a = 0;
            i = k;
        }
        long time5 = System.currentTimeMillis();

        for (Symbol symbol : index) {
            if (symbol.getNum() > 1) {
                String wrong = text.substring(symbol.getBegin(), symbol.getEnd());
                System.out.println(wrong);
                String suggestion = EnWordCheckers.correct(wrong);//纠错
                System.out.println(suggestion);
            }
        }
        long time6 = System.currentTimeMillis();

        System.out.println("bulidDAT : " + (time2 - time1) + "ms");
        System.out.println("解析时间 : " + (time3 - time2) + "ms");
        System.out.println("倒序时间 : " + (time4 - time3) + "ms");
        System.out.println("查错时间 : " + (time5 - time4) + "ms");
        System.out.println("纠错时间 : " + (time6 - time5) + "ms");

    }

    /**
     * 深度优先遍历
     *
     * @param cur 顶点编号
     * @param hit 存放待遍历元素的列表
     * @return 返回由该顶点所找到的最前置节点
     */

    public static int DFSSearch(List<AhoCorasickDoubleArrayTrie.Hit<String>> hit, int cur, int j) {
        a++;
        int mid = cur;
        for (int i = j; i < hit.size(); i++) {
            if (hit.get(i).end == cur) {
                j = i;
                mid = DFSSearch(hit, hit.get(j).begin, j);
            }
            if (hit.get(i).end < cur) {
                break;
            }
        }
        return mid;
    }
}

