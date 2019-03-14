import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Reader {
	
	public ArrayList<Node> read(ArrayList<Node> d, String args) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(args));
        scanner.useDelimiter(",");
        while(scanner.hasNextLine()){
        	int deg= 0;
        	Node node= new Node();
        	String line= scanner.nextLine();
        	String[] fields= line.split(",");
        	
        	for(int i=0; i<fields.length;i++) {
        		if(i==0)
        			node.setName(fields[0]);
        		else {
	        		Node n= new Node();
	        		n.setName(fields[i]);
	        		deg++;
        		}
        	}
        	
        	node.setDegree(deg);
			d.add(node);
        } 
        
		Collections.sort(d, new Sort());

        scanner.close();
		return d;
	}
	
	class Sort implements Comparator<Node>{
	    public int compare(Node a, Node b){
	        return b.degree - a.degree;
	    }
	}
}
