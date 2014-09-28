package fudan.mmdb.mds.wordsimilarity;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fudan.mmdb.mds.wordsimilarity.model.Primitive;

public class SimilarityCalculatorImplTest {

	private static SimilarityCalculatorImpl simCalculatorImpl;

	private static Logger logger = Logger.getLogger(SimilarityCalculatorImplTest.class);

	@BeforeClass
	public static void BeforeTests() {
		simCalculatorImpl = new SimilarityCalculatorImpl();
	}

	@Test
	public void test_disPrimitive() {
		int dis = simCalculatorImpl.disPrimitive("雇用", "争斗");
		logger.info("雇用 and 争斗  distance : " + dis);
	}

	@Test
	public void test_simPrimitive() {
		double simP = simCalculatorImpl.simPrimitive("雇用", "争斗");
		logger.info("雇用 and 争斗 simimarity: " + simP);
	}

	@Test
	public void test_simWord() {
		String word1 = "脾气";
		String word2 = "脾气";
		double sim = simCalculatorImpl.simWord(word2, word1);
		logger.info(word1 + " " + word2 + " sim = " + sim);
		word1 = "别号";
		word2 = "老脾气";
		sim = simCalculatorImpl.simWord(word2, word1);
		logger.info(word1 + " " + word2 + " sim = " + sim);
		word1 = "安生";
		word2 = "安适";
		sim = simCalculatorImpl.simWord(word2, word1);
		logger.info(word1 + " " + word2 + " sim = " + sim);
	}

	@Test
	public void test_simWordCilin() {
		String word1 = "打胎";
		String word2 = "人流";
		double sim1 = simCalculatorImpl.simWordCiLin(word1, word2);
		double sim2 = simCalculatorImpl.simWordHowNet(word1, word2);
		logger.info(word1 + " " + word2 + " sim1 = " + sim1);
		logger.info(word1 + " " + word2 + " sim2 = " + sim2);
		// ==================
		word1 = "访谈";
		word2 = "访问";
		sim1 = simCalculatorImpl.simWordCiLin(word1, word2);
		sim2 = simCalculatorImpl.simWordHowNet(word1, word2);
		logger.info(word1 + " " + word2 + " sim1 = " + sim1);
		logger.info(word1 + " " + word2 + " sim2 = " + sim2);
		word1 = "翻新";
		word2 = "翻修";
		sim1 = simCalculatorImpl.simWordCiLin(word1, word2);
		sim2 = simCalculatorImpl.simWordHowNet(word1, word2);
		logger.info(word1 + " " + word2 + " sim1 = " + sim1);
		logger.info(word1 + " " + word2 + " sim2 = " + sim2);
		word1 = "失信";
		word2 = "失约";
		sim1 = simCalculatorImpl.simWordCiLin(word1, word2);
		sim2 = simCalculatorImpl.simWordHowNet(word1, word2);
		logger.info(word1 + " " + word2 + " sim1 = " + sim1);
		logger.info(word1 + " " + word2 + " sim2 = " + sim2);
		word1 = "胡乱";
		word2 = "莽撞";
		sim1 = simCalculatorImpl.simWordCiLin(word1, word2);
		sim2 = simCalculatorImpl.simWordHowNet(word1, word2);
		logger.info(word1 + " " + word2 + " sim1 = " + sim1);
		logger.info(word1 + " " + word2 + " sim2 = " + sim2);
	}

	@Test
	public void test_simWord_1() {
		String word1 = "我";
		String word2 = "他";
		double sim = simCalculatorImpl.simWord(word2, word1);
		 logger.info(word1 + " " + word2 + " sim = " + sim);
		word1 = "爱";
		word2 = "喜欢";
		sim = simCalculatorImpl.simWord(word2, word1);
		 logger.info(word1 + " " + word2 + " sim = " + sim);
		word1 = "吃";
		word2 = "喝";
		sim = simCalculatorImpl.simWord(word2, word1);
		 logger.info(word1 + " " + word2 + " sim = " + sim);
	}

}
