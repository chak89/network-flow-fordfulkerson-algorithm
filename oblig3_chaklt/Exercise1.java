import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Exercise1.java
 * 
 * INF4130
 * Mandatory Assignment 3, 2016
 * Exercise 1
 * 
 * @author chaklt
 */
public class Exercise1 {

	/**
	 * Main program
	 * Takes two arguments as inputs.
	 * 
	 * @param args[0]=input file name , args[1]=output file name
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static void main (String [] args) throws NumberFormatException, IOException {

		// Make sure that exactly two arguments are provided.
		if (args.length <2 || args.length > 2 ) {
			System.out.println("Invalid number of arguments, please input excatly 2 arguments");
			System.out.println("Usage: Application inputFileName OutPutFilename");
			System.exit(0);
		}

		//Instantiate class to reads text from a character-input stream designated from file args[0].
		BufferedReader br = new BufferedReader(new FileReader(args[0])); 

		boolean firstRun = true;  
		int M = 0;  // Number of vertices.
		int source = 0;  // 
		int sink = 0;  //
		int[][] graphCapacity =null;  //Initial state of the graph/matrix.
		int lineX = 0;  //Represents which row of the matrix.
		String sCurrentLine = null;  //Current line of the input file.

		
		// Reads one line per iteration as long as a new line exists.
		while ((sCurrentLine = br.readLine()) != null) {

			String[] splitted = sCurrentLine.split("\\s+"); //Splits with whitespace as delimiter.

			if(firstRun) {  //Do this when run for the first time.
				M = Integer.parseInt(splitted[0]);  // Reads in integer M from first line of the input file.
				graphCapacity = new int[M][M];  //Initiate graph as MxM dimension matrix.
				sink = M - 1;  //Set sink number, first sink has number 0.
				firstRun = false;
			} else {  //Populate the matrix.
				for (int j = 0; j < M; j++) {
					graphCapacity[lineX][j] = Integer.parseInt(splitted[j]);
				}
				lineX++;
			}
		} 
		br.close();
		
		//Instantiate the initial state of the matrix.
		FordFulkerson fordfulkerson = new FordFulkerson(graphCapacity,source,sink);
		
		//Run Edmond and Karp verison of the FordFulkerson-algorithm.
		fordfulkerson.edmondAndKarp();
		
		//Call method writeToFile. args[1] is the output filename.
		writeToFile(fordfulkerson,args[1]);
	}
	
	
	/**
	 * Method that writes the required fields as described in the assignment.
	 * @param fordfulkerson - An instant of FordFulkerson.
	 * @param outputFileName - Filename of the output file.
	 * @throws IOException
	 */
	public static void writeToFile(FordFulkerson fordfulkerson, String outputFileName) throws IOException  {
		FileWriter fw = new FileWriter(outputFileName);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);
		
		//Value of the optimal flow/max flow.
		pw.println(fordfulkerson.getSolutions().getOptimalFlow());

		//Matrix with flow.
		for(int i=0; i<fordfulkerson.getSolutions().getFlowGraphMatrix().length; i++) {
			for(int j=0; j<fordfulkerson.getSolutions().getFlowGraphMatrix().length; j++) {
				pw.print(fordfulkerson.getSolutions().getFlowGraphMatrix()[i][j]);
				pw.print(" ");
			}
			pw.println();
		}

		//Mumbers of the vertices on the source side of a cut with the capacity of the flow, in sorted order. Including source.
		for(int i=0; i<fordfulkerson.getSolutions().getsVertices().size(); i++){
			pw.print(fordfulkerson.getSolutions().getsVertices().get(i)+1+" ");
		}
		
		pw.println();
		//Number of times a new constructed N(f) and found a path through it from the source to the sink.
		pw.println(fordfulkerson.getSolutions().getNewConstructed());
		pw.close();
	}
}
