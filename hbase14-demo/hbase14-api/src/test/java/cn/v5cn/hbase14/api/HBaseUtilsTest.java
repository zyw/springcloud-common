package cn.v5cn.hbase14.api;

import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-05 20:46
 */
public class HBaseUtilsTest {

    /**
     * 创建表，本机需要配置hbase的hastname到ip地址
     */
    @Test
    public void createTable() {
        Assert.assertTrue(HBaseUtils.createTable("FileTable1", "fileInfo", "saveInfo"));
    }

    @Test
    public void addFileDetails() {
        HBaseUtils.putRow("FileTable1", "rowkey1", "fileInfo", "name", "file1.txt");
        HBaseUtils.putRow("FileTable1", "rowkey1", "fileInfo", "type", "txt");
        HBaseUtils.putRow("FileTable1", "rowkey1", "fileInfo", "size", "1024");
        HBaseUtils.putRow("FileTable1", "rowkey1", "saveInfo", "creator", "jixin");
        HBaseUtils.putRow("FileTable1", "rowkey2", "fileInfo", "name", "file2.jpg");
        HBaseUtils.putRow("FileTable1", "rowkey2", "fileInfo", "type", "jpg");
        HBaseUtils.putRow("FileTable1", "rowkey2", "fileInfo", "size", "1024");
        HBaseUtils.putRow("FileTable1", "rowkey2", "saveInfo", "creator", "jixin");
    }

    @Test
    public void scanFileDetails() {
        ResultScanner scanner = HBaseUtils.getScanner("FileTable1", "rowkey1", "rowkey2");
        System.out.println(scanner);
        if(scanner != null) {
            scanner.forEach(result -> {
                System.out.println("rowkey=" + Bytes.toString(result.getRow()));
                System.out.println("fileName=" + Bytes.toString(result.getValue(Bytes.toBytes("fileInfo"),Bytes.toBytes("name"))));
            });
            scanner.close();
        }
    }

    @Test
    public void deleteRowTest() {
        HBaseUtils.deleteRow("FileTable1","rowkey2");
    }

    @Test
    public void deleteTable() {
        HBaseUtils.deleteTable("FileTable1");
    }

}
