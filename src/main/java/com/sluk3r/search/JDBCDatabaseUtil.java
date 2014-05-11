package com.sluk3r.search;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: wangxc
 * Date: 14-5-11
 * Time: 下午4:49
 * To change this template use File | Settings | File Templates.
 */
public class JDBCDatabaseUtil {
    /**
     * Gets the data source.
     *
     * @return the data source
     */
    public static DataSource getDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setUrl("jdbc:mysql://localhost:3306/search_schema?emulateLocators=true&useUnicode=true&characterEncoding=UTF-8&useFastDateParsing=false");
        return dataSource;
    }

    /**
     * Gets the connection.
     *
     * @return the connection
     * @throws SQLException
     *             the sQL exception
     */
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }
}
