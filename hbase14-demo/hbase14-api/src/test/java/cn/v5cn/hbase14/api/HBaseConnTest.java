package cn.v5cn.hbase14.api;

import org.apache.hadoop.hbase.client.Table;
import org.junit.Test;

import java.io.IOException;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-05 14:32
 */
public class HBaseConnTest {

    @Test
    public void testInstanceConn() {
        System.out.println(HBaseConn.instanceConn().isClosed());
    }

    @Test
    public void testGetTable() {
        try {
            Table table = HBaseConn.getTable("FileTable");
            System.out.println(table.getName().getNameAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
