package cn.v5cn.netty.im.connector.service.rest;

import cn.v5cn.netty.im.common.domain.po.Offline;
import cn.v5cn.netty.im.common.rest.AbstractRestService;
import com.google.inject.Inject;

import java.util.List;

/**
 * Rest 服务
 * @author yrw
 */
public class ConnectorRestService extends AbstractRestService<ConnectorRestClient> {

    @Inject
    public ConnectorRestService(String url) {
        super(ConnectorRestClient.class, url);
    }

    public List<Offline> offlines(String token) {
        return doRequest(() -> restClient.pollOfflineMsg(token).execute());
    }
}
