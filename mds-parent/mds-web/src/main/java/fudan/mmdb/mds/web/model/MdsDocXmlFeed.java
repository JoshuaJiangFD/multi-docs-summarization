package fudan.mmdb.mds.web.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MdsDocXmlFeed {

	@XmlElementWrapper(name="docList")
	@XmlElement(name="document")
	private ArrayList<MdsSolrDocument> documents;
	
	@XmlAttribute
	private int size;

	public ArrayList<MdsSolrDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(ArrayList<MdsSolrDocument> documents) {
		this.documents = documents;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
