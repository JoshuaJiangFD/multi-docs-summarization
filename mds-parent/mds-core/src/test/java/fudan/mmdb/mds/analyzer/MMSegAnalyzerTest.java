package fudan.mmdb.mds.analyzer;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MMSegAnalyzerTest {

    private     MMSegAnalyzer analyzer;
    
    @Before
    public void setUp() throws Exception {
        analyzer=new MMSegAnalyzer(AnalyzeMode.Complex);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        //String text="京华时报２００８年1月23日报道 昨天，受一股来自中西伯利亚的强冷空气影响，本市出现大风降温天气，白天最高气温只有零下7摄氏度，同时伴有6到7级的偏北风。";
        String text="填写暑假休假信息汇总表交导师";
        List<String> words=analyzer.analyze(text);
        for(String str: words)
        System.out.println(str);
        fail("Not yet implemented");
    }

}
