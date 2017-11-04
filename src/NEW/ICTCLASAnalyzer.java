package NEW;

import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import kevin.zhang.NLPIR;

public class ICTCLASAnalyzer extends Analyzer {
    private NLPIR nlpir;


    @Override
    public TokenStreamComponents createComponents(String fieldname, Reader reader) {
        Tokenizer ictclastokenizer = new ICTCLASTokenizer(reader);
        TokenStreamComponents tokenstreamcomponents = new TokenStreamComponents(ictclastokenizer);
        return tokenstreamcomponents;
    }
    public static void main(String[] args) throws Exception 
{ 
    Analyzer analyzer = new ICTCLASAnalyzer(); 
    String text="【产品设计】樱花瓣图钉，设计师小玉一徳，KOKUYO设计大赛2009获奖作品。Kokuyo是日本的一个文具和办公用品品牌，它每年都会举办一个设计比赛，奖励在文具等方面作出改进的创意。";  
    TokenStream ts=analyzer.tokenStream("field", new StringReader(text));  
    CharTermAttribute term=ts.addAttribute(CharTermAttribute.class);  
     ts.reset();//重置做准备  
     while(ts.incrementToken()){  
         System.out.println(term.toString());  
     }  
     ts.end();//  
     ts.close();//关闭流  
   
    
} 

}
