package com.bistu.edu.cs.textproofreading.pojo.ch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 此类用于封装中文错误词语分词后出现连续多个单字词的记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SegmentForm {

    /** 单字词 */
    private String word;

    /** 词pinyin */
    private String pinyin;

    /**  词位置 */
    private String pos;

}
