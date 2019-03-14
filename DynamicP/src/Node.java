import java.util.ArrayList;

public class Node {
	String name;
	int degree;
	ArrayList<String> edge;
	
	public Node() {
		this.name= "";
		this.degree= 0;
		this.edge= new ArrayList<String>();
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the degree
	 */
	public int getDegree() {
		return degree;
	}
	/**
	 * @param degree the degree to set
	 */
	public void setDegree(int degree) {
		this.degree = degree;
	}
}
