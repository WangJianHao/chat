package com.sen.chat.chatauth.controller;

import com.sen.chat.chatauth.domain.Oauth2TokenDto;
import com.sen.chat.common.api.SenCommonResponse;
import com.sen.chat.common.constant.AuthConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义Oauth2获取令牌接口
 *
 * @description:
 * @author: sensen
 * @date: 2023/8/26 13:16
 */
@RestController
@Api(tags = "AuthController", description = "认证中心登录认证")
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @ApiOperation("Oauth2获取token")
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public SenCommonResponse<Oauth2TokenDto> postAccessToken(HttpServletRequest request,
                                                             @ApiParam("授权模式") @RequestParam String grant_type,
                                                             @ApiParam("Oauth2客户端ID") @RequestParam String client_id,
                                                             @ApiParam("Oauth2客户端秘钥") @RequestParam String client_secret,
                                                             @ApiParam("刷新token") @RequestParam(required = false) String refresh_token,
                                                             @ApiParam("登录用户名") @RequestParam(required = false) String username,
                                                             @ApiParam("登录密码") @RequestParam(required = false) String password) throws HttpRequestMethodNotSupportedException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", grant_type);
        parameters.put("client_id", client_id);
        parameters.put("client_secret", client_secret);
        parameters.putIfAbsent("refresh_token", refresh_token);
        parameters.putIfAbsent("username", username);
        parameters.putIfAbsent("password", password);
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(request.getUserPrincipal(), parameters).getBody();
        Oauth2TokenDto oauth2TokenDto = Oauth2TokenDto.builder()
                .token(oAuth2AccessToken.getValue())
                .refreshToken(oAuth2AccessToken.getRefreshToken().getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .tokenHead(AuthConstant.JWT_TOKEN_PREFIX).build();

        return SenCommonResponse.success(oauth2TokenDto);
    }
}
