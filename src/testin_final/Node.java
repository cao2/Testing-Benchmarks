package testin_final;
public class Node {
	String in1,in2;
	String out1;
	gate func;
	String name;
	
	Node(String nm,String i1, String i2, String o,gate f){
		name=nm.toLowerCase();
		in1=i1;
		in2=i2;
		out1=o;
		func=f;
		
	}
	
	public int calc(int in1, int in2){
		
		 if (in1<2 &&in2<2){
			 //System.out.println("both norma;");
			if (func.getName().equals("and"))
				return in1&in2;
			else if (func.getName().equals("or"))
				return in1|in2;
			else if (func.getName().equals("nor"))
				return 1-(in1|in2);
			else if (func.getName().equals("nand"))
				return 1-(in1&in2);
			else if (func.getName().equals("xor"))
				return in1^in2;
			else if (func.getName().equals("xnor"))
				return 1-(in1^in2);
			else if (func.getName().equals("not"))
				return 1-(in1);
		}
		else if (in1==func.getControl()||in2==func.getControl()){
			//System.out.println("has control value "+func.getInversion());
			if (func.getInversion()==1)
				return 1-func.getControl();
			else
				return func.getControl();
			}
		//now non of the input has control values
		//if both have faulty values
		else if (in1==3 && in2==3)
			return 3;
		else if (in1>3 && in2>3){
			if (in1==in2)
				if (func.getInversion()==1)
					return in1;
				else
					return 9-in1;
			else{
				if (func.getName().equals("and"))
					return 0;
				else if (func.getName().equals("or"))
					return 1;
				else if (func.getName().equals("nor"))
					return 0;
				else if (func.getName().equals("nand"))
					return 1;
				else if (func.getName().equals("xor"))
					return 0;
				else if (func.getName().equals("xnor"))
					return 1;
				else if (func.getName().equals("not"))
					return 9-in1;
			}
		}
		else if(in1>2 && in2>2)
			return 3;
		else if(in1>3){
			if (func.getInversion()==1)
				return 9-in1;
			else
				return in1;}
		else if(in2>3){
				if (func.getInversion()==1)
					return 9-in2;
				else
					return in2;
		}
			
		return 3;
	}
	
	public String getIn1() {
		return in1;
	}


	public void setIn1(String in1) {
		this.in1 = in1;
	}


	public String getIn2() {
		return in2;
	}


	public void setIn2(String in2) {
		this.in2 = in2;
	}


	public String getOut1() {
		return out1;
	}


	public void setOut1(String out1) {
		this.out1 = out1;
	}


	public gate getFunc() {
		return func;
	}


	public void setFunc(gate func) {
		this.func = func;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}



	public String toString(){
	//gate includes and, or, nand, xor,not,nor
		return "Node "+name+"\n "+func.toString()+"\n input1: "+ in1+"\n input 2: "+in2+"\n output: "+out1;
	}
}
