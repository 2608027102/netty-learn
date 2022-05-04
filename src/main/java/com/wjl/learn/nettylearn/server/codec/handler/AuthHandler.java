package com.wjl.learn.nettylearn.server.codec.handler;

import com.wjl.learn.nettylearn.common.Operation;
import com.wjl.learn.nettylearn.common.RequestMessage;
import com.wjl.learn.nettylearn.common.ResponseMessage;
import com.wjl.learn.nettylearn.common.auth.AuthOperation;
import com.wjl.learn.nettylearn.common.auth.AuthOperationResult;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class AuthHandler extends SimpleChannelInboundHandler<RequestMessage> {

    /**
     * Is called for each message of type {@link I}.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception is thrown if an error occurred
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage msg) throws Exception {
        Operation operation = msg.getMessageBody();

        try {
            if (operation instanceof AuthOperation authOperation) {
                AuthOperationResult execute = authOperation.execute();

                ResponseMessage responseMessage = new ResponseMessage();
                responseMessage.setMessageHeader(msg.getMessageHeader());
                responseMessage.setMessageBody(execute);

                if (execute.isPassAuth()) {
                    log.info("auth pass");
                    ctx.writeAndFlush(responseMessage);
                } else {
                    log.error("auth fail");
                    ctx.writeAndFlush(responseMessage);
                    ctx.close();
                }

            } else {
                log.error("expect first msg is auth");
                ctx.close();
            }
        } catch (Exception e) {
            log.error("auth error", e);
            ctx.close();
        } finally {
            // 授权只做一次，不需要每次都授权
            ctx.pipeline().remove(this);
        }

    }
}
