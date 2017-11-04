package NEW;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Searcher {
    private static String Index_Dir = "C:\\Users\\msi\\Desktop\\web\\NLPIRAvailable\\INDEX";
    public int hits;
    public String[] title, content;

    public Searcher() {

    }


    public void searchIndex(String querystr) {
        try {
            Directory directory = FSDirectory.open(new File(Index_Dir));
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer ictclasAnalyzer = new ICTCLASAnalyzer();
            String[] fields = { "title", "description", "keywords", "content" };
            MultiFieldQueryParser mp = new MultiFieldQueryParser(Version.LUCENE_44, fields, ictclasAnalyzer);
            Query q = mp.parse(querystr);
            TopDocs topDocs = searcher.search(q, 20);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            this.hits = scoreDocs.length;
            if (this.hits > 0) {
                this.title = new String[this.hits];
                this.content = new String[this.hits];
            }
            int i = 0;
            for (ScoreDoc scdoc : scoreDocs) {
                Document doc = searcher.doc(scdoc.doc);
                this.title[i] = doc.get("title");
                this.content[i] = doc.get("content");
                System.out.println("doc:" + i++);
                System.out.println("title:" + doc.get("title"));
                System.out.println("content:" + doc.get("content"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Searcher searcher = new Searcher();
        String s = null;
        searcher.searchIndex("中国");

    }

}
