package app;

import model.Node;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SingleProcessorNoEdgesScheduler extends Scheduler{

    private Map<String, Node> nodeMap;


    public SingleProcessorNoEdgesScheduler(DotFileReader dotFileReader) {
        nodeMap = dotFileReader.getNodeMap();

    }

    public BnBSchedule getSchedule() {
        Processor processor = new Processor();

        for (Node n : nodeMap.values()) {
            int startTime = processor.getAvailableStartTime();
            processor.scheduleNode(n, startTime);
        }

        List<Processor> pList = new ArrayList<Processor>();
        pList.add(processor);
        BnBSchedule output = new BnBSchedule(pList);
        for (PropertyChangeListener l : listeners) {
            l.propertyChange(new PropertyChangeEvent(this, "optimal schedule", "old", output.getWeight()));
        }
        return output;

    }

}
