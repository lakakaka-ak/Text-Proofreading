package com.bistu.edu.cs.textproofreading.correct.english;

import com.bistu.edu.cs.textproofreading.constant.DicConstant;
import com.github.houbb.heaven.annotation.ThreadSafe;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import lombok.extern.slf4j.Slf4j;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * <p>
 * 此类主要实现英文词典的DAT储存
 * </p>
 *
 * @author lak
 */


@Slf4j
@ThreadSafe
public class EnDictionary {

    /** 英文词典加载 */
    private static final AhoCorasickDoubleArrayTrie<String> enDat = new AhoCorasickDoubleArrayTrie<>();

    static {
        Set<String> dictionary = new TreeSet<>();
        try {
            dictionary = loadDictionary(DicConstant.En_DIC);
        }
        catch (IOException e) {
           log.error("英文词典加载错误");
        }
        Map<String,String> map = new TreeMap<>();
        for(String key :dictionary){
            map.put(key,key);
        }
        EnDictionary.enDat.build(map);
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
        return EnDictionary.enDat;
    }


    /**
     * 词典加载
     *
     * @param path 词典的路径
     * @return 将字符串类型的词典转化成TreeSet结果
     */
    public static Set<String> loadDictionary(String path) throws IOException {
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
        final String path = DicConstant.En_Fre;
        BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            String[] word = line.split(",");
            map.put(word[0], Integer.parseInt(word[1]));
        }
        br.close();
        return map;
    }


}
