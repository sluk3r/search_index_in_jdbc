package com.sluk3r.search.index; /**
 * Created by IntelliJ IDEA.
 * User: wangxc
 * Date: 14-5-11
 * Time: 下午4:46
 * To change this template use File | Settings | File Templates.
 */



import java.io.IOException;
import javax.sql.DataSource;

import org.apache.lucene.store.jdbc.JdbcDirectory;
import org.apache.lucene.store.jdbc.JdbcDirectorySettings;
import org.apache.lucene.store.jdbc.JdbcStoreException;
import org.apache.lucene.store.jdbc.dialect.Dialect;
import org.apache.lucene.store.jdbc.support.JdbcTable;


public class MyJDBCDirectory extends JdbcDirectory {
    /**
     * Instantiates a new my jdbc directory.
     *
     * @param dataSource
     *            the data source
     * @param dialect
     *            the dialect
     * @param settings
     *            the settings
     * @param tableName
     *            the table name
     */
    public MyJDBCDirectory(DataSource dataSource, Dialect dialect, JdbcDirectorySettings settings, String tableName) {
        super(dataSource, dialect, settings, tableName);
    }

    /**
     * Instantiates a new my jdbc directory.
     *
     * @param dataSource the data source
     * @param dialect the dialect
     * @param tableName the table name
     */
    public MyJDBCDirectory(DataSource dataSource, Dialect dialect, String tableName) {
        super(dataSource, dialect, tableName);
    }

    /**
     * Instantiates a new my jdbc directory.
     *
     * @param dataSource the data source
     * @param settings the settings
     * @param tableName the table name
     * @throws JdbcStoreException the jdbc store exception
     */
    public MyJDBCDirectory(DataSource dataSource, JdbcDirectorySettings settings, String tableName) throws JdbcStoreException {
        super(dataSource, settings, tableName);
    }

    /**
     * Instantiates a new my jdbc directory.
     *
     * @param dataSource the data source
     * @param table the table
     */
    public MyJDBCDirectory(DataSource dataSource, JdbcTable table) {
        super(dataSource, table);
    }

    /**
     * Instantiates a new my jdbc directory.
     *
     * @param dataSource the data source
     * @param tableName the table name
     * @throws JdbcStoreException the jdbc store exception
     */
    public MyJDBCDirectory(DataSource dataSource, String tableName) throws JdbcStoreException {
        super(dataSource, tableName);
    }

    /**
     * (non-Javadoc).
     *
     * @return the string[]
     * @throws IOException Signals that an I/O exception has occurred.
     * @see org.apache.lucene.store.Directory#listAll()
     */
    @Override
    public String[] listAll() throws IOException {
        return super.list();
    }
}
