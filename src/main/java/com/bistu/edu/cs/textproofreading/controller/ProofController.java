package com.bistu.edu.cs.textproofreading.controller;

import com.bistu.edu.cs.textproofreading.correct.english.SpellCorrect;
import com.bistu.edu.cs.textproofreading.pojo.ErrorForm;
import com.bistu.edu.cs.textproofreading.pojo.TextForm;
import com.bistu.edu.cs.textproofreading.result.Result;
import com.hankcs.algorithm.AhoCorasickDoubleArrayTrie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


/**
 * URL接口前缀为/proof
 */
@Slf4j
@RestController
@RequestMapping("/proof")
public class ProofController {

    /**
     * 检索textinfo中单词，将错误信息输出到表格中并显示
     */
    @PostMapping(value = "/check")
    public Result<Object> checkText(@RequestBody @Validated TextForm textForm) throws Exception {
        // 获取请求参数
        String text = textForm.getText();
        log.info(text);
        String textInfo=text.replaceAll("[,.!?:'\"]"," ");

        /**
         * 英文校对算法接口
         */
        //当前时间
        long startTime=System.currentTimeMillis();
        //TODO 构建英文校对对象
        SpellCorrect sc = new SpellCorrect();
        //TODO 构建ACDAT树
        AhoCorasickDoubleArrayTrie<String> acdat= sc.buildDAT();
        //TODO 根据加入到ACDAT的词典进行文本解析
        List<AhoCorasickDoubleArrayTrie.Hit<String>> hits = acdat.parseText(textInfo);
        //TODO 将hits倒序
        Collections.reverse(hits);
        //TODO 英文查错及纠错
        List<ErrorForm> result = sc.findWrongWords(textInfo, hits);
        //结束时间
        long endTime=System.currentTimeMillis();
        log.info("当前程序耗时："+(endTime-startTime)+"ms");
        return Result.ok(result);


       /* // 返回数据封装
        ErrorForm errorForm = new ErrorForm();
        errorForm.setWord("aa");
        errorForm.setLocation("[1,2]");
        errorForm.setSuggest("aaa");
        List<ErrorForm> List = new ArrayList<>();
        List.add(errorForm);
        return Result.ok(List);*/
    }
}


