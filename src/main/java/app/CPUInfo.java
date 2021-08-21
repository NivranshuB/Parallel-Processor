package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CPUInfo implements Runnable {
    private MainController controller;
  

    public CPUInfo(MainController controller) {
        this.controller = controller;
    }

  
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            List<Double> cpuLoad = new LinkedList<>();
            
            // Use MPStat for an average every 1 second
            ProcessBuilder cpuPB = new ProcessBuilder("/bin/bash", "-c", "mpstat -P ALL 1 1");
            try {
                Process cpuP = cpuPB.start();
                cpuP.waitFor();

                BufferedReader reader = new BufferedReader(new InputStreamReader((cpuP.getInputStream())));

                // Skip mpstat head
                reader.readLine();
                reader.readLine();
                reader.readLine();
                // Skip all summary
                reader.readLine();

                // Read CPU lines
                String line = reader.readLine();
                Pattern cpuLineMatcher = Pattern.compile("^Average:\\W*[0-9]*(\\W*[0-9]*\\.[0-9]*]*)*([0-9]*)$");
                while (line != null)  {
                    Matcher m = cpuLineMatcher.matcher(line);  
                    if (m.matches())    {
                    
                        cpuLoad.add((100 - Double.parseDouble(m.group(1)))/100);
             
                    }
                    line = reader.readLine();
                
                }

                try {
                    this.controller.updateCPU(cpuLoad);
                } catch (NullPointerException e) {
                    return; 
                }
            } catch (IOException e) {
             
                e.printStackTrace();
            } catch (InterruptedException e)    {
       
                return;
            }
        }
    }
}