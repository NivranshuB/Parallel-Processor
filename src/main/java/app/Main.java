package app;



/**
 * Author: Team UNTESTED
 * Main method where the implementation of the program begins.
 */
public class Main {

    public static void main(String[] args) {
        //SV's code to parse command line input arguments to extract the number of
        //processors, DOT file and other args

        //Joe's code that uses the DOT filepath from the args to create a new
        //DotFileReader object/graph representation
        DotFileReader dfr = new DotFileReader("Nodes_7_OutTree.dot");

        Scheduler scheduler = Scheduler.getInstance();

        scheduler.getOptimalSchedule(dfr.getNodeMap(), dfr.getEdgeMap(), 1);

        //optimalSchedule = scheduler.getOptimalSchedule(nodeMap, edgeMap, numberOfProcessors);

        //Corban's code to parse the optimal schedule to the output DOT file
        OutputParser op = new OutputParser(dfr);

        op.writeFile();
    }



}
