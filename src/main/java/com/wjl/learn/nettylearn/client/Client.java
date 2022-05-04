package com.wjl.learn.nettylearn.client;

import com.wjl.learn.nettylearn.client.codec.OrderFrameDecoder;
import com.wjl.learn.nettylearn.client.codec.OrderFrameEncoder;
import com.wjl.learn.nettylearn.client.codec.OrderProtocolDecoder;
import com.wjl.learn.nettylearn.client.codec.OrderProtocolEncoder;
import com.wjl.learn.nettylearn.client.codec.handler.ClientIdleCheckHandler;
import com.wjl.learn.nettylearn.client.codec.handler.KeepaliveHandler;
import com.wjl.learn.nettylearn.common.RequestMessage;
import com.wjl.learn.nettylearn.common.order.OrderOperation;
import com.wjl.learn.nettylearn.util.IdUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

public class Client {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        bootstrap.group(boosGroup);
        bootstrap.option(NioChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 1000);
        KeepaliveHandler keepaliveHandler = new KeepaliveHandler();


        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();

                pipeline.addLast("idleCheckHandler", new ClientIdleCheckHandler());

                pipeline.addLast(new OrderFrameDecoder());
                pipeline.addLast(new OrderFrameEncoder());
                pipeline.addLast(new OrderProtocolEncoder());
                pipeline.addLast(new OrderProtocolDecoder());

                pipeline.addLast(keepaliveHandler);

                pipeline.addLast(new LoggingHandler(LogLevel.INFO));

            }
        });


        try {
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090).sync();

            RequestMessage requestMessage = new RequestMessage(IdUtil.nextId(), new OrderOperation(1001, "土豆"));
            channelFuture.channel().writeAndFlush(requestMessage);

            channelFuture.channel().closeFuture().get();
        } finally {
            boosGroup.shutdownGracefully();
        }
    }

}
