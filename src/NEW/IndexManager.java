package NEW;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;



public class IndexManager {
    private static String File_Dir = "C:\\Users\\msi\\Desktop\\web\\NLPIRAvailable\\SOURCE";
    private static String Index_Dir = "C:\\Users\\msi\\Desktop\\web\\NLPIRAvailable\\INDEX";
    private IndexWriter indexWriter;
    private Analyzer luceneAnalyzer;
    private IndexWriterConfig indexWriterConfig;
    private List<File> fileList;
    private List<Document> docList;

    /*
     * 鍒涘缓绱㈠紩
     */
    public boolean createIndex(String path) throws IOException {
        this.fileList = this.getFileList(path);
        Directory directory = FSDirectory.open(new File(Index_Dir));
        this.luceneAnalyzer = new ICTCLASAnalyzer();
        this.indexWriterConfig = new IndexWriterConfig(Version.LUCENE_44, this.luceneAnalyzer);
        this.indexWriter = new IndexWriter(directory, this.indexWriterConfig);
        for (File file : this.fileList) {
            System.out.println(file.getName());
            this.docList = this.getDocList(file);
            for (Document doc : this.docList) {
                this.indexWriter.addDocument(doc);
            }
            this.indexWriter.commit();
        }
        if (this.indexWriter != null) {
            this.indexWriter.close();
            return true;
        } else {
            return false;
        }
    }


    public Document getDoc(String docContent) throws IOException {
        String s, url, publishid, subjectid, title, keywords, description, content;
        BufferedReader bf = new BufferedReader(new StringReader(docContent));
        Document doc = new Document();
        while ((s = bf.readLine()) != null) {
            if (s.equals("<doc>")) {
                url = publishid = subjectid = title = keywords = description = content = "";
                s = bf.readLine(); 
                url = s.substring(5, s.lastIndexOf('<'));
                doc.add(new StringField("url", url, Store.YES));
                s = bf.readLine(); 
                while (!s.substring(1, 6).equals("title")) {
                    s = bf.readLine();
                }
                title = s.substring(7, s.lastIndexOf('<'));
                doc.add(new TextField("title", title, Store.YES));
                s = bf.readLine();
                while (s.length() > 5 && s.substring(1, 5).equals("meta")) {
                    if (s.substring(11, 19).equals("keywords")) {
                        keywords = s.substring(s.indexOf('"') + 1, s.lastIndexOf('"'));
                        doc.add(new TextField("keywords", keywords, Store.YES));
                    } else if (s.substring(11, 22).equals("description")) {
                        description = s.substring(s.indexOf('"') + 1, s.lastIndexOf('"'));
                        doc.add(new TextField("description", description, Store.YES));
                    } else if (s.substring(12, 21).equals("publishid")) {
                        publishid = s.substring(22);
                        publishid = publishid.substring(publishid.indexOf('"') + 1, publishid.lastIndexOf('"'));
                        doc.add(new StringField("publishid", publishid, Store.YES));
                    } else if (s.substring(12, 21).equals("subjectid")) {
                        subjectid = s.substring(22);
                        subjectid = subjectid.substring(subjectid.indexOf('"') + 1, subjectid.lastIndexOf('"'));
                        doc.add(new StringField("subjectid", subjectid, Store.YES));
                    }
                    s = bf.readLine();
                }

                while (!s.equals("</doc>")) {
                    s = s.replaceAll("<a[^>]*?>[\\s\\S]*?</a>","");
                    s = s.replaceAll("<strong[^>]*?>[\\s\\S]*?</strong>","");
                    s = s.replaceAll("<span[^>]*?>[\\s\\S]*?</span>","");
                    s = s.replaceAll("<em[^>]*?>[\\s\\S]*?</em>","");
                    s = s.replaceAll("<iframe[^>]*?>[\\s\\S]*?</iframe>","");
                    s = s.replaceAll("&nbsp;", "");
                    content = content + s + "\n";
                    s = bf.readLine();
                }
                doc.add(new TextField("content", content, Store.YES));
            }
        }
        bf.close();
        return doc;
    }


    public List<Document> getDocList(File file) throws IOException {
        List<Document> docList = new ArrayList<Document>();
        Document doc;
        String result = "";
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        String s = bf.readLine();
        while (s != null) {
            result = "";
 
            if (s.equals("<doc>")) {
                while (!s.equals("</doc>")) {
                    result = result + s + "\n";
                    s = bf.readLine();
                }
                result = result + s + "\n";
                doc = this.getDoc(result);
                docList.add(doc);
            }
            s = bf.readLine();
        }
        bf.close();

        return docList;
    }

    public List<File> getFileList(String path) {
        File[] files = new File(path).listFiles();
        List<File> fileList = new ArrayList<File>();
        for (File file : files) {
            if (this.isTextFile(file.getName())) {
                fileList.add(file);
            }
        }
        return fileList;

    }

    public boolean isTextFile(String filename) {
        if (filename.lastIndexOf(".txt") > 0) {
            return true;
        }
        return false;
    }

    public static void main(String args[]) throws IOException, ParseException {
        IndexManager index = new IndexManager();
        index.createIndex(File_Dir);
    }

}

