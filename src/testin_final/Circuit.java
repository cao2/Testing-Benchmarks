package testin_final;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
public class Circuit {
	ArrayList<Node> com=new ArrayList<Node> ();
	ArrayList<String> pin=new ArrayList<String> ();
	ArrayList<String> pout=new ArrayList<String> ();
	ArrayList<String> wires=new ArrayList<String> ();
	public void menu(){
		System.out.println("Please Selection one of the following options");
		System.out.println("[0] Read the input net-list");
		System.out.println("[1] Perform Fault Collapsing");
		System.out.println("[2] List fault classes");
		System.out.println("[3] Generate tests (D-Algorithm)");
		System.out.println("[4] Generate tests (PODEM)");
		System.out.println("[5] Exit");				
	}
	
	 
	public void input() throws FileNotFoundException, IOException{
		System.out.println("Input Name: ");
		Scanner sc=new Scanner(System.in);
		String filename=sc.nextLine();
		String line;
		int linenum=0;
		try(BufferedReader br=new BufferedReader(new FileReader(filename))){
			line=br.readLine();
			while(line!=null ){
				if (line.charAt(0)!='$'){
					System.out.println(line);
					if (line.toLowerCase().contains("input")){
						Scanner name=new Scanner(line);
						String in=name.next();
						pin.add(in);
					}
					else if (line.toLowerCase().contains("output")){
						Scanner name=new Scanner(line);
						String in=name.next();
						pout.add(in);
					}
					else{
						Scanner name=new Scanner(line);
						String out=name.next();
						if (wires.contains(out)==false)
							wires.add(out);
						String func=name.next();
						String in1=name.next();
						if (wires.contains(in1)==false)
							wires.add(in1);
						String in2=name.next();
						if (wires.contains(in2)==false)
							wires.add(in2);
						gate x=new gate(func);
						String nm=new String("node_"+linenum);
						Node nd=new Node(nm,in1,in2,out,x);
						com.add(nd);
					}
				
				
				
				
				}
				line=br.readLine();
				linenum++;
			}
			
		}
		for (Node x: com)
			System.out.println(x.toString());
	}
	public void fault_collapse(){
		
	}
	public void list_fault_class(){
		
	}
	public void dalgo(){
		
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
