package cn.v5cn.hadoop3.hdfs.demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import java.io.IOException;
import java.net.URI;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-15 15:34
 */
public class HdfsFileSystem {
    public static void main(String[] args) throws IOException {
        FileSystem fileSystem = FileSystem.get(URI.create("hdfs://hadoop2:9000"), new Configuration());
        //获取/out目录中的文件信息
        RemoteIterator<LocatedFileStatus> listFiles = fileSystem.listFiles(new Path("/out"), false);
        while (listFiles.hasNext()) {
            LocatedFileStatus status = listFiles.next();
            System.out.println(status.getPath().getName());
            System.out.println(status.getGroup());
            System.out.println(status.getOwner());
        }
    }
}
