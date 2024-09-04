package com.sen.chat.chatserver.event.listener;

import com.sen.chat.chatserver.dao.ApplyDAO;
import com.sen.chat.chatserver.dao.GroupMemberDAO;
import com.sen.chat.chatserver.dto.ws.WSApply;
import com.sen.chat.chatserver.entity.ApplyDO;
import com.sen.chat.chatserver.entity.query.ApplyQuery;
import com.sen.chat.chatserver.event.ApplyEvent;
import com.sen.chat.chatserver.event.BatchApplyEvent;
import com.sen.chat.chatserver.event.mq.PushService;
import com.sen.chat.chatserver.websocket.WSAdapter;
import com.sen.chat.common.constant.dict.ApplyStatusEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 16:12
 */
@Slf4j
@Component
@AllArgsConstructor
public class ApplyListener {

    private ApplyDAO applyDAO;

    private GroupMemberDAO groupMemberDAO;

    private PushService pushService;

    private WSAdapter wsAdapter;

    @Async
    @TransactionalEventListener(classes = ApplyEvent.class, fallbackExecution = true)
    public void notify(ApplyEvent event) {
        ApplyDO apply = event.getApplyDO();
        pushApply(apply);
    }

    @Async
    @TransactionalEventListener(classes = BatchApplyEvent.class, fallbackExecution = true)
    public void notify(BatchApplyEvent event) {
        List<ApplyDO> applyDOS = event.getApplyDOS();
        for (ApplyDO applyDO : applyDOS) {
            pushApply(applyDO);
        }
    }

    private void pushApply(ApplyDO apply) {
        ApplyQuery query = ApplyQuery.builder()
                .receiveUid(apply.getReceiveUid())
                .status(ApplyStatusEnum.WAIT_DEAL.getCode()).build();
        int unReadCount = applyDAO.selectCount(query);
        WSApply wsApply = new WSApply();
        wsApply.setApplyUid(apply.getApplyUid());
        wsApply.setApplyType(apply.getApplyType());
        wsApply.setApplyInfo(apply.getApplyInfo());
        wsApply.setGroupId(apply.getGroupId());
        wsApply.setUnreadCount(unReadCount);
        pushService.sendPushMsg(wsAdapter.buildApplySend(wsApply), apply.getReceiveUid());
    }
}
