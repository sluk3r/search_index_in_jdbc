package com.sluk3r.search.index;
/**
 * Created by IntelliJ IDEA.
 * User: wangxc
 * Date: 14-5-11
 * Time: 下午4:48
 * To change this template use File | Settings | File Templates.
 */


import com.sluk3r.search.JDBCDatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCBatchInsert {
    /** The Constant QUERY. */
    private static final String     QUERY           = "INSERT INTO BOOKS (BOOK_ID, BOOK_NAME, BOOK_AUTHOR, BOOK_PUBLISHER) VALUES (?, ?, ?, ?)";

    /** The Constant BOOK_FIRST_PART. */
    private final static String[]   BOOK_FIRST_PART = {"Spring", "Hibernate", "Lucene", "Mahout", "JPA", "JSF", "Swing", "Hadoop", "Hbase"};

    /** The Constant BOOK_LAST_PART. */
    private final static String[]   BOOK_LAST_PART  = {"In Action", "Complete Reference", "Demystified", "Tutorial", "Explained",
            "Simplified", "Bible", "Cook Book", "Crash Course"};

    /** The Constant BLANK_SPACE. */
    private final static String     BLANK_SPACE     = " ";

    /**
     * Insert records.
     */
    public void insertRecords() {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCDatabaseUtil.getConnection();
            pstmt = connection.prepareStatement(QUERY);
            int index = 0;
            for (String firstPart : BOOK_FIRST_PART) {
                for (String lastPart : BOOK_LAST_PART) {
                    pstmt.setInt(1, ++index);
                    pstmt.setString(2, firstPart + BLANK_SPACE + lastPart);
                    pstmt.setString(3, "Test Author" + BLANK_SPACE + firstPart + BLANK_SPACE + lastPart + index);
                    pstmt.setString(4, "Test Publisher" + BLANK_SPACE + firstPart + BLANK_SPACE + lastPart + index);
                    pstmt.addBatch();
                }
            }
            pstmt.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
                resultSet = null;
                pstmt = null;
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
