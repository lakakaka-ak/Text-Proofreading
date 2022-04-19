package com.bistu.edu.cs.textproofreading.correct.english;

import com.bistu.edu.cs.textproofreading.pojo.ErrorForm;
import com.bistu.edu.cs.textproofreading.pojo.en.Symbol;
import com.github.houbb.word.checker.util.EnWordCheckers;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * <p>
 * 此类用于英文单词拼写错误的检查
 * 主要实现英文词典的DAT储存，英文文本的分词，英文文本的查错以及纠错处理
 * </p>
 *
 * @author lak
 */


public class SpellCorrect {

    /**
     * 用于定义DFS递归次数
     */

    static int a = 0;

    /**
     * 词典加载
     *
     * @param path 词典的路径
     * @return 将字符串类型的词典转化成TreeSet结果
     */
    public Set<String> loadDictionary(String path) throws IOException {
        Set<String> dictionary = new TreeSet<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            dictionary.add(line);
        }
        br.close();

        return dictionary;
    }

    /**
     * 英语词典加载（单词，词频）
     *
     * @return 分好的词典
     */

    public Map<String, Integer> loadEn() throws IOException {
        Map<String, Integer> map = new HashMap<>();
        final String path = "static/dic/en_fre";
        BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            String[] word = line.split(",");
            map.put(word[0], Integer.parseInt(word[1]));
        }
        br.close();
        return map;
    }

    /**
     * <p>
     * 将词典以DAT结构存储
     * 使用此方法可以进行字符串模式匹配
     * SpellCorrect sc=new SpellCorrect();
     * AhoCorasickDoubleArrayTrie<String> acdat= sc.buildDAT();
     * List<AhoCorasickDoubleArrayTrie.Hit<String>> hits = acdat.parseText("I like apples.");
     * </p>
     */
    public AhoCorasickDoubleArrayTrie<String> buildDAT() throws Exception {
        // 加载词典
        Set<String> dictionary = loadDictionary("static/dic/en");
        // 可以使用任何类型存储数据
        Map<String, String> map = new TreeMap<>();
//        Map<String, String> map = new HashMap<String, String>();
//        Map<String, String> map = new LinkedHashMap<String, String>();
        for (String key : dictionary) {
            map.put(key, key);
        }
        // 建立AhoCorasickDoubleArrayTrie
        AhoCorasickDoubleArrayTrie<String> dat = new AhoCorasickDoubleArrayTrie<>();
        dat.build(map);
        return dat;
    }

    /**
     * <p>
     * Hanlp标准分词 不显示词性 去掉停用词
     * </p>
     *
     * @param textInfo 文本信息
     * @return Term类型List集合
     */
    public List<Term> removalOfStopWords(String textInfo) {

        HanLP.Config.ShowTermNature = false;//不显示词性
        List<Term> termList = HanLP.segment(textInfo);//标准分词
        CoreStopWordDictionary.apply(termList);//去停用词
        return termList;
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
                mid = DFSSearch(hit, hit.get(j).begin, j);
            }
            if (hit.get(i).end < cur) {
                break;
            }
        }
        return mid;
    }

    /**
     * <p>
     *     英文单词查错基于对匹配串进行DFS查找，找出错误单词位置
     *     英文单词纠错 调用EnWordCheckers.correct方法
     * </p>
     *
     * @param textInfo 要校对的字符串
     * @param hits 匹配串
     * @return 错误集合
     */

    public List<ErrorForm> findWrongWords(String textInfo, List<AhoCorasickDoubleArrayTrie.Hit<String>> hits) {
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
        for (Symbol symbol : index) {
            if (symbol.getNum() > 1) {
                String wrong = textInfo.substring(symbol.getBegin(), symbol.getEnd());
                String position = "[" + symbol.getBegin() + ":" + symbol.getEnd() + "]";
                String suggestion = EnWordCheckers.correct(wrong);//纠错
                result.add(new ErrorForm(wrong, position, suggestion));
            }
        }
        return result;
    }

}
