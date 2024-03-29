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
import java.net.MalformedURLException;
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
        try {
            clusteringWebService = loadService();
        } finally {
            if(clusteringWebService == null) {
                System.out.println("Error was occured");
                System.exit(0);
            }
        }
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

    private ClusteringWebService loadService() throws MalformedURLException {
        URL wsdlURL = new URL("http://localhost:8888/newsaggregator");
        QName qname = new QName("http://server/", "ClusteringWebServiceImplService");
        Service service = Service.create(wsdlURL, qname);
        return service.getPort(ClusteringWebService.class);
    }


    public void changeToClusterTablePage(ClusterModel clusterModel, boolean showDistance) {
        clusterTableController.updatePage(clusterModel, showDistance);
        scene.setRoot(clusterTablePage);
    }

    public ClusteringResult getClusters() {
        ClusteringResult clusteringResult = clusteringWebService.getClusters();

        ClusterModel allNews = new ClusterModel();
        for(ClusterModel cluster : clusteringResult.getClusters()) {
            allNews.getItemModels().addAll(cluster.getItemModels());
        }

        allNews.setName("Все Новости");

        clusteringResult.getClusters().add(0, allNews);

        return clusteringResult;
    }

    public void changeToClustersPage() {
        scene.setRoot(clustersPage);
    }
}
