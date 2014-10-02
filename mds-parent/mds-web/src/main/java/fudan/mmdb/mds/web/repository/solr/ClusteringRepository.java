package fudan.mmdb.mds.web.repository.solr;

import java.util.List;

import fudan.mmdb.mds.web.model.ClusteredItem;

public interface ClusteringRepository {
	
	public List<ClusteredItem> getClusters(String searchTerm);
	
}
