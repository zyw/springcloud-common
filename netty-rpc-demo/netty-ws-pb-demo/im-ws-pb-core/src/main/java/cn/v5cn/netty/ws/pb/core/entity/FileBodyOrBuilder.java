// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: chat.proto

package cn.v5cn.netty.ws.pb.core.entity;

public interface FileBodyOrBuilder extends
    // @@protoc_insertion_point(interface_extends:cn.v5cn.netty.ws.pb.generate.FileBody)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * 媒体文件上传得到的KEY,用于生成下载URL
   * </pre>
   *
   * <code>string fileId = 1;</code>
   * @return The fileId.
   */
  java.lang.String getFileId();
  /**
   * <pre>
   * 媒体文件上传得到的KEY,用于生成下载URL
   * </pre>
   *
   * <code>string fileId = 1;</code>
   * @return The bytes for fileId.
   */
  com.google.protobuf.ByteString
      getFileIdBytes();

  /**
   * <pre>
   * 文件的CRC32校验码
   * </pre>
   *
   * <code>int32 media_crc32 = 2;</code>
   * @return The mediaCrc32.
   */
  int getMediaCrc32();

  /**
   * <pre>
   * 文件大小（字节数）
   * </pre>
   *
   * <code>int32 fSize = 3;</code>
   * @return The fSize.
   */
  int getFSize();

  /**
   * <pre>
   * 文件名称
   * </pre>
   *
   * <code>string fName = 4;</code>
   * @return The fName.
   */
  java.lang.String getFName();
  /**
   * <pre>
   * 文件名称
   * </pre>
   *
   * <code>string fName = 4;</code>
   * @return The bytes for fName.
   */
  com.google.protobuf.ByteString
      getFNameBytes();
}