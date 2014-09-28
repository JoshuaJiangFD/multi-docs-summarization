package fudan.mmdb.mds.wordsimilarity.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Word {
	
	private String word;
	
	private String type;
	
    private String firstPrimitive;

    private List<String> otherPrimitives = new ArrayList<String>();


    private List<String> structruralWords = new ArrayList<String>();


    private Map<String, List<String>> relationalPrimitives = new HashMap<String, List<String>>();


    private Map<String, List<String>> relationSimbolPrimitives = new HashMap<String, List<String>>();

    
    public Word(){
       
    }
    
    
    public Word(String wordStr,String type){
        this.word=wordStr;
        this.type=type;
    }
    
    public String getWord() {
        return word;
    }
    
    public boolean isStructruralWord() {
        return !structruralWords.isEmpty();
    }


    public void setWord(String word) {
        this.word = word;
    }


    @Override
    public String toString() {
        return "[word=" + word + ", type=" + type + "]";
    }


    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getFirstPrimitive() {
        return firstPrimitive;
    }


    public void setFirstPrimitive(String firstPrimitive) {
        this.firstPrimitive = firstPrimitive;
    }

 
    public List<String> getOtherPrimitives() {
        return new ArrayList<String>(otherPrimitives);
    }


    public void setOtherPrimitives(List<String> otherPrimitives) {
        this.otherPrimitives = new ArrayList<String>(otherPrimitives);
    }


    public void addOtherPrimitive(String otherPrimitive) {
        this.otherPrimitives.add(otherPrimitive);
    }


    public List<String> getStructruralWords() {
        return new ArrayList<String>(structruralWords);
    }


    public void setStructruralWords(List<String> structruralWords) {
        this.structruralWords = new ArrayList<String>(structruralWords);
    }


    public void addStructruralWord(String structruralWord) {
        this.structruralWords.add(structruralWord);
    }

    public void addRelationalPrimitive(String key, String value) {
        List<String> list = relationalPrimitives.get(key);

        if (list == null) {
            list = new ArrayList<String>();
            list.add(value);
            relationalPrimitives.put(key, list);
        } else {
            list.add(value);
        }
    }


    public void addRelationSimbolPrimitive(String key, String value) {
        List<String> list = relationSimbolPrimitives.get(key);

        if (list == null) {
            list = new ArrayList<String>();
            list.add(value);
            relationSimbolPrimitives.put(key, list);
        } else {
            list.add(value);
        }
    }


    public Map<String, List<String>> getRelationalPrimitives() {
        return new HashMap<String, List<String>>(relationalPrimitives);
    }


    public Map<String, List<String>> getRelationSimbolPrimitives() {
        return new HashMap<String, List<String>>(relationSimbolPrimitives);
    }
}
