package documentprocessing;

import clustering.DivisiveClusterer;
import documentsdatastructures.DocumentVector;
import documentsdatastructures.InvertedIndex;
import documentsdatastructures.InvertedIndexVectorizer;
import documentsdatastructures.NewsDocument;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import treedatastructures.TreeNode;

import java.util.List;
import java.util.stream.Collectors;

public class DocumentProcessor {
    InvertedIndex invertedIndex;
    DivisiveClusterer divisiveClusterer;

    public DocumentProcessor() {
        invertedIndex = new InvertedIndex();
        divisiveClusterer = new DivisiveClusterer();
    }

    public List<SearchResultItem> search(String query, int size) {
        return invertedIndex.search(query, size);
    }

    public Pair<List<List<ClusterResultItem>>, Double> clusterize(int clusterSize) {
        List<DocumentVector> documentVectors = InvertedIndexVectorizer.vectorize(invertedIndex);

       /* divisiveClusterer.cluster(documentVectors, 1, 3);
        double min = divisiveClusterer.getClusteringError();
        int minCs = 3;

        for(int cs = 4; cs <= clusterSize; cs++) {
            System.out.println("cs = " + cs);
            divisiveClusterer.cluster(documentVectors, 1, cs);
            if(divisiveClusterer.getClusteringError() < min) {
                min = divisiveClusterer.getClusteringError();
                minCs = cs;
            }
        }

        divisiveClusterer.cluster(documentVectors, 1, minCs);*/
        divisiveClusterer.cluster(documentVectors, 1, clusterSize);

        TreeNode<List<ClusterResultItem>> clusters = divisiveClusterer.getRootNode();
        Double error = divisiveClusterer.getClusteringError();

        List<List<ClusterResultItem>> result = clusters.getChildren().stream().map(treeNode -> treeNode.getValue()).collect(Collectors.toList());

        return new ImmutablePair<>(result, error);
    }

    public void addAll(List<NewsDocument> newsDocuments) {
        invertedIndex.addAll(newsDocuments);
    }
}
