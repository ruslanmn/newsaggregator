package documentprocessing;

import clustering.AngularDistanceMeasure;
import clustering.KMeansClusterer;
import documentprocessing.datastructures.ClusteringResult;
import documentsdatastructures.InvertedIndex;
import documentsdatastructures.InvertedIndexVectorizer;
import documentsdatastructures.NewsDocument;
import documentsdatastructures.VectorizedDocuments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentProcessor {

    private static final int TITLE_NAME_SIZE = 10;

    InvertedIndex fullInvertedIndex;
    KMeansClusterer kMeansClusterer;
    VectorizedDocuments vectorizedDocuments;

    Map<String, String> termsToWords;


    public DocumentProcessor() {
        termsToWords = new HashMap<>();
        fullInvertedIndex = new InvertedIndex();
        kMeansClusterer = new KMeansClusterer(new AngularDistanceMeasure());
    }

    /*public List<SearchResultItem> search(String query, int size) {
        return fullInvertedIndex.search(query, size);
    }*/

    public ClusteringResult performClustering(int clusterSize) {
         int numTrials = 5;

        ClusteringResult bestClusteringResult;
        int cs = clusterSize;//20;
        if(cs > clusterSize)
            cs = clusterSize;

        bestClusteringResult = kMeansClusterer.performClustering(vectorizedDocuments.getDocumentVectors(), cs, numTrials,
                TITLE_NAME_SIZE, vectorizedDocuments.getTermsSpaceMap(), termsToWords);
        //double minRss = bestClusteringResult.getRss();

       /* FileWriter fw = null;
        try {
            fw = new FileWriter("rss_result_to_analyze.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("[");
        for(cs = 2; cs <= 45; cs++) {
            ClusteringResult clusteringResult = kMeansClusterer.performClustering(vectorizedDocuments.getDocumentVectors(), cs, numTrials);
            System.out.println(clusteringResult.getRss() + ",");
            try {
                fw.write(clusteringResult.getRss() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("]");

        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        return bestClusteringResult;
    }

    public void addAll(List<NewsDocument> newsDocuments, Map<String, String> termsToWords) {
        this.termsToWords.putAll(termsToWords);
        fullInvertedIndex.addAll(newsDocuments);
    }

    public void finish() {
        fullInvertedIndex.normalizeDocuments();
        vectorizedDocuments = InvertedIndexVectorizer.vectorize(fullInvertedIndex);

    }

    /*public InvertedIndex getFullInvertedIndex() {
        return fullInvertedIndex;
    }*/
}
