package com.wjl.learn.nettylearn.server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.wjl.learn.nettylearn.server.codec.OrderFrameDecoder;
import com.wjl.learn.nettylearn.server.codec.OrderFrameEncoder;
import com.wjl.learn.nettylearn.server.codec.OrderProtocolDecoder;
import com.wjl.learn.nettylearn.server.codec.OrderProtocolEncoder;
import com.wjl.learn.nettylearn.server.codec.handler.AuthHandler;
import com.wjl.learn.nettylearn.server.codec.handler.MetricHandler;
import com.wjl.learn.nettylearn.server.codec.handler.OrderServerProcessHandler;
import com.wjl.learn.nettylearn.server.codec.handler.ServerIdleCheckHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.flush.FlushConsolidationHandler;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import io.netty.handler.ipfilter.RuleBasedIpFilter;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;

import java.util.concurrent.ExecutionException;

public class Server {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("Nio-Boss-Group"));
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("Nio-Worker-Group"));
        serverBootstrap.group(bossGroup, workerGroup);

        serverBootstrap.childOption(NioChannelOption.TCP_NODELAY, true);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 2014);

        MetricHandler metricHandler = new MetricHandler();

        // ?????????????????????
        UnorderedThreadPoolEventExecutor eventExecutors = new UnorderedThreadPoolEventExecutor(10);
        // ??????NioEventLoopGroup ?????????????????????????????????????????????

        long traffic = 100 * 1024 * 1024;
        GlobalTrafficShapingHandler globalTrafficShapingHandler = new GlobalTrafficShapingHandler(new NioEventLoopGroup(), traffic, traffic);

        // ???????????????
//        IpSubnetFilterRule ipSubnetFilterRule = new IpSubnetFilterRule("127.0.0.1", 8, IpFilterRuleType.REJECT);
        IpSubnetFilterRule ipSubnetFilterRule = new IpSubnetFilterRule("127.0.0.1", 8, IpFilterRuleType.ACCEPT);
        RuleBasedIpFilter ruleBasedIpFilter = new RuleBasedIpFilter(ipSubnetFilterRule);

        // ??????
        AuthHandler authHandler = new AuthHandler();

        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();

                pipeline.addLast("ipFilter", ruleBasedIpFilter);

                // V???????????????????????????????????????????????????????????????????????????????????????????????????
                pipeline.addLast("TShandler", globalTrafficShapingHandler);
                // readIdleCheck
                pipeline.addLast("idleCheck", new ServerIdleCheckHandler());

                pipeline.addLast("frameDecoder", new OrderFrameDecoder());
                pipeline.addLast("frameEncoder", new OrderFrameEncoder());
                pipeline.addLast("protocolDecoder", new OrderProtocolDecoder());
                pipeline.addLast("protocolEncoder", new OrderProtocolEncoder());

                pipeline.addLast("metricHandler", metricHandler);

                pipeline.addLast(new LoggingHandler(LogLevel.INFO));

                // ??????flush????????????????????????
                pipeline.addLast("flushEnhance", new FlushConsolidationHandler(5, true));

                pipeline.addLast("authHandler", authHandler);

                pipeline.addLast(eventExecutors, new OrderServerProcessHandler());
            }
        });


        try {
            ChannelFuture channelFuture = serverBootstrap.bind(8090).sync();
            channelFuture.channel().closeFuture().get();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}

