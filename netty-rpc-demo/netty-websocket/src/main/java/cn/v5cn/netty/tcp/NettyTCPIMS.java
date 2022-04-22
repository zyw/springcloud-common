package cn.v5cn.netty.tcp;

import cn.v5cn.netty.bean.IMSMsg;
import cn.v5cn.netty.config.IMSOptions;
import cn.v5cn.netty.core.IMSInterface;
import cn.v5cn.netty.listener.IMSMsgReceivedListener;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.Inet4Address;

/**
 * @author FreddyChen
 * @name Netty TCP IM Service，基于Netty实现的TCP协议客户端
 */
public class NettyTCPIMS implements IMSInterface {

    private static final Logger logger = LoggerFactory.getLogger(NettyTCPIMS.class);

    private ServerBootstrap bootstrap;
    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private volatile boolean isClosed;
    private boolean initialized;
    private IMSOptions mIMSOptions;
    private IMSMsgReceivedListener mIMSMsgReceivedListener;

    private NettyTCPIMS(){}

    public static NettyTCPIMS getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public boolean init(IMSOptions options, IMSMsgReceivedListener msgReceivedListener) {
        if(options == null) {
            initialized = false;
            return false;
        }
        this.mIMSOptions = options;
        this.mIMSMsgReceivedListener = msgReceivedListener;
        initialized = true;
        isClosed = false;
        return true;
    }

    @Override
    public void start() {
        if(!initialized) {
            logger.warn("NettyTCPIMS启动失败：初始化失败");
            return;
        }
        try {
            initServerBootstrap();
            ChannelFuture future = bootstrap.bind(mIMSOptions.getPort()).sync();
            channel = future.channel();
            if(channel != null && channel.isOpen() && channel.isActive() && channel.isRegistered() && channel.isWritable()) {
                logger.debug(String.format("NettyTCPIMS启动成功，ip【%1$s】\tport【%2$d】", Inet4Address.getLocalHost().getHostAddress(), mIMSOptions.getPort()));
            } else {
                logger.debug("NettyTCPIMS启动失败");
            }
            future.awaitUninterruptibly();
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bossGroup.shutdownGracefully();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                bossGroup = null;
            }

            try {
                workGroup.shutdownGracefully();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workGroup = null;
            }
        }
    }

    @Override
    public void sendMsg(IMSMsg msg) {

    }

    @Override
    public void sendMsg(IMSMsg msg, boolean isJoinResendManager) {

    }

    @Override
    public void release() {
        closeChannel();
        closeServerBootstrap();
        isClosed = true;
    }

    private static class SingletonHolder {
        private static final NettyTCPIMS INSTANCE = new NettyTCPIMS();
    }

    /**
     * 初始化ServerBootstrap
     */
    private void initServerBootstrap() {
        try {
            // 先关闭之前的bootstrap
            closeServerBootstrap();
            // boss线程池用于处理TCP连接，通常服务端开启的都是一个端口，所以线程数指定为1即可
            bossGroup = new NioEventLoopGroup(1);
            // work线程用于处理IO事件，需要多线程处理，不知道线程数，默认就是CPU核心数*2
            workGroup = new NioEventLoopGroup();
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    //设置TCP接收缓冲区大小（字节数）
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)
                    // 服务端接受连接的队列长度，如果队列已满，客户端连接将被拒绝。默认值，Windows为200，其他为128
                    .option(ChannelOption.SO_BACKLOG, 256)
                    // 设置该选项以后，如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 设置禁用nagle算法，如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new NettyTCPChannelInitializerHandler(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeServerBootstrap() {
        try {
            if(bootstrap != null) {
                bootstrap.config().group().shutdownGracefully();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bootstrap = null;
        }
    }

    private void closeChannel() {
        try {
            if(channel == null) {
                try {
                    channel.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    channel.eventLoop().shutdownGracefully();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel = null;
        }
    }

}
