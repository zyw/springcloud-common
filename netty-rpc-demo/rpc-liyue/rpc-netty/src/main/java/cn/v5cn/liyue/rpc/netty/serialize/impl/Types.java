package cn.v5cn.liyue.rpc.netty.serialize.impl;

/**
 * 序列化的对象类型
 * @author LiYue
 * Date: 2019/9/20
 */
public class Types {
    /**
     * String类型
     */
    public final static int TYPE_STRING = 0;
    /**
     *   { @link cn.v5cn.liyue.rpc.netty.nameservice.Metadata } 类 类型
     */
    public final static int TYPE_METADATA = 100;
    /**
     * RPCRequest 类型
     */
    public final static int TYPE_RPC_REQUEST = 101;
    /**
     * Object对象序列化
     */
    public final static int TYPE_OBJECT = 102;
    /**
     * Object对象数组序列化
     */
    public final static int TYPE_OBJECT_ARRAY = 103;
}
