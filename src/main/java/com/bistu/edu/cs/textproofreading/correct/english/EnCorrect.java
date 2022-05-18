package com.bistu.edu.cs.textproofreading.correct.english;

import com.bistu.edu.cs.textproofreading.pojo.ErrorForm;
import com.bistu.edu.cs.textproofreading.pojo.en.Symbol;
import com.bistu.edu.cs.textproofreading.util.StringUtils;
import com.github.houbb.word.checker.util.EnWordCheckers;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class EnCorrect {
    /**
     * 用于定义DFS递归次数
     */
    static int a = 0;

    /**
     * <p>
     * 英文单词查错基于对匹配串进行DFS查找，找出错误单词位置
     * 英文单词纠错 调用EnWordCheckers.correct方法
     * </p>
     *
     * @return 错误集合
     */
    public List<ErrorForm> findWrongWords(String text) throws Exception {
        //TODO 对文本标点符号进行处理
        String textInfo = StringUtils.replaceCharacter(text);

        //TODO 构建英文词典ACDAT
        long time1 = System.currentTimeMillis();
        EnDictionary ed = new EnDictionary();
        AhoCorasickDoubleArrayTrie<String> acdat = ed.buildDAT();
        long time2 = System.currentTimeMillis();
        log.info("英文词典构建：" + (time2 - time1) + "ms");

        //TODO 根据加入到ACDAT的词典进行文本解析
        long time3 = System.currentTimeMillis();
        List<AhoCorasickDoubleArrayTrie.Hit<String>> hits = acdat.parseText(textInfo);
        //TODO 将hits倒序
        Collections.reverse(hits);
        long time4 = System.currentTimeMillis();
        log.info("英文文本字符串匹配：" + (time4 - time3) + "ms");

        long time5 = System.currentTimeMillis();
        List<ErrorForm> result = new ArrayList<>();//返回错误单词集合
        int start = 0;//begin值
        int j = 0;//获取递归时hits列表当前位置
        List<Symbol> index = new ArrayList<>();
        for (int i = 0; i < hits.size(); ) {
            if (hits.get(i).end > start && i != 0) {
                i++;
                continue;
            }
            int curEnd = hits.get(i).end;//当前hit.end
            int minBegin = hits.get(i).begin;//记录最长匹配串的begin值
            int k = i + 1;
            if (k < hits.size()) {
                while (curEnd == hits.get(k).end) {
                    if (hits.get(k).begin < minBegin) {
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
        long time6 = System.currentTimeMillis();
        log.info("英文文本查错：" + (time6 - time5) + "ms");
        long time7 = System.currentTimeMillis();
        for (Symbol symbol : index) {
            if (symbol.getNum() > 1) {
                String wrong = textInfo.substring(symbol.getBegin(), symbol.getEnd());
                String position = "[" + symbol.getBegin() + ":" + symbol.getEnd() + "]";
                String suggestion = EnWordCheckers.correct(wrong);//纠错
                result.add(new ErrorForm(wrong, position, suggestion));
            }
        }
        long time8 = System.currentTimeMillis();
        log.info("英文文本纠错：" + (time8 - time7) + "ms");
        return result;
    }

    /**
     * 深度优先遍历
     *
     * @param cur begin值
     * @param hit 存放待遍历元素的列表
     * @return 返回由该顶点所找到的最前置节点
     */
    public static int DFSSearch(List<AhoCorasickDoubleArrayTrie.Hit<String>> hit, int cur, int j) {
        a++;
        int mid = cur;
        for (int i = j; i < hit.size(); i++) {
            if (hit.get(i).end == cur) {
                j = i;
                return DFSSearch(hit, hit.get(j).begin, j);
            }
            if (hit.get(i).end < cur) {
                break;
            }
        }
        return mid;
    }

}
