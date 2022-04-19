package com.bistu.edu.cs.textproofreading.correct.english;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 此类用于实现英文单词的纠错
 * 采用编辑距离 对英文单词进行部分增删改
 * 得到的新单词在词典中进行匹配并非返回使用频率最高的单词作为修改建议
 * </p>
 *
 * @author lak
 */
public class EnWordsCheckers {

    /**
     * 构建出以后单词的所有可能谬误状况
     *
     * @param word 输出单词
     * @return 返回后果集合
     */
    private List<String> dis1(String word) {
        List<String> result = new LinkedList<>();
        //删除一个字母
        for (int i = 0; i < word.length(); ++i) {
            result.add(word.substring(0, i) + word.substring(i + 1));
        }
        //字母交换顺序
        for (int i = 0; i < word.length() - 1; ++i) {
            result.add(word.substring(0, i) + word.substring(i + 1, i + 2) + word.substring(i, i + 1) + word.substring(i + 2));
        }
        //在i处修改一个字母
        for (int i = 0; i < word.length(); ++i) {
            for (char c = 'a'; c <= 'z'; ++c) {
                result.add(word.substring(0, i) + c + word.substring(i + 1));
            }
        }
        //在i处增加一个字母
        for (int i = 0; i <= word.length(); ++i) {
            for (char c = 'a'; c <= 'z'; ++c) {
                result.add(word.substring(0, i) + c + word.substring(i));
            }
        }
        return result;
    }

    /**
     * 在编辑距离为1的基础上在对单词进行修改，使得编辑距离为2
     *
     * @param result
     * @return
     */
    private List<String> dis2(List<String> result) {
        List<String> dis2result = new LinkedList<>();
        for (String result1 : result) {
            List<String> dis2 = dis1(result1);
            for (String dis : dis2) {
                dis2result.add(dis);
            }
        }
        return dis2result;
    }


    /**
     * 获取修改建议集合
     * @param word
     * @return
     * @throws IOException
     */

    public List<String> getSuggestions(String word) throws IOException {
        SpellCorrect sc = new SpellCorrect();
        Set<String> dictionary = sc.loadDictionary("static/dic/en");
        List<String> first = dis1(word);//distance1
//        List<String> second = dis2(first);//distance2耗时太久
        List<String> suggestions = new ArrayList<>();
        for (String option : first) {
//            if (sc.loadEn().containsKey(option)) {
//                suggestions.add(option);
//            }//速度更慢
            if (dictionary.contains(option)) {
                suggestions.add(option);
            }
        }
        return suggestions;
    }

    public static void main(String args[]) throws IOException {
        long startTime=System.currentTimeMillis();
        EnWordsCheckers ewc = new EnWordsCheckers();
        List<String> suggestion = ewc.getSuggestions("appl");
        long endTime=System.currentTimeMillis();
        System.out.println(suggestion);
        System.out.println("当前程序耗时："+(endTime-startTime)+"ms");
    }
}
