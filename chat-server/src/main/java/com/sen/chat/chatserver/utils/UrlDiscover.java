package com.sen.chat.chatserver.utils;

import cn.hutool.core.date.StopWatch;
import com.sen.chat.chatserver.entity.message.UrlInfo;
import org.jsoup.nodes.Document;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * URL解析
 *
 * @description:
 * @author: sensen
 * @date: 2023/7/31 23:03
 */
public interface UrlDiscover {

    @Nullable
    Map<String, UrlInfo> getUrlContentMap(String content);

    @Nullable
    UrlInfo getContent(String url);

    @Nullable
    String getTitle(Document document);

    @Nullable
    String getDescription(Document document);

    @Nullable
    String getImage(String url, Document document);

}
