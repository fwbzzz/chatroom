package fwb.chatroom.dao;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import fwb.chatroom.utils.CommUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
/*
1. 加载数据源
2. 获取数据库连接
3. 执行SQL语句（AccountDao层）
利用连接池框架，数据源连接后，如果不使用，可以暂时放到池中，不关闭连接，而直连是每次创建新的连接，使
用完毕后，手工去关闭，下次要使用，再次创建、关闭。连接池框架实现了池的缓存能力后，就无需这样了。
4. 释放资源
 */

public class BaseDao {
    private static DataSource dataSource;
    static {
        Properties properties = CommUtils.
                loadProperties("datasource.properties");
        try {
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            System.err.println("数据源加载失败");
        }
    }

    // 获取数据库连接
    protected Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            System.err.println("获取连接失败");
        }
        return null;
    }

    // 关闭资源Statement ResultSet Connection
    protected void closeResources(Connection connection,
                                  Statement statement) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void closeResources(Connection connection,
                                  Statement statement,
                                  ResultSet resultSet) {
        closeResources(connection,statement);
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
