package com.bistu.edu.cs.textproofreading.correct.chinese;

import com.bistu.edu.cs.textproofreading.constant.DicConstant;
import com.bistu.edu.cs.textproofreading.pojo.ErrorForm;
import com.bistu.edu.cs.textproofreading.pojo.en.Symbol;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.word.checker.util.EnWordCheckers;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * <p>
 * 此类用于中文拼音输入法造成的拼写错误的检查
 * 主要实现拼音词典的DAT储存
 * </p>
 *
 * @author lak
 */
public class PinyinCorrect {

    /**
     * 加载拼音词典到map
     *
     * @return treemap
     * @throws IOException
     */
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

    /**
     * 构建拼音词典 acdat
     *
     * @return dat
     * @throws Exception
     */
    public AhoCorasickDoubleArrayTrie<String> buildChDAT() throws Exception {
        // 加载词典
        Map<String, String> map = loadCh();
        // 建立AhoCorasickDoubleArrayTrie
        AhoCorasickDoubleArrayTrie<String> dat = new AhoCorasickDoubleArrayTrie<>();
        dat.build(map);
        return dat;
    }

    /**
     * 拼音纠错
     *
     * @param textInfo 文本
     * @param hits     匹配串
     * @return 错误单词 位置以及建议
     */
    public List<ErrorForm> findWrongPinyin(String textInfo, List<AhoCorasickDoubleArrayTrie.Hit<String>> hits) {
        List<ErrorForm> pinyin = new ArrayList<>();//返回错误单词集合
        int start = 0;//begin值
        int length = textInfo.length();//文本长度
        List<Integer> index = new ArrayList<>();//记录所以匹配上的正确单词的hits下标
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
        for (int i = 0; i < index.size(); i++) {
            String correct = hits.get(index.get(i)).value;//匹配到的正确串
            int correctLength = correct.length();//串长度
            String orgText = textInfo.substring(length - correctLength, length);//原本text的位置
            if (!orgText.equals(correct)) {
                String position = "[" + (length - correctLength) + ":" + length + "]";
                pinyin.add(new ErrorForm(orgText, position, correct));
            }
            length = length - correctLength;//更新text长度
        }
        return pinyin;
    }


}
