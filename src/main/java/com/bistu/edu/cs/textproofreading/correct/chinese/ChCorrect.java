package com.bistu.edu.cs.textproofreading.correct.chinese;

import com.bistu.edu.cs.textproofreading.pojo.ErrorForm;
import com.bistu.edu.cs.textproofreading.pojo.ch.SegmentForm;
import com.bistu.edu.cs.textproofreading.pojo.mysql.PinyinData;
import com.bistu.edu.cs.textproofreading.service.PinyinToneService;
import com.bistu.edu.cs.textproofreading.util.StringUtils;
import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum;
import com.github.houbb.pinyin.util.PinyinHelper;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.xm.Similarity;

import java.util.ArrayList;
import java.util.List;

public class ChCorrect {

    private final PinyinToneService pinyinToneService;

    public ChCorrect(@Autowired PinyinToneService pinyinToneService) {
        this.pinyinToneService = pinyinToneService;
    }


    /**
     * <p>
     *     此方法用于解决拼音输入法中字词级别的查错与纠错
     * </p>
     *
     * @param text 要纠错的文本信息
     * @return 错误列表
     *
     */
    public List<ErrorForm> WrongInfo(String text) throws Exception {
        PinyinCorrect pinyinCorrect = new PinyinCorrect();
        AhoCorasickDoubleArrayTrie<String> dat = pinyinCorrect.buildPinyinDAT();
        List<ErrorForm> ef = new ArrayList<>();

        //TODO 使用Hanlp对文本进行分词
        HanLP.Config.ShowTermNature = false;//不显示词性
        String[] strings = text.split("[，,。；！？、]");//为了解决分词时将前后两句话的句尾和句首分在一起
        int textLength = 0;//记录每句话长度，用于错位置输出
        for (String sentence : strings) {
            String textInfo = StringUtils.clearCharacter(sentence);//清除标点符号
            List<Term> seg = HanLP.segment(textInfo);//分词
            //System.out.println(seg);

            //TODO 对于seg集合 如果有连续多个单个词出现则把他们存入集合中 获取他们的字，拼音，位置
            //TODO 对list集合进行查找，若位置相邻则添加到一起
            List<SegmentForm> s = new ArrayList<>();
            int index = 0;
            int length = 0;
            index += textLength;
            length += textLength;
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
            //System.out.println(s);

            //TODO 将拼音存储在数据库中
            //TODO 遍历SegmentForm 获取有调拼音
            // 在数据库中获取拼音对应的word 若存在则输出list集合
            // 对list集合里的建议做一个与原单词的最大匹配
            for (int i = 0; i < s.size(); i++) {
                String word = s.get(i).getWord();//获取错误词
                if (!pinyinToneService.wordExist(word)) {//排除一些单字词，例如’在‘’被‘’的‘等
                    String py = s.get(i).getPinyin();//有调拼音
                    if (pinyinToneService.pinyinExist(py)) {//排除大量单字词连在一起，数据库无法查询到，需要再划分
                        List<PinyinData> data = pinyinToneService.SearchByPinyin(py);
                        double similarity1 = Similarity.charBasedSimilarity(word, data.get(0).getWord());//字符串相似度
                        //System.out.println(data);
                        int k = 0;//data下标
                        for (int j = 1; j < data.size(); j++) {
                            String suggestion = data.get(j).getWord();
                            double similarity2 = Similarity.charBasedSimilarity(word, suggestion);
                            if (similarity2 > similarity1) {
                                similarity1 = similarity2;
                                k = j;
                            }
                        }
                        ef.add(new ErrorForm(word, s.get(i).getPos(), data.get(k).getWord()));
                        //System.out.println(word+" "+s.get(i).getPos()+" "+data.get(k).getWord());
                    } else {
                        // todo 获取相对位置
                        String[] curPos = s.get(i).getPos().split("[\\[\\]:]");
                        int end = Integer.parseInt(curPos[2]);
                        //System.out.println(word+":还需要再分割"+begin+":"+end);
                        //todo 用hit进行匹配 确定分词结构
                        String pinyin = PinyinHelper.toPinyin(s.get(i).getWord(), PinyinStyleEnum.NORMAL);//无声调拼音
                        List<AhoCorasickDoubleArrayTrie.Hit<String>> hits = dat.parseText(pinyin);
                        List<Integer> hitsPos = pinyinCorrect.findSegPos(hits);
                        for (int a = 0; a < hitsPos.size(); a++) {
                            int hitLen = hits.get(hitsPos.get(a)).value.length();//匹配串长度（注意是从后向前的匹配）
                            String p = PinyinHelper.toPinyin(hits.get(hitsPos.get(a)).value, PinyinStyleEnum.DEFAULT);//声调
                            List<PinyinData> data = pinyinToneService.SearchByPinyin(p);
                            double similarity1 = Similarity.charBasedSimilarity(word, data.get(0).getWord());
                            int k = 0;//data下标
                            for (int j = 1; j < data.size(); j++) {
                                String suggestion = data.get(j).getWord();
                                double similarity2 = Similarity.charBasedSimilarity(word, suggestion);
                                if (similarity2 > similarity1) {
                                    similarity1 = similarity2;
                                    k = j;
                                }
                            }
                            String wrong = word.substring((end - hitLen), end);
                            String position = "[" + (end - hitLen) + ":" + end + "]";
                            if (!pinyinToneService.wordExist(wrong)) {
                                ef.add(new ErrorForm(wrong, position, data.get(k).getWord()));
                            }
                            //System.out.println(data.get(k).getWord() + wrong);
                            end -= hitLen;
                        }

                    }

                }

            }
            textLength += sentence.length() + 1;
        }
        return ef;
    }
}
