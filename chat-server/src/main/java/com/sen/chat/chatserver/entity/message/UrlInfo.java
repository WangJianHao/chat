package com.sen.chat.chatserver.entity.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/31 0:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlInfo {

    /**
     * 标题
     **/
    String title;

    /**
     * 描述
     **/
    String description;

    /**
     * 网站LOGO
     **/
    String image;
}
