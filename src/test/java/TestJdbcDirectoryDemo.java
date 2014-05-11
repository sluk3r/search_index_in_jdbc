import com.sluk3r.search.JDBCDatabaseUtil;
import com.sluk3r.search.index.JDBCBatchInsert;
import com.sluk3r.search.index.JDBCIndexer;
import com.sluk3r.search.index.MyJDBCDirectory;
import com.sluk3r.search.search.JDBCSearcher;
import com.sluk3r.search.utils.ScriptRunner;
import junit.framework.TestCase;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.jdbc.dialect.MySQLDialect;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;

import static junit.framework.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: wangxc
 * Date: 14-5-11
 * Time: 下午5:37
 * To change this template use File | Settings | File Templates.
 */
public class TestJdbcDirectoryDemo {
    private Directory directory   = null;
    ScriptRunner sqlRunner = null;

    @Before
    public void setUp() throws IOException, SQLException {
        directory = new MyJDBCDirectory(JDBCDatabaseUtil.getDataSource(), new MySQLDialect(), "LUCENE_INDEX_TABLE");

        createTable();

        new JDBCBatchInsert().insertRecords();
        new JDBCIndexer(directory).buildIndex();
    }

    @After
    public void tearDown() throws Exception {
        if(directory != null) {
            directory.close();
        }


        String dropBookTable = "drop table `search_schema`.`books`;";
        String dropIndexTable = "drop table `search_schema`.`lucene_index_table`;";

        sqlRunner.runScript(dropBookTable + System.getProperty("line.separator") + dropIndexTable);
    }

    public void testSearchRecordOnName() {
        assertTrue(new JDBCSearcher(directory).search("name", "Spring In Action"));
    }

    /**
     * Test com.sluk3r.search record fail on name.
     */
    public void testSearchRecordFailOnName() {
        assertTrue(new JDBCSearcher(directory).search("name", "No Such BookName"));
    }

    /**
     * Test com.sluk3r.search record on author.
     */
    public void testSearchRecordOnAuthor() {
        assertTrue(new JDBCSearcher(directory).search("author", "Test Author Hibernate In Action10"));
    }

    /**
     * Test com.sluk3r.search record fail on author.
     */
    public void testSearchRecordFailOnAuthor() {
        assertTrue(new JDBCSearcher(directory).search("name", "No Such Author"));
    }

    /**
     * Test com.sluk3r.search record on publisher.
     */
    public void testSearchRecordOnPublisher() {
        assertTrue(new JDBCSearcher(directory).search("publisher", "Test Publisher Spring Bible7"));
    }

    /**
     * Test com.sluk3r.search record fail on publisher.
     */
    public void testSearchRecordFailOnPublisher() {
        assertTrue(new JDBCSearcher(directory).search("name", "No Such Publisher"));
    }

    //setup table
    private void createTable() throws SQLException, IOException {
        //执行schema创建脚本
        boolean autoCommit = false;
        boolean stopOnError = true;
        Connection connection = JDBCDatabaseUtil.getConnection();
        sqlRunner = new ScriptRunner(connection, autoCommit, stopOnError);
        InputStream resource = TestJdbcDirectoryDemo.class.getResourceAsStream("./schema.sql");
        Reader reader = new InputStreamReader(resource);
        sqlRunner.runScript(reader);
    }
}
