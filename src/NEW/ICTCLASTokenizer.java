package NEW;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import kevin.zhang.NLPIR;
import zlz.tools.NLPIRInstance;

public class ICTCLASTokenizer extends Tokenizer {
    private List<String> tokens;
    private Iterator<String> tokenIter;
    private CharTermAttribute termAtt;
    private TypeAttribute typeAtt;
    private NLPIR nlpir;

    public ICTCLASTokenizer(Reader reader) {
        super(reader);
        this.termAtt = this.addAttribute(CharTermAttribute.class);
        this.typeAtt = this.addAttribute(TypeAttribute.class);
    }

    protected List<String> tokenizerReader() {
        List<String> result = new ArrayList<String>();
        BufferedReader bf = new BufferedReader(this.input);
        String s, content = "";
        try {
            while ((s = bf.readLine()) != null) {
                content = content + s + "\n";
            }
            this.nlpir = new NLPIR();
            String argu = "E:\\JAVA\\webSearcher\\file";
			System.out.println("NLPIR_Init");
			if (this.nlpir.NLPIR_Init(argu.getBytes("GB2312"), 1) == false)
			{
				System.out.println("Init Fail!");
				return null;
			}
			this.nlpir.NLPIR_SetPOSmap(0);
            byte[] contentByte = this.nlpir.NLPIR_ParagraphProcess(content.getBytes("utf8"), 1);
        	String contentStr = new String(contentByte, 0, contentByte.length, "utf8");
            String[] terms = contentStr.split("\\s+");
            for (String string : terms) {
                result.add(string);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
       
        return result;
    }

   

    @Override
    public boolean incrementToken() {
        this.clearAttributes();
        if (this.tokenIter.hasNext()) {
            String tokenstr = this.tokenIter.next();
            int pos = tokenstr.lastIndexOf('/');
            if (pos != -1) {
                this.termAtt.append(tokenstr.substring(0, pos));
                this.termAtt.setLength(pos);
                this.typeAtt.setType(tokenstr.substring(pos, tokenstr.length()));
                return true;
            }
        }
        return false;
    }

    @Override
    public void reset() {
        this.tokens = this.tokenizerReader();
        this.tokenIter = this.tokens.iterator();
    }

}


