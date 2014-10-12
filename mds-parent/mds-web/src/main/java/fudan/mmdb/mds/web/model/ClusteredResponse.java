package fudan.mmdb.mds.web.model;

import java.util.List;

import fudan.mmdb.mds.core.model.solr.MdsSolrDocument;

public class ClusteredResponse {

	private List<ClusteredItem> clusters;
	
	private List<MdsSolrDocument> docs;

	
	
	
	//**** setters/getters*****
	public List<ClusteredItem> getClusters() {
		return clusters;
	}

	public void setClusters(List<ClusteredItem> clusters) {
		this.clusters = clusters;
	}

	public List<MdsSolrDocument> getDocs() {
		return docs;
	}

	public void setDocs(List<MdsSolrDocument> docs) {
		this.docs = docs;
	}


	
	
}
