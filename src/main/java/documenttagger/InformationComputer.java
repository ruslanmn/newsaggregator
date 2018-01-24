package documenttagger;

import java.util.List;
import java.util.PriorityQueue;

public interface InformationComputer {
    List<PriorityQueue<String>> compute(List<ClusterCount> clusterInfos, ClusterCount totalInfo);
}
