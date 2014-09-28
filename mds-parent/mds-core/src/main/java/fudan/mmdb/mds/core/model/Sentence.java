package fudan.mmdb.mds.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fudan.mmdb.mds.wordsimilarity.model.Word;

public class Sentence {
    
    private String id;
    
    private int docId;
    
    private String content;
        
    private double docPos;

    private List<Word> words;
    
   
    public int getWordCount() {
        if (words != null)
            return this.words.size();
        else
            return 0;
    }

    /**
     * 获得这个句子的n-gram。 <br/>
     *  "欢迎/您/登录/招商/银行/信用卡/网站" 2元划分的话，返回的是一个list
     * [欢迎/您,您/登录,登录/招商,招商/银行,银行/信用卡,信用卡/网站]
     * 
     * @param n 
     * 
     * @return
     */
    public List<Ngram> getNgrams(int n) {
        if (n > words.size()) {
            throw new IllegalArgumentException("n is bigger than the word count.");
        }
        List<Ngram> result = new ArrayList<Ngram>();
        for (int i = 0; i <= (words.size() - n); i++) {
            List<Word> subList = words.subList(i, i + n);
            Ngram ngram = new Ngram(subList);
            result.add(ngram);
        }
        return result;
    }

    // ++++ getters/setters+++++
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public double getDocPos() {
        return docPos;
    }

    public void setDocPos(double docPos) {
        this.docPos = docPos;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

}
