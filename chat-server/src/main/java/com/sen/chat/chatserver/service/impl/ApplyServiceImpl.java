package com.sen.chat.chatserver.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sen.chat.chatserver.config.ThreadPoolConfig;
import com.sen.chat.chatserver.convert.ApplyConverter;
import com.sen.chat.chatserver.dao.*;
import com.sen.chat.chatserver.dto.req.DealWithApplyReq;
import com.sen.chat.chatserver.dto.req.FriendApplyReq;
import com.sen.chat.chatserver.dto.req.GroupApplyReq;
import com.sen.chat.chatserver.dto.resp.ApplyResp;
import com.sen.chat.chatserver.entity.*;
import com.sen.chat.chatserver.entity.query.ApplyQuery;
import com.sen.chat.chatserver.entity.query.GroupMemberQuery;
import com.sen.chat.chatserver.event.ApplyEvent;
import com.sen.chat.chatserver.event.BatchApplyEvent;
import com.sen.chat.chatserver.service.*;
import com.sen.chat.chatserver.service.handler.MessageAdapter;
import com.sen.chat.chatserver.utils.PageUtils;
import com.sen.chat.common.api.BasePageReq;
import com.sen.chat.common.api.SenCommonPage;
import com.sen.chat.common.constant.IDHashKeyEnum;
import com.sen.chat.common.constant.dict.*;
import com.sen.chat.common.constant.errorcode.BaseErrorCodeEnum;
import com.sen.chat.common.exception.BusinessException;
import com.sen.chat.common.interceptor.RequestHolder;
import com.sen.chat.common.service.CommonIDGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 14:55
 */
@Slf4j
@Service
@AllArgsConstructor
public class ApplyServiceImpl implements ApplyService {

    private ApplyDAO applyDAO;

    private UserInfoService userInfoService;

    private ContactService contactService;

    private CommonIDGenerator commonIDGenerator;

    private UserInfoDAO userInfoDAO;

    private GroupMemberDAO groupMemberDAO;

    private GroupDAO groupDAO;

    private ContactDAO contactDAO;

    private ChatService chatService;

    private GroupService groupService;

    private ApplicationEventPublisher applicationEventPublisher;

    @Qualifier(ThreadPoolConfig.COMMON_EXECUTOR)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitFriendApply(FriendApplyReq friendApplyReq) {
        UserInfoDO currentUser = userInfoService.getCurrentUser();
        Long uid = currentUser.getUid();
        Long receiveUid = friendApplyReq.getReceiveUid();
        UserInfoDO userInfoDO = userInfoDAO.selectByUid(receiveUid);
        if (Objects.isNull(userInfoDO)) {
            throw new BusinessException(BaseErrorCodeEnum.USER_NOT_EXISTS);
        }

        ContactDO userContact = contactDAO.selectByContactId(uid, receiveUid, ContactTypeEnum.ACCOUNT.getCode());
        if (Objects.nonNull(userContact)) {
            throw new BusinessException(BaseErrorCodeEnum.ALREADY_FRIEND);
        }

        //检查好友申请类型
        Integer joinType = userInfoDO.getJoinType();
        //对方好友申请类型为直接添加好友则执行添加好友的操作
        if (Objects.equals(UserJoinTypeEnum.WITHOUT_ADMIT.getCode(), joinType)) {
            //不需要同意的不保存申请

            //添加好友
            addFriend(uid, friendApplyReq.getReceiveUid());

//            //修改申请状态为已同意
//            applyDAO.updateStatus(applyDO.getApplyId(), ApplyStatusEnum.WAIT_DEAL.getCode(), ApplyStatusEnum.AGREED.getCode());
        } else if (Objects.equals(UserJoinTypeEnum.WITH_ADMIT.getCode(), joinType)) {
            //对方好友申请类型为同意 ,保存申请后推送申请到对方
            //保存好友申请
            ApplyDO applyDO = new ApplyDO();
            applyDO.setApplyId(commonIDGenerator.nextId(IDHashKeyEnum.APPLY_ID.getHashKey()));
            applyDO.setApplyUid(uid);
            applyDO.setReceiveUid(receiveUid);
            applyDO.setLastApplyTime(Timestamp.valueOf(LocalDateTime.now()));
            applyDO.setStatus(ApplyStatusEnum.WAIT_DEAL.getCode());
            applyDO.setApplyInfo(friendApplyReq.getApplyInfo());
            applyDO.setApplyType(ApplyTypeEnum.FRIEND_APPLY.getCode());
            applyDO.setGroupId(null);
            int count = applyDAO.insert(applyDO);
            if (count < 1) {
                throw new BusinessException(BaseErrorCodeEnum.PARAM_VALID_FAIL);
            }

            //发布好友申请事件
            ApplyEvent applyEvent = new ApplyEvent(this, applyDO);
            applicationEventPublisher.publishEvent(applyEvent);
        }
    }

    @Override
    public void addDefaultFriend(Long uid) {
        //todo 系统消息硬编码 后面要通过数据库获取系统机器人ID
        addFriend(uid, 1L);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dealWithFriendApply(DealWithApplyReq dealWithApplyReq) {
        UserInfoDO currentUser = userInfoService.getCurrentUser();
        ApplyDO applyDO = applyDAO.selectByPrimaryKey(dealWithApplyReq.getApplyId());
        if (Objects.isNull(applyDO)) {
            throw new BusinessException(BaseErrorCodeEnum.PARAM_VALID_FAIL);
        }
        if (ApplyDealOptTypeEnum.AGREED.getCode().equals(dealWithApplyReq.getOptType())) {
            //同意申请
            //添加好友
            addFriend(applyDO.getApplyUid(), applyDO.getReceiveUid());

            //修改申请状态为已同意
            applyDAO.updateStatus(applyDO.getApplyId(), currentUser.getUid(), ApplyStatusEnum.WAIT_DEAL.getCode(), ApplyStatusEnum.AGREED.getCode());
        } else if (ApplyDealOptTypeEnum.REJECTED.getCode().equals(dealWithApplyReq.getOptType())) {
            //修改申请状态为已拒绝
            applyDAO.updateStatus(applyDO.getApplyId(), currentUser.getUid(), ApplyStatusEnum.WAIT_DEAL.getCode(), ApplyStatusEnum.REJECTED.getCode());
        } else if (ApplyDealOptTypeEnum.BLACKED.getCode().equals(dealWithApplyReq.getOptType())) {
            //修改申请状态为已拉黑
            applyDAO.updateStatus(applyDO.getApplyId(), currentUser.getUid(), ApplyStatusEnum.WAIT_DEAL.getCode(), ApplyStatusEnum.BLACKED.getCode());

            //todo 拉黑对方

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitGroupApply(GroupApplyReq groupApplyReq) {
        UserInfoDO currentUser = userInfoService.getCurrentUser();
        Long uid = currentUser.getUid();
        Long groupId = groupApplyReq.getGroupId();
        RoomGroupDO groupDO = groupDAO.selectByGroupId(groupId);
        if (Objects.isNull(groupDO)) {
            throw new BusinessException(BaseErrorCodeEnum.GROUP_NOT_EXISTS);
        }
        ContactDO groupContact = contactDAO.selectByContactId(uid, groupId, ContactTypeEnum.GROUP.getCode());
        if (Objects.nonNull(groupContact)) {
            throw new BusinessException(BaseErrorCodeEnum.ALREADY_IN_GROUP);
        }


        //检查好友申请类型
        Integer joinType = groupDO.getJoinType();
        //对方好友申请类型为直接添加好友则执行添加好友的操作
        if (Objects.equals(GroupJoinTypeEnum.WITHOUT_ADMIT.getCode(), joinType)) {
            //无需同意的直接执行入群操作，不保存入群申请
            //入群
            groupService.enterGroup(uid, groupId);
        } else if (Objects.equals(UserJoinTypeEnum.WITH_ADMIT.getCode(), joinType)) {
            //对方好友申请类型为同意 ,保存申请后发送申请到对方
            //保存入群申请
            GroupMemberQuery groupMemberQuery = GroupMemberQuery.builder()
                    .groupId(groupId)
                    .roles(Arrays.asList(GroupRoleEnum.OWNER.getCode(), GroupRoleEnum.ADMINISTRATOR.getCode()))
                    .build();
            List<GroupUserRelationDO> groupAdminDOS = groupMemberDAO.selectList(groupMemberQuery);
            List<ApplyDO> applyDOS = new ArrayList<>();
            Timestamp current = Timestamp.valueOf(LocalDateTime.now());
            for (GroupUserRelationDO groupAdmin : groupAdminDOS) {
                ApplyDO applyDO = new ApplyDO();
                applyDO.setApplyId(commonIDGenerator.nextId(IDHashKeyEnum.APPLY_ID.getHashKey()));
                applyDO.setApplyUid(uid);
                applyDO.setReceiveUid(groupAdmin.getUid());
                applyDO.setLastApplyTime(current);
                applyDO.setStatus(ApplyStatusEnum.WAIT_DEAL.getCode());
                applyDO.setApplyInfo(groupApplyReq.getApplyInfo());
                applyDO.setApplyType(ApplyTypeEnum.GROUP_APPLY.getCode());
                applyDO.setGroupId(groupId);
                applyDOS.add(applyDO);
            }
            int count = applyDAO.insertBatch(applyDOS);
            if (count < applyDOS.size()) {
                throw new BusinessException(BaseErrorCodeEnum.PARAM_VALID_FAIL);
            }

            //推送入群申请给管理员
            BatchApplyEvent batchApplyEvent = new BatchApplyEvent(this, applyDOS);
            applicationEventPublisher.publishEvent(batchApplyEvent);
        }
    }

    @Override
    public void dealWithGroupApply(DealWithApplyReq dealWithApplyReq) {
        UserInfoDO currentUser = userInfoService.getCurrentUser();
        ApplyDO applyDO = applyDAO.selectByPrimaryKey(dealWithApplyReq.getApplyId());
        if (Objects.isNull(applyDO)) {
            throw new BusinessException(BaseErrorCodeEnum.PARAM_VALID_FAIL);
        }
        GroupUserRelationDO groupMember = groupMemberDAO.selectByPrimaryKey(applyDO.getGroupId(), currentUser.getUid());
        if (Objects.isNull(groupMember) || GroupRoleEnum.MATE.getCode().equals(groupMember.getRole())) {
            throw new BusinessException("无权处理该申请");
        }
        if (ApplyDealOptTypeEnum.AGREED.getCode().equals(dealWithApplyReq.getOptType())) {
            //同意申请
            //入群
            groupService.enterGroup(applyDO.getApplyUid(), applyDO.getGroupId());

            //修改申请状态为已同意
            int count = applyDAO.updateGroupStatus(applyDO.getGroupId(), applyDO.getApplyUid(), applyDO.getLastApplyTime(),
                    currentUser.getUid(), ApplyStatusEnum.WAIT_DEAL.getCode(), ApplyStatusEnum.AGREED.getCode());
            if (count < 1) {
                throw new BusinessException("其他管理员已处理");
            }
        } else if (ApplyDealOptTypeEnum.REJECTED.getCode().equals(dealWithApplyReq.getOptType())) {
            //修改申请状态为已拒绝
            int count = applyDAO.updateGroupStatus(applyDO.getGroupId(), applyDO.getApplyUid(), applyDO.getLastApplyTime(),
                    currentUser.getUid(), ApplyStatusEnum.WAIT_DEAL.getCode(), ApplyStatusEnum.REJECTED.getCode());
            if (count < 1) {
                throw new BusinessException("其他管理员已处理");
            }
        }
        //群组申请只能同意和拒绝，暂时不设置拉黑选项
    }

    @Override
    public SenCommonPage<ApplyResp> queryApplyWithPage(BasePageReq request) {
        Long uid = RequestHolder.get().getUid();
        Page<ApplyDO> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getSize());
        ApplyQuery query = ApplyQuery.builder().receiveUid(uid).build();
        page = applyDAO.selectPage(page, query);
        return PageUtils.restPage(page.convert(ApplyConverter.INSTANCE::convert));
    }


    private void addFriend(Long uid, Long receiveUid) {
        Long roomId = contactService.addFriend(uid, receiveUid);
        //当前事务提交之后发送消息给对方 异步发送消息
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                if (Objects.nonNull(roomId)) {
                    CompletableFuture.runAsync(new Runnable() {
                        @Override
                        public void run() {
                            //发送好友提醒消息
                            chatService.sendMsg(MessageAdapter.buildAgreeMsg(roomId), uid);
                        }
                    }, threadPoolTaskExecutor).whenComplete((unused, throwable) -> {
                        if (Objects.nonNull(throwable)) {
                            log.error("消息发送出现异常", throwable);
                        }
                    });
                }
            }
        });
    }
}
