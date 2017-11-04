package zlz.tools;
import java.io.*;
import kevin.zhang.NLPIR;
public class NLPIRInstance {
	
	private static NLPIRInstance instance = null;
	private static NLPIR testNLPIR; 
	
	private NLPIRInstance() {
		
	}
 
	public static synchronized NLPIRInstance getInstance() throws Exception {
		if(instance == null) {
			instance = new NLPIRInstance();
			
			testNLPIR = new NLPIR();
			String argu = "E:\\JAVA\\webSearcher\\file";
			System.out.println("NLPIR_Init");
			if (testNLPIR.NLPIR_Init(argu.getBytes("GB2312"), 0) == false)
			{
				System.out.println("Init Fail!");
				return null;
			}
			testNLPIR.NLPIR_SetPOSmap(0);
		}
		return instance;
	}
	
	public void importUserDict(String userDict) throws Exception {
		File file = new File(userDict);
		FileReader reader = new FileReader(file);
		BufferedReader rd = new BufferedReader(reader);
		String s = "";
		while((s = rd.readLine())!=null) {
			s = s.trim();
//			System.out.println(s);
			testNLPIR.NLPIR_AddUserWord(s.getBytes("utf8")); 
		}
		rd.close();
	}
	
	public void importUserDict(String userDict, String inputEncoding) throws Exception {
		File file = new File(userDict);
		FileReader reader = new FileReader(file);
		BufferedReader rd = new BufferedReader(reader);
		String s = "";
		while((s = rd.readLine())!=null) {
			s = s.trim();
			testNLPIR.NLPIR_AddUserWord(s.getBytes("utf8")); 
		}
		rd.close();
	}
	
	public NLPIR getNLPIR() {
		return testNLPIR;
	}
	
	

}
