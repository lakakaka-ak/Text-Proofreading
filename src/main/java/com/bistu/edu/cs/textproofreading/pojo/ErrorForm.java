package com.bistu.edu.cs.textproofreading.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 文本参数封装
 *
 * @author lak
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorForm implements Serializable {

    /** 拼写错误 */
    private String word;

    /** 位置 */
    private String location;

    /** 建议 */
    private String suggest;

}
