package testin_final;
public class Node {
	String in1,in2;
	String out1;
	gate func;
	String name;
	Node(String nm,String i1, String i2, String o,gate f){
		name=nm;
		in1=i1;
		in2=i2;
		out1=o;
		func=f;
	}
	public String toString(){
		return "Node "+name+"\n "+func.toString()+"\n input1: "+ in1+"\n input 2: "+in2+"\n output: "+out1;
	}
}
