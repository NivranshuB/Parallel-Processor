package app;

import java.io.File;

/**
 * Singleton class which stores argument options/config from the command line.
 */
public class Config {

    //Static variable single_instance of type Singleton
    private static Config singleInstance = null;
   
    private int numOfTasks;
    private int numOfCores;
    private int numOfProcessors;
    private File inputFile;
    private File outputFile;
    private boolean visualise;

    /**
     * Private constructor used to ensure a Singleton instance of Config class.
     */
    private Config() {
    }

    /**
     * Static method to create/return a Singleton instance of Config class.
     * @return Singleton Config instance.
     */
    public static Config getInstance() {
    
        if (singleInstance == null)
            singleInstance = new Config();
  
        return singleInstance;
    }

    /**
     * Gets the input file stored.
     * @return Input DOT file of task graph.
     */
    public File getInputFile() {
        return inputFile;
    }

    /**
     * Sets the input file for storage.
     * @param inputFile Input DOT file of task graph.
     */
    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Gets the number of cores to use when executing algorithm.
     * @return Number of cores to use when executing algorithm.
     */
    public int getNumOfTasks() {
        return numOfTasks;
    }

    /**
     * Sets the number of cores to use when executing algorithm for storage.
     * @param numOfCores Number of cores to use when executing algorithm.
     */
    public void setNumOfTasks(int numOfTasks) {
        this.numOfTasks = numOfTasks;
    }

    /**
     * Gets the number of cores to use when executing algorithm.
     * @return Number of cores to use when executing algorithm.
     */
    public int getNumOfCores() {
        return numOfCores;
    }

    /**
     * Sets the number of cores to use when executing algorithm for storage.
     * @param numOfCores Number of cores to use when executing algorithm.
     */
    public void setNumOfCores(int numOfCores) {
        this.numOfCores = numOfCores;
    }

    /**
     * Gets the number of processors to use to schedule the tasks.
     * @return Number of processors to use to schedule tasks.
     */
    public int getNumOfProcessors() {
        return numOfProcessors;
    }

    /**
     * Sets the number of processors to use to schedule the tasks for storage.
     * @param numOfProcessors Number of processors to use to schedule tasks.
     */
    public void setNumOfProcessors(int numOfProcessors) {
        this.numOfProcessors = numOfProcessors;
    }

    /**
     * Gets the user's desired visualisation status of the search.
     * @return True if the user would like to see a visualisation of the search, False otherwise.
     */
    public boolean getVisualise() {
        return visualise;
    }

    /**
     * Sets the user's desired visualisation status of the search for storage.
     * @param visualise True if the user would like to see a visualisation of the search, False otherwise.
     */
    public void setVisualise(boolean visualise) {
        this.visualise = visualise;
    }

    /**
     * Gets the output file stored.
     * @return Output DOT file of scheduled task graph.
     */
    public File getOutputFile() {
        return outputFile;
    }

    /**
     * Sets the output file for storage.
     * @param outputFile Output DOT file of scheduled task graph.
     */
    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

}
