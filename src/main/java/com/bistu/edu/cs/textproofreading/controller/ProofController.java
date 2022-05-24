package com.bistu.edu.cs.textproofreading.controller;

import com.bistu.edu.cs.textproofreading.correct.chinese.ChCorrect;
import com.bistu.edu.cs.textproofreading.correct.english.EnCorrect;
import com.bistu.edu.cs.textproofreading.pojo.ErrorForm;
import com.bistu.edu.cs.textproofreading.pojo.TextForm;
import com.bistu.edu.cs.textproofreading.result.Result;
import com.bistu.edu.cs.textproofreading.service.PinyinService;
import com.bistu.edu.cs.textproofreading.util.BooleanEN;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * URL接口前缀为/proof
 */
@Slf4j
@RestController
@RequestMapping("/proof")
public class ProofController {

//    private final PinyinService pinyinService;
//
//    public ProofController(@Autowired PinyinService pinyinService) {
//        this.pinyinService = pinyinService;
//    }

    /**
     * <p>
     * 检索textinfo中单词
     * 将错误信息输出到表格中并显示
     * </p>
     */
    @PostMapping(value = "/check")
    public Result<Object> checkText(@RequestBody @Validated TextForm textForm) throws Exception {
        /* 获取请求参数 */
        String text = textForm.getText();
        /* 返回校对结果（转Json） */
        List<ErrorForm> result = new ArrayList<>();
        log.info(text);

        /**
         * 英文校对算法
         */
        if (BooleanEN.isEnglishStr(text)) {
            //当前时间
            long startTime = System.currentTimeMillis();
            //TODO 构建英文校对对象
            EnCorrect ec = new EnCorrect();
            //TODO 英文查错及纠错
            List<ErrorForm> resultEN = ec.findWrongWords(text);
            result.addAll(resultEN);
            //结束时间
            long endTime = System.currentTimeMillis();
            log.info("英文校对程序耗时：" + (endTime - startTime) + "ms");
        }

        /**
         * 中文校对算法
         */
        if (BooleanEN.isContainChinese(text)) {
            //当前时间
            long startTime = System.currentTimeMillis();
            //TODO 构建中文校对对象
            ChCorrect cc = new ChCorrect();
            //TODO 中文查错及纠错
            List<ErrorForm> resultCH = cc.WrongInfo(text);
            result.addAll(resultCH);
            //结束时间
            long endTime = System.currentTimeMillis();
            log.info("中文校对程序耗时：" + (endTime - startTime) + "ms");
        }
        log.info("校对完成");
        return Result.ok(result);
    }

}


