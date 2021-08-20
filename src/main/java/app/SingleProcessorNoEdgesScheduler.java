package app;

import model.Node;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SingleProcessorNoEdgesScheduler {

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
        return output;

    }

}
