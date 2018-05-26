package server;

import documentprocessing.DocumentProcessor;
import documentprocessing.datastructures.ClusteringResult;
import documentsdatastructures.NewsDocument;
import org.xml.sax.SAXException;
import storage.DocumentLoader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Endpoint;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class NewsAggregatorServer {

    private static final String APP_NAME = "newsaggregator";
    private static final int CLUSTER_SIZE = 15;

    public void start(String host, Integer port) throws ParserConfigurationException, SAXException, ParseException, IOException, InterruptedException {
        final String url = String.format("http://%s:%d/%s", host, port, APP_NAME);

        ClusteringWebServiceImpl clusteringWebService = new ClusteringWebServiceImpl();
        clusteringWebService.setClusteringResult(updateDataAndReclusterize());
        Endpoint.publish(url, clusteringWebService);

        while(true) {
            Thread.sleep(TimeUnit.HOURS.toMillis(8));
            clusteringWebService.setClusteringResult(updateDataAndReclusterize());
            System.out.println("Updated");
        }
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, InterruptedException, ParseException {
        NewsAggregatorServer newsAggregatorServer = new NewsAggregatorServer();
        newsAggregatorServer.start("localhost", 8888);
    }

    public ClusteringResult updateDataAndReclusterize() throws ParserConfigurationException, SAXException, IOException, ParseException {
        System.out.println("Loading news...");
        DocumentLoader.loadNewsFromSources();

        System.out.println("Loading vectors...");
        DocumentProcessor documentProcessor = new DocumentProcessor();
        Map<String, String> termsToWords = new HashMap<>();
        List<NewsDocument> newsDocuments = DocumentLoader.loadNewsDocuments(termsToWords);
        documentProcessor.addAll(newsDocuments, termsToWords);
        documentProcessor.finish();

        System.out.println("Clustering...");
        ClusteringResult clusteringResult = documentProcessor.performClustering(CLUSTER_SIZE);

        return clusteringResult;
    }
}
