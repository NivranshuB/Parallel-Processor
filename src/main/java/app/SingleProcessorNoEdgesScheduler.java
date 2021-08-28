package app;

import model.Node;
import parsers.DotFileReader;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: Team Untested (13)
 * Class used for special cases where no edges are present in the input graph and when scheduled on only one processor
 */
public class SingleProcessorNoEdgesScheduler extends Scheduler{

    private Map<String, Node> nodeMap;

    /**
     * Constructor for scheduler that is run to schedule the edge case situation of having all nodes without edges,
     * running on a single processor.
     * @param dotFileReader reference to DotFileReader object.
     */
    public SingleProcessorNoEdgesScheduler(DotFileReader dotFileReader) {
        nodeMap = dotFileReader.getNodeMap();

    }

    /**
     * Method that returns the optimum schedule.
     * @return returns optimum BnBSchedule.
     */
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
