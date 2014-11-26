package fudan.mmdb.mds.analyzer;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.base.Joiner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={AnalyzerTestConfig.class})
public class ICTCLASAnalyzerTest {

	@Autowired
	private  ICTCLASAnalyzer analyzer;
	
	private static Logger logger=Logger.getLogger(ICTCLASAnalyzerTest.class);
	
	
	@Test
	public void testAnalyze() {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		URL[] urls = ((URLClassLoader) cl).getURLs();
		for (URL url: urls) {
		    System.out.println(url.getFile());
		}
		
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
