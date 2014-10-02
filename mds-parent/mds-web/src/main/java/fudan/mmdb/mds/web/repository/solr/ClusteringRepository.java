package fudan.mmdb.mds.web.repository.solr;

import fudan.mmdb.mds.web.model.ClusteredResponse;

public interface ClusteringRepository {
	
	public ClusteredResponse getClusters(String searchTerm);
	
}
