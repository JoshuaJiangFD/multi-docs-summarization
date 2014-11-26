package ICTCLAS.I3S.AC;

import java.io.File;

public class ICTCLAS50 {
	
	private final static Object lock=new Object();
    // public enum eCodeType
    // {
    // CODE_TYPE_UNKNOWN,//type unknown
    // CODE_TYPE_ASCII,//ASCII
    // CODE_TYPE_GB,//GB2312,GBK,GB10380
    // CODE_TYPE_UTF8,//UTF-8
    // CODE_TYPE_BIG5//BIG5
    // }

    public native boolean ICTCLAS_Init(byte[] sPath);

    public native boolean ICTCLAS_Exit();

    public native int ICTCLAS_ImportUserDictFile(byte[] sPath, int eCodeType);

    public native int ICTCLAS_SaveTheUsrDic();

    public native int ICTCLAS_SetPOSmap(int nPOSmap);

    public native boolean ICTCLAS_FileProcess(byte[] sSrcFilename,
            int eCodeType, int bPOSTagged, byte[] sDestFilename);

    public native byte[] ICTCLAS_ParagraphProcess(byte[] sSrc, int eCodeType,
            int bPOSTagged);

    public native byte[] nativeProcAPara(byte[] sSrc, int eCodeType,
            int bPOStagged);

    private ICTCLAS50(String ictclasRoot){
        try {
        	File file=new File(ictclasRoot,"ICTCLAS50.dll");
            String dllPath = file.getAbsolutePath();
            System.load(dllPath);
        } catch (Exception ex) {
        	ex.printStackTrace();
            System.exit(-1);
        }
    }
    
    private static ICTCLAS50 instance;
    
    public static ICTCLAS50 getInstance(String ictclasRoot){
    	if(instance==null){
    		synchronized(lock){
    			if(instance==null)
    				instance=new ICTCLAS50(ictclasRoot);
    		}
    	}
    		return instance;
    }
    
    /**
     * use spring resource loader to find the directory under classpath
     */
//    /* Use static initializer */
//    static {
//        ResourceLoader loader = new DefaultResourceLoader();
//        Resource resource = loader
//                .getResource("classpath:ictclas/ICTCLAS50.dll");
//        try {
//            String dllPath = resource.getFile().getAbsolutePath();
//            System.load(dllPath);
//        } catch (Exception ex) {
//            System.exit(-1);
//        }
//        // System.loadLibrary("ICTCLAS50");
//    }
}
