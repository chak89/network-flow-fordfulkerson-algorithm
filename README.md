# Network flow - FordFulkerson-algorithm 

Implementation of FordFulkerson-algorithm in Java, using the shortest augmentation path in each step (Edmonds and Karp’s version). Given a graph with capacities my program output the value of an optimal flow, the flow over each edge, and a cut (the one given by the algorithm) proving that the flow is optimal.    
The graph is a directed graph, i.e. the capacity from vertex u to vertex v can be different from the capacity from v back to u. All capacities are integer and non-negative. (We use the term vertex in this exercise; it is sometimes also called a node.)


## Usage


### Input 
The program reads its input from a designated file. The filename is given to the program, together with the name of an output file, as command line arguments. The input file contains:
- First a line with the number of vertices m.
- Then m lines with m numbers each (a matrix) defining the capacities between each pair of vertices. The number in line i and column j is the capacity from vertex i to vertex j, in other words the capacity of the edge (i, j) in the graph.
The vertices are numbered 1 through m, with 1 as the source and m as the sink. Note that there may be a positive capacity both from a vertex v to a vertex u, and from u back to v. On the diagonal all capacities are 0. There are no edges going into the source or out of the sink (the first column and the last line contain only zeros).


### Output  
- First a line with value of the optimal flow.
- Then m lines with m numbers each (a matrix) where the flow is written, in the same format as the capacities of the input (vertical index is from, and horizontal is to).
- Then a separate line with the numbers of the vertices on the source side of a cut with the capacity of the flow, in sorted order. The source should be included.
- Finally, also on a separate line, the number of incremental steps. That is, the number of times you constructed a new N(f) and found a path through it from the source to the sink (not counting the last search that failed and pointed out the cut). This number may vary depending on which path


## Example 
![alt text](https://i.imgur.com/DuM5IFx.jpg) 