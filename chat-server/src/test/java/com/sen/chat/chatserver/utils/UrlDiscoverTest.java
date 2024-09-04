package com.sen.chat.chatserver.utils;

import com.sen.chat.chatserver.entity.message.UrlInfo;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @description:
 * @author: sensen
 * @date: 2023/7/31 23:04
 */
class UrlDiscoverTest {

    @Test
    void getTitle() {
        CommonUrlDiscover commonUrlDiscover = new CommonUrlDiscover();
//        UrlInfo content = commonUrlDiscover.getContent("https://blog.csdn.net/xichji/article/details/140917180");
//        System.out.println(content.getDescription());
        String str = "测试测试 https://blog.csdn.net/xichji/article/details/140917180";
        Map<String, UrlInfo> urlContentMap = commonUrlDiscover.getUrlContentMap(str);
        System.out.println(urlContentMap);
    }
}