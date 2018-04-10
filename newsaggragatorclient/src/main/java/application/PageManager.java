package application;

import datastructures.ClusterModel;
import datastructures.ClusteringResult;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import lombok.Getter;
import server.ClusteringWebService;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.URL;

public class PageManager {

    private static final String SEARCH_PAGE_FXML = "search_page.fxml";
    private static final String CLUSTERS_PAGE_FXML = "clusters_page.fxml";
    private static final String CLUSTER_TABLE_PAGE_FXML = "cluster_table_page.fxml";


    @Getter
    private Scene scene;
    private Parent clustersPage, clusterTablePage;

    private ClusteringWebService clusteringWebService;

    private ClustersController clustersController;
    private ClusterTableController clusterTableController;

    public PageManager() throws IOException {
        URL wsdlURL = new URL("http://localhost:8888/newsaggregator");
        QName qname = new QName("http://server/", "ClusteringWebServiceImplService");
        Service service = Service.create(wsdlURL, qname);
        clusteringWebService = service.getPort(ClusteringWebService.class);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(CLUSTERS_PAGE_FXML));

        clustersPage = fxmlLoader.load();
        clustersController = fxmlLoader.getController();
        clustersController.setPageManager(this);

        fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(CLUSTER_TABLE_PAGE_FXML));
        clusterTablePage = fxmlLoader.load();
        clusterTableController = fxmlLoader.getController();
        clusterTableController.setPageManager(this);

        scene = new Scene(clustersPage);
    }


    public void changeToClusterTablePage(ClusterModel clusterModel) {
        clusterTableController.updatePage(clusterModel);
        scene.setRoot(clusterTablePage);
    }

    public ClusteringResult getClusters() {
        return clusteringWebService.getClusters();
    }

    public void changeToClustersPage() {
        scene.setRoot(clustersPage);
    }
}
