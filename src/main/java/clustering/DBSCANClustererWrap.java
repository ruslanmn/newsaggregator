package clustering;

import documentprocessing.datastructures.ClusterModel;
import documentprocessing.datastructures.ClusteringResult;
import documentprocessing.datastructures.ItemModel;
import documentsdatastructures.DocumentVector;
import documentsdatastructures.NewsDocument;
import org.apache.commons.math3.ml.clustering.*;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.util.ArrayList;
import java.util.List;

public class DBSCANClustererWrap {
    private DistanceMeasure distanceMeasure;

    private final EuclideanDistance euclideanDistance;

    public DBSCANClustererWrap(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
        this.euclideanDistance = new EuclideanDistance();
    }


    public ClusteringResult cluster(List<DocumentVector> points, int clustersSize, int numTrials) {
        List<ClusterModel> clusters = new ArrayList<>(clustersSize);

        DBSCANClusterer<DocumentVector> dbscanClusterer = new DBSCANClusterer<>(1, 5);
        List<Cluster<DocumentVector>> centroids = dbscanClusterer.cluster(points);

        double rss = 0;


        for(Cluster<DocumentVector> centroidCluster : centroids) {
            List<DocumentVector> centroidPoints = centroidCluster.getPoints();
            double[] centroid = centroidPoints.get(0).getPoint();

            ArrayList<ItemModel> docs = new ArrayList<>(centroidPoints.size());

            for(DocumentVector docVec : centroidPoints) {
                double[] point = docVec.getPoint();
                double dist = distanceMeasure.compute(centroid, point);
                NewsDocument doc = docVec.getDocument();
                ItemModel clusterItem = new ItemModel(doc.getTitle(), doc.getSource(), doc.getDate(), dist, doc.getUrl());

                docs.add(clusterItem);

                double euclidDist = euclideanDistance.compute(centroid, point);
                rss += euclidDist * euclidDist;
            }

            ClusterModel model = new ClusterModel();
            model.setItemModels(docs);
            clusters.add(model);
        }

        return new ClusteringResult(clusters, rss);
    }
}
