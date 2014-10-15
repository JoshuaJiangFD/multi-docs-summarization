package fudan.mmdb.mds.datafeeding;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.io.Files;

import fudan.mmdb.mds.core.model.solr.MdsSolrDocument;
import fudan.mmdb.mds.core.model.xmlfeed.MdsDocXmlFeed;
import fudan.mmdb.mds.core.model.xmlfeed.XmlDocConverter;
import fudan.mmdb.mds.core.model.xmlfeed.XmlDocConverterImpl;

public class XmlGenTool {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmlDocConverterImpl.class);
	
	private static final String Default_Charset="utf-8";
	
	CharsetDetector  detector=new CharsetDetector();
	
	private XmlDocConverter converter;
	
	private File sourceDir;
	
	private File targetDir;
	
	private List<String> errorFiles=new ArrayList<String>();
	
	private List<String> shortFiles=new ArrayList<String>();
	
	public XmlGenTool(){
		
	}
	
	 public XmlGenTool(File sourceDir, File targetDir) throws JAXBException {
		super();
		this.sourceDir = sourceDir;
		this.targetDir = targetDir;
		converter=new XmlDocConverterImpl();
	}
	 
	public void execute(){
		File[] files=sourceDir.listFiles();
		for(File file:files){
			if(!file.isDirectory())
				continue;
			MdsDocXmlFeed feed=new MdsDocXmlFeed();
			ArrayList<MdsSolrDocument>  docs=new ArrayList<MdsSolrDocument>();
			File[] docFiles=file.listFiles();
			for(File f:docFiles){
				MdsSolrDocument doc=parseSingleFile(f);
				if(doc!=null)
					docs.add(doc);
			}
			feed.setDocuments(docs);
			feed.setSize(docs.size());
			File newXmlFile=new File(this.targetDir,String.format("%s.xml", file.getName()));
			converter.persistToXml(feed, newXmlFile);
		}
		//persist the error files
		File errorLog=new File(this.targetDir,"error.log");
		try{
			Files.write(Joiner.on("\n").join(errorFiles), errorLog, Charsets.UTF_8);	
		}catch(Exception ex){
			LOGGER.error("Error in persisting error log.", ex);
		}
		File errorLog2=new File(this.targetDir,"error-short.log");
		try{
			Files.write(Joiner.on("\n").join(this.shortFiles), errorLog2, Charsets.UTF_8);	
		}catch(Exception ex){
			LOGGER.error("Error in persisting error log.", ex);
		}
	}
	
	public  MdsSolrDocument parseSingleFile(File docFile){
		try {
			FileInputStream stream=new FileInputStream(docFile);

			
			List<String> allLines=Files.readLines(docFile, Charset.forName("gb2312"));
			if(allLines.size()<4){
				shortFiles.add(docFile.getAbsolutePath());
				return null;
			}
			String title=allLines.get(0).trim();
			String url=allLines.get(1).trim();
			String dateStr=allLines.get(2).trim();
			DateFormat formatter=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			Date date=formatter.parse(dateStr);
			List<String> contentLst=allLines.subList(3,allLines.size());
			Iterable<String> filtered=Iterables.filter(contentLst, new Predicate<String>(){
				@Override
				public boolean apply(String str) {
					if(Strings.isNullOrEmpty(str))
					return false;
					String trimmed=str.trim();
					if(Strings.isNullOrEmpty(trimmed))
						return false;
					return true;
				}
				
			});
			String content=Joiner.on("\n").skipNulls().join(filtered);
			return new MdsSolrDocument(title,content,date,url);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorFiles.add(docFile.getAbsolutePath());
			return null;
		}
	}
	
	
	
	String detectCharset(FileInputStream inputStream){
		try{
			byte[] bytesToCheck=new byte[10];
			inputStream.read(bytesToCheck, 0, 10);
			detector.setText(bytesToCheck);
			CharsetMatch match=detector.detect();
			if(match==null)
				return  Default_Charset;
			else
				return match.getName();	
		}catch(Exception ex){
			this.LOGGER.error("Error in detect file eocoding.",ex);
			return this.Default_Charset;
		}
		
		
	}
	//****** getter/setter	***********
	public File getSourceDir() {
		return sourceDir;
	}


	public void setSourceDir(File sourceDir) {
		this.sourceDir = sourceDir;
	}


	public File getTargetDir() {
		return targetDir;
	}


	public void setTargetDir(File targetDir) {
		this.targetDir = targetDir;
	}
}
