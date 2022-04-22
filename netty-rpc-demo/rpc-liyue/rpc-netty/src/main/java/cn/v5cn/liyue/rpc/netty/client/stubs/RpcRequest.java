package cn.v5cn.liyue.rpc.netty.client.stubs;

import java.util.Arrays;

/**
 * Rpc请求数据封装
 * @author LiYue
 * Date: 2019/9/27
 */
public class RpcRequest {
    private final String interfaceName;
    private final String methodName;
    private final byte[] serializedArguments;

    public RpcRequest(String interfaceName, String methodName, byte[] serializedArguments) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.serializedArguments = serializedArguments;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public byte[] getSerializedArguments() {
        return serializedArguments;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "interfaceName='" + interfaceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", serializedArguments=" + Arrays.toString(serializedArguments) +
                '}';
    }
}
