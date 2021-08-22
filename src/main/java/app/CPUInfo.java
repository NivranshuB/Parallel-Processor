package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Team Untested (13)
 * This class runs in its own thread and pushes the CPU usage to the UI
 * every 1 second.
 */
public class CPUInfo implements Runnable {

	private MainController controller;

	/**
	 * Constructs the CPUInfo object used to monitor and push CPU usage.
	 * @param controller UI controller.
	 */
	public CPUInfo(MainController controller) {
		this.controller = controller;
	}

	/**
	 * Start the CPUInfo thread and push CPU usage to the main controller.
	 */
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			List<Double> cpuLoad = new LinkedList<>();

			// Uses mpstat to read and send cpu usage every 1 second
			ProcessBuilder cpuPB = new ProcessBuilder("/bin/bash", "-c", "mpstat -P ALL 1 1");
			try {
				Process cpuP = cpuPB.start();
				cpuP.waitFor();

				BufferedReader reader = new BufferedReader(new InputStreamReader((cpuP.getInputStream())));

				// Skip starting lines
				reader.readLine();
				reader.readLine();
				reader.readLine();
				reader.readLine();

				// Read CPU lines
				String line = reader.readLine();
				Pattern cpuLineMatcher = Pattern.compile("^Average:\\W*[0-9]*(\\W*[0-9]*\\.[0-9]*]*)*([0-9]*)$");
				while (line != null)  {
					Matcher m = cpuLineMatcher.matcher(line);  
					if (m.matches())    {
						// get % of cpu that is free and subtract that from 100
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