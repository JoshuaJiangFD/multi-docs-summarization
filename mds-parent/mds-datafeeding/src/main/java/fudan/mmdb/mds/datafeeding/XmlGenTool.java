package fudan.mmdb.mds.datafeeding;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.sax.WriteOutContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.Files;

import fudan.mmdb.mds.core.model.MdsDocument;
import fudan.mmdb.mds.core.model.xmlfeed.MdsDocXmlFeed;
import fudan.mmdb.mds.core.model.xmlfeed.XmlDocConverter;
import fudan.mmdb.mds.core.model.xmlfeed.XmlDocConverterImpl;

public class XmlGenTool {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(XmlDocConverterImpl.class);

	private static final String Default_Charset = "utf-8";

	DateFormat formatter_1 = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

	DateFormat formatter_2 = new SimpleDateFormat("yyyy-mm-dd hh:mm");

	Parser parser = new TXTParser();

	private XmlDocConverter converter;

	private File sourceDir;

	private File targetDir;

	private List<String> errorFiles = new ArrayList<String>();

	private List<String> shortFiles = new ArrayList<String>();

	public XmlGenTool() {

	}

	public XmlGenTool(File sourceDir, File targetDir) throws JAXBException {
		super();
		this.sourceDir = sourceDir;
		this.targetDir = targetDir;
		converter = new XmlDocConverterImpl();
	}

	public void execute() {
		File[] files = sourceDir.listFiles();
		for (File file : files) {
			if (!file.isDirectory())
				continue;
			MdsDocXmlFeed feed = new MdsDocXmlFeed();
			ArrayList<MdsDocument> docs = new ArrayList<MdsDocument>();
			File[] docFiles = file.listFiles();
			for (File f : docFiles) {
				MdsDocument doc = parseSingleFile(f);
				if (doc != null)
					docs.add(doc);
			}
			feed.setDocuments(docs);
			feed.setSize(docs.size());
			File newXmlFile = new File(this.targetDir, String.format("%s.xml",
					file.getName()));
			converter.persistToXml(feed, newXmlFile);
		}
		// persist the error files
		File errorLog = new File(this.targetDir, "error.log");
		try {
			Files.write(Joiner.on("\n").join(errorFiles), errorLog,
					Charsets.UTF_8);
		} catch (Exception ex) {
			LOGGER.error("Error in persisting error log.", ex);
		}
		File errorLog2 = new File(this.targetDir, "error-short.log");
		try {
			Files.write(Joiner.on("\n").join(this.shortFiles), errorLog2,
					Charsets.UTF_8);
		} catch (Exception ex) {
			LOGGER.error("Error in persisting error log.", ex);
		}
	}

	public MdsDocument parseSingleFile(File docFile) {
		try {
			if(LOGGER.isDebugEnabled())
				LOGGER.info("handling: {}",docFile.getAbsolutePath());
			
			FileInputStream inputStream=new FileInputStream(docFile);
			Metadata meta =new Metadata();
			WriteOutContentHandler handler = new WriteOutContentHandler();
			ParseContext parse = new ParseContext();
			parser.parse(inputStream, handler, meta, parse);
			String rawcontent = handler.toString();
			
			List<String> allLines=Lists.newArrayList(rawcontent.split("\n"));
			
			Iterable<String> filtered = Iterables.filter(allLines,
					new Predicate<String>() {
						@Override
						public boolean apply(String str) {
							if (Strings.isNullOrEmpty(str))
								return false;
							String trimmed = str.trim();
							if (Strings.isNullOrEmpty(trimmed))
								return false;
							return true;
						}
					});
			allLines=Lists.newArrayList(filtered);

			String title = allLines.get(0).trim();
			String url = allLines.get(1).trim();
			String dateStr = allLines.get(2).trim();
			Date date = parseDateStr(dateStr);
			
			List<String> contentLst = allLines.subList(3, allLines.size());
			if(contentLst.size()<1)
			{
				shortFiles.add(docFile.getAbsolutePath());
				return null;
			}
			
			 String content = Joiner.on("\n").skipNulls().join(contentLst);
			return new MdsDocument(title, content, url, date);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorFiles.add(docFile.getAbsolutePath());
			return null;
		}
	}

	String detectCharset(FileInputStream inputStream) {
		try {
			Parser p = new TXTParser();
			Metadata meta =new Metadata();
			WriteOutContentHandler handler = new WriteOutContentHandler();
			ParseContext parse = new ParseContext();
			p.parse(inputStream, handler, meta, parse);
			String content = handler.toString();
			List<String> lines=Lists.newArrayList(content.split("\n"));
			Iterable<String> filtered = Iterables.filter(lines,
					new Predicate<String>() {
						@Override
						public boolean apply(String str) {
							if (Strings.isNullOrEmpty(str))
								return false;
							String trimmed = str.trim();
							if (Strings.isNullOrEmpty(trimmed))
								return false;
							return true;
						}
					});
			lines=Lists.newArrayList(filtered);
			System.out.println(lines.get(0));
			System.out.println(lines.get(1));
			System.out.println(lines.get(2));
			return content;
		} catch (Exception ex) {
			this.LOGGER.error("Error in detect file eocoding.", ex);
			return this.Default_Charset;
		}
	}

	Date parseDateStr(String dateStr) {
		try {
			return formatter_1.parse(dateStr);
		} catch (ParseException ex) {
			try {
				return formatter_2.parse(dateStr);
			} catch (Exception ex1) {
				return null;
			}
		}
	}

	// ****** getter/setter ***********
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
