package cn.v5cn.hbase14.api;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-05 14:46
 */
public class HBaseUtils {

    /**
     * 创建表
     * @param tableName 表面
     * @param cfs 列族的数组
     * @return 是否创建成功
     */
    public static boolean createTable(String tableName,String... cfs) {
        try (HBaseAdmin admin = (HBaseAdmin) HBaseConn.instanceConn().getAdmin()) {
            if(admin.tableExists(tableName)) {
                return false;
            }

            HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            Arrays.stream(cfs).forEach(cf -> {
                HColumnDescriptor columnDescriptor = new HColumnDescriptor(cf);
                columnDescriptor.setMaxVersions(1);
                tableDescriptor.addFamily(columnDescriptor);
            });
            admin.createTable(tableDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 删除表
     * @param tableName 表名
     * @return
     */
    public static boolean deleteTable(String tableName) {
        try (HBaseAdmin admin = (HBaseAdmin) HBaseConn.instanceConn().getAdmin()) {
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * hbase插入数据
     * @param tableName 表名
     * @param rowKey 行键
     * @param cfName 列族名
     * @param qualifier 列标识
     * @param data 数据
     * @return 是否插入成功
     */
    public static boolean putRow(String tableName,String rowKey,String cfName,String qualifier,String data) {
        try (Table table = HBaseConn.getTable(tableName)){
            Put put = new Put(Bytes.toBytes(rowKey));
            put.addColumn(Bytes.toBytes(cfName),Bytes.toBytes(qualifier),Bytes.toBytes(data));
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 批量hbase传入数据
     * @param tableName 表名
     * @param puts put列表
     * @return 返回是否插入成功
     */
    public static boolean putRows(String tableName, List<Put> puts) {
        try (Table table = HBaseConn.getTable(tableName)) {
            table.put(puts);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 根据rowKey查询数据
     * @param tableName 表名
     * @param rowkey 唯一标识
     * @return 返回查询结果
     */
    public static Result getRow(String tableName,String rowkey) {
        try (Table table = HBaseConn.getTable(tableName)) {
            Get get = new Get(Bytes.toBytes(rowkey));
            return table.get(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据rowKey查询数据
     * @param tableName 表名
     * @param rowkey 唯一标识
     * @param filterList 过滤器集合
     * @return 返回查询结果
     */
    public static Result getRow(String tableName, String rowkey, FilterList filterList) {
        try (Table table = HBaseConn.getTable(tableName)) {
            Get get = new Get(Bytes.toBytes(rowkey));
            get.setFilter(filterList);
            return table.get(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 批量检索数据
     * @param tableName 表名
     * @return
     */
    public static ResultScanner getScanner(String tableName) {
        try (Table table = HBaseConn.getTable(tableName)) {
            Scan scan = new Scan();
            scan.setCaching(1000);
            return table.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultScanner getScanner(String tableName,String startRowKey,String endRowKey) {
        try (Table table = HBaseConn.getTable(tableName)) {
            Scan scan = new Scan();
            scan.withStartRow(Bytes.toBytes(startRowKey));
            scan.withStopRow(Bytes.toBytes(endRowKey));
            scan.setCaching(1000);
            return table.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResultScanner getScanner(String tableName,String startRowKey,String endRowKey,FilterList filterList) {
        try (Table table = HBaseConn.getTable(tableName)) {
            Scan scan = new Scan();
            scan.withStartRow(Bytes.toBytes(startRowKey));
            scan.withStopRow(Bytes.toBytes(endRowKey));
            scan.setFilter(filterList);
            scan.setCaching(1000);
            return table.getScanner(scan);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteRow(String tableName,String rowKey) {
        try (Table table = HBaseConn.getTable(tableName)) {
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            table.delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean deleteColumnFamily(String tableName,String cfName) {
        try (HBaseAdmin admin = (HBaseAdmin)HBaseConn.instanceConn().getAdmin()) {
            admin.deleteColumn(tableName,cfName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean deleteQualifier(String tableName,String rowKey,String cfName,String qualifier) {
        try (Table table = HBaseConn.getTable(tableName)) {
            Delete delete = new Delete(Bytes.toBytes(rowKey));
            delete.addColumn(Bytes.toBytes(cfName),Bytes.toBytes(qualifier));
            table.delete(delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

}
