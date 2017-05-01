package testin_final;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
public class Circuit {
	static int d_value=4;
	static int dp_value=5;
	static int x_value=3;
	ArrayList<Node> com=new ArrayList<Node> ();
	ArrayList<String> pin=new ArrayList<String> ();
	ArrayList<String> pout=new ArrayList<String> ();
	ArrayList<String> wires=new ArrayList<String> ();
	ArrayList<Integer> wires_value=new ArrayList<Integer> ();
	ArrayList< ArrayList<String> > faults= new ArrayList< ArrayList<String> >  ();
	public void menu(){
		System.out.println("Please Selection one of the following options");
		System.out.println("[0] Read the input net-list");
		System.out.println("[1] Perform Fault Collapsing");
		System.out.println("[2] List fault classes");
		System.out.println("[3] Generate tests (D-Algorithm)");
		System.out.println("[4] Generate tests (PODEM)");
		System.out.println("[5] Exit");				
	}
	
	///Users/cao2/Desktop/Testing Benchmarks/t5_10.ckt
	 
	public void input() throws FileNotFoundException, IOException{
		System.out.println("Input Name: ");
		Scanner sc=new Scanner(System.in);
		String filename=sc.nextLine();
		String line;
		int linenum=0;
		try(BufferedReader br=new BufferedReader(new FileReader(filename))){
			line=br.readLine();
			while(line!=null){
				Scanner name=new Scanner(line);
				if (name.hasNext() && line.charAt(0)!='$'){
					//System.out.println(line);
					if (line.toLowerCase().contains("input")){
						String in=name.next();
						pin.add(in);
					}
					else if (line.toLowerCase().contains("output")){
						String in=name.next();
						pout.add(in);
					}
					else{
							String out=name.next();
							if (wires.contains(out)==false){
								wires.add(out);
								wires_value.add(x_value);
							}	
							String func=name.next();
							String in1=name.next();
							if (wires.contains(in1)==false){
								wires.add(in1);
								wires_value.add(x_value);
							}
							String in2=name.next();
							if (wires.contains(in2)==false){
								wires.add(in2);
								wires_value.add(x_value);
							}
							gate x=new gate(func);
							String nm=new String("node_"+linenum);
							Node nd=new Node(nm,in1,in2,out,x);
							com.add(nd);
					}
				
				
				
				
				}
				line=br.readLine();
				linenum++;
			}
			
		} catch (IOException e) {
		    System.err.println("Caught IOException: " + e.getMessage());
		} 
//		for (Node x: com)
//			System.out.println(x.toString());
	}
	public void fault_collapse(){
		//for each component of the soc, conduct fault collapsing
		int control;
		int inversion;
		boolean nw;
		for (Node n:com){
			control=n.getFunc().getControl();
			inversion=n.getFunc().getInversion();
			inversion=inversion^control;
			nw=true;
			ArrayList<String> ifault=new ArrayList<String> ();
			ifault.add(n.getIn1()+"_"+control);
			ifault.add(n.getIn2()+"_"+control);
			ifault.add(n.getOut1()+"_"+inversion);
			for (ArrayList<String> x: faults){
				if (x.contains(ifault.get(0))||x.contains(ifault.get(1))||x.contains(ifault.get(2))){
					nw=false;
					if (!x.contains(ifault.get(0)))
						x.add(ifault.get(0));
					if (!x.contains(ifault.get(1)))
						x.add(ifault.get(1));
					if (!x.contains(ifault.get(2)))
						x.add(ifault.get(2));
				}
			}
			if (nw==true)
				faults.add(ifault);
		}
		boolean one=true;
		boolean zero=true;
		
		for (String wire:wires){
			//System.out.println(wire);
			one=true;
			zero=true;
			for (ArrayList<String> x:faults){
				if (x.contains(wire+"_1"))
					one=false;
				if (x.contains(wire+"_0"))
					zero=false;
			}
			if (one==true){
				ArrayList<String> tmp=new ArrayList<String> ();
				tmp.add(wire+"_1");
				faults.add(tmp);
				//System.out.println("1");
			}
			if (zero==true){
				ArrayList<String> tmp=new ArrayList<String> ();
				tmp.add(wire+"_0");
				faults.add(tmp);
				//System.out.println("0");
			}
			
				
		}
	}
	public void list_fault_class(){
		for (ArrayList<String> one : faults){
			for (String two: one)
				System.out.print(two+" ");
			System.out.println(" ");
		}
	}
	public void dalgo(){
		String falt_gate;
		int fault_value;
		
		//one input is faulty, output unknown
		ArrayList<Node> D=new ArrayList<Node> ();
		//output known, some input unkown
		ArrayList<Node> J=new ArrayList<Node> ();
		
		for (ArrayList<String> x: faults){
			ArrayList<Integer> values=new ArrayList<Integer> (wires_value);
			System.out.print("Tests sets to detect ");
			for (String y: x)
				System.out.print(y+" ");
			//System.out.println(": ");
			
			falt_gate=x.get(0).substring(0, x.get(0).length()-2);
			System.out.println(falt_gate);
			if (x.get(0).contains("0"))
				fault_value=d_value;
			else
				fault_value=dp_value;
			//System.out.println(fault_value);
			//find that wire
			
			values.set(wires.indexOf(falt_gate), d_value);
				
			boolean finished=Dalgo(wires_value,D,J);
			
			
		}
	}
	//values of wires her
	// 3 -> x
	//4 -> d
	//5->d'
	public boolean Dalgo(ArrayList<Integer> wi_value, ArrayList<Node> D, ArrayList<Node> J){
		ArrayList<Integer> wires_value=new ArrayList<Integer> (wi_value);
		System.out.println("enter Dalgo");
		//imply and check
		boolean changed=true;
		while (changed==true){
			changed=false;
			for (Node node: com){
				String in1=node.getIn1();
				String in2=node.getIn2();
				int in1_v=wires_value.get(wires.indexOf(in1));
				int in2_v=wires_value.get(wires.indexOf(in2));
				int out_v=wires_value.get(wires.indexOf(node.getOut1()));
				int new_out=node.calc(in1_v, in2_v);
				System.out.println("in1 : "+in1_v+"   in2: "+in2_v+"  output: "+new_out);
				if (out_v==x_value){
					wires_value.set(wires.indexOf(node.getOut1()),new_out);
					changed =true;
				}
				else if(out_v!=new_out)
					return false;
			}
		}
		//add D and J
		for (Node node:com){
			int in1_v=wires_value.get(wires.indexOf(node.getIn1()));
			int in2_v=wires_value.get(wires.indexOf(node.getIn2()));
			int out_v=wires_value.get(wires.indexOf(node.getOut1()));
			if (out_v==x_value&&(in1_v>3 || in2_v>3))
				D.add(node);
			else if (out_v<2 && (in1_v==3||in2_v==3))
				J.add(node);
		}
		
		boolean errorPO=false;
		for (String out: pout){
			if (wires_value.get(wires.indexOf(out))>3)
				errorPO=true;
		}
		
		if (!errorPO){
			if (D.size()<=0)
				return false;
			else{
				while(D.size()>0){
					Node nd=D.get(0);
					if (wires_value.get(wires.indexOf(nd.getIn1()))==x_value)
						wires_value.set(wires.indexOf(nd.getIn1()),1-nd.func.getControl());
					if (wires_value.get(wires.indexOf(nd.getIn2()))==x_value)
						wires_value.set(wires.indexOf(nd.getIn2()),1-nd.func.getControl());
					D.remove(0);
					if (Dalgo(wires_value,D,J))	{
						wi_value=wires_value;
						return true;
						}
				}
				return false;
			}
		}
		//this is the justification part
		else{
			if (J.size()==0)
				return true;
			else{
				Node nd=J.get(0);
				J.remove(0);
				if (wires_value.get(wires.indexOf(nd.getIn1()))==x_value){
					wires_value.set(wires.indexOf(nd.getIn1()),nd.func.getControl());
					if (Dalgo(wires_value,D,J)){
						wi_value=wires_value;
						return true;
					}
					else{
						wires_value.set(wires.indexOf(nd.getIn1()),1-nd.func.getControl());
						if (Dalgo(wires_value,D,J)){
							wi_value=wires_value;
							return true;
						}
					}
				}
				if (wires_value.get(wires.indexOf(nd.getIn2()))==x_value){
					wires_value.set(wires.indexOf(nd.getIn2()),nd.func.getControl());
					if (Dalgo(wires_value,D,J)){
						wi_value=wires_value;
						return true;
					}
					else{
						wires_value.set(wires.indexOf(nd.getIn2()),1-nd.func.getControl());
						if (Dalgo(wires_value,D,J)){
							wi_value=wires_value;
							return true;
						}
					}
				}
				return false;
			}
		}
	}
	public void podem(){
		
	}
	Circuit() throws FileNotFoundException, IOException{
		int i=0;
		while(i!=5){
			menu();
			Scanner sc = new Scanner(System.in);
			i = sc.nextInt();
			switch(i)
			{
				case 0: 
					input();
					break;
				case 1:
					fault_collapse();
					break;
				case 2:
					list_fault_class();
					break;
				case 3:
					dalgo();
					break;
				case 4:
					podem();
					break;
				case 5:
					System.out.println("Thank you, Good Bye.");
					break;
				default:
					System.out.println("unknown selection, please insert another value");
					break;
		}
	}
}
}
