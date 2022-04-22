package cn.v5cn.cim.common.pojo;

/**
 * @author crossoverJie
 */
public class RouteInfo {

    private String ip;
    private Integer cimServerPort;
    private Integer httpPort;

    public RouteInfo(String ip, Integer cimServerPort, Integer httpPort) {
        this.ip = ip;
        this.cimServerPort = cimServerPort;
        this.httpPort = httpPort;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getCimServerPort() {
        return cimServerPort;
    }

    public void setCimServerPort(Integer cimServerPort) {
        this.cimServerPort = cimServerPort;
    }

    public Integer getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }
}
