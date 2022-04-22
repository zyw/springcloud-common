package cn.v5cn.netty.ws.pb.server.service;

import cn.v5cn.netty.ws.pb.core.parser.MsgParserService;
import cn.v5cn.netty.ws.pb.core.po.Offline;
import com.google.inject.Inject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zyw
 */
public class OfflineService {

    private final MsgParserService parserService;

    private static List<Offline> offlineList = new ArrayList<>();

    @Inject
    public OfflineService(MsgParserService parseService) {
        this.parserService = parseService;
    }

    public List<Message> pollOfflineMsg(String userId) {
        List<Offline> msgs = offlineList.stream()
                .filter(item -> userId.equals(String.valueOf(item.getId())))
                .collect(Collectors.toList());

        return msgs.stream()
                .map(o -> {
                    try {
                        return parserService.getMsgByCode(o.getMsgCode(), o.getContent());
                    } catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
