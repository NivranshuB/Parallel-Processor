package app;

/**
 * Author: Team UNTESTED
 * This class runs in its own thread and pushes the memory usage to the UI
 * at a set interval.
 */
public class MemoryInfo implements Runnable {

	private Thread cpuInfo;
	private int interval;
	private MainController controller;

	/**
	 * Constructs the MemoryInfo object used to monitor and push memory usage.
	 * @param controller UI controller.
	 * @param interval   The interval to push updates.
	 */
	public MemoryInfo(MainController controller, int interval)   {
		this.interval = interval;
		this.controller = controller;

		// Launch CPUInfo in a different thread.
		this.cpuInfo = new Thread(new CPUInfo(this.controller));
		this.cpuInfo.start();
	}

	/**
	 * Start the MemoryInfo thread and push memory usage to the main controller.
	 */
	public void run() {
		while (true)    {
			long start = System.currentTimeMillis();
			if (Thread.currentThread().isInterrupted()) {
				cpuInfo.interrupt();
				break;
			}

			Runtime runtime = Runtime.getRuntime();
			controller.updateMemory(runtime.totalMemory() - runtime.freeMemory());

			// Sleep for the given interval
			try {
				long sleep = interval - (System.currentTimeMillis() - start);
				Thread.sleep(sleep > 0 ? sleep : 1);
			} catch (InterruptedException e)    {
				cpuInfo.interrupt();
				break;
			}

		}
	}

}