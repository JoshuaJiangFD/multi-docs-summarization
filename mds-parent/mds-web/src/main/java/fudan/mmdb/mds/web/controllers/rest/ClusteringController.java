package fudan.mmdb.mds.web.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fudan.mmdb.mds.web.model.ClusteredResponse;
import fudan.mmdb.mds.web.model.SumRequest;
import fudan.mmdb.mds.web.services.RootService;

@Controller
@RequestMapping(value = { "/rest" })
public class ClusteringController {

	@Autowired
	private RootService rootService;

	public RootService getRootService() {
		return rootService;
	}

	public void setRootService(RootService rootService) {
		this.rootService = rootService;
	}

	@RequestMapping(value = "/cluster/{queryTerm}", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody ClusteredResponse clusteringViaQuery(String queryTerm) {
		return this.rootService.searchAndCluster(queryTerm);
	}

	@RequestMapping(value = "/sum", method = RequestMethod.GET, headers = { "Accept=application/json" })
	@ResponseBody
	public String summarize(@RequestBody SumRequest request) {
		if (!validateSumRequest(request))
			return null;
		return this.rootService.getSummary(request);
	}

	private Boolean validateSumRequest(SumRequest request){
		if(request.getDocIds()==null)
			return false;
		if(request.getDocIds().size()==0)
			return false;
		return true;
	}
}
