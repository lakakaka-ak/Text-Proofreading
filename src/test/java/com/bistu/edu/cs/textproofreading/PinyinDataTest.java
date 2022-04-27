package com.bistu.edu.cs.textproofreading;

import com.bistu.edu.cs.textproofreading.mapper.PinyinToneMapper;
import com.bistu.edu.cs.textproofreading.pojo.mysql.PinyinData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class PinyinDataTest {


    @Autowired
    private PinyinToneMapper pinyinToneMapper;

    @Test
    @Rollback
    public void test() throws Exception {
        pinyinToneMapper.insert("实着", "shí zhe");
        List<PinyinData> p = pinyinToneMapper.findPinyin("shí zhe");
//        pinyinToneMapper.delete(1);
//        pinyinToneMapper.delete(2);
//        Assert.assertEquals(null, p);

    }


}
