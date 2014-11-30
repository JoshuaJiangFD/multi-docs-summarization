package fudan.mmdb.mds.web.controllers.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;

import fudan.mmdb.mds.web.model.ClusteredResponse;
import fudan.mmdb.mds.web.model.SumRequest;
import fudan.mmdb.mds.web.model.SumResponse;
import fudan.mmdb.mds.web.services.RootService;

@Controller
@RequestMapping(value = { "/rest" })
public class ClusteringController {

	//slf4j: simple log facade library.
	private static Logger LOGGER=LoggerFactory.getLogger(ClusteringController.class);
	
	@Autowired
	private RootService rootService;

	public RootService getRootService() {
		return rootService;
	}

	public void setRootService(RootService rootService) {
		this.rootService = rootService;
	}

	@RequestMapping(value = "/cluster/{queryTerm}", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody ClusteredResponse clusteringViaQuery(@PathVariable("queryTerm") String queryTerm) {
		if(Strings.isNullOrEmpty(queryTerm)){
			LOGGER.error("empty queryTerm is not allowed.");
			return null;
		}
		return this.rootService.searchAndCluster(queryTerm);
	}

	@RequestMapping(value = "/sum", method = RequestMethod.POST, headers = { "Accept=application/json" })
	public 	@ResponseBody SumResponse summarize(@RequestBody SumRequest request) {
		if (!validateSumRequest(request))
			return null;
		request.setSize(7);
		String sum=this.rootService.getSummary(request);
		return new SumResponse(sum);
	}

	private Boolean validateSumRequest(SumRequest request){
		if(request.getDocIds()==null)
			return false;
		if(request.getDocIds().size()==0)
			return false;
		return true;
	}
}
