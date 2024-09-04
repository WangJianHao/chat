package com.sen.chat.chatserver.websocket.netty;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.sen.chat.chatserver.constant.WSReqTypeEnum;
import com.sen.chat.chatserver.dto.ws.WSBaseReq;
import com.sen.chat.chatserver.websocket.WebSocketService;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


@Slf4j
@Sharable
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private WebSocketService webSocketService;

    // 当web客户端连接后，触发该方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        this.webSocketService = getService();
    }

    // 客户端离线
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        userOffLine(ctx);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("有新的连接加入");
        super.channelActive(ctx);
    }

    /**
     * 取消绑定
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 可能出现业务判断离线后再次触发 channelInactive
        log.warn("触发 channelInactive 掉线![{}]", ctx.channel().id());
        userOffLine(ctx);
    }

    /**
     * 心跳检查
     *
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            //心跳
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            // 读空闲 心跳断开
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                // 关闭用户的连接
                log.info("心跳超时");
                userOffLine(ctx);
            }
        } else if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            //ws 连接事件
            this.webSocketService.connect(ctx.channel());
            String token = NettyContextHolder.getAttr(ctx.channel(), NettyContextHolder.TOKEN);
            //需要携带token
            if (StringUtils.isEmpty(token)) {
                userOffLine(ctx);
                return;
            }
            //验证token 验证成功发布用户上线事件
            this.webSocketService.authorize(ctx.channel(), token);
            log.info("websocket已连接");
        }
        super.userEventTriggered(ctx, evt);
    }

    // 处理异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("异常发生，异常消息：", cause);
        ctx.channel().close();
    }

    // 读取客户端发送的请求报文
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        WSBaseReq wsBaseReq = JSONUtil.toBean(msg.text(), WSBaseReq.class);
        WSReqTypeEnum wsReqTypeEnum = WSReqTypeEnum.of(wsBaseReq.getType());
        //目前仅有心跳检测
        switch (wsReqTypeEnum) {
            case HEARTBEAT:
                //心跳消息
                break;
            default:
                log.info("未知类型");
        }
    }

    /**
     * 获取ws service对象
     *
     * @return ws service对象
     */
    private WebSocketService getService() {
        return SpringUtil.getBean(WebSocketService.class);
    }

    /**
     * 下线
     */
    private void userOffLine(ChannelHandlerContext ctx) {
        this.webSocketService.removed(ctx.channel());
        ctx.channel().close();
    }
}
