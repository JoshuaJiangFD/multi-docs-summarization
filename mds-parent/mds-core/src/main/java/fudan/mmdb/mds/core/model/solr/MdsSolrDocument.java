package fudan.mmdb.mds.core.model.solr;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.solr.client.solrj.beans.Field;

@XmlRootElement(name = "document")
@XmlType(propOrder = { "id", "title", "date","content","url" })
public class MdsSolrDocument {

	public static final String SOLR_FIELD_CONTENT = "content";
	public static final String SOLR_FIELD_ID = "id";
	public static final String SOLR_FIELD_TITLE = "title";

	@Field(SOLR_FIELD_ID)
	private String id;

	@Field(SOLR_FIELD_TITLE)
	private String title;

	@Field(SOLR_FIELD_CONTENT)
	private String content;

	private Date date;

	private String url;

	public MdsSolrDocument() {
		super();
	}

	public MdsSolrDocument(String id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}

	public MdsSolrDocument(String title, String content, Date date, String url) {
		super();
		this.title = title;
		this.content = content;
		this.date = date;
		this.url = url;
	}

	// *** getter/setters ****
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
