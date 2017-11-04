package zlz.tools;
import java.io.*;
import java.util.*;


import kevin.zhang.NLPIR;

public class Split {
	public static String Split13(String sInput, NLPIR testNLPIR) throws Exception {
		byte nativeBytes[] = testNLPIR.NLPIR_ParagraphProcess(sInput.getBytes("utf8"), 1);
		String nativeStr = new String(nativeBytes, 0, nativeBytes.length, "utf8");
//		System.out.println(nativeStr);
//		nativeStr = combineEntity(nativeStr);
//		System.out.println(nativeStr);
//		testICTCLAS50.ICTCLAS_Exit();
		return nativeStr;
	}
	public static void main(String[] args) {
		try {
			
			NLPIRInstance nlpirInstance = NLPIRInstance.getInstance();
			NLPIR testNLPIR = nlpirInstance.getNLPIR();
			String s = "【产品设计】樱花瓣图钉，设计师小玉一徳，KOKUYO设计大赛2009获奖作品。Kokuyo是日本的一个文具和办公用品品牌，它每年都会举办一个设计比赛，奖励在文具等方面作出改进的创意。";		
			String split = Split13(s, testNLPIR);

			System.out.println(split);

			testNLPIR.NLPIR_Exit();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
