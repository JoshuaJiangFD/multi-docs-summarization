package fudan.mmdb.mds.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fudan.mmdb.mds.wordsimilarity.model.Word;

public class Ngram {

    private int n;

    private List<Word> grams;

    public Ngram(List<Word> grams) {
        this.n = grams.size();
        this.grams = new ArrayList<Word>(grams);
    }
  
    public int getN() {
        return n;
    }


    public void setN(int n) {
        this.n = n;
    }


    public List<Word> getGrams() {
        return new ArrayList<Word>(grams);
    }


    public void setGrams(List<Word> grams) {
        this.grams = new ArrayList<Word>(grams);
    }


    public String toString() {
        String result = "";
        result += n;

        for (int i = 0; i < grams.size(); i++) {
            result += grams.get(i).getWord();
        }

        return result;
    }
}
