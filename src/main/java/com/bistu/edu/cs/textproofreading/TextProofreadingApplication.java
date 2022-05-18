package com.bistu.edu.cs.textproofreading;


import com.bistu.edu.cs.textproofreading.constant.DicConstant;
import com.bistu.edu.cs.textproofreading.pojo.ErrorForm;
import com.bistu.edu.cs.textproofreading.pojo.ch.SegmentForm;
import com.bistu.edu.cs.textproofreading.pojo.mysql.PinyinData;
import com.bistu.edu.cs.textproofreading.service.PinyinService;
import com.bistu.edu.cs.textproofreading.util.StringUtils;
import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum;
import com.github.houbb.pinyin.util.PinyinHelper;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;
import lombok.SneakyThrows;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xm.Similarity;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


@SpringBootApplication
@MapperScan("com.bistu.edu.cs.textproofreading.mapper")
//implements CommandLineRunner
public class TextProofreadingApplication  {
//    private final Logger logger = LoggerFactory.getLogger(TextProofreadingApplication.class);
//
//    private final PinyinService pinyinService;
//
//    public TextProofreadingApplication(@Autowired PinyinService pinyinService) {
//        this.pinyinService = pinyinService;
//    }
//

    public static void main(String[] args) throws IOException {

        SpringApplication.run(TextProofreadingApplication.class, args);
    }

//    @SneakyThrows
//    @Override
//    public void run(String... args) throws Exception {
//        String text = "中国各地都要举行丰符多猜的几念活动";
//        PinyinCorrect pinyinCorrect = new PinyinCorrect();
//        AhoCorasickDoubleArrayTrie<String> dat = pinyinCorrect.buildPinyinDAT();
//        Map<String, String> definePinyinDic = pinyinCorrect.DefinePinyinDic();
//        List<ErrorForm> ef = new ArrayList<>();
//
//        //TODO 使用Hanlp对文本进行分词
//        HanLP.Config.ShowTermNature = false;//不显示词性
//        String[] strings = text.split("[，,。；！？、“\"]");//为了解决分词时将前后两句话的句尾和句首分在一起
//        int textLength = 0;//记录每句话长度，用于错位置输出
//        for (String sentence : strings) {
//            String textInfo = StringUtils.clearCharacter(sentence);//清除标点符号
//            List<Term> seg = SpeedTokenizer.segment(textInfo);//使用极速分词，速度快于标准分词
//            //List<Term> seg = HanLP.segment(textInfo);//分词
//            System.out.println(seg);
//
//            //TODO 对于seg集合 如果有连续多个单个词出现则把他们存入集合中 获取他们的字，拼音，位置
//            //TODO 对list集合进行查找，若位置相邻则添加到一起
//            List<SegmentForm> s = new ArrayList<>();
//            int index = 0;
//            int length = 0;
//            index += textLength;
//            length += textLength;
//            for (int i = 0; i < seg.size() - 1; i++) {
//                if (seg.get(i).length() > 1) {
//                    length += seg.get(i).length();
//                    index = length;
//                } else {
//                    StringBuffer sb = new StringBuffer();
//                    while (i < seg.size() && seg.get(i).length() == 1) {
//                        sb.append(seg.get(i).word);
//                        length += seg.get(i).length();
//                        i++;
//                    }
//                    String pos = "[" + index + ":" + length + "]";
//                    String pinyin = PinyinHelper.toPinyin(sb.toString(), PinyinStyleEnum.NORMAL);
//                    s.add(new SegmentForm(sb.toString(), pinyin, pos));
//                    i--;
//                }
//            }
//            System.out.println(s);
//
//            //TODO 将拼音存储在数据库中
//            //TODO 遍历SegmentForm 获取有调拼音
//            // 在数据库中获取拼音对应的word 若存在则输出list集合
//            // 对list集合里的建议做一个与原单词的最大匹配
//            for (int i = 0; i < s.size(); i++) {
//                String word = s.get(i).getWord();//获取错误词
//                if (!pinyinService.wordExist(word) && !definePinyinDic.containsKey(word)) {//排除一些单字词，例如’在‘’被‘’的‘等
//                    String py = s.get(i).getPinyin();//拼音
//                    if (pinyinService.pinyinExist(py)) {//排除大量单字词连在一起，数据库无法查询到，需要再划分
//                        List<PinyinData> data = pinyinService.SearchByPinyin(py);
//                        double similarity1 = Similarity.charBasedSimilarity(word, data.get(0).getWord());//字符串相似度
//                        System.out.println(data);
//                        int k = 0;//data下标
//                        for (int j = 1; j < data.size(); j++) {
//                            String suggestion = data.get(j).getWord();
//                            double similarity2 = Similarity.charBasedSimilarity(word, suggestion);
//                            if (similarity2 > similarity1) {
//                                similarity1 = similarity2;
//                                k = j;
//                            }
//                        }
//                        ef.add(new ErrorForm(word, s.get(i).getPos(), data.get(k).getWord()));
//                        System.out.println(word + " " + s.get(i).getPos() + " " + data.get(k).getWord());
//                    } else {
//                        // todo 获取相对位置
//                        String[] curPos = s.get(i).getPos().split("[\\[\\]:]");
//                        int end = Integer.parseInt(curPos[2]);
//                        System.out.println(word + ":还需要再分割");
//                        //todo 用hit进行匹配 确定分词结构
//                        String pinyin = PinyinHelper.toPinyin(s.get(i).getWord(), PinyinStyleEnum.NORMAL);//无声调拼音
//                        List<AhoCorasickDoubleArrayTrie.Hit<String>> hits = dat.parseText(pinyin);
//                        List<Integer> hitsPos = pinyinCorrect.findSegPos(hits);
//                        for (int a = 0; a < hitsPos.size(); a++) {
//                            int hitLen = hits.get(hitsPos.get(a)).value.length();//匹配串长度（注意是从后向前的匹配）
//                            String wrong = text.substring((end - hitLen), end);
//                            String p = PinyinHelper.toPinyin(wrong, PinyinStyleEnum.NORMAL);//声调
//                            //todo 数据库与自定义库共同查询
//                            List<String> data = pinyinService.SearchWordByPinyin(p);
//                            List<String> define = pinyinCorrect.getKeysByStream(definePinyinDic, p);
//                            data.addAll(define);
//                            //若字典中不存在则进行纠错
//                            if (!data.contains(wrong)) {
//                                String position = "[" + (end - hitLen) + ":" + end + "]";
//                                double similarity1 = Similarity.charBasedSimilarity(wrong, data.get(0));
//                                int k = 0;//data下标
//                                for (int j = 1; j < data.size(); j++) {
//                                    String suggestion = data.get(j);
//                                    double similarity2 = Similarity.charBasedSimilarity(wrong, suggestion);
//                                    if (similarity2 > similarity1) {
//                                        similarity1 = similarity2;
//                                        k = j;
//                                    }
//                                }
//                                ef.add(new ErrorForm(wrong, position, data.get(k)));
//                            }
//                            end -= hitLen;
//                        }
//
//                    }
//
//                }
//
//            }
//            textLength += sentence.length() + 1;
//        }
//        System.out.println(ef);
//    }
}


//        List<PinyinData> baseData = new ArrayList<>();
//        final String path = DicConstant.Pinyin;
//        logger.info("开始插入数据啦！嘻嘻嘻");
//        BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)), StandardCharsets.UTF_8));
//        String line;
//        while ((line = br.readLine()) != null) {
//            String[] word = line.split(StringUtils.COLON);
//            PinyinData singleData = new PinyinData();
//            singleData.setPinyin(word[1]);
//            singleData.setWord(word[0]);
//            baseData.add(singleData);
//        }
//        pinyinService.addDictBatch(baseData);
//        logger.info("插入数据完成啦，哈哈哈！");
//        br.close();
//    }
//}