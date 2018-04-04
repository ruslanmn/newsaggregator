package application;

import datastructures.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

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


    NumberFormat formatter = new DecimalFormat("#0.00");

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

        ClusteringResult clusteringResult = NewsAggregatorApp.app.clusteringWebService.getClusters();

        List<ClusterModel> clusters = clusteringResult.getClusters();



        TreeItem<String> root = new TreeItem<>();
        for(ClusterModel cluster : clusters) {
            List<ItemModel> itemModels = cluster.getItemModels();
            TreeItem<String> clusterTreeItem = new TreeItem<>(cluster.getName());

            for(ItemModel item : itemModels) {
                double distanceFromCluster = item.getDistanceFromCentroid();

                String name = formatter.format(distanceFromCluster) + " " + item.getTitle() + " " + item.getSourceName();

                TreeItem<String> newsTreeItem = new TreeItem<>(name);
                clusterTreeItem.getChildren().add(newsTreeItem);
            }

            root.getChildren().add(clusterTreeItem);
        }

        Platform.runLater(() -> {
            clusterTree.setRoot(root);
            root.setExpanded(true);
            clusteringErrorTextField.setText(formatter.format(clusteringResult.getRss()) + " for "
            + root.getChildren().size() + " clusters");

            refreshButton.setText(ButtonModes.ready);
            clustering = false;
            refreshButton.setDisable(false);
        });

    }
    /*

    private void tag(TreeItem<String> root, Collection<Collection<NewsDocument>> newsDocumentsClusters) {
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

    }

    private void tagByClosest(TreeItem<String> root, Collection<Collection<NewsDocument>> newsDocumentsClusters) {
        Iterator<TreeItem<String>> treeItemIterator = root.getChildren().iterator();
        Iterator<Collection<NewsDocument>> newsDocumentsIterator = newsDocumentsClusters.iterator();

        while(treeItemIterator.hasNext()) {
            treeItemIterator.next().setValue(newsDocumentsIterator.next().iterator().next().getTitle());
        }
    }


    @FXML
    public void clusterItemClicked(MouseEvent mouseEvent) {
        Node node = mouseEvent.getPickResult().getIntersectedNode();
        if(node instanceof Text) {
            clusterTree.getSelectionModel().get
            int ind = clusterTree.getSelectionModel().getSelectedIndex();
            System.out.println(ind);
        }
    }*/

    static class ButtonModes {
        public static final String clustering = "Кластеризируем...";
        public static final String ready = "Кластеризировать";
    }



}
