package cn.v5cn.phoenix.jdbc;

import java.sql.*;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-02-06 21:57
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");

        Connection connection = DriverManager.getConnection("jdbc:phoenix:hbase1:2181");

        PreparedStatement statement = connection.prepareStatement("select * from user");
        ResultSet rs = statement.executeQuery();

        while (rs.next()) {
            String id = rs.getString("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");

            System.out.println("ID: " + id + " 姓名：" + name + " 年龄：" + age);
        }
    }
}
