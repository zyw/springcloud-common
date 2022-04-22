// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: internal.proto

package cn.v5cn.netty.ws.pb.core.entity;

public interface InternalMsgOrBuilder extends
    // @@protoc_insertion_point(interface_extends:cn.v5cn.netty.ws.pb.generate.InternalMsg)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   *协议版本号。第一版本：1，以此类推。
   * </pre>
   *
   * <code>int32 version = 1;</code>
   * @return The version.
   */
  int getVersion();

  /**
   * <pre>
   *消息id
   * </pre>
   *
   * <code>int64 id = 2;</code>
   * @return The id.
   */
  long getId();

  /**
   * <pre>
   *发送模块
   * </pre>
   *
   * <code>.cn.v5cn.netty.ws.pb.generate.InternalMsg.Module from = 3;</code>
   * @return The enum numeric value on the wire for from.
   */
  int getFromValue();
  /**
   * <pre>
   *发送模块
   * </pre>
   *
   * <code>.cn.v5cn.netty.ws.pb.generate.InternalMsg.Module from = 3;</code>
   * @return The from.
   */
  InternalMsg.Module getFrom();

  /**
   * <pre>
   *接收模块
   * </pre>
   *
   * <code>.cn.v5cn.netty.ws.pb.generate.InternalMsg.Module dest = 4;</code>
   * @return The enum numeric value on the wire for dest.
   */
  int getDestValue();
  /**
   * <pre>
   *接收模块
   * </pre>
   *
   * <code>.cn.v5cn.netty.ws.pb.generate.InternalMsg.Module dest = 4;</code>
   * @return The dest.
   */
  InternalMsg.Module getDest();

  /**
   * <pre>
   *发送时间
   * </pre>
   *
   * <code>int64 createTime = 5;</code>
   * @return The createTime.
   */
  long getCreateTime();

  /**
   * <pre>
   *消息类型
   * </pre>
   *
   * <code>.cn.v5cn.netty.ws.pb.generate.InternalMsg.MsgType msgType = 6;</code>
   * @return The enum numeric value on the wire for msgType.
   */
  int getMsgTypeValue();
  /**
   * <pre>
   *消息类型
   * </pre>
   *
   * <code>.cn.v5cn.netty.ws.pb.generate.InternalMsg.MsgType msgType = 6;</code>
   * @return The msgType.
   */
  InternalMsg.MsgType getMsgType();

  /**
   * <pre>
   *消息体
   * </pre>
   *
   * <code>optional string msgBody = 7;</code>
   * @return Whether the msgBody field is set.
   */
  boolean hasMsgBody();
  /**
   * <pre>
   *消息体
   * </pre>
   *
   * <code>optional string msgBody = 7;</code>
   * @return The msgBody.
   */
  String getMsgBody();
  /**
   * <pre>
   *消息体
   * </pre>
   *
   * <code>optional string msgBody = 7;</code>
   * @return The bytes for msgBody.
   */
  com.google.protobuf.ByteString
      getMsgBodyBytes();
}
