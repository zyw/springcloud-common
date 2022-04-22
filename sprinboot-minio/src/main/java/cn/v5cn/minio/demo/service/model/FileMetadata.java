package cn.v5cn.minio.demo.service.model;

import org.springframework.util.Base64Utils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-12 13:00
 */
public class FileMetadata {

    private InputStream stream;

    private String origName;

    private Date createdTime;

    private long length;

    private String contentType;

    public FileMetadata(InputStream stream, String origName, Date createdTime, long length, String contentType) {
        this.stream = stream;
        this.origName = origName;
        this.createdTime = createdTime;
        this.length = length;
        this.contentType = contentType;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    public String getOrigName() {
        return new String(Base64Utils.decodeFromString(origName), StandardCharsets.UTF_8);
    }

    public void setOrigName(String origName) {
        this.origName = origName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "FileMetadata{" +
                "stream=" + stream +
                ", origName='" + origName + '\'' +
                ", createdTime=" + createdTime +
                ", length=" + length +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
