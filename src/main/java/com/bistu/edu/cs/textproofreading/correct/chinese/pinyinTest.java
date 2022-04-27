package com.bistu.edu.cs.textproofreading.correct.chinese;


import com.bistu.edu.cs.textproofreading.pojo.ch.SegmentForm;
import com.bistu.edu.cs.textproofreading.pojo.mysql.PinyinData;
import com.bistu.edu.cs.textproofreading.service.PinyinToneService;
import com.bistu.edu.cs.textproofreading.util.StringUtils;
import com.github.houbb.pinyin.util.PinyinHelper;


import com.hankcs.hanlp.HanLP;

import com.hankcs.hanlp.seg.common.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.*;

/**
 * 此类用于拼音测试
 */

public class pinyinTest implements CommandLineRunner {

    private final PinyinToneService pinyinToneService;

    public pinyinTest(@Autowired PinyinToneService pinyinToneService) {
        this.pinyinToneService = pinyinToneService;
    }


    @Override
    public void run(String... args) throws Exception {
        //        String text = "抗战末期，一群溃败下来的国民党士兵聚集在西南小镇的收容所里，他们被几年来国土渐次沦丧弄得毫无斗志，只想苟且偷生。" ;
        String text = "你号";

        //TODO 使用Hanlp对文本进行分词
        HanLP.Config.ShowTermNature = false;//不显示词性
        String textInfo = StringUtils.clearCharacter(text);//清除标点符号
        List<Term> seg = HanLP.segment(textInfo);//分词
        System.out.println(seg);

        //TODO 对于seg集合 如果有连续多个单个词出现则把他们存入集合中 获取他们的字，拼音，位置
        //TODO 对list集合进行查找，若位置相邻则添加到一起
        List<SegmentForm> s = new ArrayList<>();
        int index = 0;
        int length = 0;
        for (int i = 0; i < seg.size() - 1; i++) {
            if (seg.get(i).length() > 1) {
                length += seg.get(i).length();
                index = length;
            } else {
                StringBuffer sb = new StringBuffer();
                while (i < seg.size() && seg.get(i).length() == 1) {
                    sb.append(seg.get(i).word);
                    length += seg.get(i).length();
                    i++;
                }
                String pos = "[" + index + ":" + length + "]";
                String pinyin = PinyinHelper.toPinyin(sb.toString());
                s.add(new SegmentForm(sb.toString(), pinyin, pos));
                i--;
            }
        }
        System.out.println(s);

        //TODO 将拼音存储在数据库中
        //TODO 遍历SegmentForm 获取有调拼音
        for(int i=0 ;i<s.size();i++){
            String py = s.get(i).getPinyin();//有调拼音
            List<PinyinData> data = pinyinToneService.SearchByPinyin(py);
            System.out.println(data);
        }
        // 在数据库中获取拼音对应的word 若存在则输出list集合 若不存在 则获取其无调的拼音 在数据库中查找
        // 对list集合里的建议做一个与原单词的最大匹配

    }
}
