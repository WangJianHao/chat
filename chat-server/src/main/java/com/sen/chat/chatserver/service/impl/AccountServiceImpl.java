package com.sen.chat.chatserver.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.sen.chat.chatserver.constant.RedisConstant;
import com.sen.chat.chatserver.dao.UserBeautyDAO;
import com.sen.chat.chatserver.dao.UserInfoDAO;
import com.sen.chat.chatserver.dao.UserLoginInfoDAO;
import com.sen.chat.chatserver.dto.Oauth2TokenDto;
import com.sen.chat.chatserver.dto.req.LoginReq;
import com.sen.chat.chatserver.dto.req.RegisterUserReq;
import com.sen.chat.chatserver.dto.vo.CaptureCodeVO;
import com.sen.chat.chatserver.entity.UserBeautyDO;
import com.sen.chat.chatserver.entity.UserInfoDO;
import com.sen.chat.chatserver.entity.UserLoginInfoDO;
import com.sen.chat.chatserver.entity.query.UserInfoQuery;
import com.sen.chat.chatserver.feign.AuthFeign;
import com.sen.chat.chatserver.service.AccountService;
import com.sen.chat.chatserver.service.ApplyService;
import com.sen.chat.chatserver.service.MailService;
import com.sen.chat.chatserver.service.UserInfoService;
import com.sen.chat.common.api.SenCommonResponse;
import com.sen.chat.common.constant.AuthConstant;
import com.sen.chat.common.constant.IDHashKeyEnum;
import com.sen.chat.common.constant.dict.UserActiveStatusEnum;
import com.sen.chat.common.constant.dict.UserBeautyStatusEnum;
import com.sen.chat.common.constant.dict.UserJoinTypeEnum;
import com.sen.chat.common.constant.dict.UserStatusEnum;
import com.sen.chat.common.constant.errorcode.BaseErrorCodeEnum;
import com.sen.chat.common.exception.BusinessException;
import com.sen.chat.common.service.CommonIDGenerator;
import com.sen.chat.common.service.RedisService;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/27 3:25
 */
@Slf4j
@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AuthFeign authFeign;

    private final RedisService redisService;

    private final UserInfoService userInfoService;

    private final UserInfoDAO userInfoDAO;

    private final UserBeautyDAO userBeautyDAO;

    private final UserLoginInfoDAO userLoginInfoDAO;

    private final CommonIDGenerator commonIDGenerator;

    private final ApplyService applyService;

    private final MailService mailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterUserReq registerReq) {
        //验证图片验证码
//        if (!verifyAuthCode(registerReq.getCaptureCode(), registerReq.getCaptureCodeKey())) {
//            throw new BusinessException(BaseErrorCodeEnum.AUTH_CODE_VALID_FAIL);
//        }
        //验证邮箱验证码

        //查询是否已有该用户
        UserInfoQuery query = UserInfoQuery.builder()
                .email(registerReq.getEmail())
                .build();
        List<UserInfoDO> user = userInfoDAO.selectList(query);
        if (!CollectionUtils.isEmpty(user)) {
            throw new BusinessException(BaseErrorCodeEnum.USER_EXISTS);
        }

        //检查是否是靓号
        Long uid = null;
        UserBeautyDO beautyAccount = userBeautyDAO.selectByEmail(registerReq.getEmail());//靓号
        boolean enableBeauty = null != beautyAccount
                && UserBeautyStatusEnum.USELESS.getCode().compareTo(beautyAccount.getStatus()) == 0;
        if (enableBeauty) {
            uid = beautyAccount.getUid();
        } else {
            //todo 生成id的时候要跳过靓号
            uid = commonIDGenerator.nextId(IDHashKeyEnum.USER_UID.getHashKey());
        }

        //没有该用户进行添加操作
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setUid(uid);
        userInfoDO.setUserName(registerReq.getUserName());
        userInfoDO.setEmail(registerReq.getEmail());
        userInfoDO.setPassword(BCrypt.hashpw(registerReq.getPassword()));
        userInfoDO.setJoinType(UserJoinTypeEnum.WITH_ADMIT.getCode());
        userInfoDO.setStatus(UserStatusEnum.ENABLE.getCode());
        int count = userInfoDAO.insert(userInfoDO);
        if (count < 1) {
            throw new BusinessException(BaseErrorCodeEnum.REGISTER_FAILED);
        }
        userInfoDO.setPassword(null);

        //使用靓号后修改靓号状态
        if (enableBeauty) {
            beautyAccount.setStatus(UserBeautyStatusEnum.USED.getCode());
            int updateCount = userBeautyDAO.updateById(beautyAccount);
            if (updateCount < 1) {
                throw new BusinessException(BaseErrorCodeEnum.REGISTER_FAILED);
            }
        }

        //TODO 创建机器人好友
        applyService.addDefaultFriend(uid);
    }

    @Override
    public SenCommonResponse<?> login(LoginReq loginReq) {
        if (Objects.isNull(loginReq.getUid()) && StringUtils.isEmpty(loginReq.getEmail())) {
            throw new BusinessException(BaseErrorCodeEnum.LOGIN_FAILED);
        }
        Long uid = null;
        if (Objects.nonNull(loginReq.getUid())) {
            uid = loginReq.getUid();
        } else {
            UserInfoQuery query = UserInfoQuery.builder()
                    .email(loginReq.getEmail())
                    .build();
            List<UserInfoDO> userList = userInfoDAO.selectList(query);
            if (CollectionUtils.isEmpty(userList)) {
                throw new BusinessException(BaseErrorCodeEnum.ACCOUNT_NOT_FOUND);
            }
            uid = userList.get(0).getUid();
        }

        Map<String, String> params = new HashMap<>();
        params.put("client_id", AuthConstant.PORTAL_CLIENT_ID);
        params.put("client_secret", "123456");
        params.put("grant_type", "password");
        params.put("username", String.valueOf(uid));//登录用户名 security框架识别
        params.put("password", loginReq.getPassword());
        SenCommonResponse<Oauth2TokenDto> accessToken = authFeign.getAccessToken(params);

        String token = accessToken.getData().getToken();
        int expiresIn = accessToken.getData().getExpiresIn();
        redisService.set(RedisConstant.TOKEN_KEY + token, userInfoService.loadUserByUid(uid), expiresIn);

        //维护loginInfo信息
        insertLoginLog(uid);

        return accessToken;
    }

    @Override
    public CaptureCodeVO generateCaptureCode() {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
        String code = captcha.text();
        String checkCodeKey = UUID.randomUUID().toString();

        //保存进redis中
        redisService.set(RedisConstant.CAPTURE_CODE_KEY + checkCodeKey, code, RedisConstant.FIVE_MINUTE);

        CaptureCodeVO captureCodeVO = new CaptureCodeVO();
        captureCodeVO.setCaptureCode(captcha.toBase64());
        captureCodeVO.setCheckCodeKey(checkCodeKey);
        return captureCodeVO;
    }

    @Override
    public void sendMailAuthCode(String email) {
        // 判断当前待发送邮箱是否已经有验证码
        String key = RedisConstant.AUTH_CODE_KEY_PREFIX + email;
        String authCodeInRedis = (String) redisService.get(key);
        if (StringUtils.isNotEmpty(authCodeInRedis)) {
            throw new BusinessException("验证码已发送");
        }

        // 生成随机验证码
        String authCode = UUID.randomUUID().toString().substring(0, 4);

//        MailDTO mailDTO = new MailDTO();
//        mailDTO.setSubject(sysSettingDTO.getRegisterEmailTitle());
//        mailDTO.setText(String.format(sysSettingDTO.getRegisterEmailContent(), authCode));
        //添加到缓存中
        redisService.set(key, authCode, RedisConstant.TEM_MINUTE);
//        mailService.sendMail(mailDTO);
    }

    @Override
    public void logout() {

    }

    @Override
    public SenCommonResponse<?> renewToken() {
        UserInfoDO currentUser = userInfoService.getCurrentUser();

        Map<String, String> params = new HashMap<>();
        params.put("client_id", AuthConstant.PORTAL_CLIENT_ID);
        params.put("client_secret", "123456");
        params.put("grant_type", "password");
        params.put("username", String.valueOf(currentUser.getUid()));//登录用户名 security框架识别
        params.put("password", currentUser.getPassword());
        SenCommonResponse<Oauth2TokenDto> accessToken = authFeign.getAccessToken(params);
        return accessToken;
    }

    /**
     * 添加登录记录
     *
     * @param uid 用户UID
     */
    private void insertLoginLog(Long uid) {
        UserLoginInfoDO loginLog = userLoginInfoDAO.selectByUid(uid);
        Timestamp nowTime = Timestamp.valueOf(LocalDateTime.now());
        //设置IP信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Assert.notNull(attributes, "请求头不为空");
        HttpServletRequest request = attributes.getRequest();
        if (Objects.isNull(loginLog)) {
            loginLog = new UserLoginInfoDO();
            loginLog.setUid(uid);
            loginLog.setActiveStatus(UserActiveStatusEnum.ONLINE.getCode());
            loginLog.setLastOptTime(nowTime);
            loginLog.setLastOffTime(nowTime);
            loginLog.setIpInfo(request.getRemoteAddr());
            userLoginInfoDAO.insert(loginLog);
        } else {
            loginLog.setActiveStatus(UserActiveStatusEnum.ONLINE.getCode());
            loginLog.setLastOptTime(nowTime);
            loginLog.setIpInfo(request.getRemoteAddr());
            userLoginInfoDAO.updateById(loginLog);
        }
    }

    //对输入的验证码进行校验
    private boolean verifyAuthCode(String authCode, String key) {
        if (StringUtils.isEmpty(authCode)) {
            return false;
        }
        String realAuthCode = (String) redisService.get(key);
        if (Objects.isNull(realAuthCode)) {
            throw new BusinessException(BaseErrorCodeEnum.AUTH_CODE_EXPIRED);
        }
        return StringUtils.equals(authCode, realAuthCode);
    }
}
