package documentprocessing.datastructures;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ClusteringResult {
    List<ClusterModel> clusters;
    double rss;

    public ClusteringResult() {
        clusters = new LinkedList<>();
    }

    /*public void export(String filename) throws IOException, ParserConfigurationException {
        NumberFormat formatter = new DecimalFormat("#0.00");

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("clusters");
        rootElement.setAttribute("rss", formatter.format(rss));
        doc.appendChild(rootElement);


        for(ClusterModel clusterModel : clusters) {
            Element performClustering = doc.createElement("performClustering");

            Element name = doc.createElement("name");
            name.setTextContent(clusterModel.getName());

            for(ItemModel item : performClustering.getItemModels())
                pw.println("        <item dist = " + formatter.format(item.getDistance()) + ">" + item.getNewsDocument().getTitle() + "</item>");
        }

    }*/
}
