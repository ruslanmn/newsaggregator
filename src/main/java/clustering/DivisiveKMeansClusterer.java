package clustering;

import documentprocessing.ClusterResultItem;
import documentsdatastructures.DocumentVector;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import treedatastructures.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class DivisiveKMeansClusterer {

    double rss;
    private TreeNode<List<ClusterResultItem>> rootNode;

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


    private TreeNode<List<ClusterResultItem>> cluster(List<DocumentVector> points,
                                         int level,
                                         int maxLevel,
                                         int divideSize,
                                         int numTrials,
                                         DistanceMeasure distanceMeasure) {
        TreeNode<List<ClusterResultItem>> clusterNode = new TreeNode<>();
        if((level >= maxLevel) || (points.size() < divideSize))
            return clusterNode;

        KMeansPlusPlusClusterer<DocumentVector> kMeansPP1 = new KMeansPlusPlusClusterer<>(divideSize, -1, distanceMeasure);
        MultiKMeansPlusPlusClusterer kMeansPP = new MultiKMeansPlusPlusClusterer(kMeansPP1, numTrials);
        List<CentroidCluster<DocumentVector>> centroids = kMeansPP.cluster(points);
        for(CentroidCluster<DocumentVector> centroid : centroids) {

            List<DocumentVector> centroidPoints = centroid.getPoints();
            List<ClusterResultItem> clusterResultItems = new ArrayList<>(centroidPoints.size());

            // compute rss for centroid
            for(DocumentVector point : centroidPoints) {
                double dist = kMeansPP.getDistanceMeasure().compute(point.getPoint(), centroid.getCenter().getPoint());
                rss += dist*dist;

                ClusterResultItem clusterResultItem = new ClusterResultItem(point.getDocument(), dist);
                clusterResultItems.add(clusterResultItem);
            }

            TreeNode<List<ClusterResultItem>> subClusterNode = cluster(centroidPoints, level + 1, maxLevel, divideSize, numTrials, distanceMeasure);
            subClusterNode.setValue(clusterResultItems);
            clusterNode.add(subClusterNode);
        }

        return clusterNode;
    }

    public TreeNode<List<ClusterResultItem>> getRootNode() {
        return rootNode;
    }
}
