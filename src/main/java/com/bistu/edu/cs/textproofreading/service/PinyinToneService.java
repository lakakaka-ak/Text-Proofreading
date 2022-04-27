package com.bistu.edu.cs.textproofreading.service;


import com.bistu.edu.cs.textproofreading.mapper.PinyinToneMapper;
import com.bistu.edu.cs.textproofreading.pojo.mysql.PinyinData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PinyinToneService {
    private static final Logger logger = LoggerFactory.getLogger(PinyinToneService.class);

    private final PinyinToneMapper pinyinToneMapper;

    public PinyinToneService(@Autowired PinyinToneMapper pinyinToneMapper){
        this.pinyinToneMapper = pinyinToneMapper;
    }


    /**
     * 检查重复
     * @param word 待查询词、字
     * @param pinyin 待查询拼音
     * @return true则存在，反之亦然
     */
    public boolean isExist(String word, String pinyin){
        //todo 检查是否为空
        if(word == null || pinyin == null){
            logger.info("输入数据不能为空");
            return false;
        }

        //todo 检查是否存在于数据库中
        if(SearchByWordPinyin(word, pinyin) != null){
            List<PinyinData> resList = pinyinToneMapper.findByWordYin(word, pinyin);
            for(PinyinData i : resList){
                if(i.getWord().equals(word) && i.getPinyin().equals(pinyin)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断数据库是否存在某个词
     * @param word 词
     * @return 是否存在
     */
    public boolean wordExist(String word){
        List<PinyinData> s = SearchByWord(word);
        if(s.isEmpty()){
           return false;
       }
       return true;
    }

    /**
     * 判断数据库是否存在某个拼音
     * @param pinyin 拼音
     * @return 是否存在
     */

    public boolean pinyinExist(String pinyin){
        List<PinyinData> yin =SearchByPinyin(pinyin);
        if(yin.size()==0){
            return false;
        }
        return true;
    }


    /**
     * 向数据库中添加信息
     * @param word 待输入词、字
     * @param pinyin 待输入拼音
     * @return true则表示成功插入，反之亦然
     */
    public boolean addDictInfo(String word, String pinyin){
        //TODO 非空检查
        if(word == null || pinyin == null){
            logger.info("输入数据不能为空");
            return false;
        }
        //todo 数据去重
        if(isExist(word, pinyin)){
            logger.error("数据库中已经存在相应数据");
            return false;
        }
        //todo 数据插入
        int result = pinyinToneMapper.insert(word, pinyin);
        if(result != 1){
            logger.error("数据插入失败");
            return false;
        }else{
            logger.info("数据插入成功");
            return true;
        }
    }

    public boolean addDictBatch(List<PinyinData> list){
        //TODO 非空检查
        if(list.isEmpty()){
            logger.error("插入数据不可以为空哦！");
            return false;
        }
        //TODO 数据插入
        pinyinToneMapper.insertBatch(list);
        return true;
    }

    /**
     * 根据相关字词查询对应数据
     * @param word 待查询词、字
     * @return 返回查询结果List类型
     */
    public List<PinyinData> SearchByWord(String word){
        //todo 检查是否为空
        if(word == null){
            logger.error("查询字段不能为空");
            return null;
        }

        //TODO 判断数据库中是否存在此元素
        if(pinyinToneMapper.findWord(word) != null){
            return pinyinToneMapper.findWord(word);
        }else{
            logger.error("数据库中无此项数据");
            return null;
        }
    }

    /**
     * 根据拼音查询对应数据
     * @param pinyin 待查询拼音
     * @return 查询结果
     */
    public List<PinyinData> SearchByPinyin(String pinyin){
        //todo 检查是否为空
        if(pinyin == null){
            logger.error("查询字段不能为空");
            return null;
        }

        //TODO 判断数据库中是否存在此元素
        if(pinyinToneMapper.findPinyin(pinyin) != null){
            return pinyinToneMapper.findPinyin(pinyin);
        }else{
            logger.error("数据库中无此项数据");
            return null;
        }
    }

    /**
     * 根据词，拼音查询对应数据
     * @param word 词
     * @param pinyin 拼音
     * @return 返回对应列表值
     */
    public List<PinyinData> SearchByWordPinyin(String word, String pinyin){
        //todo 检查是否为空
        if(word == null || pinyin == null){
            logger.error("查询字段不能为空");
            return null;
        }

        //TODO 判断数据库中是否存在此元素
        if(pinyinToneMapper.findByWordYin(word, pinyin) != null){
            return pinyinToneMapper.findByWordYin(word, pinyin);
        }else{
            logger.error("数据库中无此项数据");
            return null;
        }
    }

}
