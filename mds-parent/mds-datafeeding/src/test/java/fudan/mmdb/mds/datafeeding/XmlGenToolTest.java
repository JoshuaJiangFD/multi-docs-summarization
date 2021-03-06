package fudan.mmdb.mds.datafeeding;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.bind.JAXBException;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.Assert;

import fudan.mmdb.mds.core.model.MdsDocument;

public class XmlGenToolTest {

	@Test
	public void testExecute() throws JAXBException {
		File sourceDir=new File("C:\\Users\\泳\\Desktop\\dataset_619961\\619961\\newsdata");
		File targetDir=new File("C:\\Users\\泳\\Desktop\\dataset_619961\\target2");
		XmlGenTool tool=new XmlGenTool(sourceDir,targetDir);
		tool.execute();
	}
	
	@Test
	@Ignore
	public void test_parseSingleFile(){
		File sourceFile=new File("C:\\Users\\泳\\Desktop\\dataset_619961\\619961\\newsdata\\20131101\\IFENG-449.txt");
		XmlGenTool tool=new XmlGenTool();
		MdsDocument  doc=tool.parseSingleFile(sourceFile);
		Assert.notNull(doc);
	}
	
	@Test
	@Ignore
	public void test_detectEncode() throws Exception{
		File sourceFile=new File("C:\\Users\\泳\\Desktop\\dataset_619961\\619961\\newsdata\\20131101\\IFENG-0.txt");
		XmlGenTool tool=new XmlGenTool();
		String encode=tool.detectCharset(new FileInputStream(sourceFile));
		System.out.println(encode);
		File file2=new File("C:/Users/泳/Desktop/dataset_619961/619961/newsdata/20131102/163-72.txt");
		String encode2=tool.detectCharset(new FileInputStream(file2));
		System.out.println(encode2);
		
		
		
	}

}
