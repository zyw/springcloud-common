package cn.v5cn.netty.im.connector.service;

import cn.v5cn.netty.im.common.domain.po.Offline;
import cn.v5cn.netty.im.common.parse.ParseService;
import cn.v5cn.netty.im.connector.config.ConnectorRestServiceFactory;
import cn.v5cn.netty.im.connector.service.rest.ConnectorRestService;
import cn.v5cn.netty.im.connector.start.ConnectorStarter;
import com.google.inject.Inject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yrw
 */
public class OfflineService {

    private final ConnectorRestService connectorRestService;
    private final ParseService parseService;

    @Inject
    public OfflineService(ConnectorRestServiceFactory connectorRestServiceFactory, ParseService parseService) {
        this.connectorRestService = connectorRestServiceFactory.createService(ConnectorStarter.CONNECTOR_CONFIG.getRestUrl());
        this.parseService = parseService;
    }

    public List<Message> pollOfflineMsg(String userId) {
        List<Offline> msgs = connectorRestService.offlines(userId);
        return msgs.stream()
                .map(o -> {
                    try {
                        return parseService.getMsgByCode(o.getMsgCode(), o.getContent());
                    } catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
