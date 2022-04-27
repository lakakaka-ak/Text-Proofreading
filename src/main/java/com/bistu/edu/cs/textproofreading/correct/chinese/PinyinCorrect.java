package com.bistu.edu.cs.textproofreading.correct.chinese;

import com.bistu.edu.cs.textproofreading.constant.DicConstant;
import com.bistu.edu.cs.textproofreading.pojo.ErrorForm;
import com.bistu.edu.cs.textproofreading.pojo.ch.SegmentForm;
import com.bistu.edu.cs.textproofreading.pojo.mysql.PinyinData;
import com.bistu.edu.cs.textproofreading.service.PinyinToneService;
import com.bistu.edu.cs.textproofreading.util.StringUtils;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum;
import com.github.houbb.pinyin.util.PinyinHelper;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.xm.Similarity;

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
     * Map主要用于存储键值（key)（value）对，根据键得到值，因此键不允许重复，但值允许重复；
     * pinyin -> 词
     *
     * @return treemap
     * @throws IOException
     */
    public Map<String, String> loadPinyin_tone() throws IOException {
        Map<String, String> map = new TreeMap<>();
        final String path = DicConstant.Pinyin_tone;
        BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            String[] word = line.split(StringUtils.COLON);
            List<String> pinyinList = StringUtil.splitToList(word[1]);
            map.put(pinyinList.get(0), word[0]);
        }
        br.close();
        return map;
    }

    /**
     * 加载无音调拼音词典到map
     * Map主要用于存储键值（key)（value）对，根据键得到值，因此键不允许重复，但值允许重复；
     * pinyin -> 词
     *
     * @return treemap
     * @throws IOException
     */
    public Map<String, String> loadPinyin() throws IOException {
        Map<String, String> map = new TreeMap<>();
        final String path = DicConstant.Pinyin;
        BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            String[] word = line.split(StringUtils.COLON);
            List<String> pinyinList = StringUtil.splitToList(word[1]);
            map.put(pinyinList.get(0), word[0]);
        }
        br.close();
        return map;
    }

    /**
     * 加载拼音词典到map
     * Map主要用于存储键值（key)（value）对，根据键得到值，因此key不允许重复，但值允许重复；
     * 词 -> pinyin
     *
     * @return treemap
     * @throws IOException
     */
    public Map<String, String> loadCh() throws IOException {
        Map<String, String> map = new TreeMap<>();
        final String path = DicConstant.Pinyin_tone;
        BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            String[] word = line.split(StringUtils.COLON);
            List<String> pinyinList = StringUtil.splitToList(word[1]);
            map.put(word[0], pinyinList.get(0));
        }
        br.close();
        return map;
    }

    /**
     * 加载成词语素到map
     * Map主要用于存储键值（key)（value）对，根据键得到值，因此key不允许重复，但值允许重复；
     *
     * @return treemap
     * @throws IOException
     */
    public Map<String, String> singleWord() throws IOException {
        Map<String, String> map = new TreeMap<>();
        final String path = DicConstant.YuSU;
        BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            map.put(line, line);
        }
        br.close();
        return map;
    }

    /**
     * 构建无声调拼音词典 acdat
     *
     * @return dat
     * @throws Exception
     */
    public AhoCorasickDoubleArrayTrie<String> buildPinyinDAT() throws Exception {
        // 加载词典
        Map<String, String> map = loadPinyin();
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
    public List<ErrorForm> findWrongPinyin(String textInfo, List<AhoCorasickDoubleArrayTrie.Hit<String>> hits) throws IOException {
        PinyinCorrect pc = new PinyinCorrect();
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
            if (!pc.loadCh().containsKey(orgText) && !orgText.equals(correct)) {
                String position = "[" + (length - correctLength) + ":" + length + "]";
                pinyin.add(new ErrorForm(orgText, position, correct));
            }
            length = length - correctLength;//更新text长度
        }
        return pinyin;
    }

    /**
     * 获取匹配好的hits下标
     *
     * @param hits 匹配串
     * @return 下标列表
     */
    public List<Integer> findSegPos(List<AhoCorasickDoubleArrayTrie.Hit<String>> hits) {
        Collections.reverse(hits);
        int start = 0;//begin值
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

        return index;
    }

}
