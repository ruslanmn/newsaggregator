package application;

import documentprocessing.DocumentProcessor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.xml.sax.SAXException;
import storage.DocumentLoader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class NewsAggregatorApp extends Application {

    protected static NewsAggregatorApp app;

    ClusterController clusterController;
    SearchController searchController;
    DocumentProcessor documentProcessor;

    private static final boolean is_search_mode = false;
    private static final String searchPage = "search_page.fxml";
    private static final String clusterPage = "cluster_page.fxml";

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        app = this;
        documentProcessor = new DocumentProcessor();
        documentProcessor.addAll(DocumentLoader.loadNewsDocuments());



        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(is_search_mode ? searchPage : clusterPage));

        Scene scene = new Scene(root);

        primaryStage.setTitle("News Aggregator");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
