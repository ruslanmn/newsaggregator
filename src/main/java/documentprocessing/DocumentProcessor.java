package documentprocessing;

import clustering.DivisiveClusterer;
import documentsdatastructures.DocumentVector;
import documentsdatastructures.InvertedIndex;
import documentsdatastructures.InvertedIndexVectorizer;
import documentsdatastructures.NewsDocument;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.xml.sax.SAXException;
import rssparser.RssParser;
import treedatastructures.TreeNode;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentProcessor {
    InvertedIndex invertedIndex;
    DivisiveClusterer<DocumentVector> divisiveClusterer;

    public DocumentProcessor() {
        invertedIndex = new InvertedIndex();
        divisiveClusterer = new DivisiveClusterer<>();
    }

    public void addRssUrl(String rssUrl) throws ParserConfigurationException, SAXException, IOException {
        invertedIndex.addAll(RssParser.getInstance().parseNewsDocumentsByUrl(rssUrl));
    }

    public List<SearchResultItem> search(String query, int size) {
        return invertedIndex.search(query, size);
    }

    public Pair<Collection<Collection<NewsDocument>>, Double> clusterize(int clusterSize) {
        List<DocumentVector> documentVectors = InvertedIndexVectorizer.vectorize(invertedIndex);
        divisiveClusterer.cluster(documentVectors, 1, clusterSize);

        TreeNode<List<DocumentVector>> clusters = divisiveClusterer.getRootNode();
        Double error = divisiveClusterer.getClusteringError();

        return new ImmutablePair<>(clusters.getChildren().stream()
                .map(clusterTreeNode ->
                        clusterTreeNode.getValue().stream()
                                .map(documentVector -> documentVector.getDocument())
                                .collect(Collectors.toList())
                ).collect(Collectors.toList()), error);
    }

    public void addAll(List<NewsDocument> newsDocuments) {
        invertedIndex.addAll(newsDocuments);
    }
}
