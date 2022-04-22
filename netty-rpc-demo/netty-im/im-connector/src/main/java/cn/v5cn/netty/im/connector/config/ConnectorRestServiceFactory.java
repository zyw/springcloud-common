package cn.v5cn.netty.im.connector.config;

import cn.v5cn.netty.im.connector.service.rest.ConnectorRestService;

/**
 * @author yrw
 */
public interface ConnectorRestServiceFactory {
    /**
     * create a ConnectorRestService
     * //todo: need to be singleton
     *
     * @param url
     * @return
     */
    ConnectorRestService createService(String url);
}
