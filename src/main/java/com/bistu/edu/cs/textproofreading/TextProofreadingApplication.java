package com.bistu.edu.cs.textproofreading;


import com.bistu.edu.cs.textproofreading.constant.DicConstant;
import com.bistu.edu.cs.textproofreading.correct.chinese.ChCorrect;
import com.bistu.edu.cs.textproofreading.correct.chinese.PinyinDictionary;
import com.bistu.edu.cs.textproofreading.correct.chinese.PinyinMap;
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

    public static void main(String[] args) throws IOException {

        SpringApplication.run(TextProofreadingApplication.class, args);
    }

//    @SneakyThrows
//    @Override
//    public void run(String... args) throws Exception {
//        String text = "重庆是中国的四大火鹿之一，因风景休丽，是人间天棠！";
//        long time1 = System.currentTimeMillis();
//        ChCorrect cc = new ChCorrect(pinyinService);
//        PinyinDictionary pd = new PinyinDictionary();
//        AhoCorasickDoubleArrayTrie<String> dat = pd.buildPinyinDAT();
//        long time2 = System.currentTimeMillis();
//        logger.info("中文词典DAT构造：" + (time2 - time1) + "ms");
//
//        long time3 = System.currentTimeMillis();
//        Map<String, String> definePinyinDic = pd.DefinePinyinDic();
//        Map<String, String> pinyinMap = PinyinMap.pyMap;
//        long time4 = System.currentTimeMillis();
//        logger.info("中文用户自定义词典加载：" + (time4 - time3) + "ms");
//        List<ErrorForm> ef = new ArrayList<>();
//
//        //TODO 使用Hanlp对文本进行分词
//        HanLP.Config.ShowTermNature = false;//不显示词性
//        String textInfo = StringUtils.replaceCharacter(text);
//        List<Term> seg = SpeedTokenizer.segment(textInfo);//使用极速分词，速度快于标准分词
//
//        //TODO 对于seg集合 如果有连续多个单个词出现则把他们存入集合中 获取他们的字，拼音，位置
//        //TODO 对list集合进行查找，若位置相邻则添加到一起
//        long time5 = System.currentTimeMillis();
//        List<SegmentForm> s = cc.findWrongWords(seg);
//        long time6 = System.currentTimeMillis();
//        logger.info("中文文本查错：" + (time6 - time5) + "ms");
//        System.out.println(s);
//        //TODO 将拼音存储在数据库中
//        //TODO 遍历SegmentForm 获取有调拼音
//        // 在数据库中获取拼音对应的word 若存在则输出list集合
//        // 对list集合里的建议做一个与原单词的最大匹配
//        long time7 = System.currentTimeMillis();
//        for (int i = 0; i < s.size(); i++) {
//            String word = s.get(i).getWord();//获取错误词
//            if (!pinyinService.wordExist(word) && !definePinyinDic.containsKey(word)) {//排除一些单字词，例如’在‘’被‘’的‘等
//                String py = s.get(i).getPinyin();//拼音
//                // todo 获取相对位置
//                    String[] curPos = s.get(i).getPos().split("[\\[\\]:]");
//                    int end = Integer.parseInt(curPos[2]);
//                //todo 用hit进行匹配 确定分词结构
//                    List<AhoCorasickDoubleArrayTrie.Hit<String>> hits = dat.parseText(py);
//                    List<Integer> hitsPos = cc.findSegPos(hits);
//                    for (int a = 0; a < hitsPos.size(); a++) {
//                        int hitLen = hits.get(hitsPos.get(a)).value.length();//匹配串长度（注意是从后向前的匹配）
//                        String wrong = text.substring((end - hitLen), end);
//                        String p = PinyinHelper.toPinyin(wrong, PinyinStyleEnum.NORMAL);//声调
//                        //todo 数据库与自定义库共同查询
//                        long time10=System.currentTimeMillis();
////                        List<String> data = pinyinService.SearchWordByPinyin(p);
//                        List<String> data = StringUtils.str2List(pinyinMap.get(p));
//                        List<String> define = pd.getKeysByStream(definePinyinDic, p);
//                        data.addAll(define);
//                        long time11=System.currentTimeMillis();
//                        System.out.println("数据查询"+(time11-time10)+"ms");
//                        //若字典中不存在则进行纠错
//                        long time12=System.currentTimeMillis();
//                        if (!data.contains(wrong)) {
//                            String position = "[" + (end - hitLen) + ":" + end + "]";
//                            int k = cc.getSuggestion(wrong, data);
//                            ef.add(new ErrorForm(wrong, position, data.get(k)));
//                            long time13=System.currentTimeMillis();
//                            System.out.println("字词建议"+(time13-time12)+"ms");
//                        }
//                        end -= hitLen;
//                    }
//                }
//            }
//            long time8 = System.currentTimeMillis();
//            logger.info("中文文本纠错：" + (time8 - time7) + "ms");
//            logger.info("总时间："+(time8-time1)+"ms");
//            System.out.println(ef);
//        }
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