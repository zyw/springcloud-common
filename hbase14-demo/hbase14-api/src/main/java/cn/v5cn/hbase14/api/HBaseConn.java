package cn.v5cn.hbase14.api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-05 14:04
 */
public class HBaseConn {
    private static final HBaseConn HBASE_CONN = new HBaseConn();

    private static Configuration configuration;
    private static Connection connection;

    /**
     * 远程连接hbase的坑
     * https://blog.csdn.net/yz930618/article/details/74173332
     * 需要在本机配置hosts,hbase所在机器的hostname和hbase所在机器的IP，
     * 为什么需要在本机配置hbase所在机器的hostname到ip的映射呢，主要与hbase的读写流程有关，
     * hbase的读写都要先跟zookeeper交互，然后拿到hbase的regionserver地址，然后在跟regionserver进行交互，
     * 而恰恰注册到zookeeper的地址是hbase所在机器的hostname而不是ip地址，所有要配置映射才能正常交互。
     */
    private HBaseConn() {
        if(configuration == null) {
            configuration = HBaseConfiguration.create();
            configuration.set("hbase.zookeeper.quorum", "192.168.33.12"); //服务器
            configuration.set("hbase.zookeeper.property.clientPort","2181"); //端口号
        }
    }

    private Connection getConnection() {
        if(connection == null || connection.isClosed()) {
            try {
                connection = ConnectionFactory.createConnection(configuration);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * 获得Connecton对象
     * @return 返回对象
     */
    public static Connection instanceConn() {
        return HBASE_CONN.getConnection();
    }

    /**
     * 获得HBase表
     * @param tableName 表名称
     * @return 返回Table对象
     * @throws IOException
     */
    public static Table getTable(String tableName) throws IOException {
        return HBASE_CONN.getConnection().getTable(TableName.valueOf(tableName));
    }

    public static void closeConn() {
        if(connection != null && !connection.isClosed()) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
