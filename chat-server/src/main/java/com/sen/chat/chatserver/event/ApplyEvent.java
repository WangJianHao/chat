package com.sen.chat.chatserver.event;

import com.sen.chat.chatserver.entity.ApplyDO;
import org.springframework.context.ApplicationEvent;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 16:16
 */
public class ApplyEvent extends ApplicationEvent {

    private ApplyDO applyDO;

    public ApplyEvent(Object source, ApplyDO applyDO) {
        super(source);
        this.applyDO = applyDO;
    }

    public ApplyDO getApplyDO() {
        return applyDO;
    }
}
