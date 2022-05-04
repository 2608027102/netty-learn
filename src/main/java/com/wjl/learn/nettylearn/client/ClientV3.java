package com.wjl.learn.nettylearn.client;

import com.wjl.learn.nettylearn.client.codec.*;
import com.wjl.learn.nettylearn.client.codec.handler.dispatch.OperationResultFuture;
import com.wjl.learn.nettylearn.client.codec.handler.dispatch.RequestPendingCenter;
import com.wjl.learn.nettylearn.client.codec.handler.dispatch.ResponseDispatchHandler;
import com.wjl.learn.nettylearn.common.OperationResult;
import com.wjl.learn.nettylearn.common.RequestMessage;
import com.wjl.learn.nettylearn.common.auth.AuthOperation;
import com.wjl.learn.nettylearn.common.auth.AuthOperationResult;
import com.wjl.learn.nettylearn.common.order.OrderOperation;
import com.wjl.learn.nettylearn.util.IdUtil;
import com.wjl.learn.nettylearn.util.RequestUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

public class ClientV3 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        bootstrap.group(boosGroup);

        RequestPendingCenter requestPendingCenter = new RequestPendingCenter();

        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();

                pipeline.addLast(new OrderFrameDecoder());
                pipeline.addLast(new OrderFrameEncoder());
                pipeline.addLast(new OrderProtocolEncoder());
                pipeline.addLast(new OrderProtocolDecoder());

                pipeline.addLast(new ResponseDispatchHandler(requestPendingCenter));

                pipeline.addLast(new OperationToRequestMessageEncoder());

                pipeline.addLast(new LoggingHandler(LogLevel.INFO));

            }
        });


        try {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090).sync();

            RequestUtil requestUtil = RequestUtil.getInstance(requestPendingCenter);

            RequestMessage authMessage = new RequestMessage(IdUtil.nextId(), new AuthOperation("admin", "132456"));

            OperationResultFuture authResultFuture = requestUtil.send(channelFuture.channel(), authMessage);
            OperationResult authResult = authResultFuture.get();

            if (authResult instanceof AuthOperationResult authOperationResult) {
                if (!authOperationResult.isPassAuth()) {
                    throw new RuntimeException("授权失败");
                }
            } else {
                throw new RuntimeException("未知异常");
            }


            RequestMessage orderMessage = new RequestMessage(IdUtil.nextId(), new OrderOperation(1001, "土豆"));

            OperationResultFuture orderResultFuture = requestUtil.send(channelFuture.channel(), orderMessage);

            OperationResult orderResult = orderResultFuture.get();
            System.out.println(orderResult);


            channelFuture.channel().closeFuture().get();
        } finally {
            boosGroup.shutdownGracefully();
        }
    }

}
