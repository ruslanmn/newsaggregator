package documenttagger;

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;

import java.util.Set;

public class ClusterCount {
    MultiSet<String> termCounts;
    int totalDocs;

    public ClusterCount() {
        termCounts = new HashMultiSet<>();
        totalDocs = 0;
    }

    public void addDocument(Set<String> doc) {
        termCounts.addAll(doc);
        totalDocs++;
    }

    public MultiSet<String> getTermCounts() {
        return termCounts;
    }

    public int size() {
        return totalDocs;
    }
}
