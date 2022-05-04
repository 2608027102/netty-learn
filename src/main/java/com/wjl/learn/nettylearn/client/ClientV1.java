package com.wjl.learn.nettylearn.client;

import com.wjl.learn.nettylearn.client.codec.*;
import com.wjl.learn.nettylearn.common.RequestMessage;
import com.wjl.learn.nettylearn.common.order.OrderOperation;
import com.wjl.learn.nettylearn.util.IdUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

public class ClientV1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        bootstrap.group(boosGroup);


        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();

                pipeline.addLast(new OrderFrameDecoder());
                pipeline.addLast(new OrderFrameEncoder());
                pipeline.addLast(new OrderProtocolEncoder());
                pipeline.addLast(new OrderProtocolDecoder());
                pipeline.addLast(new OperationToRequestMessageEncoder());

                pipeline.addLast(new LoggingHandler(LogLevel.INFO));

            }
        });


        try {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090).sync();

            /*RequestMessage requestMessage = new RequestMessage(IdUtil.nextId(), new OrderOperation(1001, "土豆"));
            channelFuture.channel().writeAndFlush(requestMessage);*/

            channelFuture.channel().writeAndFlush(new OrderOperation(1001, "土豆"));

            channelFuture.channel().closeFuture().get();
        } finally {
            boosGroup.shutdownGracefully();
        }
    }

}
