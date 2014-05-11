package com.sluk3r.search.search; /**
 * Created by IntelliJ IDEA.
 * User: wangxc
 * Date: 14-5-11
 * Time: 下午4:51
 * To change this template use File | Settings | File Templates.
 */
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public class JDBCSearcher {
    /** The directory. */
    private Directory   directory   = null;

    /**
     * The Constructor.
     *
     * @param directory
     *            the directory
     */
    public JDBCSearcher(Directory directory) {
        this.directory = directory;
    }

    /**
     * Search.
     *
     * @param columnName
     *            the file name
     * @return the string
     */
    public boolean search(String columnName, String value) {
        IndexSearcher indexSearcher = null;
        try {
            /**
             * Specify the version
             */
            Analyzer analyzer = new SimpleAnalyzer(Version.LUCENE_36);
            /**
             * Create query columnname (index name passed), we built out index for name, author and publisher so
             * we have to com.sluk3r.search against the same.
             */
            Query query = new QueryParser(Version.LUCENE_36, columnName, analyzer).parse(value);
            IndexReader indexReader = IndexReader.open(directory);
            indexSearcher = new IndexSearcher(indexReader);
            /**
             * This will hold all the results which results from the com.sluk3r.search
             * operation
             */
            TopDocs topDocs = indexSearcher.search(query, 1);
            if (topDocs.scoreDocs.length > 0) {
                System.out.println("Found :  Book with id = " + indexSearcher.doc(topDocs.scoreDocs[0].doc).get("BOOKID") + " , Name = "
                        + indexSearcher.doc(topDocs.scoreDocs[0].doc).get("name") + " ,Author = "
                        + indexSearcher.doc(topDocs.scoreDocs[0].doc).get("author") + " ,Publisher = "
                        + indexSearcher.doc(topDocs.scoreDocs[0].doc).get("publisher") + " with hits : " + topDocs.scoreDocs[0].doc);
                return true;
            } else {
                System.out.println("No Record found");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (indexSearcher != null) {
                try {
                    indexSearcher.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            indexSearcher = null;
        }
        return false;
    }
}
