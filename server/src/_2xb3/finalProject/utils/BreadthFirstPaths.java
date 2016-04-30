//DISCLAIMER: I had used Algorithms 4ed by Robert Sedgewick as REFERENCE (not copying) for BFS algorithm
package _2xb3.finalProject.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

import _2xb3.finalProject.model.GrantGraph;


//BFS graph path
public class BreadthFirstPaths {
	//using hashmaps as ADT is not index
	private HashMap<String,Boolean> marked;
	private HashMap<String,String> edgeTo;
	private final String s;
	private final GrantGraph g;
	
	//init vars
	public BreadthFirstPaths(GrantGraph g,String s){
		marked = new HashMap<String,Boolean>();
		edgeTo = new HashMap<String,String>();
		this.s = s;
		this.g= g;
		bfs(s);
	}
	
	/*BFS algorithm that uses a min PQ to make shortest path
	 * Checks adds all nodes connected to PQ if not visited
	 * Goes to next node in PQ and repeats until all nodes visited. 
	 */
	private void bfs(String a){
		LinkedList<String> queue = new LinkedList<String>();
		marked.put(a, true);
		queue.add(a);
		while(!queue.isEmpty()){
			String v = queue.removeFirst();
			for(String e: g.getConn(v).keySet()){
				String b = e;
				if(!(marked.containsKey(b))){
					marked.put(b, false);
				}
				if (!marked.get(b)){
					edgeTo.put(b, v);
					marked.put(b, true);
					queue.add(b);
				}
			}
	
		}
		
	}
	
	//checks if there is a path connected the original node to new node
	public boolean hasPathTo(String v){
		return marked.get(v);
	}
	
	//creates a stack of cities in the path from original node to v
	public Stack<String> pathTo(String v){
		if(!hasPathTo(v)){
			return null;
		}
		Stack<String> path = new Stack<String>();
		for(String x= v;x!=s;x= edgeTo.get(x)){
			path.push(x);
		}
		path.push(s);
		return path;
	}
}
