package cn.v5cn.cim.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author crossoverJie
 */
@Component
public class AppConfiguration {

    @Value("${app.zk.root}")
    private String zkRoot;

    @Value("${app.zk.addr}")
    private String zkAddr;

    @Value("${app.zk.switch}")
    private boolean zkSwitch;

    @Value("${cim.server.port}")
    private int cimServerPort;

    @Value("${cim.route.url}")
    private String routeUrl ;

    public String getRouteUrl() {
        return routeUrl;
    }

    public void setRouteUrl(String routeUrl) {
        this.routeUrl = routeUrl;
    }

    @Value("${cim.heartbeat.time}")
    private long heartBeatTime ;

    @Value("${app.zk.connect.timeout}")
    private int zkConnectTimeout;

    public int getZkConnectTimeout() {
        return zkConnectTimeout;
    }

    public String getZkRoot() {
        return zkRoot;
    }

    public void setZkRoot(String zkRoot) {
        this.zkRoot = zkRoot;
    }

    public String getZkAddr() {
        return zkAddr;
    }

    public void setZkAddr(String zkAddr) {
        this.zkAddr = zkAddr;
    }

    public boolean isZkSwitch() {
        return zkSwitch;
    }

    public void setZkSwitch(boolean zkSwitch) {
        this.zkSwitch = zkSwitch;
    }

    public int getCimServerPort() {
        return cimServerPort;
    }

    public void setCimServerPort(int cimServerPort) {
        this.cimServerPort = cimServerPort;
    }

    public long getHeartBeatTime() {
        return heartBeatTime;
    }

    public void setHeartBeatTime(long heartBeatTime) {
        this.heartBeatTime = heartBeatTime;
    }
}
