package fudan.mmdb.mds.analyzer;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.base.Joiner;

public class ICTCLASAnalyzerTest {

	private static ICTCLASAnalyzer analyzer;
	
	private static Logger logger=Logger.getLogger(ICTCLASAnalyzerTest.class);
	
	@BeforeClass
	public static void BeforeClass(){
		analyzer=new ICTCLASAnalyzer();
	}
	
	@Test
	public void testAnalyze() {
	    String sInput = "随后温总理就离开了舟曲县城，预计温总理今天下午就回到北京。以上就是今天上午的最新动态";
	    List<String> result=analyzer.analyze(sInput);
	    System.out.println(Joiner.on("|").join(result));
	}

	@Test
	public void testParagraphProcess(){
	    String sInput = "随后温总理就离开了舟曲县城，预计温总理今天下午就回到北京。以上就是今天上午的最新动态";
	    String result=analyzer.paragraphProcess(sInput, "utf-8");
	    logger.info(result);
	}
}
