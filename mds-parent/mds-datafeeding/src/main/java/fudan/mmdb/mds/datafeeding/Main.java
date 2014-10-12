package fudan.mmdb.mds.datafeeding;

import java.io.File;

import javax.xml.bind.JAXBException;

public class Main {

	public static void main(String[] args) throws Exception {
		info("Simple  Xml Generating Tool version 1.5");
		if ((0 < args.length)
				&& (("-help".equals(args[0])) || ("--help".equals(args[0])) || ("-h"
						.equals(args[0])))) {
			usage();
		} else if (args.length != 2) {
			usage();
		} else {
			XmlGenTool t = parseArgsAndInit(args);
			if(t==null)
				usage();
			else
				t.execute();
		}
	}

	protected static XmlGenTool parseArgsAndInit(String[] args) throws Exception {
		String sourceDir=args[0];
		String targetDir=args[1];
		File sourceFile=new File(sourceDir);
		File targetFile=new File(targetDir);
		if(!sourceFile.exists()||!sourceFile.isDirectory())
			return null;
		if(!targetFile.exists()||!sourceFile.isDirectory())
			return null;
		return new XmlGenTool(sourceFile,targetFile);
	}

	static void info(String msg) {
		System.out.println(msg);
	}

	private static void usage() {
		System.out
				.println("Usage: java [SystemProperties] -jar mds-datafeeding.jar <sourcedir> <targetdir>..\n\n"
						+ "Supported System Properties and their defaults:\n  "
						+ "This is a simple command line tool for converting  raw text data to a xml file with type supported by MDS project.  "
						+ "Data can be read from files specified as commandline args,\n"
						+ "\nExamples:\n  " + "java -jar post.jar *.xml\n");
	}
}
