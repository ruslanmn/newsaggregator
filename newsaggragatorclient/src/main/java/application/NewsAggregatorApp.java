package application;

import documentprocessing.DocumentProcessor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.xml.sax.SAXException;
import server.ClusteringWebService;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class NewsAggregatorApp extends Application {

    protected static NewsAggregatorApp app;

    ClusterController clusterController;
    SearchController searchController;
    DocumentProcessor documentProcessor;

    public ClusteringWebService clusteringWebService;

    private static final boolean is_search_mode = false;
    private static final String searchPage = "search_page.fxml";
    private static final String clusterPage = "cluster_page.fxml";

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, ParseException {
        String dateStr = "Thu, 29 Mar 2018 00:32:54";
        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = formatter.parse(dateStr);

        DateFormat outFormat = new SimpleDateFormat("dd MM yyyy HH:mm");
        outFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        System.out.println(outFormat.format(date));
        //launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException, ParserConfigurationException, SAXException {
        app = this;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(is_search_mode ? searchPage : clusterPage));

        Scene scene = new Scene(root);

        URL wsdlURL = new URL("http://localhost:8888/newsaggregator");
        QName qname = new QName("http://server/", "ClusteringWebServiceImplService");
        Service service = Service.create(wsdlURL, qname);
        clusteringWebService = service.getPort(ClusteringWebService.class);

        primaryStage.setTitle("Client");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
