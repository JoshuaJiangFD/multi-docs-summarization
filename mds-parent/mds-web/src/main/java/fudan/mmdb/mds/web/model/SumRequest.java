package fudan.mmdb.mds.web.model;

import java.util.List;

public class SumRequest {

	private int size;
	
	private double ratio;
	
	private List<String> docIds;
	
	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public List<String> getDocIds() {
		return docIds;
	}

	public void setDocIds(List<String> docIds) {
		this.docIds = docIds;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
}
