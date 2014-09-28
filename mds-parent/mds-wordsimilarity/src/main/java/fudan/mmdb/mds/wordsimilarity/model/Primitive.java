package fudan.mmdb.mds.wordsimilarity.model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;


public class Primitive {
    private static Logger logger = Logger.getLogger(Primitive.class);

    public static Map<Integer, Primitive> ALLPRIMITIVES = new HashMap<Integer, Primitive>();


    public static Map<String, Integer> PRIMITIVESID = new HashMap<String, Integer>();
    
    private String primitive;

    private int id;
    
    private int parentId;
    
    static{
    	LoadData();
    }
    
    /**
     * 加载义原文件。
     */
     static void LoadData(){
        String line = null;
        BufferedReader reader = null;
        try {
            logger.info("start to load the file: WHOLE.DAT");
			ResourceLoader loader = new DefaultResourceLoader();
			Resource resource = loader.getResource("classpath:dict-utf8/WHOLE.DAT");
			reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), "utf-8"));
            line = reader.readLine();

            while (line != null) {
                line = line.trim().replaceAll("\\s+", " ");
                String[] strs = line.split(" ");
                int id = Integer.parseInt(strs[0]);
                String[] words = strs[1].split("\\|");
                String english = words[0];
                String chinaese = strs[1].split("\\|")[1];
                int parentId = Integer.parseInt(strs[2]);
                ALLPRIMITIVES.put(id, new Primitive(id, chinaese, parentId));

                PRIMITIVESID.put(chinaese, id);
                PRIMITIVESID.put(english, id);
                
                line = reader.readLine();
            }
            logger.info("finished loading the file WHOLE.DAT");
        } catch (Exception e) {
        	logger.fatal(  "Failed to load the file WHOLE.DAT. ", e);
        }finally{
             try {
                reader.close();
            } catch (IOException e) {
            	logger.fatal("Failed to load the file WHOLE.DAT. " + e);
            }
        }
    }


    public Primitive(int id, String primitive, int parentId) {
        this.id = id;
        this.parentId = parentId;
        this.primitive = primitive;
    }


    public String getPrimitive() {
        return primitive;
    }


    public int getId() {
        return id;
    }


    public int getParentId() {
        return parentId;
    }


    public boolean isTop() {
        return id == parentId;
    }


    public static List<Integer> getParents(String primitive) {
        List<Integer> list = new ArrayList<Integer>();

        // get the id of this primitive
        Integer id = PRIMITIVESID.get(primitive);

        if (id != null) {
            Primitive parent = ALLPRIMITIVES.get(id);
            list.add(id);
            while (!parent.isTop()) {
                list.add(parent.getParentId());
                parent = ALLPRIMITIVES.get(parent.getParentId());
            }
        }
        return list;
    }

    public static boolean isPrimitive(String primitive){
        return PRIMITIVESID.containsKey(primitive);
    }
}
