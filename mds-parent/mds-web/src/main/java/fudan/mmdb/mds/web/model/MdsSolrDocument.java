package fudan.mmdb.mds.web.model;

import org.apache.solr.client.solrj.beans.Field;

public class MdsSolrDocument {

    public static final String FIELD_CONTENT = "content";
    public static final String FIELD_ID = "id";
    public static final String FIELD_TITLE = "title";

    @Field(FIELD_ID)
    private String id;

    @Field(FIELD_TITLE)
    private String title;

    @Field(FIELD_CONTENT)
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
