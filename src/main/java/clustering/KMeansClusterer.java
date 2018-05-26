package clustering;

import documentprocessing.datastructures.ClusterModel;
import documentprocessing.datastructures.ClusteringResult;
import documentprocessing.datastructures.ItemModel;
import documentsdatastructures.DocumentVector;
import documentsdatastructures.NewsDocument;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class KMeansClusterer {
    private DistanceMeasure distanceMeasure;

    private final EuclideanDistance euclideanDistance;

    Comparator<ItemModel> clusterResultItemComparator;

    public KMeansClusterer(DistanceMeasure distanceMeasure) {
        this.distanceMeasure = distanceMeasure;
        this.euclideanDistance = new EuclideanDistance();

        clusterResultItemComparator = (o1, o2) -> {
            if(o1.getDistance() > o2.getDistance())
                return 1;
            else if (o1.getDistance() < o2.getDistance())
                return -1;
            else
                return 0;
        };
    }


    public ClusteringResult performClustering(List<DocumentVector> points, int clustersSize, int numTrials,
                                              int titleNameSize, Map<String, Integer> termsSpaceMap,
                                              Map<String, String> termsToWords)  {
        List<ClusterModel> clusters = new ArrayList<>(clustersSize);

        KMeansPlusPlusClusterer<DocumentVector> kMeansPPSingle = new KMeansPlusPlusClusterer<DocumentVector>(clustersSize, -1, distanceMeasure);
        MultiKMeansPlusPlusClusterer<DocumentVector> kMeansMulti = new MultiKMeansPlusPlusClusterer<DocumentVector>(kMeansPPSingle, numTrials);

        List<CentroidCluster<DocumentVector>> centroids = kMeansMulti.cluster(points);

        double rss = 0;


        for(CentroidCluster<DocumentVector> centroidCluster : centroids) {
            List<DocumentVector> centroidPoints = centroidCluster.getPoints();
            double[] centroid = centroidCluster.getCenter().getPoint();

            ArrayList<ItemModel> docs = new ArrayList<>(centroidPoints.size());

            for(DocumentVector docVec : centroidPoints) {
                double[] point = docVec.getPoint();
                double dist = distanceMeasure.compute(centroid, point);
                NewsDocument doc = docVec.getDocument();
                ItemModel clusterItem = new ItemModel(doc.getTitle(), doc.getSource(), doc.getDate(), dist, doc.getUrl());

                docs.add(clusterItem);

                double euclidDist = distanceMeasure.compute(centroid, point);
                rss += euclidDist;// * euclidDist;
            }

            Collections.sort(docs, clusterResultItemComparator);

            ClusterModel model = new ClusterModel();
            model.setItemModels(docs);
            model.setName(generateName(centroid, termsSpaceMap, termsToWords, titleNameSize));
            clusters.add(model);
        }

        return new ClusteringResult(clusters, rss);
    }


    private String generateName(double[] centroid, Map<String, Integer> termsSpaceMap, Map<String, String> termsToWords, int titleNameSize) {
        PriorityQueue<String> termsTop = new PriorityQueue<>((o1, o2) -> {
            int i1 = termsSpaceMap.get(o1);
            int i2 = termsSpaceMap.get(o2);

            return Double.compare(centroid[i2], centroid[i1]);
        });

        for(String term : termsSpaceMap.keySet()) {
            termsTop.add(term);
        }

        NumberFormat formatter = new DecimalFormat("#0.00");

        StringBuilder stringBuilder = new StringBuilder();


        //.append("[").append(formatter.format(centroid[termsSpaceMap.get(term)])).append("]");
        for(int i = 0; i < titleNameSize; i++) {
            String term = termsTop.poll();
            String word = termsToWords.get(term);
            stringBuilder.append(" ").append(word);
            //.append("[").append(formatter.format(centroid[termsSpaceMap.get(term)])).append("]");
        }

        return stringBuilder.toString();
    }
}
