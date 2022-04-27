package com.bistu.edu.cs.textproofreading.pojo;



import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 构建TextForm类，用于接收前端传递的文本数据
 *
 * @author lak
 */
@Data
public class TextForm implements Serializable {

    /** 文本框中的文本内容，不允许为null，最多不能超过5000 */
    @NotNull
    @Length(max = 5000)
    private String text;

}
