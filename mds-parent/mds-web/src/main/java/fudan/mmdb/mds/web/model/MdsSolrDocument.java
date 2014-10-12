package fudan.mmdb.mds.web.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.solr.client.solrj.beans.Field;

@XmlRootElement(name = "document")
@XmlType(propOrder={"id","title","content"})
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


    public MdsSolrDocument() {
        super();
    }

    public MdsSolrDocument(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    //***   getter/setters ****
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

}
