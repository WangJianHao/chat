package com.sen.chat.common.constant.errorcode;

/**
 * 业务异常信息
 *
 * @description:
 * @author: sensen
 * @date: 2023/8/7 16:40
 */
public enum BaseErrorCodeEnum {
    PARAM_VALID_FAIL(1000, "参数异常"),
    LOGIN_NONNULL(1001, "用户名或密码不能为空"),
    LOGIN_FAILED(1002, "用户名或密码错误"),
    AUTH_CODE_EXPIRED(1003, "验证码过期"),
    AUTH_CODE_VALID_FAIL(1004, "验证码不正确"),
    USER_EXISTS(1005, "该用户已存在"),
    USER_NOT_EXISTS(1005, "该用户不存在"),
    REGISTER_FAILED(1006, "注册失败"),
    ACCOUNT_NOT_FOUND(1007, "账号不存在"),
    OUT_OF_MAX_GROUP_COUNT(2001, "群组超过数量限制，不允许创建"),
    GROUP_NOT_EXISTS(2002, "群组不存在"),
    GROUP_IS_DELETE(2003, "群组已解散"),
    NO_AUTH_GROUP(2004, "无权访问该群组信息"),
    ROOM_NOT_EXIST(2005, "聊天室不存在"),
    BE_BLACKED(2006,"你已被对方拉黑"),
    BE_DELETED(2007,"你已被对方删除"),
    DELETE_FRIEND(2008,"你已将对方删除"),
    NOT_FRIEND(2009,"非好友无法发送消息"),
    BE_GROUP_REMOVED(2010,"你已被移除该群"),
    ALREADY_FRIEND(2011,"已经是好友，无需发送申请"),
    ALREADY_IN_GROUP(2011,"已经在群组中，无需发送申请"),
    ;

    private final Integer code;
    private final String message;

    BaseErrorCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
