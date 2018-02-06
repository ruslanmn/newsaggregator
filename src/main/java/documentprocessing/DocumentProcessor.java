package documentprocessing;

import clustering.DivisiveKMeansClusterer;
import documentsdatastructures.DocumentVector;
import documentsdatastructures.InvertedIndex;
import documentsdatastructures.InvertedIndexVectorizer;
import documentsdatastructures.NewsDocument;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import treedatastructures.TreeNode;

import java.util.List;
import java.util.stream.Collectors;

public class DocumentProcessor {
    InvertedIndex invertedIndex;
    DivisiveKMeansClusterer divisiveKMeansClusterer;

    public DocumentProcessor() {
        invertedIndex = new InvertedIndex();
        divisiveKMeansClusterer = new DivisiveKMeansClusterer();
    }

    public List<SearchResultItem> search(String query, int size) {
        return invertedIndex.search(query, size);
    }

    public Pair<List<List<ClusterResultItem>>, Double> clusterize(int clusterSize) {
        List<DocumentVector> documentVectors = InvertedIndexVectorizer.vectorize(invertedIndex);
        int numTrials = 1;
        DistanceMeasure measure = new EuclideanDistance();
        /*divisiveKMeansClusterer.cluster(documentVectors, 1, 3, numTrials, measure);
        double min = divisiveKMeansClusterer.getClusteringError();
        int minCs = 3;

        for(int cs = 4; cs <= clusterSize; cs++) {
            System.out.println("cs = " + cs);
            divisiveKMeansClusterer.cluster(documentVectors, 1, cs, numTrials, measure);
            if(divisiveKMeansClusterer.getClusteringError() < min) {
                min = divisiveKMeansClusterer.getClusteringError();
                minCs = cs;
            }
        }

        divisiveKMeansClusterer.cluster(documentVectors, 1, minCs, numTrials, measure);*/
        divisiveKMeansClusterer.cluster(documentVectors, 1, clusterSize, numTrials, measure);

        TreeNode<List<ClusterResultItem>> clusters = divisiveKMeansClusterer.getRootNode();
        Double error = divisiveKMeansClusterer.getClusteringError();

        List<List<ClusterResultItem>> result = clusters.getChildren().stream().map(treeNode -> treeNode.getValue()).collect(Collectors.toList());

        return new ImmutablePair<>(result, error);
    }

    public void addAll(List<NewsDocument> newsDocuments) {
        invertedIndex.addAll(newsDocuments);
    }

    public void finish() {
        invertedIndex.normalizeDocuments();
    }
}
