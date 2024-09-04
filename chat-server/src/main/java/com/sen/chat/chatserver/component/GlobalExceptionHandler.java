package com.sen.chat.chatserver.component;

import com.sen.chat.common.api.SenCommonResponse;
import com.sen.chat.common.exception.BaseExceptionHandler;
import com.sen.chat.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: sensen
 * @date: 2023/6/26 0:27
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler {

    /**
     * 参数校验异常拦截
     *
     * @param e 参数校验异常
     * @return 返回错误信息
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public SenCommonResponse<?> handleValidException(MethodArgumentNotValidException e) {
        return super.handleValidException(e);
    }

    /**
     * 业务异常拦截
     *
     * @param e 业务异常
     * @return 失败返回体
     */
    @ExceptionHandler(BusinessException.class)
    public SenCommonResponse<String> handleBusinessException(BusinessException e) {
        return super.handleBusinessException(e);
    }

    /**
     * 未知异常拦截
     *
     * @param e        异常信息
     * @param request  请求信息
     * @param response 响应信息
     * @return 失败返回体
     */
    @ExceptionHandler({RuntimeException.class, Exception.class})
    @ResponseBody
    public SenCommonResponse<String> handleRuntimeExceptions(Exception e, HttpServletRequest request, HttpServletResponse response) {
        log.error("请求发生未知异常错误，请求地址：{}，错误信息", request.getRequestURL(), e);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return SenCommonResponse.failed("服务器异常");
    }
}
