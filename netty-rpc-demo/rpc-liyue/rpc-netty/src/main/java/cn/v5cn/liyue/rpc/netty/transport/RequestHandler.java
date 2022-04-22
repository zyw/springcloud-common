package cn.v5cn.liyue.rpc.netty.transport;

import cn.v5cn.liyue.rpc.netty.transport.command.Command;

/**
 * 请求处理器
 * @author LiYue
 * Date: 2019/9/20
 */
public interface RequestHandler {
    /**
     * 处理请求
     * @param request  请求命令
     * @return 响应命令
     */
    Command handle(Command request);

    /**
     * 支持的请求类型
     * @return
     */
    int type();
}
