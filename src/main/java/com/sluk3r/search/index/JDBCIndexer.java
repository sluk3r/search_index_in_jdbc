package com.sluk3r.search.index;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sluk3r.search.JDBCDatabaseUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.jdbc.JdbcDirectory;
import org.apache.lucene.store.jdbc.dialect.MySQLDialect;
import org.apache.lucene.util.Version;

/**
 * Created by IntelliJ IDEA.
 * User: wangxc
 * Date: 14-5-11
 * Time: 下午4:49
 * To change this template use File | Settings | File Templates.
 */
public class JDBCIndexer {
    /** The jdbc directory. */
    private Directory jdbcDirectory   = null;

    /**
     * Instantiates a new jDBC indexer.
     *
     * @param jdbcDirectory
     *            the jdbc directory
     */
    public JDBCIndexer(Directory jdbcDirectory) {
        super();
        this.jdbcDirectory = jdbcDirectory;
    }
    /**
     * Gets the jdbc directory.
     *
     * @return the jdbc directory
     */
    public Directory getJdbcDirectory() {
        if (jdbcDirectory == null) {
            throw new IllegalStateException("Index not yet build, rerun indexing");
        }
        return jdbcDirectory;
    }

    /**
     * Sets the jdbc directory.
     *
     * @param jdbcDirectory
     *            the new jdbc directory
     */
    public void setJdbcDirectory(Directory jdbcDirectory) {
        this.jdbcDirectory = jdbcDirectory;
    }

    /**
     * Builds the index.
     */
    public void buildIndex() {
        createAndBuildIndex();
    }

    /**
     * Creates the and build index.
     */
    private void createAndBuildIndex() {
        createIndexTable();
        index();
    }

    /**
     * Index.
     */
    private void index() {
        Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_36);
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, analyzer);
        IndexWriter indexWriter = null;
        try {
            indexWriter = new IndexWriter(getJdbcDirectory(), indexWriterConfig);
            addIndex(indexWriter);
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (LockObtainFailedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (CorruptIndexException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    indexWriter = null;
                }
            }
        }
    }

    /**
     * Add index on records present in BOOKS table
     *
     * @param indexWriter
     *            the index writer
     */
    private void addIndex(IndexWriter indexWriter) {
        try {
            Connection connection = JDBCDatabaseUtil.getConnection();
            String query = "SELECT BOOK_ID, BOOK_NAME, BOOK_AUTHOR, BOOK_PUBLISHER FROM BOOKS";
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                Document document = new Document();
                document.add(new Field("name", String.valueOf(resultSet.getString(2)), Field.Store.YES, Field.Index.ANALYZED));
                document.add(new Field("author", String.valueOf(resultSet.getString(3)), Field.Store.YES, Field.Index.ANALYZED));
                document.add(new Field("publisher", String.valueOf(resultSet.getString(4)), Field.Store.YES, Field.Index.ANALYZED));
                indexWriter.addDocument(document);              }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (CorruptIndexException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
     * Creates the index table.
     */
    private void createIndexTable() {
        if (this.jdbcDirectory == null) {
            setJdbcDirectory(new MyJDBCDirectory(JDBCDatabaseUtil.getDataSource(), new MySQLDialect(), "LUCENE_INDEX_TABLE"));
        }
        try {
            /**
             * No need to manually create index table, create method will
             * automatically create it.
             */
            ((JdbcDirectory) getJdbcDirectory()).create();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
