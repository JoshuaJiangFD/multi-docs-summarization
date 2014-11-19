package fudan.mmdb.mds.core.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class StanfordNLPHelperTest {

	@Test
	public void testSplitSentences() {
		
		String str="今天天气很好。你是谁?";
		StanfordNLPHelper helper=new StanfordNLPHelper();
		helper.splitSentences(str);
	}

}
