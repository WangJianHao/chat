package com.sen.chat.chatserver.event;

import com.sen.chat.chatserver.entity.ApplyDO;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/3 20:40
 */
public class BatchApplyEvent extends ApplicationEvent {
    private List<ApplyDO> applyDOS;

    public BatchApplyEvent(Object source, List<ApplyDO> applyDOS) {
        super(source);
        this.applyDOS = applyDOS;
    }

    public List<ApplyDO> getApplyDOS() {
        return applyDOS;
    }
}
