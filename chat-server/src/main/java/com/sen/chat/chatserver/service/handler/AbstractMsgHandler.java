package com.sen.chat.chatserver.service.handler;

import cn.hutool.core.bean.BeanUtil;
import com.sen.chat.chatserver.dao.MessageDAO;
import com.sen.chat.chatserver.dto.msg.TextMsgReq;
import com.sen.chat.chatserver.dto.req.ChatMessageReq;
import com.sen.chat.chatserver.entity.MessageDO;
import com.sen.chat.common.constant.dict.MessageTypeEnum;
import com.sen.chat.common.constant.errorcode.BaseErrorCodeEnum;
import com.sen.chat.common.exception.BusinessException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.ParameterizedType;
import java.util.Set;

/**
 * 消息处理器抽象类
 *
 * @description: 消息处理器抽象类
 * @author: sensen
 * @date: 2023/6/30 20:22
 */
public abstract class AbstractMsgHandler<Req> {

    @Resource
    private MessageDAO messageDAO;

    //对应消息类型的class对象
    private Class<Req> bodyClass;

    /**
     * 全部校验
     */
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    /**
     * 初始化操作
     */
    @PostConstruct
    private void init() {
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.bodyClass = (Class<Req>) genericSuperclass.getActualTypeArguments()[0];
        MsgHandlerFactory.register(getMsgTypeEnum().getCode(), this);
    }

    /**
     * 获取消息类型
     * 子类实现该方法获取其具体的类型
     *
     * @return 消息类型
     */
    abstract MessageTypeEnum getMsgTypeEnum();

    /**
     * 保存数据库前检查消息
     * 文本消息可做敏感词系统
     *
     * @param body   消息体
     * @param roomId 聊天室ID
     * @param uid    发送人UID
     */
    protected void checkMsg(Req body, Long roomId, Long uid) {

    }


    /**
     * 每个消息保存的抽象步骤
     *
     * @param request 请求消息体
     * @param uid     发送人UID
     * @return 消息ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Long checkAndSaveMsg(ChatMessageReq request, Long uid) {
        //将body转为具体的消息类型消息体
        Req body = this.toBean(request.getBody());

        //统一校验
        allCheckValidateThrow(body);

        //子类扩展校验
        checkMsg(body, request.getRoomId(), uid);

        //统一保存
        MessageDO insert = MessageAdapter.buildMsgSave(request, uid);
        messageDAO.insert(insert);

        //子类扩展保存
        saveMsg(insert, body);
        return insert.getId();
    }

    private Req toBean(Object body) {
        if (bodyClass.isAssignableFrom(body.getClass())) {
            return (Req) body;
        }
        return BeanUtil.toBean(body, bodyClass);
    }

    /**
     * 注解验证参数(全部校验,抛出异常)
     *
     * @param obj 对应消息类型的具体消息请求体
     * @see TextMsgReq
     */
    private void allCheckValidateThrow(Req obj) {
        Set<ConstraintViolation<Req>> constraintViolations = validator.validate(obj);
        if (constraintViolations.size() > 0) {
            StringBuilder errorMsg = new StringBuilder();
            for (ConstraintViolation<Req> violation : constraintViolations) {
                //拼接异常信息
                errorMsg.append(violation.getPropertyPath().toString()).append(":").append(violation.getMessage()).append(",");
            }
            throw new BusinessException(BaseErrorCodeEnum.PARAM_VALID_FAIL.getCode(), errorMsg.substring(0, errorMsg.length() - 1));
        }
    }

    protected abstract void saveMsg(MessageDO message, Req body);

    /**
     * 展示消息
     */
    public abstract Object showMsg(MessageDO msg);

    /**
     * 被回复时——展示的消息
     */
    public abstract Object showReplyMsg(MessageDO msg);

    /**
     * 会话列表——展示的消息
     */
    public abstract String showContactMsg(MessageDO msg);
}
