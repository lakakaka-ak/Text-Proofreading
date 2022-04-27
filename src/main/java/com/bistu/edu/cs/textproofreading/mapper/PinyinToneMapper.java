package com.bistu.edu.cs.textproofreading.mapper;

import com.bistu.edu.cs.textproofreading.pojo.mysql.PinyinData;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PinyinToneMapper {

    /**
     * 根据对应拼音唯一查询相关数据
     *
     * @param yin 拼音
     * @return 相关列表
     */
    @Results({
            @Result(column = "word", property = "word"),
            @Result(column = "yin", property = "pinyin")
    })
    @Select("select * from Pinyin_tone where yin = #{yin}")
    List<PinyinData> findPinyin(@Param("yin") String yin);

    /**
     * 根据词唯一查询相关数据
     *
     * @param word 相关的字、词
     * @return 相关列表
     */
    @Results({
            @Result(column = "word", property = "word"),
            @Result(column = "yin", property = "pinyin")
    })
    @Select("select * from Pinyin_tone where word = #{word}")
    List<PinyinData> findWord(@Param("word") String word);

    /**
     * 根据词和相对应拼音唯一查询相关数据
     *
     * @param word 相关字
     * @param yin  拼音
     * @return 相关列表
     */
    @Results({
            @Result(column = "word", property = "word"),
            @Result(column = "yin", property = "pinyin")
    })
    @Select("select * from Pinyin_tone where word = #{word} and yin = #{yin}")
    List<PinyinData> findByWordYin(@Param("word") String word,
                                   @Param("yin") String yin);

    /**
     * 根据词和相对应拼音唯一查询相关数据
     *
     * @param word 待插入字
     * @param yin  待插入拼音
     * @return 返回值如果是1，代表已经成功插入一条数据
     */
    @Insert("insert into Pinyin_tone(word, yin) values(#{word}, #{yin})")
    int insert(@Param("word") String word, @Param("yin") String yin);


    /**
     * 批量插入字典数据
     * @param baseData 插入数据
     * @return 返回是否插入
     */
    @Insert({
            "<script>" ,
            "insert into pinyin_tone(word, yin) values" ,
            "<foreach collection='baseData' item='item' separator=','>" ,
            "(#{item.word}, #{item.pinyin})" ,
            "</foreach>" ,
            "</script>"
    })
    boolean insertBatch(@Param(value = "baseData") List<PinyinData> baseData);


    /**
     * 删除词
     *
     * @param id id主键
     */
    @Delete("delete from Pinyin_tone where id = #{id}")
    void delete(int id);
}
