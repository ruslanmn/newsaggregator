package documenttagger;

import documentsdatastructures.NewsDocument;

import java.util.*;

public class ClusterTagger {
    /**
     *
     * @param clusters список кластеров, где каждый кластер - список документов, где каждый документ - множество слов
     * @return
     */
    public static List<PriorityQueue<String>> tag(Collection<Collection<NewsDocument>> clusters, InformationComputer ic) {
        ClusterCount totalCount = new ClusterCount();
        List<ClusterCount> clusterCounts = new ArrayList<>(clusters.size());

        for(Collection<NewsDocument> cluster : clusters) {
            ClusterCount clusterCount = new ClusterCount();

            for(NewsDocument document : cluster) {
                clusterCount.addDocument(document.getWords());
                totalCount.addDocument(document.getWords());
            }

            clusterCounts.add(clusterCount);
        }

        List<PriorityQueue<String>> clustersTags = ic.compute(clusterCounts, totalCount);
        /*List<List<List<String>>> tags = new LinkedList<>();


        for(int clusterInd = 0; clusterInd < clusters.size(); clusterInd++) {
            PriorityQueue<String> clusterTags = clustersTags.get(clusterInd);
            List<Set<String>> performClustering = clusters.get(clusterInd);
            List<List<String>> clusterDocs = new ArrayList<>(performClustering.size());
            for(Set<String> doc : performClustering) {
                List<String> docTags = new ArrayList<>(tagsSize);
                int tagInd = 0;
                for(String tag : clusterTags) {
                    if(tagInd == tagsSize)
                        break;
                    if(doc.contains(tag))
                        docTags.add(tag);
                    tagInd++;
                }
                clusterDocs.add(docTags);
            }
            tags.add(clusterDocs);
        }*/

        return clustersTags;
    }
}
