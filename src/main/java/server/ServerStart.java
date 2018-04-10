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
import java.util.List;

public class ServerStart {

    public static DocumentProcessor documentProcessor;

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, InterruptedException, ParseException {

        System.out.println("Loading vectors...");
        documentProcessor = new DocumentProcessor();
        List<NewsDocument> newsDocuments = DocumentLoader.loadNewsDocuments();
        documentProcessor.addAll(newsDocuments);
        documentProcessor.finish();

        System.out.println("Clustering...");
        ClusteringResult clusteringResult = ServerStart.documentProcessor.performClustering(10);
        ClusteringWebServiceImpl clusteringWebService = new ClusteringWebServiceImpl();
        clusteringWebService.clusteringResult = clusteringResult;


        System.out.println("Finished");

        Endpoint.publish("http://localhost:8888/newsaggregator", clusteringWebService);
        while(true) {
            Thread.sleep(Long.MAX_VALUE);
        }
    }
}
