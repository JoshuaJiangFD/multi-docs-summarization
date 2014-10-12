package fudan.mmdb.mds.datafeeding;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.google.common.base.Joiner;

import fudan.mmdb.mds.core.model.solr.MdsSolrDocument;
import fudan.mmdb.mds.core.model.xmlfeed.MdsDocXmlFeed;
import fudan.mmdb.mds.core.model.xmlfeed.XmlDocConverter;
import fudan.mmdb.mds.core.model.xmlfeed.XmlDocConverterImpl;

public class XmlGenTool {

	private XmlDocConverter converter;
	
	private File sourceDir;
	
	private File targetDir;
	
	 public XmlGenTool(File sourceDir, File targetDir) throws JAXBException {
		super();
		this.sourceDir = sourceDir;
		this.targetDir = targetDir;
		converter=new XmlDocConverterImpl();
	}


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
	}
	
	private MdsSolrDocument parseSingleFile(File docFile){
		try {
			List<String> allLines=Files.readAllLines(docFile.toPath(),Charset.forName("gb2312"));
			String title=allLines.get(0).trim();
			String url=allLines.get(1).trim();
			String dateStr=allLines.get(2).trim();
			DateFormat formatter=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			Date date=formatter.parse(dateStr);
			List<String> contentLst=allLines.subList(3,allLines.size()-1);
			String content=Joiner.on("\n").skipNulls().join(contentLst);
			return new MdsSolrDocument(title,content,date,url);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}