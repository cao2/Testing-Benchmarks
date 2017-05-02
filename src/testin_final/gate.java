package testin_final;

public class gate {
	String name;
	int control;
	int inversion;
	public gate(String nm){
		name=nm.toLowerCase();
		setControl();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getControl() {
		return control;
	}
	public void setControl(int control) {
		this.control = control;
	}
	public int getInversion() {
		return inversion;
	}
	public void setInversion(int inversion) {
		this.inversion = inversion;
	}
	public String toString(){
		return name+ " gate \n control value: "+control+"\n inversion "+inversion;
	}
	void setControl(){
		if (name.contains("and"))
			control=0;
		else if (name.contains("xor"))
			control=2;
		else if (name.contains("or"))
			control=1;
		else if (name.equals("not"))
			control=1;
		else
			control=3;
		
		if (name.contains("nand"))
			inversion=1;
		else if (name.contains("and"))
			inversion=0;
		else if (name.contains("n"))
			inversion=1;
		else
			inversion=0;
		
	}

}
