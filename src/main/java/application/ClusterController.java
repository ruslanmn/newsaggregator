package application;

import documentsdatastructures.NewsDocument;
import documenttagger.ClusterTagger;
import documenttagger.MutualInformationFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class ClusterController implements Runnable {
    @FXML
    public Spinner<Integer> clusterSizeSpinner;
    @FXML
    public TextField clusteringErrorTextField;
    @FXML
    public Button refreshButton;
    @FXML
    TreeView<String> clusterTree;

    private static int clusterSize = 20;
    private static int tagSize = 10;

    private boolean clustering;


    @FXML
    public void initialize() {
        NewsAggregatorApp.app.clusterController = this;
        refreshButton.setText(ButtonModes.ready);
        clustering = false;
    }


    @FXML
    public void handleRefreshButtonAction(ActionEvent actionEvent) {
        if(clustering)
            return;

        refreshButton.setText(ButtonModes.clustering);
        refreshButton.setDisable(true);

        new Thread(this).start();
    }

    @Override
    public void run() {
        clustering = true;

        clusterSize = clusterSizeSpinner.getValue();
        Pair<Collection<Collection<NewsDocument>>, Double> clusteringResult = NewsAggregatorApp.app.documentProcessor.clusterize(clusterSize);

        Collection<Collection<NewsDocument>> clusters = clusteringResult.getKey();

        TreeItem<String> root = new TreeItem<>();
        for(Collection<NewsDocument> cluster : clusters) {
            TreeItem<String> clusterItem = new TreeItem<>();
            for(NewsDocument doc : cluster) {
                clusterItem.getChildren().add(new TreeItem<>(doc.getTitle()));
            }
            root.getChildren().add(clusterItem);
        }

        List<PriorityQueue<String>> tagQueues = ClusterTagger.tag(clusters, MutualInformationFactory.getInstance());

        Iterator<TreeItem<String>> treeItemIterator = root.getChildren().iterator();
        Iterator<PriorityQueue<String>> tagIterator = tagQueues.iterator();
        while(treeItemIterator.hasNext()) {
            StringBuilder clusterName = new StringBuilder();

            PriorityQueue<String> tagQueue = tagIterator.next();
            int i = 0;
            while((i < tagSize) && (!tagQueue.isEmpty()))
                clusterName.append(tagQueue.poll()).append(" ");


            treeItemIterator.next().setValue(clusterName.toString());
        }
        Platform.runLater(() -> {
            clusterTree.setRoot(root);
            root.setExpanded(true);
            clusteringErrorTextField.setText(clusteringResult.getValue().toString());

            refreshButton.setDisable(false);
            clustering = false;
        });
    }

    static class ButtonModes {
        public static final String clustering = "Кластеризируем...";
        public static final String ready = "Кластеризировать";
    }
}
