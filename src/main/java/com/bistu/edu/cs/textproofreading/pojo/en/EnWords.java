package com.bistu.edu.cs.textproofreading.pojo.en;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 此类用于构建英文词频词典
 * 英文单词 包含单词及其频率
 */
@Data
@AllArgsConstructor
public class EnWords implements Serializable {

    /** 拼写错误 */
    private String word;

    /** 频率 */
    private Integer frequency;

}
