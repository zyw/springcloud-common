package cn.v5cn.jim.demo;

import cn.v5cn.jim.demo.command.DemoWsHandshakeProcessor;
import cn.v5cn.jim.demo.listener.ImDemoGroupListener;
import cn.v5cn.jim.demo.listener.ImDemoUserListener;
import cn.v5cn.jim.demo.service.LoginServiceProcessor;
import org.jim.core.packets.Command;
import org.jim.server.JimServer;
import org.jim.server.command.CommandManager;
import org.jim.server.command.handler.ChatReqHandler;
import org.jim.server.command.handler.HandshakeReqHandler;
import org.jim.server.command.handler.LoginReqHandler;
import org.jim.server.config.ImServerConfig;
import org.jim.server.config.PropertyImServerConfigBuilder;
import org.jim.server.processor.chat.DefaultAsyncChatMessageProcessor;

/**
 * IM服务端DEMO演示启动类
 * @author powertime
 */
public class ImServerDemoStart {
    public static void main(String[] args) throws Exception {
        ImServerConfig imServerConfig = new PropertyImServerConfigBuilder("jim.properties").build();
        // 设置群组监听器，非必须，根据需要自己选择性实现
        imServerConfig.setImGroupListener(new ImDemoGroupListener());
        // 设置绑定用户监听器，非必须，根据自己的需要自己选择性实现
        imServerConfig.setImUserListener(new ImDemoUserListener());
        JimServer jimServer = new JimServer(imServerConfig);

        /*****************start 以下处理器根据业务需要自行添加与扩展，每个Command都可以添加扩展,此处为demo中处理**********************************/

        HandshakeReqHandler handshakeReqHandler = CommandManager.getCommand(Command.COMMAND_HANDSHAKE_REQ, HandshakeReqHandler.class);
        // 添加自定义握手处理器
        handshakeReqHandler.addMultiProtocolProcessor(new DemoWsHandshakeProcessor());
        LoginReqHandler loginReqHandler = CommandManager.getCommand(Command.COMMAND_LOGIN_REQ, LoginReqHandler.class);
        // 添加登录业务处理器
        loginReqHandler.setSingleProcessor(new LoginServiceProcessor());
        // 添加用户业务聊天记录处理器，用户自己继承抽象类BaseAsyncChatMessageProcessor即可，以下为内置默认的处理器！
        ChatReqHandler chatReqHandler = CommandManager.getCommand(Command.COMMAND_CHAT_REQ, ChatReqHandler.class);
        chatReqHandler.setSingleProcessor(new DefaultAsyncChatMessageProcessor());

        /*****************end *******************************************************************************************/

        jimServer.start();
    }
}
