package clustering;

import documentprocessing.datastructures.ItemModel;
import documentsdatastructures.DocumentVector;
import documentsdatastructures.NewsDocument;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import treedatastructures.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class DivisiveDBSCANClusterer {

    double rss;
    private TreeNode<List<ItemModel>> rootNode;


    public void cluster(List<DocumentVector> points,
                                      int maxLevel,
                                      int divideSize) {
        rss = 0;
        if(maxLevel < 1)
            throw new RuntimeException("Level should be >= 1");
        this.rootNode = cluster(points, 0, maxLevel, divideSize);
        rss /= points.size();
    }


    public double getClusteringError() {
        return rss;
    }


    private TreeNode<List<ItemModel>> cluster(List<DocumentVector> points,
                                              int level,
                                              int maxLevel,
                                              int divideSize) {
        TreeNode<List<ItemModel>> clusterNode = new TreeNode<>();
        if((level >= maxLevel) || (points.size() < divideSize))
            return clusterNode;

        DBSCANClusterer<DocumentVector> dbscan = new DBSCANClusterer<>(10000000, 5);
        List<Cluster<DocumentVector>> centroids = dbscan.cluster(points);
        for(Cluster<DocumentVector> centroid : centroids) {

            List<DocumentVector> centroidPoints = centroid.getPoints();
            List<ItemModel> clusterItems = new ArrayList<>(centroidPoints.size());

            // compute rss for centroid
            for(DocumentVector point : centroidPoints) {
                double dist = 0;//dbscan.getDistanceMeasure().compute(point.getPoint(), centroid.getCenter().getPoint());
                rss += dist*dist;
                NewsDocument doc = point.getDocument();
                ItemModel clusterItem = new ItemModel(doc.getTitle(), doc.getSource(), doc.getDate(), dist, doc.getUrl());
                clusterItems.add(clusterItem);
            }

            TreeNode<List<ItemModel>> subClusterNode = cluster(centroidPoints,
                    level + 1, maxLevel, divideSize);
            subClusterNode.setValue(clusterItems);
            clusterNode.add(subClusterNode);
        }

        return clusterNode;
    }

    public TreeNode<List<ItemModel>> getRootNode() {
        return rootNode;
    }
}
