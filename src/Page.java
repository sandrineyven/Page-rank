import java.util.ArrayList;

public class Page {
	
	private String name;

	private double pageRank;
	
	private double dumpingFactor;
	
	private ArrayList<Page> outBoundLinks = new ArrayList<>();
	private ArrayList<Page> inBoundLinks = new ArrayList<>();
	
	public Page(String name){
		this.name = name;
		this.pageRank = 1;
		this.dumpingFactor = 0.85;
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

	public double getDumpingFactor() {
		return dumpingFactor;
	}

	public void setDumpingFactor(double dumpingFactor) {
		this.dumpingFactor = dumpingFactor;
	}

	public ArrayList<Page> getOutBoundLinks() {
		return outBoundLinks;
	}

	public void setOutBoundLinks(ArrayList<Page> outBoundLinks) {
		this.outBoundLinks = outBoundLinks;
	}

	public ArrayList<Page> getInBoundLinks() {
		return inBoundLinks;
	}

	public void setInBoundLinks(ArrayList<Page> inBoundLinks) {
		this.inBoundLinks = inBoundLinks;
	}
	
	public void addInBoundLinks(Page page){
		this.getInBoundLinks().add(page);
	}
	
	public void addOutBoundLinks(Page page){
		this.getOutBoundLinks().add(page);
	}
	
	
	
}
