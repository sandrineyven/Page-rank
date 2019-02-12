import java.util.List;

public class Spell {

	private String name;
	
	private String classe;
	
	private String level;
	
	private List<String> components;
	
	private boolean resistance;
	
	public Spell(){
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getClasse() {
		return classe;
	}


	public void setClasse(String classe) {
		this.classe = classe;
	}


	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}


	public List<String> getComponents() {
		return components;
	}


	public void setComponents(List<String> components) {
		this.components = components;
	}


	public boolean isResistance() {
		return resistance;
	}


	public void setResistance(boolean resistance) {
		this.resistance = resistance;
	}

}
