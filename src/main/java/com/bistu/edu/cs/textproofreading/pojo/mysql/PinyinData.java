package com.bistu.edu.cs.textproofreading.pojo.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 此类用于数据库的映射
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PinyinData {

    /**  自增主键 */
    private int id;

    /**  词语  */
    private String word;

    /**  拼音  */
    private String pinyin;
}
