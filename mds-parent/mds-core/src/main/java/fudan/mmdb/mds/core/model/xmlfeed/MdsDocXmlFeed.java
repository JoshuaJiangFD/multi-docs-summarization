package fudan.mmdb.mds.core.model.xmlfeed;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import fudan.mmdb.mds.core.model.MdsDocument;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MdsDocXmlFeed {

	@XmlElementWrapper(name="docList")
	@XmlElement(name="document")
	private ArrayList<MdsDocument> documents;
	
	@XmlAttribute
	private int size;

	public ArrayList<MdsDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(ArrayList<MdsDocument> documents) {
		this.documents = documents;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
