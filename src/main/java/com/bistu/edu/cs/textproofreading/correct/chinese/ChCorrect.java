package com.bistu.edu.cs.textproofreading.correct.chinese;

import com.bistu.edu.cs.textproofreading.pojo.ErrorForm;
import com.bistu.edu.cs.textproofreading.pojo.ch.SegmentForm;
import com.bistu.edu.cs.textproofreading.pojo.mysql.PinyinData;
import com.bistu.edu.cs.textproofreading.service.PinyinService;
import com.bistu.edu.cs.textproofreading.service.PinyinToneService;
import com.bistu.edu.cs.textproofreading.util.StringUtils;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum;
import com.github.houbb.pinyin.util.PinyinHelper;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.xm.Similarity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
public class ChCorrect {

//    private final PinyinService pinyinService;
//
//    public ChCorrect(@Autowired PinyinService pinyinService) {
//        this.pinyinService = pinyinService;
//    }
//

    /**
     * <p>
     * 此方法用于解决拼音输入法中字词级别的查错与纠错
     * </p>
     *
     * @param text 要纠错的文本信息
     * @return 错误列表
     */
    public List<ErrorForm> WrongInfo(String text) throws Exception {
        long time1 = System.currentTimeMillis();
        PinyinDictionary pd = new PinyinDictionary();
        Map<String, String> definePinyinDic = pd.DefinePinyinDic();
        long time2 = System.currentTimeMillis();
        log.info("中文词典加载：" + (time2 - time1) + "ms");
        List<ErrorForm> ef = new ArrayList<>();

        //TODO 使用Hanlp对文本进行分词
        long time3 = System.currentTimeMillis();
        HanLP.Config.ShowTermNature = false;//不显示词性
        String textInfo = StringUtils.replaceCharacter(text);
        List<Term> seg = SpeedTokenizer.segment(textInfo);//使用极速分词，速度快于标准分词
        //TODO 对于seg集合 如果有连续多个单个词出现则把他们存入集合中 获取他们的字，拼音，位置
        //TODO 对list集合进行查找，若位置相邻则添加到一起
        List<SegmentForm> s = findWrongWords(seg);
        long time4 = System.currentTimeMillis();
        log.info("中文文本查错：" + (time4 - time3) + "ms");

        //TODO 将拼音存储在数据库中
        //TODO 遍历SegmentForm 获取有调拼音
        // 在数据库中获取拼音对应的word 若存在则输出list集合
        // 对list集合里的建议做一个与原单词的最大匹配
        long time5 = System.currentTimeMillis();
        for (int i = 0; i < s.size(); i++) {
            String word = s.get(i).getWord();//获取错误词
//            !pinyinService.wordExist(word)
            if (!PinyinMap.pyDic.containsKey(word) && !definePinyinDic.containsKey(word)) {//排除一些单字词，例如’在‘’被‘’的‘等
                String py = s.get(i).getPinyin();//拼音
                // todo 获取相对位置
                String[] curPos = s.get(i).getPos().split("[\\[\\]:]");
                int end = Integer.parseInt(curPos[2]);
                //todo 用hit进行匹配 确定分词结构
                List<AhoCorasickDoubleArrayTrie.Hit<String>> hits = PinyinMap.pyDat.parseText(py);
                List<Integer> hitsPos = findSegPos(hits);
                for (int a = 0; a < hitsPos.size(); a++) {
                    String suggestions = hits.get(hitsPos.get(a)).value;
                    List<String> data = StringUtil.splitToList(suggestions);
                    int hitLen = data.get(0).length();//匹配串长度（注意是从后向前的匹配）
                    String wrong = text.substring((end - hitLen), end);
                    String p = PinyinHelper.toPinyin(wrong, PinyinStyleEnum.NORMAL);//声调
                    //todo 数据库与自定义库共同查询
//                    List<String> data = pinyinService.SearchWordByPinyin(p);
                    List<String> define = pd.getKeysByStream(definePinyinDic, p);
                    data.addAll(define);
                    //若字典中不存在则进行纠错
                    if (!data.contains(wrong)) {
                        String position = "[" + (end - hitLen) + ":" + end + "]";
                        int k = getSuggestion(wrong, data);
                        ef.add(new ErrorForm(wrong, position, data.get(k)));
                    }
                    end -= hitLen;
                }
            }
        }
        long time6 = System.currentTimeMillis();
        log.info("中文文本纠错：" + (time6 - time5) + "ms");
        return ef;
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
            while (k < hits.size() && curEnd == hits.get(k).end) {
                if (hits.get(k).begin < minBegin) {
                    minBegin = hits.get(k).begin;
                    curHits = k;//更新下标
                }
                k++;
            }
            index.add(curHits);
            start = minBegin;
            if (minBegin == 0) {
                break;
            }
            i = k;
        }

        return index;
    }

    public List<SegmentForm> findWrongWords(List<Term> seg) {
        List<SegmentForm> s = new ArrayList<>();
        int start = 0;
        int end = 0;
        for (int i = 0; i < seg.size() - 1; i++) {
            if (seg.get(i).length() > 1) {
                end += seg.get(i).length();
                start = end;
            } else {
                StringBuffer sb = new StringBuffer();
                while (i < seg.size() && seg.get(i).length() == 1) {
                    if (seg.get(i).word.equals(" ")) {
                        i++;
                        break;
                    } else {
                        sb.append(seg.get(i).word);
                        end += 1;
                        i++;
                    }
                }
                i--;
                if (start != end) {
                    String pos = "[" + start + ":" + end + "]";
                    String pinyin = PinyinHelper.toPinyin(sb.toString(), PinyinStyleEnum.NORMAL);
                    s.add(new SegmentForm(sb.toString(), pinyin, pos));
                }
                if (seg.get(i).word.equals(" ")) {
                    end += 1;
                }
                start = end;
            }
        }
        return s;
    }

    /**
     * 基于charBasedSimilarity进行纠错
     */
    public int getSuggestion(String wrong, List<String> data) {
        double similarity1 = Similarity.charBasedSimilarity(wrong, data.get(0));//字符串相似度
        int k = 0;//data下标
        for (int j = 1; j < data.size(); j++) {
            String suggestion = data.get(j);
            double similarity2 = Similarity.charBasedSimilarity(wrong, suggestion);
            if (similarity2 > similarity1) {
                similarity1 = similarity2;
                k = j;
            }
        }
        return k;
    }

}
