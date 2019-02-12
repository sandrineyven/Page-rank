import java.util.ArrayList;

public class Page {
	
	private String name;

	private double pageRank;
	
	private ArrayList<Page> outBoundLinks = new ArrayList<>();
	
	public Page(String name){
		this.name = name;
		this.pageRank = 1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPageRank() {
		return pageRank;
	}

	public void setPageRank(double pageRank) {
		this.pageRank = pageRank;
	}

	public ArrayList<Page> getOutBoundLinks() {
		return outBoundLinks;
	}

	public void setOutBoundLinks(ArrayList<Page> outBoundLinks) {
		this.outBoundLinks = outBoundLinks;
	}

}
