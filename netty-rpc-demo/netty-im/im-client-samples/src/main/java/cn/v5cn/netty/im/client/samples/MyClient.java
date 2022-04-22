package cn.v5cn.netty.im.client.samples;

import cn.v5cn.netty.im.client.ImClient;
import cn.v5cn.netty.im.client.api.ChatApi;
import cn.v5cn.netty.im.client.api.ClientMsgListener;
import cn.v5cn.netty.im.client.domain.Friend;
import cn.v5cn.netty.im.common.domain.UserInfo;
import cn.v5cn.netty.im.protobuf.generate.Chat;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class MyClient {
    private static Logger logger = LoggerFactory.getLogger(MyClient.class);

    private ChatApi chatApi;
    private UserInfo userInfo;

    private Map<String, Friend> friendMap;

    public MyClient(String connectorHost, Integer connectorPort, String restUrl, String username, String password) {

    }

    private ImClient start(String connectorHost, Integer connectorPort, String restUrl) {
        ImClient imClient = new ImClient(connectorHost, connectorPort, restUrl);
        imClient.setClientMsgListener(new ClientMsgListener() {
            @Override
            public void online() {
                logger.info("[client] i have connected to server!");
            }

            @Override
            public void read(Chat.ChatMsg chatMsg) {
                //when it's confirmed that user has read this msg
                System.out.println(friendMap.get(chatMsg.getFromId()).getUsername() + ": "
                        + chatMsg.getMsgBody().toStringUtf8());
                chatApi.confirmRead(chatMsg);
            }

            @Override
            public void hasSent(Long id) {
                System.out.println(String.format("msg {%d} has been sent", id));
            }

            @Override
            public void hasDelivered(Long id) {
                System.out.println(String.format("msg {%d} has been delivered", id));
            }

            @Override
            public void hasRead(Long id) {
                System.out.println(String.format("msg {%d} has been read", id));
            }

            @Override
            public void offline() {
                logger.info("[{}] I am offline!", userInfo != null ? userInfo.getUsername() : "client");
            }

            @Override
            public void hasException(ChannelHandlerContext ctx, Throwable cause) {
                logger.error("[" + userInfo.getUsername() + "] has error ", cause);
            }
        });

        imClient.start();

        return imClient;
    }

    public void printUserInfo() {
        System.out.println("id: " + userInfo.getId());
        System.out.println("username: " + userInfo.getUsername());
    }

    public void send(String id, String text) {
        if(!friendMap.containsKey(id)) {
            System.out.println("friend " + id + " not found!");
            return;
        }
        chatApi.text(id, text);
    }
}
