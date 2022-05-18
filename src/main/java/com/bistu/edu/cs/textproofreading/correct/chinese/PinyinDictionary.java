package com.bistu.edu.cs.textproofreading.correct.chinese;

import com.bistu.edu.cs.textproofreading.constant.DicConstant;
import com.bistu.edu.cs.textproofreading.util.StringUtils;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.collection.trie.DoubleArrayTrie;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.notification.RunListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 此类用于中文拼音输入法造成的拼写错误的检查
 * 主要实现拼音词典的DAT储存
 * </p>
 *
 * @author lak
 */
@Slf4j
@RunListener.ThreadSafe
public class PinyinDictionary {

    /** 拼音词典DAT加载 */
    private static final AhoCorasickDoubleArrayTrie<String> chDat = new AhoCorasickDoubleArrayTrie<>();

    /** 用户自定义词典加载 */
    private static final Map<String,String> defineMap = new HashMap<>();

    /** 拼音词典预加载 */
    static {
        TreeMap<String, String> map = new TreeMap<>();
        try {
            final String path = DicConstant.Pinyin;
            BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                String[] word = line.split(StringUtils.COLON);
                List<String> pinyinList = StringUtil.splitToList(word[1]);
                map.put(pinyinList.get(0), word[0]);
            }
            br.close();
        }
        catch (IOException e){
            log.error("中文词典加载错误");
        }
        PinyinDictionary.chDat.build(map);
    }

    /**
     * 构建无声调拼音词典 acdat
     *
     * @return dat
     */
    public AhoCorasickDoubleArrayTrie<String> buildPinyinDAT() throws Exception {
        return PinyinDictionary.chDat;
    }


    /**
     * 用户自定义词典预加载
     * 加载用户自定义拼音词典到map
     * Map主要用于存储键值（key)（value）对，根据键得到值，因此key不允许重复，但值允许重复；
     * 词 -> pinyin（无声调）
     *
     * @return hashmap
     */
    public static Map<String, String> DefinePinyinDic() throws IOException {
        if(defineMap.isEmpty()){
            final String path = DicConstant.DefinePy;
            BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                String[] word = line.split(StringUtils.COLON);
                defineMap.put(word[0], word[1]);
            }
            br.close();
        }

        return defineMap;
    }

    public <K, V> List<String> getKeysByStream(Map<String, String> map, V value) {
        return map.entrySet()
                .stream()
                .filter(kvEntry -> Objects.equals(kvEntry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }


    /**
     * 加载拼音词典到map
     * Map主要用于存储键值（key)（value）对，根据键得到值，因此键不允许重复，但值允许重复；
     * pinyin_tone -> 词
     *
     * @return treemap
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
     * 词 -> pinyin（无声调）
     *
     * @return treemap
     */
    public Map<String, String> loadCh() throws IOException {
        Map<String, String> map = new TreeMap<>();
        final String path = DicConstant.Pinyin;
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

    public List<String> singleWords() throws IOException {
        List<String> sw = new ArrayList<>();
        final String path = "static/dic/ch/single_words";
        BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)), StandardCharsets.UTF_8));
        String line;
        while ((line = br.readLine()) != null) {
            sw.add(line);
        }
        br.close();
        return sw;
    }


}
