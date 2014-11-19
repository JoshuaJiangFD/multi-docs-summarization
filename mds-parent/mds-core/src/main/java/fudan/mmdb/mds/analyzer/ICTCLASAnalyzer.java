package fudan.mmdb.mds.analyzer;

import ICTCLAS.I3S.AC.ICTCLAS50;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.google.common.base.Strings;

public class ICTCLASAnalyzer implements IZHAnalyzer {

    private Logger logger = Logger.getLogger(ICTCLASAnalyzer.class);

    ICTCLAS50 testICTCLAS50 = new ICTCLAS50();

    public ICTCLASAnalyzer() {
        ResourceLoader loader = new DefaultResourceLoader();
        Resource resource = loader
                .getResource("classpath:ictclas/ICTCLAS50.dll");
        try {
            String configDirPath = resource.getFile().getParentFile().getAbsolutePath();
            if (testICTCLAS50.ICTCLAS_Init(configDirPath.getBytes("GB2312")) == false) {
                logger.fatal("Init ICTCLAS50 Fail!");
            }
            // 设置词性标注集(0 计算所二级标注集，1 计算所一级标注集，2 北大二级标注集，3 北大一级标注集)
            testICTCLAS50.ICTCLAS_SetPOSmap(2);

        } catch (Exception ex) {
        	logger.fatal("Exception in ICTCLASAnalyzer cstr.",ex);
            System.exit(-1);
        }
    }

    @Override
    public List<String> analyze(String sInput) { 
        String result=paragraphProcess(sInput, "utf-8");
        if(Strings.isNullOrEmpty(result))
            return null;
        
        return Arrays.asList(result.split("\\s+"));
    }

    public synchronized String paragraphProcess(String sParagraph,
            String charSet) {
        String result = null;
        try {
            byte nativeBytes[];
            nativeBytes = testICTCLAS50.ICTCLAS_ParagraphProcess(
                    sParagraph.getBytes(charSet), 0, 1);
            result = new String(nativeBytes, 0, nativeBytes.length, charSet);
            return result;
        } catch (Exception e) {
            logger.error("Error in processing paragraph.", e);
            return null;
        }
    }
}

