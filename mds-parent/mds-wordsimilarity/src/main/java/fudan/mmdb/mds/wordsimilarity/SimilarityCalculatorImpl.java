package fudan.mmdb.mds.wordsimilarity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import fudan.mmdb.mds.wordsimilarity.model.Primitive;
import fudan.mmdb.mds.wordsimilarity.model.Word;

public class SimilarityCalculatorImpl implements ISimilarityCalculator {

	/**
	 * 词库中所有的具体词，或者义原,知网中定义的词语
	 */
	public static Map<String, List<Word>> ALLWORDS = new HashMap<String, List<Word>>();
	/**
	 * 哈工大同义词林中的所有词语。key类别号，value是这个类别下的所有词语组成的list； list中的所有词都为同义词或者相关的词语。
	 */
	public static Map<String, List<String>> CILIN = new HashMap<String, List<String>>();
	/**
	 * 哈工大同义词林中的所有词语。key为词语，value为类别号
	 */
	public static Map<String, String> ALLWORDS_IN_CILIN = new HashMap<String, String>();
	/**
	 * sim(p1,p2) = alpha/(d+alpha)
	 */
	private static double alpha = 1.6;
	/**
	 * 计算实词的相似度，参数，基本义原权重
	 */
	private static double beta1 = 0.5;
	/**
	 * 计算实词的相似度，参数，其他义原权重
	 */
	private static double beta2 = 0.2;
	/**
	 * 计算实词的相似度，参数，关系义原权重
	 */
	private static double beta3 = 0.17;
	/**
	 * 计算实词的相似度，参数，关系符号义原权重
	 */
	private static double beta4 = 0.13;
	/**
	 * 具体词与义原的相似度一律处理为一个比较小的常数. 具体词和具体词的相似度，如果两个词相同，则为1，否则为0.
	 */
	private static double gamma = 0.2;
	/**
	 * 将任一非空值与空值的相似度定义为一个比较小的常数
	 */
	private static double delta = 0.2;
	/**
	 * 两个无关义原之间的默认距离
	 */
	private static int DEFAULT_PRIMITIVE_DIS = 20;
	/**
	 * 知网中的逻辑符号
	 */
	private static String LOGICAL_SYMBOL = ",~^";
	/**
	 * 知网中的关系符号
	 */
	private static String RELATIONAL_SYMBOL = "#%$*+&@?!";
	/**
	 * 知网中的特殊符号，虚词，或具体词
	 */
	private static String SPECIAL_SYMBOL = "{";
	/**
	 * the logger for this class
	 */
	private static Logger logger;

	public SimilarityCalculatorImpl() {
		logger = Logger.getLogger("fudan.mmdb.mds.wordsimilarity");
		loadGlossary();
		loadCiLin();
	}

	/**
	 * load the file, CILIN.txt.
	 */
	private static void loadCiLin() {
		String line = null;
		BufferedReader reader = null;
		try {
			ResourceLoader loader = new DefaultResourceLoader();
			Resource resource = loader.getResource("classpath:dict-utf8/CILIN.txt");
			reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), "utf-8"));
			logger.info("start to load the file: CILIN.txt(哈工大信息检索研究室同义词词林扩展版)");
			line = reader.readLine();
			while (line != null) {
				String[] strs = line.split("\\s+");
				String category = strs[0];
				List<String> list = new ArrayList<String>();
				for (int i = 1; i < strs.length; i++) {
					ALLWORDS_IN_CILIN.put(strs[i], category);
					list.add(strs[i]);
				}
				CILIN.put(category, list);
				line = reader.readLine();
			}
			logger.info("finished loading the file CILIN.txt");
		} catch (Exception e) {
			logger.fatal("Failed to load the file CILIN.txt.", e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.fatal("Failed to load the file CILIN.txt. ", e);
			}
		}
	}

	/**
	 * load glossary.bat file
	 */
	private static void loadGlossary() {
		String line = null;
		BufferedReader reader = null;
		try {
			logger.info("start to load the file: glossary.dat");
			ResourceLoader loader = new DefaultResourceLoader();
			Resource resource = loader.getResource("classpath:dict-utf8/glossary.dat");
			reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), "utf-8"));
			line = reader.readLine();
			while (line != null) {
				// parse the line
				// the line format is like this:
				// 阿布扎比 N place|地方,capital|国都,ProperName|专,(the United Arab
				// Emirates|阿拉伯联合酋长国)
				line = line.trim().replaceAll("\\s+", " ");
				String[] strs = line.split(" ");
				String word = strs[0];
				String type = strs[1];
				// 因为是按空格划分，最后一部分的加回去
				String related = strs[2];
				for (int i = 3; i < strs.length; i++) {
					related += (" " + strs[i]);
				}
				// Create a new word
				Word w = new Word();
				w.setWord(word);
				w.setType(type);
				parseDetail(related, w);
				// save this word.
				addWord(w);
				// read the next line
				line = reader.readLine();
			}
			logger.info("finished loading the file glossary.dat");
		} catch (Exception e) {
			logger.fatal("Exception in loading glossary.dat.", e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.fatal("Exception in loading glossary.dat.", e);
			}
		}
	}

	/**
	 * 解析具体概念部分，将解析的结果存入<code>Word word</code>.
	 * 
	 * @param related
	 */
	private static void parseDetail(String related, Word word) {
		// spilt by ","
		String[] parts = related.split(",");
		boolean isFirst = true;
		boolean isRelational = false;
		boolean isSimbol = false;
		String chinese = null;
		String relationalPrimitiveKey = null;
		String simbolKey = null;
		for (int i = 0; i < parts.length; i++) {
			// 如果是具体词，则以括号开始和结尾: (Bahrain|巴林)
			if (parts[i].startsWith("(")) {
				parts[i] = parts[i].substring(1, parts[i].length() - 1);
				// parts[i] = parts[i].replaceAll("\\s+", "");
			}
			// 关系义原，之后的都是关系义原
			if (parts[i].contains("=")) {
				isRelational = true;
				// format: content=fact|事情
				String[] strs = parts[i].split("=");
				relationalPrimitiveKey = strs[0];
				String value = strs[1].split("\\|")[1];
				word.addRelationalPrimitive(relationalPrimitiveKey, value);

				continue;
			}
			String[] strs = parts[i].split("\\|");
			// 开始的第一个字符，确定是否为义原，或是其他关系。
			int type = getPrimitiveType(strs[0]);
			// 其中中文部分的词语,部分虚词没有中文解释
			if (strs.length > 1) {
				chinese = strs[1];
			}
			if (chinese != null
					&& (chinese.endsWith(")") || chinese.endsWith("}"))) {
				chinese = chinese.substring(0, chinese.length() - 1);
			}
			// 义原
			if (type == 0) {
				// 之前有一个关系义原
				if (isRelational) {
					word.addRelationalPrimitive(relationalPrimitiveKey, chinese);
					continue;
				}
				// 之前有一个是符号义原
				if (isSimbol) {
					word.addRelationSimbolPrimitive(simbolKey, chinese);
					continue;
				}
				if (isFirst) {
					word.setFirstPrimitive(chinese);
					isFirst = false;
					continue;
				} else {
					word.addOtherPrimitive(chinese);
					continue;
				}
			}
			// 关系符号表
			if (type == 1) {
				isSimbol = true;
				isRelational = false;
				simbolKey = Character.toString(strs[0].charAt(0));
				word.addRelationSimbolPrimitive(simbolKey, chinese);
				continue;
			}
			if (type == 2) {
				// 虚词
				if (strs[0].startsWith("{")) {
					// 去掉开始第一个字符 "{"
					String english = strs[0].substring(1);
					// 去掉有半部分 "}"
					if (chinese != null) {
						word.addStructruralWord(chinese);
						continue;
					} else {
						// 如果没有中文部分，则使用英文词
						word.addStructruralWord(english);
						continue;
					}
				}
			}
		}
	}

	/**
	 * <p>
	 * 从英文部分确定这个义原的类别。
	 * </p>
	 * <p>
	 * 0-----Primitive<br/>
	 * 1-----Relational<br/>
	 * 2-----Special
	 * </p>
	 * 
	 * @param english
	 * @return 一个代表类别的整数，其值为1，2，3。
	 */
	public static int getPrimitiveType(String str) {
		String first = Character.toString(str.charAt(0));
		if (RELATIONAL_SYMBOL.contains(first)) {
			return 1;
		}
		if (SPECIAL_SYMBOL.contains(first)) {
			return 2;
		}
		return 0;
	}

	/**
	 * 计算两个词语的相似度
	 */
	public double simWord(String word1, String word2) {
		double sim1 = simWordHowNet(word1, word2);
		double sim2 = simWordCiLin(word1, word2);
		return sim1 > sim2 ? sim1 : sim2;
	}

	public static double simWordHowNet(String word1, String word2) {
		if (ALLWORDS.containsKey(word1) && ALLWORDS.containsKey(word2)) {
			List<Word> list1 = ALLWORDS.get(word1);
			List<Word> list2 = ALLWORDS.get(word2);
			double max = 0;
			for (Word w1 : list1) {
				for (Word w2 : list2) {
					double sim = simWord(w1, w2);
					max = (sim > max) ? sim : max;
				}
			}
			return max;
		}
		if (!ALLWORDS.containsKey(word1)) {
			logger.debug(String.format("[%s] 没有被知网中收录.", word1));
		}
		if (!ALLWORDS.containsKey(word2)) {
			logger.debug(String.format("[%s] 没有被知网中收录.", word2));
		}
		return 0.0;
	}

	/**
	 * 使用知网获得一个词于的所有相关概念。
	 * 
	 * @param word
	 * @return
	 */
	public static List<String> getReletiveWords(String word) {
		List<String> list = new ArrayList<String>();
		if (ALLWORDS.containsKey(word)) {
			List<Word> list1 = ALLWORDS.get(word);
			for (int i = 0; i < list1.size(); i++) {
				list.add(list1.get(i).getWord());
			}
		}
		return list;
	}

	/**
	 * 使用同义词词林获得这个词的近义词
	 * 
	 * @param word
	 * @return
	 */
	public static List<String> getSynonym(String word) {
		List<String> list = new ArrayList<String>();
		if (ALLWORDS_IN_CILIN.containsKey(word)) {
			List<String> list1 = CILIN.get(ALLWORDS_IN_CILIN.get(word));
			for (String w : list1) {
				if (!w.equals(word)) {
					list.add(w);
				}
			}
		}
		return list;
	}

	/**
	 * caculate the word similarity using CiLin.
	 * 
	 * @param word1
	 * @param word2
	 * @return
	 */
	public static double simWordCiLin(String word1, String word2) {
		if (ALLWORDS_IN_CILIN.containsKey(word1)
				&& ALLWORDS_IN_CILIN.containsKey(word2)) {
			logger.debug(String.format("use cilin to calulate the word similarity: [%s] and [%s].", word1,word2));
			String category1 = ALLWORDS_IN_CILIN.get(word1);
			String category2 = ALLWORDS_IN_CILIN.get(word2);
			return simCategory(category1, category2);
		}
		if (!ALLWORDS_IN_CILIN.containsKey(word1)) {
			logger.debug(String.format("[%s] 没有被同义词词林收录.", word1));
		}
		if (!ALLWORDS_IN_CILIN.containsKey(word2)) {
			logger.debug(String.format("[%s] 没有被同义词词林收录.", word2));
		}
		return 0.0;
	}

	/**
	 * 计算两个类别直接的距离，在词林中，我们将词语的相似度，等同于词语所属类别的相似度.<br/>
	 * category：Aa01B03#<br/>
	 * 第一位：大写字母，大类,第一级<br/>
	 * 第二位：小写字母，中类,第二级<br/>
	 * 第三、四位：数字，小类，第三级<br/>
	 * 第五位：大写字母，词群，第四级<br/>
	 * 第六、七位：数字，原子词群，第五级<br/>
	 * 第八位：“=#@”，“=”代表相等，同义；“#”代表不等，同类；“@”代表自我封闭，独立，在词典既没有同义词，也没有相关词<br/>
	 * 
	 * @param category1
	 * @param category2
	 * @return
	 */
	public static double simCategory(String category1, String category2) {
		String big1 = category1.substring(0, 1);
		String middle1 = category1.substring(1, 2);
		String small1 = category1.substring(2, 4);
		String wordGroup1 = category1.substring(4, 5);
		String UnitWordGroup1 = category1.substring(5, 7);
		String big2 = category2.substring(0, 1);
		String middle2 = category2.substring(1, 2);
		String small2 = category2.substring(2, 4);
		String wordGroup2 = category2.substring(4, 5);
		String UnitWordGroup2 = category2.substring(5, 7);
		// d 为语义距离,使用1/d
		// int d = 0;
		// int a = 2.5; //
		if (!big1.equals(big2)) {
			// 默认使用最远距离
			return 0.1;
			// d=10;
			// return
		}
		if (!middle1.equals(middle2)) {
			// d = 8
			return 0.125;
		}
		if (!small1.equals(small2)) {
			// d=6
			return 0.1667;
		}
		if (!wordGroup1.equals(wordGroup2)) {
			// 4
			return 0.25;
		}
		if (!UnitWordGroup1.equals(UnitWordGroup2)) {
			// 2
			return 0.5;
		}
		return 1;
	}

	/**
	 * 计算两个词语的相似度
	 * 
	 * @param w1
	 * @param w2
	 * @return
	 */
	public static double simWord(Word w1, Word w2) {
		// 虚词和实词的相似度为零
		if (w1.isStructruralWord() != w2.isStructruralWord()) {
			return 0;
		}
		// 虚词
		if (w1.isStructruralWord() && w2.isStructruralWord()) {
			List<String> list1 = w1.getStructruralWords();
			List<String> list2 = w2.getStructruralWords();
			return simList(list1, list2);
		}
		// 实词
		if (!w1.isStructruralWord() && !w2.isStructruralWord()) {
			// 实词的相似度分为4个部分
			// 基本义原相似度
			String firstPrimitive1 = w1.getFirstPrimitive();
			String firstPrimitive2 = w2.getFirstPrimitive();
			double sim1 = simPrimitive(firstPrimitive1, firstPrimitive2);
			// 其他基本义原相似度
			List<String> list1 = w1.getOtherPrimitives();
			List<String> list2 = w2.getOtherPrimitives();
			double sim2 = simList(list1, list2);
			// 关系义原相似度
			Map<String, List<String>> map1 = w1.getRelationalPrimitives();
			Map<String, List<String>> map2 = w2.getRelationalPrimitives();
			double sim3 = simMap(map1, map2);
			// 关系符号相似度
			map1 = w1.getRelationSimbolPrimitives();
			map2 = w2.getRelationSimbolPrimitives();
			double sim4 = simMap(map1, map2);
			double product = sim1;
			double sum = beta1 * product;
			product *= sim2;
			sum += beta2 * product;
			product *= sim3;
			sum += beta3 * product;
			product *= sim4;
			sum += beta4 * product;
			return sum;
		}
		return 0.0;
	}

	/**
	 * map的相似度。
	 * 
	 * @param map1
	 * @param map2
	 * @return
	 */
	public static double simMap(Map<String, List<String>> map1,
			Map<String, List<String>> map2) {
		if (map1.isEmpty() && map2.isEmpty()) {
			return 1;
		}
		int total = map1.size() + map2.size();
		double sim = 0;
		int count = 0;
		for (String key : map1.keySet()) {
			if (map2.containsKey(key)) {
				// shallow copy
				List<String> list1 = new ArrayList<String>(map1.get(key));
				List<String> list2 = new ArrayList<String>(map2.get(key));
				sim += simList(list1, list2);
				count++;
			}
		}
		return (sim + delta * (total - 2 * count)) / (total - count);
	}

	/**
	 * 比较两个集合的相似度
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static double simList(List<String> list1, List<String> list2) {
		if (list1.isEmpty() && list2.isEmpty())
			return 1;
		int m = list1.size();
		int n = list2.size();
		int big = m > n ? m : n;
		int N = (m < n) ? m : n;
		int count = 0;
		// int index1 = 0, index2 = 0;
		double sum = 0;
		double max = 0;
		// ����������
		Map<String, Double> map = new HashMap<String, Double>();
		for (int i = 0; i < list1.size(); i++) {
			for (int j = 0; j < list2.size(); j++) {
				double sim = innerSimWord(list1.get(i), list2.get(j));
				map.put(i + "#" + j, sim);
			}
		}
		while (count < N) {
			max = 0;
			String index = "";
			for (String key : map.keySet()) {
				double sim = map.get(key);
				if (sim >= max) {
					max = sim;
					index = key;
				}
			}
			sum += max;
			// remove the useless value in the temp map.
			map.remove(index);
			int sharp_index = index.indexOf('#');
			// if(sharp_index==-1){
			// System.out.println(list1);
			// System.out.println(list2);
			// }
			String str1 = index.substring(0, sharp_index);
			String str2 = index.substring(sharp_index + 1);
			Set<String> keys = new HashSet<String>(map.keySet());
			for (String key : keys) {
				if (key.startsWith(str1 + '#') || key.endsWith('#' + str2)) {
					map.remove(key);
				}
			}
			count++;
		}

		return (sum + delta * (big - N)) / big;
	}

	/**
	 * 内部比较两个词，可能是为具体词，也可能是义原
	 * 
	 * @param word1
	 * @param word2
	 * @return
	 */
	private static double innerSimWord(String word1, String word2) {
		boolean isPrimitive1 = Primitive.isPrimitive(word1);
		boolean isPrimitive2 = Primitive.isPrimitive(word2);
		// 两个义原
		if (isPrimitive1 && isPrimitive2)
			return simPrimitive(word1, word2);
		// 具体词
		if (!isPrimitive1 && !isPrimitive2) {
			if (word1.equals(word2))
				return 1;
			else
				return 0;
		}
		// 义原和具体词的相似度, 默认为gamma=0.2
		return gamma;
	}

	/**
	 * @param primitive1
	 * @param primitive2
	 * @return
	 */
	public static double simPrimitive(String primitive1, String primitive2) {
		int dis = disPrimitive(primitive1, primitive2);
		return alpha / (dis + alpha);
	}

	/**
	 * 计算两个义原之间的距离，如果两个义原层次没有共同节点，则设置他们的距离为20。
	 * 
	 * @param primitive1
	 * @param primitive2
	 * @return
	 */
	public static int disPrimitive(String primitive1, String primitive2) {
		List<Integer> list1 = Primitive.getParents(primitive1);
		List<Integer> list2 = Primitive.getParents(primitive2);
		for (int i = 0; i < list1.size(); i++) {
			int id1 = list1.get(i);
			if (list2.contains(id1)) {
				int index = list2.indexOf(id1);
				return index + i;
			}
		}
		return DEFAULT_PRIMITIVE_DIS;
	}

	/**
	 * 加入一个词语
	 * 
	 * @param word
	 */
	public static void addWord(Word word) {
		List<Word> list = ALLWORDS.get(word.getWord());

		if (list == null) {
			list = new ArrayList<Word>();
			list.add(word);
			ALLWORDS.put(word.getWord(), list);
		} else {
			list.add(word);
		}
	}

	// /**
	// * @param args
	// */
	// public static void main(String[] args) throws Exception {
	// // TODO Auto-generated method stub
	// BufferedReader reader = new BufferedReader(new FileReader(
	// "dict/glossary.dat"));
	// Set<String> set = new HashSet<String>();
	// String line = reader.readLine();
	// while (line != null) {
	// // System.out.println(line);
	// line = line.replaceAll("\\s+", " ");
	// String[] strs = line.split(" ");
	// for (int i = 0; i < strs.length; i++) {
	// System.out.print(" " + strs[i]);
	// }
	// System.out.println();
	// set.add(strs[1]);
	// line = reader.readLine();
	// }
	// System.out.println(set.size());
	// for (String name : set) {
	// System.out.println(name);
	// }
	// }
}
