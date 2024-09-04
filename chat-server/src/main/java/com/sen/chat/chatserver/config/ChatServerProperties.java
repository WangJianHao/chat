package com.sen.chat.chatserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/27 15:10
 */
@Configuration
public class ChatServerProperties {

    @Value("${websocket.port:5001}")
    private Integer websocketPort;

    @Value("${project.folder}")
    private String projectFolder;


    public Integer getWebsocketPort() {
        return websocketPort;
    }

    public String getProjectFolder() {
        return projectFolder;
    }
}
