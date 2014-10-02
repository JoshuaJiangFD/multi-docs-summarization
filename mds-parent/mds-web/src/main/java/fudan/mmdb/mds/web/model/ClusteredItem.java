package fudan.mmdb.mds.web.model;

import java.util.List;

public class ClusteredItem {

	private String label;
	
	private List<String> ids;
	
	private double score;
	
	private Boolean isOtherTopsic;

	public ClusteredItem(){
		
	}
	
	public ClusteredItem(String label, List<String> ids) {
		super();
		this.label = label;
		this.ids = ids;
	}
	
	//***** getters/setters**********
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	public Boolean getIsOtherTopsic() {
		return isOtherTopsic;
	}

	public void setIsOtherTopsic(Boolean isOtherTopsic) {
		this.isOtherTopsic = isOtherTopsic;
	}
}
