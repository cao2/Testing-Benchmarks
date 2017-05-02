package testin_final;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
public class Circuit {
	static int d_value=4;
	static int dp_value=5;
	static int x_value=3;
	boolean print_state=false;
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
	 //	/Users/cao2/Desktop/Testing Benchmarks/t4_3.ckt
	////				/Users/cao2/Desktop/Testing Benchmarks/t5_26a.ckt
	public void input() throws FileNotFoundException, IOException{
		com.clear();
		pin.clear();
		pout.clear();
		wires.clear();
		wires_value.clear();
		faults.clear();
		System.out.println("Input Name: ");
		Scanner sc=new Scanner(System.in);
		String filename=sc.nextLine();
		String line;
		int linenum=0;
		try(BufferedReader br=new BufferedReader(new FileReader(filename))){
			line=br.readLine();
			while(line!=null){
				Scanner name=new Scanner(line);
				if (name.hasNext()){
					String in=name.next();
					if ( in.charAt(0)!='$'){
						//System.out.println(line);
						if (line.toLowerCase().contains("input")){
							pin.add(in);
						}
						else if (line.toLowerCase().contains("output")){
							pout.add(in);
						}
						else{
								String out=in;
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
								String in2="nada";
								if (name.hasNext())
									in2=name.next();
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
				
				
				
				}
				line=br.readLine();
				linenum++;
			}
			if (print_state){
				System.out.println("output");
			for (String x:pout)
				System.out.println(x);}
		} catch (IOException e) {
		    System.err.println("Caught IOException: " + e.getMessage());
		} 
//		for (String x: pout)
//			System.out.println(x);
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
			if (!n.getFunc().getName().equals("not")){
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
			else{
				
				ifault.add(n.getIn1()+"_0");
				ifault.add(n.getOut1()+"_0");
				for (ArrayList<String> x: faults){
					if (x.contains(ifault.get(0))||x.contains(ifault.get(1))){
						nw=false;
						if (!x.contains(ifault.get(0)))
							x.add(ifault.get(0));
						if (!x.contains(ifault.get(1)))
							x.add(ifault.get(1));
					}
				}
				if (nw==true){
					
					faults.add(ifault);}
				
				nw=true;
				
				
				ArrayList<String> ifaults=new ArrayList<String> ();
				ifaults.add(n.getIn1()+"_1");
				ifaults.add(n.getOut1()+"_1");
				for (ArrayList<String> x: faults){
					if (x.contains(ifaults.get(0))||x.contains(ifaults.get(1))){
						nw=false;
						if (!x.contains(ifaults.get(0)))
							x.add(ifaults.get(0));
						if (!x.contains(ifaults.get(1)))
							x.add(ifaults.get(1));
					}
				}
				if (nw==true)
					faults.add(ifaults);
				
			}
			
			
//			if (print_state){
//				System.out.println("****");
//				list_fault_class();}
		}
		boolean one=true;
		boolean zero=true;
		
		for (String wire:wires){
			if (!wire.equals("nada")){
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
			//x=faults.get(0);
			ArrayList<Integer> values=new ArrayList<Integer> (wires_value);
			System.out.print("Tests sets to detect ");
			for (String y: x)
				System.out.print(y+" ");
			System.out.println(": ");
			
			falt_gate=x.get(0).substring(0, x.get(0).length()-2);
			if (x.get(0).contains("_0"))
				fault_value=d_value;
			else
				fault_value=dp_value;
			//System.out.println(fault_value);
			//find that wire
			
			values.set(wires.indexOf(falt_gate), fault_value);
				
			boolean finished=Dalgo(values,D,J);
			if (!finished){
				System.out.println("can't find solution");
			}
			//break;
			
		}
	}
	//values of wires her
	// 3 -> x
	//4 -> d
	//5->d'
	public boolean Dalgo(ArrayList<Integer> wi_value, ArrayList<Node> Dd, ArrayList<Node> Jj){
		ArrayList<Node> D=new ArrayList<Node> ();
		ArrayList<Node> J=new ArrayList<Node> ();
		if (print_state)
		System.out.println("==================================================");
		ArrayList<Integer> wires_value=new ArrayList<Integer> (wi_value);
		//imply and check
		boolean changed=true;
		while (changed==true){
			changed=false;
			if (print_state)
			System.out.println("**********************");
			for (Node node: com){
				
				String in1=node.getIn1();
				String in2=node.getIn2();
				int in1_v=wires_value.get(wires.indexOf(in1));
				
				int in2_v=3;
				if (!in2.equals("nada"))
					in2_v=wires_value.get(wires.indexOf(in2));
				int out_v=wires_value.get(wires.indexOf(node.getOut1()));
				int new_out=node.calc(in1_v, in2_v);
				//System.out.println(node.getFunc().getName()+" "+in1_v+" "+in2_v+":"+new_out);
				if (print_state)
				System.out.println(node.getName()+", "+node.getFunc().getName());
				if (print_state)
				System.out.println(node.getIn1()+" : "+in1_v+" "+node.getIn2()+" : "+in2_v+" "+node.getOut1()+": "+out_v+"/"+new_out);
				if (out_v!=new_out && out_v==x_value){
					wires_value.set(wires.indexOf(node.getOut1()),new_out);
					changed =true;
				}
				else if(out_v!=new_out&&new_out==x_value)
					changed=changed;
				else if((out_v==4&&new_out==1)||(out_v==5&&new_out==0))
					changed=changed;
				else if(out_v!=new_out){
					return false;
				}
				
			}
		}
		//add D and J
		for (Node node:com){
			int in1_v=wires_value.get(wires.indexOf(node.getIn1()));
			int in2_v=-1;
			if (!node.getIn2().equals("nada"))
				in2_v=wires_value.get(wires.indexOf(node.getIn2()));
			int out_v=wires_value.get(wires.indexOf(node.getOut1()));
			if (out_v==x_value&&(in1_v>3 || in2_v>3)){
				if (!D.contains(node))
					D.add(node);}
			else if (out_v!=x_value && (in1_v==3||in2_v==3)){
				if (!J.contains(node))
					J.add(node);
				}
		}
		if (print_state)
		System.out.println("D list");
		if (print_state)
		for(Node d: D)
			System.out.println(d.getName());
		if (print_state)
		System.out.println("J list");
		if (print_state)
		for(Node d: J)
			System.out.println(d.getName());
		boolean errorPO=false;
		for (String out: pout){
			if (wires_value.get(wires.indexOf(out))>3)
				errorPO=true;
		}
		if (!errorPO){
			if (D.size()<=0)
				return false;
			else{
				if (print_state)
				System.out.println("not at pout");
				while(D.size()>0){
					Node nd=D.get(0);
					ArrayList<Integer> wv=new ArrayList<Integer> (wires_value);
					if (print_state){
						System.out.println("_______________________________");
						System.out.println("for D, change "+nd.getName());
						System.out.println("_______________________________");
						}
					if (wv.get(wires.indexOf(nd.getIn1()))==x_value)
						wv.set(wires.indexOf(nd.getIn1()),1-nd.func.getControl());
					if (wv.get(wires.indexOf(nd.getIn2()))==x_value)
						wv.set(wires.indexOf(nd.getIn2()),1-nd.func.getControl());
					D.remove(0);
					if (Dalgo(wv,D,J))	{
						//wi_value=wires_value;
						return true;
						}
					if (print_state)
					System.out.println(nd.getName()+"failled ((((((((((returned false from dalgo)))))))))) 1");
					
//					ArrayList<Integer> wv1=new ArrayList<Integer> (wires_value);
//					if (print_state)
//						System.out.println("for D, change "+nd.getName());
//					if (wv.get(wires.indexOf(nd.getIn1()))==x_value)
//						wv.set(wires.indexOf(nd.getIn1()),wv.get( wires.indexOf(nd.getIn2()) ));
//					if (wv.get(wires.indexOf(nd.getIn2()))==x_value)
//						wv.set(wires.indexOf(nd.getIn2()),wv.get( wires.indexOf(nd.getIn1()) ));
//					
//					if (Dalgo(wv,D,J))	{
//						//wi_value=wires_value;
//						return true;
//						}
//					if (print_state)
//					System.out.println("((((((((((returned false from dalgo)))))))))) 2");
				}
				return false;
			}
		}
		//this is the justification part
		else{
			if (print_state){
			System.out.println("==================================================");
			System.out.println("propagated to output");
			System.out.println("==================================================");}
			if (J.size()==0){
				System.out.print("(");
				for (String in: pin){
					//System.out.print(in+":");
					int value=wires_value.get(wires.indexOf(in));
					if (value==4)
						value=1;
					else if (value==5)
						value=0;
					System.out.print(value+" ");
				}
				System.out.println(")");
				return true;
				}
			else{
				Node nd=J.get(0);
				J.remove(0);
				if (print_state)
					System.out.println("for J, change "+nd.getName());
				
				if (wires_value.get(wires.indexOf(nd.getIn1()))==x_value){
					if (print_state)
						System.out.println("for J, change n1 control "+nd.getName());
					wires_value.set(wires.indexOf(nd.getIn1()),nd.func.getControl());
					if (Dalgo(wires_value,D,J)){
						//wi_value=wires_value;
						return true;
					}
					else{
						if (print_state)
							System.out.println("for J, change n1 control'''' "+nd.getName());
						wires_value.set(wires.indexOf(nd.getIn1()),1-nd.func.getControl());
						if (Dalgo(wires_value,D,J)){
							//wi_value=wires_value;
							return true;
						}
					}
				}
				if (wires_value.get(wires.indexOf(nd.getIn2()))==x_value){
					if (print_state)
						System.out.println("for J, change n2 control "+nd.getName());
					wires_value.set(wires.indexOf(nd.getIn2()),nd.func.getControl());
					if (Dalgo(wires_value,D,J)){
						//wi_value=wires_value;
						return true;
					}
					else{
						if (print_state)
							System.out.println("for J, change n2 control''''' "+nd.getName());
						wires_value.set(wires.indexOf(nd.getIn2()),1-nd.func.getControl());
						if (Dalgo(wires_value,D,J)){
							//wi_value=wires_value;
							return true;
						}
					}
				}
				return false;
			}
		}
		//return false;
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
