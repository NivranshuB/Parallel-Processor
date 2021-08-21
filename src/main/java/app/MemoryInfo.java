package app;


public class MemoryInfo implements Runnable {

	private Thread cpuMonitor;
	private int interval;

	private MainController controller;

	public MemoryInfo(MainController controller, int interval)   {

		this.interval = interval;
		this.controller = controller;

	}


	public void run() {
        while (true)    {
            long start = System.currentTimeMillis();
            if (Thread.currentThread().isInterrupted()) {
           
                cpuMonitor.interrupt();
                break;
            }

            Runtime runtime = Runtime.getRuntime();
            controller.updateMemory(runtime.totalMemory() - runtime.freeMemory());
          
            try {
                long sleep = interval - (System.currentTimeMillis() - start);
                Thread.sleep(sleep > 0 ? sleep : 1);
            } catch (InterruptedException e)    {
              
                cpuMonitor.interrupt();
                break;
            }

        }
    }

}