package application;

import datastructures.ClusterModel;
import datastructures.ClusteringResult;
import datastructures.ItemModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.Setter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClustersController implements Runnable {

    @Setter
    private PageManager pageManager;

    @FXML
    public Button refreshButton;
    @FXML
    ListView<String> clusterListView;


    List<ClusterModel> clusters;

    private boolean clustering;


    NumberFormat formatter = new DecimalFormat("#0.00");

    @FXML
    public void initialize() {
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


        ClusteringResult clusteringResult = pageManager.getClusters();

        clusters = new ArrayList<>(clusteringResult.getClusters());


/*        TreeItem<String> root = new TreeItem<>();
        for(ClusterModel cluster : clusters) {
            List<ItemModel> itemModels = cluster.getItemModels();
            TreeItem<String> clusterTreeItem = new TreeItem<>(cluster.getName());

            for(ItemModel item : itemModels) {
                double distanceFromCluster = item.getDistance();

                String name = formatter.format(distanceFromCluster) + " " + item.getTitle() + " " + item.getSource();

                TreeItem<String> newsTreeItem = new TreeItem<>(name);
                clusterTreeItem.getChildren().add(newsTreeItem);
            }

            root.getChildren().add(clusterTreeItem);
        }*/


        Platform.runLater(() -> {
            clusterListView.getItems().clear();
            for(ClusterModel clusterModel : clusters) {
                clusterListView.getItems().add(clusterModel.getName());
            }

            refreshButton.setText(ButtonModes.ready);
            clustering = false;
            refreshButton.setDisable(false);
        });

    }

    private ClusteringResult fakeInit() {
        Date d = new Date();
        ItemModel item1 = new ItemModel("fakeTitle1", "fakeSource1", d, 10);

        d = new Date();
        d.setYear(10);
        ItemModel item2 = new ItemModel("fakeTitle2", "fakeSource2", d, 15);

        d = new Date();
        d.setYear(50);
        ItemModel item3 = new ItemModel("fakeTitle3", "fakeSource1", d, 10);

        d = new Date();
        d.setYear(30);
        ItemModel item4 = new ItemModel("fakeTitle4", "fakeSource2", d, 15);


        ClusterModel cluster1 = new ClusterModel();
        cluster1.setName("cluster1");
        cluster1.getItemModels().add(item1);
        cluster1.getItemModels().add(item2);

        ClusterModel cluster2 = new ClusterModel();
        cluster2.setName("cluster2");
        cluster2.getItemModels().add(item3);
        cluster2.getItemModels().add(item4);

        ClusteringResult clusteringResult = new ClusteringResult();
        clusteringResult.setRss(10);
        clusteringResult.getClusters().add(cluster1);
        clusteringResult.getClusters().add(cluster2);

        return clusteringResult;
    }


    public void onClusterClick(MouseEvent mouseEvent) {
        if (MouseButton.PRIMARY.equals(mouseEvent.getButton()) && mouseEvent.getClickCount() == 2) {
            handleItemSelection();
        }
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        if(KeyCode.ENTER.equals(keyEvent.getCode())) {
            handleItemSelection();
        }
    }

    private void handleItemSelection() {
        int ind = clusterListView.getSelectionModel().getSelectedIndex();
        pageManager.changeToClusterTablePage(clusters.get(ind), ind != 0);
        System.out.println(clusterListView.getSelectionModel().getSelectedItem());
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
        public static final String clustering = "Обновление...";
        public static final String ready = "Обновить";
    }



}
