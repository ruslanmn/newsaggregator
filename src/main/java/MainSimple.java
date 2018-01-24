import documentsdatastructures.DocumentVector;
import treedatastructures.DocumentClusterTree;
import treedatastructures.TreeNode;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainSimple {

    private static final int DIVIDE_SIZE = 20;
    private static final int MAX_LEVEL = 1;
/*
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        List<NewsDocument> newsDocuments = new LinkedList<>();
        //newsDocuments.addAll(RssParser.parseByUrl("https://lenta.ru/rss/news"));
        newsDocuments.addAll(RssParser.parseByUrl("https://meduza.io/rss/podcasts/meduza-v-kurse"));
        newsDocuments.addAll(RssParser.parseByUrl("http://fakty.ua/rss_feed/all"));
        newsDocuments.addAll(RssParser.parseByUrl("https://hi-news.ru/feed"));
        newsDocuments.addAll(RssParser.parseByUrl("http://www.mk.ru/rss/index.xml"));

        NewsVectorizer nv = new NewsVectorizer();
        for (NewsDocument d : newsDocuments) {
            nv.add(d);
        }

        FileWriter fw = new FileWriter("news_clusters.txt");

        List<DocumentVector> vecs = nv.vectorize();
        TreeNode<List<DocumentVector>> rootNode = DivisiveClusterer.cluster(vecs, MAX_LEVEL, DIVIDE_SIZE);
        DocumentClusterTree taggedClusters = tag(rootNode);

        draw(taggedClusters);

        TestForm tf = new TestForm(taggedClusters);
        tf.setVisible(true);
    }

    private static void draw(DocumentClusterTree taggedClusters) {
    }

    private static DocumentClusterTree tag(TreeNode<List<DocumentVector>> rootNode) {
        List<TreeNode<List<DocumentVector>>> children = rootNode.getChildren();
        List<TreeNode<List<DocumentVector>>> clusters = new ArrayList<>(children.size());
        DocumentClusterTree rootDCT = new DocumentClusterTree(children.size());
        List<DocumentClusterTree> dcts = new ArrayList<>(children.size());
        formNextDocumentClusterTrees(children, dcts, clusters, rootDCT);

        while(!clusters.isEmpty()) {
            List<TreeNode<List<DocumentVector>>> nextLevel = new ArrayList<>(clusters.size() * DIVIDE_SIZE);
            List<DocumentClusterTree> nextDCTs = new ArrayList<>(dcts.size() * DIVIDE_SIZE);


            int i = 0;
            for(TreeNode<List<DocumentVector>> cluster : clusters) {
                DocumentClusterTree parentDCT = dcts.get(i);
                children = cluster.getChildren();
                formNextDocumentClusterTrees(children, nextDCTs, nextLevel, parentDCT);
                i++;
            }
            List<PriorityQueue<String>> clustersTags = ClusterTagger.tag(toDocuments(clusters), MutualInformationFactory.getInstance());

            i = 0;
            if(dcts.size() != clustersTags.size())
                return null;
            for(PriorityQueue<String> clusterTags : clustersTags) {
                DocumentClusterTree dct = dcts.get(i);
                dct.setTags(clusterTags);
                i++;
            }

            clusters = nextLevel;
            for(DocumentClusterTree dct : dcts)
                if(dct.getTags() == null)
                    System.out.print("whoaps");
            dcts = nextDCTs;
        }

        return rootDCT;
    }*/

    /*
        добавляет детей в новый уровень кластеров
        формирует для детей DCT и добавляет их в родительский DCT
     */
    private static void formNextDocumentClusterTrees(List<TreeNode<List<DocumentVector>>> children,
                                                     List<DocumentClusterTree> nextDCTs,
                                                     List<TreeNode<List<DocumentVector>>> nextLevel,
                                                     DocumentClusterTree parentDCT) {
        for(TreeNode<List<DocumentVector>> child : children) {
            int size = child.getChildren().size();
            DocumentClusterTree dct;
            if(size == 0) {
                dct = new DocumentClusterTree(child.getValue().stream()
                        .map(docVec -> docVec.getDocument())
                        .collect(Collectors.toList()));
            } else {
                dct = new DocumentClusterTree(size);
            }
            nextDCTs.add(dct);
            nextLevel.add(child);
            parentDCT.add(dct);
        }
    }

    private static List<List<Set<String>>> toDocuments(List<TreeNode<List<DocumentVector>>> clusters) {
        return clusters.stream().map(cluster -> cluster.getValue().stream().map(
                documentVector -> documentVector.getDocument().getWords()).collect(Collectors.toList()))
        .collect(Collectors.toList());
    }


}





/*
        KMeansPlusPlusClusterer<DocumentVector> kMeansPP = new KMeansPlusPlusClusterer<>(12);
        List<CentroidCluster<DocumentVector>> centroids = kMeansPP.cluster(vecs);
        List<List<Set<String>>> documentClusters = centroids.stream()
                .map(centroid -> centroid.getPoints().stream().map(documentVector ->
                        documentVector.getDocument().getWords())
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
        List<List<NewsDocument>> newsDocs = new LinkedList<>();/*
        List<List<Set<String>>> documentClusters = new LinkedList<>();
        for(CentroidCluster<DocumentVector> centroid : centroids) {
            for(DocumentVector dv : centroid.getPoints()) {
                newsDocs.add(Arrays.asList(dv.getDocument()));
                documentClusters.add(Arrays.asList(dv.getDocument().getWords()));
            }
        }
        List<List<List<String>>> tags = ClusterTagger.tag(documentClusters, 10, MutualInformationFactory.getInstance());
        for(int i = 0; i < tags.size(); i++) {
            List<NewsDocument> clusterDocs = newsDocs.get(i);
            for(int j = 0; j < clusterDocs.size(); j++) {
                NewsDocument newsDocument = clusterDocs.get(j);
                System.out.println(newsDocument.getTitle());
                List<String> docTags = tags.get(i).get(j);
                for(String tag : docTags)
                    System.out.print(tag + " ");
                System.out.println();
                System.out.println();
            }
        }*/
