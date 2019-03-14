import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
	private static ArrayList<Node> d= new ArrayList<Node>();
	private static ArrayList<Node> dNew= new ArrayList<Node>();
	private static int k=13;
	private static int T=0;

	public static void main(String[] args) throws FileNotFoundException {

        d= new ArrayList<Node>();
        dNew= new ArrayList<Node>();
		Reader reader= new Reader();
		d= reader.read(d, args[0]);
		dNew= reader.read(dNew, args[0]);
		long start= System.nanoTime();
		int s= 0;
		
		for(int i=0; i<d.size();i++) {
			int min=Integer.MAX_VALUE;
			int singleGroup= 0;
			int divideGroup= 0;	
			
			if(d.size()-s<2*k) {
				for(int j=s;j<d.size();j++)
					dNew.get(j).degree=dNew.get(s).degree;
				break;
			}
			else{
				for(int j=s;j<d.size();j++) {
					singleGroup= I(s,j);
					for(int t=s+k-1;t<=j-k;t++) {
						divideGroup= I(s,t) + I(t+1, j);
						if(divideGroup<min) {
							min=divideGroup;
							T=t;
						}
						if(singleGroup<divideGroup) {
							min= singleGroup;
							T=j;
						}
					}
				}
				for(int j=s;j<=T;j++) 
					dNew.get(j).degree=dNew.get(s).degree;
			
				i= T;
				s= T+1;
			}
		}
		
		long end = System.nanoTime()-start;
		
/////////////////////*Degree_original*/////////////////

		PrintWriter pw2 = new PrintWriter("Degree_original.csv");
		StringBuilder sb2 = new StringBuilder();

		for(int i=0;i<d.size();i++) {
			Node node= d.get(i);
			sb2.append(node.name);
			sb2.append(';');
			sb2.append(node.degree);
			sb2.append('\n');
		}
		pw2.write(sb2.toString());
		pw2.close();
		
/////////////////////*Degree_anonymized*/////////////////
		PrintWriter pw1 = new PrintWriter("Degree_anonymized.csv");
		StringBuilder sb1 = new StringBuilder();

		for(Node node: dNew) {
			sb1.append(node.name);
			sb1.append(';');
			sb1.append(node.degree);
			sb1.append('\n');
		}
		int grado=0;
		for(int g=0;g<dNew.size();g++) {
			grado+=dNew.get(g).degree - d.get(g).degree;
		}
		
		sb1.append('\n');
		System.out.println(grado);			
		sb1.append("Obj");
		sb1.append(';');
		sb1.append(grado);
		sb1.append('\n');
		sb1.append("CpuTime");
		sb1.append(';');
		sb1.append(end);

		pw1.write(sb1.toString());
		pw1.flush();
		pw1.close();
	    
	    
	    
		//////////////////////////////////////ConstructGraph		
		int totalSum=0;
		for(Node node: dNew) {
			totalSum= totalSum +  node.degree;	
		}
		if(totalSum % 2 != 0) {
			System.out.println("NO");
		}
		else {			
			while(totalSum>0) {
				Random random = new Random();
				int selNode= random.nextInt(dNew.size());
				Node node= dNew.get(selNode);
				
				if(node.getDegree()>0) {
					int currentDegree= node.getDegree();
					node.setDegree(0);
					Collections.sort(dNew, new Sort());
					for(int t=0;t<currentDegree;t++) {
						Node n= dNew.get(t);
						if(n.degree<currentDegree) {
							break;
						}
						else if(!n.name.equals(node.name)){
							node.edge.add(n.name);
							n.edge.add(node.name);
							n.setDegree(n.getDegree()-1);
						}	
					}
					totalSum=0;
					for(Node n: dNew) {
						totalSum += n.degree;
					}
				}
			}

			PrintWriter pw = new PrintWriter(k + "_graph.csv");
			StringBuilder sb = new StringBuilder();
			
			for(Node node: dNew) {
				sb.append(node.name);
				sb.append(';');
				for(String string: node.edge) {
					sb.append(string);
					sb.append(';');
				}
				sb.append('\n');
			}
			pw.write(sb.toString());
			pw.flush();
		    pw.close();
		}	
	}
	

	private static int I(int start, int end) {
		int i=0;
		for(int j=start;j<=end;j++) {  
			i+= dNew.get(start).degree-dNew.get(j).degree;
		}
		return i;
	}
	
	static class Sort implements Comparator<Node>{
		public int compare(Node node1, Node node2) {
            return node2.degree - node1.degree;
        }
	}
}

