package app;

import java.io.File;

/**
 * Singleton class which stores argument options/config from the command line.
 */
public class Config {

    // static variable single_instance of type Singleton
    private static Config singleInstance = null;
   
    private int numOfCores;
    private int numOfProcessors;
    private File inputFile;
    private File outputFile;
    private boolean visualise;

    // private constructor
    private Config() {
    }

    // static method to create instance of Singleton class
    public static Config getInstance() {
    
        if (singleInstance == null)
            singleInstance = new Config();
  
        return singleInstance;
    }

    // getters and setters for config options
    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public int getNumOfCores() {
        return numOfCores;
    }

    public void setNumOfCores(int numOfCores) {
        this.numOfCores = numOfCores;
    }

    public int getNumOfProcessors() {
        return numOfProcessors;
    }

    public void setNumOfProcessors(int numOfProcessors) {
        this.numOfProcessors = numOfProcessors;
    }

    public boolean getVisualise() {
        return visualise;
    }

    public void setVisualise(boolean visualise) {
        this.visualise = visualise;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

}
