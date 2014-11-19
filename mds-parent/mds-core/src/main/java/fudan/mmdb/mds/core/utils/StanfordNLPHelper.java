package fudan.mmdb.mds.core.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.ClassName;
import edu.stanford.nlp.util.CoreMap;

public class StanfordNLPHelper {

	private StanfordCoreNLP pipeline;
	
	public StanfordNLPHelper(){
	    Properties props = new Properties();
	    try{
	    	props.load(ClassName.class.getClassLoader().getResourceAsStream("StanfordCoreNLP-chinese.properties"));	
	    	props.setProperty("ssplit.boundaryTokenRegex", "[.]|[!?]+|[。]|[！？]+");	
	    	props.setProperty("annotators", "segment,ssplit");
	    }catch(Exception ex){
	    	throw new RuntimeException("Fatal Error: can't load StanfordCoreNLP-chinese.properties from class path.",ex);
	    }
	    pipeline = new StanfordCoreNLP(props);
	}
	
	public List<String> splitSentences(String paragraph){
		
		List<String> sentenceStrs=new LinkedList<String>();
		
	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(paragraph);
	    
	    // run all Annotators on this text
	    pipeline.annotate(document);
	    
	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    for(CoreMap sent:sentences)
	    {
	    	System.out.println(sent.toString());
	    	sentenceStrs.add(sent.toString());
	    }
	    return sentenceStrs;
	}
	
}
