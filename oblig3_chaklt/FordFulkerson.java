import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * FordFulkerson.java
 * 
 * INF4130
 * Mandatory Assignment 3, 2016
 * Exercise 1
 * 
 * @author chaklt
 */
public class FordFulkerson {

	private final int M;  //Dimensionality of the matrix.
	private int residualCapacity[][];  //Matrix that contains the residual capacity as any given time.
	private int flowGraph[][];  //Matrix that contains the flow.
	private int source; //Source vertex is always 0.
	private int sink; //Sink is always the number of total vertices-1.
	private int foundPathTimes = 0;  //Number of times you constructed a new N(f) and found a path through it from the source to the sink.
	List<Integer> VerticesCut = new ArrayList<Integer>();  //An array list with the numbers of the vertices on the source side of a cut.
	Map<Integer,Integer> parentMap = new HashMap<>();  //Parent map for vertices. 
	private OutputResults solutions;  //Object that holds the field we want to write to file.


	/**
	 * Constructor for the initial state of the graph/matrix.
	 * 
	 * @param graphCapacity - initial state of the matrix.
	 * @param source - number of the source.
	 * @param sink - number of the sink.
	 */
	public FordFulkerson(int[][] graphCapacity, int source, int sink) {
		M = graphCapacity[0].length;
		residualCapacity = new int[M][M];  //a new matrix that contains the residual capacity of the network.

		//Copy initial state of matrix.
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < M; j++) {
				residualCapacity[i][j] = graphCapacity[i][j];
			}
		}

		this.source = source;
		this.sink = sink;
		flowGraph = new int[M][M];  //Matrix with the flow(s) of the network.
	}


	/**
	 * 
	 */
	public void edmondAndKarp() {

		int maxFlow = 0;  //Max flow.

		while(runBFS()) {  //Run as long as an augmented path from source to sink exists.
			int si = sink;
			int pathFlow = Integer.MAX_VALUE;  //Auxiliary variable to find the smallest path flow.
			foundPathTimes++;

			//Traverse backwards starting from sink, run as long as source is not reached.
			while(si != source) {
				int vertexParent = parentMap.get(si);  //Get the parent vertex of the sink vertex.

				//Find the path with less flow.
				if(pathFlow > residualCapacity[vertexParent][si]) {
					pathFlow = residualCapacity[vertexParent][si];
				}
				si = vertexParent;
			}

			maxFlow +=pathFlow;  //Optimal flow.
			si = sink;

			//Update two matrixes.The matrix containing the residual capacity, and the matrix containing the flow.
			while(si != source) {
				int vertexParent = parentMap.get(si);

				residualCapacity[vertexParent][si] -= pathFlow;  //Subtract residual capacity.
				residualCapacity[si][vertexParent] += pathFlow;  //Add residual capacity on opposite direction.

				flowGraph[vertexParent][si] += pathFlow;  //Add flow to flow matrix.

				si = vertexParent;
			}
		}
		//Instantiate an object with the information we want to write to file.
		solutions = new OutputResults(maxFlow, flowGraph, findCut(), foundPathTimes);
	}



	/**
	 *  Do a BFS search.
	 * @return true/false - if an augmented path exists from source to sink.
	 */
	public boolean runBFS() {

		Set<Integer> visitedSet = new HashSet<>();  //Hashset with all visited vertices.
		Queue<Integer> queue = new LinkedList<Integer>();  //FIFO queue.
		queue.add(source);
		visitedSet.add(source);

		while(!queue.isEmpty()){ //Run as long as the queue is not empty.

			int currentVertex = queue.poll();

			for(int ve=0; ve<M; ve++) {  //Run if: the vertex is not visited and the residual capacity is greater than 0.
				
				if(!visitedSet.contains(ve) && residualCapacity[currentVertex][ve]>0) {
					queue.add(ve);  //add vertex to queue
					visitedSet.add(ve);  //add vertex to visited list
					parentMap.put(ve, currentVertex);  //Add where this vertex is pointed from.
				}	
			}

			if(visitedSet.contains(sink)) {  //We have reached the sink.
				return true;
			}
		}
		//No augmented path to sink found.
		return false;
	}


	/**
	 * Find all vertices on the source side of a cut.
	 * Execute a BFS search from source vertex, only following edges in which some capacity for flow is remaining, aka the non-saturated edges.
	 * @return - a list with the numbers of the vertices on the source side of a cut.
	 */
	public List<Integer> findCut() {

		Queue<Integer> queueCut = new LinkedList<Integer>();
		VerticesCut.add(source);
		queueCut.add(source);

		while(!queueCut.isEmpty()){
			int currentVertex = queueCut.poll();

			for(int ve=0; ve<M; ve++) {
				if(residualCapacity[currentVertex][ve]>0 && !VerticesCut.contains(ve)) {
					VerticesCut.add(ve);
					queueCut.add(ve);
				}
			}
		}
		Collections.sort(VerticesCut);
		return VerticesCut;
	}


	/**
	 * Inner Class that contains the fields to be written to output file.
	 * Information required as described in the assignment.
	 */
	public class OutputResults {
		private int optimalFlow; //Value of the optimal flow.
		private int[][] flowGraphMatrix; //A matrix where the flow is written.
		private List<Integer> sVertices; //Numbers of the vertices on the source side of a cut with the capacity of the flow, in sorted order.
		private int newConstructed; //Number of times a new N(f) is constructed and found a path through it from the source to the sink.

		public OutputResults (int optimalFlow, int[][] flowGraphMatrix, List<Integer> sVertices, int newStates) {
			this.optimalFlow = optimalFlow;
			this.flowGraphMatrix = flowGraphMatrix;
			this.sVertices = sVertices;  //
			this.newConstructed = newStates;	//+1 = includes the start state.
		}

		public int getOptimalFlow() {
			return optimalFlow;
		}

		public int[][] getFlowGraphMatrix() {
			return flowGraphMatrix;
		}

		public List<Integer> getsVertices(){
			return sVertices;
		}

		public int getNewConstructed() {
			return newConstructed;
		}
	}


	/**
	 * Get the solutions instance.
	 * @return instance of OutputResults.
	 */
	public OutputResults getSolutions(){
		return solutions;
	}
}
