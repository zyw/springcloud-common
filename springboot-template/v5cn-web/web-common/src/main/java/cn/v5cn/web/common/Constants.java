package cn.v5cn.web.common;

public interface Constants {

    /**
     * 文件路径分隔符，windows
     */
    String PATH_SEPARATOR_WIN = "\\";

    /**
     * 文件路径分隔符 linux
     */
    String PATH_SEPARATOR_LINUX = "/";

    /**
     * 分割符
     */
    String SEPARATOR = ",";

    /**
     * 业务日志的AOP
     */
    int BUSINESS_LOG_AOP = 100;

    /**
     * 未知标识
     */
    String UNKNOWN = "Unknown";

    /**
     * 登录用户保存在session中的用户名key
     */
    String LOGIN_USER_NAME_KEY = "LOGIN-USER-NAME";

    /**
     * 请求号在header中的唯一标识
     */
    String REQUEST_NO_HEADER_NAME = "Request-No";

    /**
     * 用户代理
     */
    String USER_AGENT = "User-Agent";

    /**
     * 请求头token表示
     */
    String AUTHORIZATION = "Authorization";

    ///////////////////////////////符号/////////////////////////////////////

    String COLON = ":";

    String DASH = "-";

    /**
     * 登录失败次数
     */
    int FAIL_COUNT = 5;

    /**
     * 登录失败锁定时间 秒
     */
    int LOCK_TIME = 300;

    /**
     * 登录失败锁定时间 秒
     */
    String LOCK_NAMESPACE = "lock:";

}
