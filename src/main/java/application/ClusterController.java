package application;

import documentprocessing.ClusterResultItem;
import documentsdatastructures.NewsDocument;
import documenttagger.ClusterTagger;
import documenttagger.MutualInformationFactory;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    Comparator<ClusterResultItem> clusterResultItemComparator;

    @FXML
    public void initialize() {
        clusterResultItemComparator = new Comparator<ClusterResultItem>() {
            @Override
            public int compare(ClusterResultItem o1, ClusterResultItem o2) {
                if(o1.getDistanceFromCentroid() > o2.getDistanceFromCentroid())
                    return 1;
                else if (o1.getDistanceFromCentroid() < o2.getDistanceFromCentroid())
                    return -1;
                else
                    return 0;
            }
        };

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
        Pair<List<List<ClusterResultItem>>, Double> clusteringResult = NewsAggregatorApp.app.documentProcessor.clusterize(clusterSize);

        List<List<ClusterResultItem>> clusters = clusteringResult.getKey();

        NumberFormat formatter = new DecimalFormat("#0.00");

        TreeItem<String> root = new TreeItem<>();
        for(List<ClusterResultItem> cluster : clusters) {

            Collections.sort(cluster, clusterResultItemComparator);

            TreeItem<String> clusterTreeItem = new TreeItem<>();
            for(ClusterResultItem clusterResultItem : cluster) {
                NewsDocument doc = clusterResultItem.getNewsDocument();

                double distanceFromCluster = clusterResultItem.getDistanceFromCentroid();

                TreeItem<String> newsTreeItem = new TreeItem<>(formatter.format(distanceFromCluster) + " " + doc.getTitle());
                clusterTreeItem.getChildren().add(newsTreeItem);
            }
            root.getChildren().add(clusterTreeItem);
        }

        Collection<Collection<NewsDocument>> newsDocumentsClusters = clusters.stream().map(
                clusterResultItems -> clusterResultItems.stream().map(
                        cri -> cri.getNewsDocument()).collect(Collectors.toList())
        ).collect(Collectors.toList());

        List<PriorityQueue<String>> tagQueues = ClusterTagger.tag(newsDocumentsClusters, MutualInformationFactory.getInstance());

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

            refreshButton.setText(ButtonModes.ready);
            clustering = false;
            refreshButton.setDisable(false);
        });
    }

    @FXML
    public void clusterItemClicked(MouseEvent mouseEvent) {
        /*Node node = mouseEvent.getPickResult().getIntersectedNode();
        if(node instanceof Text) {
            clusterTree.getSelectionModel().get
            int ind = clusterTree.getSelectionModel().getSelectedIndex();
            System.out.println(ind);
        }*/
    }

    static class ButtonModes {
        public static final String clustering = "Кластеризируем...";
        public static final String ready = "Кластеризировать";
    }
}
