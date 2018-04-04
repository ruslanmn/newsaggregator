package documenttagger;

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.math3.util.FastMath;

import java.util.*;

public class MutualInformationFactory {

    private static MutualInformation mutualInformationInstance = new MutualInformation();

    public static InformationComputer getInstance() {
        return mutualInformationInstance;
    }

    static class MutualInformation implements InformationComputer {
        @Override
        public List<PriorityQueue<String>> compute(List<ClusterCount> clusterCounts, ClusterCount totalCount) {

            List<PriorityQueue<String>> clusterInformations = new LinkedList<>();

            for(ClusterCount clusterCount : clusterCounts) {
                Map<String, Float> termInformations = new HashMap<>();
                PriorityQueue<String> sortedTerms = new PriorityQueue<>(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return termInformations.get(o2).compareTo(termInformations.get(o1));
                    }
                });

                for(MultiSet.Entry<String> termCount : clusterCount.getTermCounts().entrySet()) {
                    /*
                        n = total docs
                        n11 = with term in performClustering
                        n10 = with term out of performClustering
                        n01 = not with term in performClustering
                        n00 = not with term out of performClustering
                     */

                    String term = termCount.getElement();
                    if(term.length() < 5)
                        continue;;

                    int n11 = termCount.getCount();
                    int n10 = totalCount.getTermCounts().getCount(term)- n11;
                    int n01 = clusterCount.size() - n11;
                    int n00 = totalCount.size() - totalCount.getTermCounts().getCount(term) - n01;
                    int n = totalCount.size();

                    int n1_ = n10 + n11;
                    int n0_ = n00 + n01;
                    int n_0 = n00 + n10;
                    int n_1 = n01 + n11;

                    double information = 0;
                    if(n00*n01*n10*n11 != 0)
                        information = n11 / n * FastMath.log(2, n*n11/(n1_*n_1)) +
                                n01 / n * FastMath.log(2, n*n01/(n0_*n_1)) +
                                n10 / n * FastMath.log(2, n*n10/(n1_*n_0)) +
                                n00 / n * FastMath.log(2, n*n00/(n0_*n_0));
                    termInformations.put(term, (float)information);
                    sortedTerms.add(term);
                }

                clusterInformations.add(sortedTerms);
            }

            return clusterInformations;
        }

        private int countDocumentsWithTerm(List<ClusterCount> clusterCounts, String term) {
            int docsCount = 0;
            for(ClusterCount clusterCount : clusterCounts) {
                docsCount += clusterCount.getTermCounts().getCount(term);
            }

            return docsCount;
        }
    }
}


