package fudan.mmdb.mds.helpers;

import java.io.File;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

public class Gbk2UftConverter {

    public static void main(String[] args) {
        convert("files-gb2312","files-utf8");

    }

    public static void convert(String rootPath,String copyRootPath){
        File rootFile=new File(rootPath);
        File copyRootFile=new File(copyRootPath);
        
        Queue<File> pendingFiles=new LinkedList<File>();
        pendingFiles.add(rootFile);
        while(!pendingFiles.isEmpty()){
            File polled=pendingFiles.poll();
            System.out.printf("process file %s\n",polled.getName());    
            if(polled.isDirectory()){
                File[] children=polled.listFiles();
                pendingFiles.addAll(Lists.newArrayList(children));
                continue;
            }
            File toFile=new File(copyRootFile,polled.getParent());
            if(!toFile.exists())
                toFile.mkdirs();
            writeAfterRead(polled,new File(toFile,polled.getName()));
        }
    }
    
    public static void writeAfterRead(File file,File toFile){
        try{
            
            List<String> allLines =Files.readLines(file,Charset.forName("gb2312"));
            String content=Joiner.on("\n").skipNulls().join(allLines);
            //write
            Files.write(content, toFile, Charsets.UTF_8);
            
        }catch(Exception ex){
           System.out.println(ex);
        }
        
    }
}
