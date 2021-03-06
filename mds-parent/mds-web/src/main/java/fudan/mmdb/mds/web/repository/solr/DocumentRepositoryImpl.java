package fudan.mmdb.mds.web.repository.solr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.stereotype.Repository;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import fudan.mmdb.mds.core.model.solr.MdsSolrDocument;
import fudan.mmdb.mds.web.controllers.HomeController;
import fudan.mmdb.mds.web.model.ClusteredItem;
import fudan.mmdb.mds.web.model.ClusteredResponse;

@Repository
public class DocumentRepositoryImpl implements ClusteringRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private SolrOperations solrTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public ClusteredResponse getClusters(String searchTerm) {
		
		ClusteredResponse clsrResp=new ClusteredResponse();
		List<ClusteredItem> clstrItems = Lists.newArrayList();
		SolrQuery query = new SolrQuery().setRequestHandler("/clustering").setParam("q",searchTerm);
		try {
			SolrServer server = solrTemplate.getSolrServer();
			QueryResponse response = server.query(query);
			NamedList<Object> repObjs = response.getResponse();
			//parse clstrs
			Object clusterNode = repObjs.get("clusters");
			List<Object> clusterInfos = (List<Object>) clusterNode;
			for (Object clusterInfo : clusterInfos) {
				NamedList<Object> clstrInfo = (NamedList<Object>) clusterInfo;
				clstrItems.add(parseClstrItem(clstrInfo));
			}
			clsrResp.setClusters(clstrItems);
			//parse docs
			List<MdsSolrDocument> docBeans=response.getBeans(MdsSolrDocument.class);
			Map<String, Map<String, List<String>>> highlighted=response.getHighlighting();
			//merge the result
			for(MdsSolrDocument doc:docBeans){
				Map<String,List<String>> entry=highlighted.get(doc.getId());
				if(entry!=null){
					List<String> snippets=entry.get("content");
					if(snippets!=null){
						String joined=Joiner.on("...").join(snippets);
						doc.setContent(joined);
					}
				}
			}
			clsrResp.setDocs(docBeans);
			return clsrResp;
		} catch (Exception ex) {
			LOGGER.error("Error in getClusters from solr.",ex);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private ClusteredItem parseClstrItem(NamedList<Object> clstrInfo) {
		ClusteredItem item = new ClusteredItem();
		for (Map.Entry<String, Object> doc : clstrInfo) {
			String key = doc.getKey();
			Object value = doc.getValue();
			if ("labels".equals(key)) {
				List<String> labels = (ArrayList<String>) value;
				String labelStr=labels.get(0);
				item.setLabel(labelStr);
				if("other-topics".equals(labelStr))
					item.setIsOtherTopsic(true);
				
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
