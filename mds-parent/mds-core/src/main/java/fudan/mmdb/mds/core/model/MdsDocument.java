package fudan.mmdb.mds.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.google.common.collect.Lists;

@XmlRootElement(name = "document")
@Document(collection = "MdsDocuments")
@XmlType(propOrder = { "id", "title", "date","content","url" })
public class MdsDocument {

    @Id
    @Field
    private String id;

    @Field
    private String title;

    @Field
    private String content;

    @Field
    private int wordCount;

    @Field
    private String url;
    
    
    private Date date;

	@Field
    private Map<String, Integer> termFreqs = new HashMap<String, Integer>();

    @Field
    private List<Sentence> sentences = new ArrayList<Sentence>();
    
    public MdsDocument(){
    	
    }
    
    public MdsDocument(String title, String content, String url, Date date) {
		super();
		this.title = title;
		this.content = content;
		this.url = url;
		this.date = date;
	}

	// ++++ getters/setters ++++
    public List<String> getWords() {
        return Lists.newArrayList(this.termFreqs.keySet());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String cont) {
        this.content = cont;
    }
    
    @XmlTransient
    public int getWordCount() {
        return wordCount;
    }
    
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    @XmlTransient
    public Map<String, Integer> getTermFreqs() {
        return termFreqs;
    }

    @XmlTransient
    public List<Sentence> getSentences() {
        return sentences;
    }

    public void setSentences(List<Sentence> sentences) {
        this.sentences = sentences;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    

    public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
