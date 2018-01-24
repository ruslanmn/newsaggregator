package clustering;

import treedatastructures.TreeNode;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;

import java.util.List;

public class DivisiveClusterer<T extends Clusterable> {

    double rss;
    private TreeNode<List<T>> rootNode;


    public void cluster(List<T> points,
                                      int maxLevel,
                                      int divideSize) {
        rss = 0;
        this.rootNode = cluster(points, 0, maxLevel, divideSize);
        rss /= points.size();
    }


    public double getClusteringError() {
        return rss;
    }


    private TreeNode<List<T>> cluster(List<T> points,
                                         int level,
                                         int maxLevel,
                                         int divideSize) {
        TreeNode<List<T>> clusterNode = new TreeNode<>(points);
        if((level >= maxLevel) || (points.size() < divideSize))
            return clusterNode;

        KMeansPlusPlusClusterer<T> kMeansPP = new KMeansPlusPlusClusterer<>(divideSize);
        List<CentroidCluster<T>> centroids = kMeansPP.cluster(points);
        for(CentroidCluster<T> centroid : centroids) {

            List<T> centroidPoints = centroid.getPoints();

            // compute rss for centroid
            for(T point : centroidPoints) {
                double dist = kMeansPP.getDistanceMeasure().compute(point.getPoint(), centroid.getCenter().getPoint());
                rss += dist*dist;
            }

            TreeNode<List<T>> subClusterNode = cluster(centroidPoints,
                    level + 1, maxLevel, divideSize);
            clusterNode.add(subClusterNode);
        }

        return clusterNode;
    }

    public TreeNode<List<T>> getRootNode() {
        return rootNode;
    }
}
