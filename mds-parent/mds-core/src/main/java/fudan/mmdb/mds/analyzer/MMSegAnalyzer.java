package fudan.mmdb.mds.analyzer;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.chenlb.mmseg4j.example.Complex;
import com.chenlb.mmseg4j.example.MaxWord;
import com.chenlb.mmseg4j.example.Simple;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

public class MMSegAnalyzer implements IZHAnalyzer {

    private static Logger logger=Logger.getLogger(MMSegAnalyzer.class);
    
    private AnalyzeMode mode;

    private Complex complexAnalyzer;

    private Simple simpleAnalyzer;

    private MaxWord maxWordAnalyzer;

    private static String SPLITTER = "|";
    
    
    public MMSegAnalyzer(){
        this(AnalyzeMode.Complex);
    }

    public MMSegAnalyzer(AnalyzeMode mode) {
        if (mode == null)
            mode = AnalyzeMode.Complex;
        this.mode = mode;
        switch (this.mode) {
        case Simple:
            simpleAnalyzer = new Simple();
            break;
        case Complex:
            complexAnalyzer = new Complex();
            break;
        case MaxWord:
            maxWordAnalyzer = new MaxWord();
            break;
        }
    }

    public List<String> analyze(String context) {
        String result = null;
        try {
            switch (this.mode) {
            case Simple:
                result = simpleAnalyzer.segWords(context, SPLITTER);
                break;
            case Complex:
                result = complexAnalyzer.segWords(context, SPLITTER);
                break;
            case MaxWord:
                result = maxWordAnalyzer.segWords(context, SPLITTER);
                break;
            }
            List<String> words = Lists.newArrayList(Splitter.on(SPLITTER)
                    .split(result));
            return words;
        } catch (Exception ex) {
            String error=String.format("Error in analyzing %s", context);
            logger.error(error,ex);
            return null;
        }

    }

}

enum AnalyzeMode {
    Simple, Complex, MaxWord
}
