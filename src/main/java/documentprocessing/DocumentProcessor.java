package documentprocessing;

import clustering.KMeansClusterer;
import documentprocessing.datastructures.ClusteringResult;
import documentsdatastructures.InvertedIndex;
import documentsdatastructures.InvertedIndexVectorizer;
import documentsdatastructures.NewsDocument;
import documentsdatastructures.VectorizedDocuments;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.List;

public class DocumentProcessor {

    private static final int TITLE_NAME_SIZE = 10;

    InvertedIndex invertedIndex;
    KMeansClusterer kMeansClusterer;


    public DocumentProcessor() {
        invertedIndex = new InvertedIndex();
        kMeansClusterer = new KMeansClusterer(new EuclideanDistance());
    }

    public List<SearchResultItem> search(String query, int size) {
        return invertedIndex.search(query, size);
    }

    public ClusteringResult performClustering(int clusterSize) {
        VectorizedDocuments vectorizedDocuments = InvertedIndexVectorizer.vectorize(invertedIndex);
        int numTrials = 1;

        ClusteringResult bestClusteringResult;
        int cs = clusterSize;//20;
        if(cs > clusterSize)
            cs = clusterSize;

        bestClusteringResult = kMeansClusterer.performClustering(vectorizedDocuments.getDocumentVectors(), cs, numTrials,
                TITLE_NAME_SIZE, vectorizedDocuments.getTermsSpaceMap());
        double minRss = bestClusteringResult.getRss();

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

    public void addAll(List<NewsDocument> newsDocuments) {
        invertedIndex.addAll(newsDocuments);
    }

    public void finish() {
        invertedIndex.normalizeDocuments();
    }

    public InvertedIndex getInvertedIndex() {
        return invertedIndex;
    }
}
