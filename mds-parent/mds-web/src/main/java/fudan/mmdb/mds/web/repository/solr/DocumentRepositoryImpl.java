package fudan.mmdb.mds.web.repository.solr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

import fudan.mmdb.mds.web.model.ClusteredItem;

@Repository
public class DocumentRepositoryImpl implements ClusteringRepository {

	@Autowired
	private SolrOperations solrTemplate;

	@Override
	public List<ClusteredItem> getClusters(String searchTerm) {
		List<ClusteredItem> items = Lists.newArrayList();
		SolrQuery query = new SolrQuery().setRequestHandler("/clustering").setParam("q",searchTerm);
		try {
			SolrServer server = solrTemplate.getSolrServer();
			QueryResponse response = server.query(query);
			NamedList<Object> repObjs = response.getResponse();
			Object clusterNode = repObjs.get("clusters");
			List<Object> clusterInfos = (List<Object>) clusterNode;
			for (Object clusterInfo : clusterInfos) {
				NamedList<Object> clstrInfo = (NamedList<Object>) clusterInfo;
				items.add(parseClstrItem(clstrInfo));
			}
			return items;
		} catch (Exception ex) {
			return null;
		}

	}

	private ClusteredItem parseClstrItem(NamedList<Object> clstrInfo) {
		ClusteredItem item = new ClusteredItem();
		for (Map.Entry<String, Object> doc : clstrInfo) {
			String key = doc.getKey();
			Object value = doc.getValue();
			if ("labels".equals(key)) {
				List<String> labels = (ArrayList<String>) value;
				item.setLabel(labels.get(0));
			} else if ("score".equals(key)) {
				double score = (Double) value;
				item.setScore(score);
			} else if ("docs".endsWith(key)) {
				List<String> docIds = (ArrayList<String>) value;
				item.setIds(docIds);
			}
		}
		return item;
	}
	
	//**getter/setters*****
	public void setSolrTemplate(SolrOperations template){
		this.solrTemplate=template;
	}

}
