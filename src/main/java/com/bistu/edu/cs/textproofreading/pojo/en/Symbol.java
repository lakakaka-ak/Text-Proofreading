package com.bistu.edu.cs.textproofreading.pojo.en;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *     此类用于记录 acdat.parseText 后的数据
 *     书写相关规则对匹配串进行再解析 直接输出错误位置信息
 * </p>
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Symbol {

    /** begin */
    private int begin;

    /** end*/
    private int end;

    /** DFS递归次数*/
    private int num;



}
