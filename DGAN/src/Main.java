import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
	private static ArrayList<Node> d= new ArrayList<Node>();
	private static ArrayList<Node> dNew= new ArrayList<Node>();
	private static int k=4;

	public static void main(String[] args) throws FileNotFoundException {

		Reader reader= new Reader();
		d= reader.read(d, args[0]);
		dNew= reader.read(dNew, args[0]);
		int index= k;
		long start= System.nanoTime();

		for(int i=0; i<d.size();i++) {
			if(i<index) {
				if(i==0)
					dNew.get(i).degree= dNew.get(0).degree;
				else
					dNew.get(i).degree= dNew.get(i-1).degree;
			}
			else {
				if(index+k+2<d.size()) {//mi assicuro che l'ultimo gruppo sia formato da k elementi
					int c_merge= cMerge(i, index);
					int c_new= I(index);
					
					if(c_merge>c_new) {
						index= i+k;  //+1 causa disuguaglianza if riga 19 per creare un nuovo gruppo
					}
					else {
						dNew.get(i).degree=dNew.get(i-1).degree; //aggiungo l'elemento k+1 esimo al gruppo
						index++;
					}
				}
				else {
					if(index+k==d.size()) //rimangono esattamente k elementi da processare -> creo un nuovo gruppo al prossimo giro
						index=d.size();
					else { //ho aggiunto un elemenento al gruppo quindi merge dei rimanenti nodi al gruppo precedente
						index=d.size();
						i--;
					}
				}
			}			
		}
		long end = System.nanoTime()-start;

/////////////////////*Degree_original*///////////////////

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
		pw1.flush();
		pw1.write(sb1.toString());
	    pw1.close();
	    
	    
/////////////////////*ConstructGraph*////////////////////
		
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
	        pw.close();
		}
	}

	public static int cMerge(int i, int index) {
		int cost= dNew.get(i-k).degree - dNew.get(i).degree;//i-k tanto hanno lo stesso grado ormai appartengono allo stesso gruppo sicuramente i-k elementi
		cost+= I(index+1);
		return cost;
	}

	private static int I(int min) {
		int i=0;
		for(int j=min;j<min+k;j++) {
			i+= dNew.get(min).degree-dNew.get(j).degree;
		}
		return i;
	}
	
	static class Sort implements Comparator<Node>{
		public int compare(Node node1, Node node2) {
            return node2.degree - node1.degree;
        }
	}
}
