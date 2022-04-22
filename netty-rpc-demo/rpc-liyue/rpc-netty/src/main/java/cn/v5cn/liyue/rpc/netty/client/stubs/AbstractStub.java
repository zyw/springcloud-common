package cn.v5cn.liyue.rpc.netty.client.stubs;

import cn.v5cn.liyue.rpc.netty.client.RequestIdSupport;
import cn.v5cn.liyue.rpc.netty.client.ServiceStub;
import cn.v5cn.liyue.rpc.netty.client.ServiceTypes;
import cn.v5cn.liyue.rpc.netty.serialize.SerializeSupport;
import cn.v5cn.liyue.rpc.netty.transport.Transport;
import cn.v5cn.liyue.rpc.netty.transport.command.Code;
import cn.v5cn.liyue.rpc.netty.transport.command.Command;
import cn.v5cn.liyue.rpc.netty.transport.command.Header;
import cn.v5cn.liyue.rpc.netty.transport.command.ResponseHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author LiYue
 * Date: 2019/9/27
 */
public class AbstractStub implements ServiceStub {

    private static final Logger logger = LoggerFactory.getLogger(AbstractStub.class);

    protected Transport transport;

    protected byte[] invokeRemote(RpcRequest request) {
        logger.info("RpcRequest: {}",request.toString());
        Header header = new Header(ServiceTypes.TYPE_RPC_REQUEST,1, RequestIdSupport.next());
        byte[] payload = SerializeSupport.serialize(request);
        Command command = new Command(header, payload);
        try {
            Command responseCommand = transport.send(command).get();
            ResponseHeader responseHeader = (ResponseHeader) responseCommand.getHeader();
            if(responseHeader.getCode() == Code.SUCCESS.getCode()) {
                return responseCommand.getPayload();
            } else {
                throw new Exception(responseHeader.getError());
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}
