package clustering;

import documentprocessing.datastructures.ItemModel;
import documentsdatastructures.DocumentVector;
import documentsdatastructures.NewsDocument;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import treedatastructures.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class DivisiveKMeansClusterer {

    double rss;
    private TreeNode<List<ItemModel>> rootNode;

    public void cluster(List<DocumentVector> points,
                                      int maxLevel,
                                      int divideSize,
                                      int numTrials,
                                      DistanceMeasure distanceMeasure) {
        rss = 0;
        if(maxLevel < 1)
            throw new RuntimeException("Level should be >= 1");
        this.rootNode = cluster(points, 0, maxLevel, divideSize, numTrials, distanceMeasure);
        rss /= points.size();
    }


    public double getClusteringError() {
        return rss;
    }


    private TreeNode<List<ItemModel>> cluster(List<DocumentVector> points,
                                              int level,
                                              int maxLevel,
                                              int divideSize,
                                              int numTrials,
                                              DistanceMeasure distanceMeasure) {
        TreeNode<List<ItemModel>> clusterNode = new TreeNode<>();
        if((level >= maxLevel) || (points.size() < divideSize))
            return clusterNode;

        KMeansPlusPlusClusterer<DocumentVector> kMeansPP1 = new KMeansPlusPlusClusterer<>(divideSize, -1, distanceMeasure);
        MultiKMeansPlusPlusClusterer<DocumentVector> kMeansPP = new MultiKMeansPlusPlusClusterer(kMeansPP1, numTrials);
        List<CentroidCluster<DocumentVector>> centroids = kMeansPP.cluster(points);
        for(CentroidCluster<DocumentVector> centroid : centroids) {

            List<DocumentVector> centroidPoints = centroid.getPoints();
            List<ItemModel> clusterItems = new ArrayList<>(centroidPoints.size());

            // compute rss for centroid
            for(DocumentVector point : centroidPoints) {
                double dist = kMeansPP.getDistanceMeasure().compute(point.getPoint(), centroid.getCenter().getPoint());
                rss += dist*dist;
                NewsDocument doc = point.getDocument();
                ItemModel clusterItem = new ItemModel(doc.getTitle(), doc.getSource(), doc.getDate(), dist);
                clusterItems.add(clusterItem);
            }

            TreeNode<List<ItemModel>> subClusterNode = cluster(centroidPoints, level + 1, maxLevel, divideSize, numTrials, distanceMeasure);
            subClusterNode.setValue(clusterItems);
            clusterNode.add(subClusterNode);
        }

        return clusterNode;
    }

    public TreeNode<List<ItemModel>> getRootNode() {
        return rootNode;
    }
}
