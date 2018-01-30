package storage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigLoader {

    public static final String APP_DIR = "news_app_resources";
    public static final String DOCUMENTS_DIR_PATH = APP_DIR + "/documents";
    public static final String URLS_FILE_PATH = APP_DIR + "/sources.xml";
    public static final String NEWS_LIST_FILENAME = "news_list.txt";

    private static final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();


    public static List<Source> loadUrls() throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document xmlSources = documentBuilder.parse(new File(URLS_FILE_PATH));
        Element sourcesElement = (Element) xmlSources.getElementsByTagName("sources").item(0);

        NodeList sourcesNodes = sourcesElement.getElementsByTagName("source");
        List<Source> sources = new ArrayList<>(sourcesNodes.getLength());

        for(int i = 0; i < sourcesNodes.getLength(); i++) {
            Element source = (Element) sourcesNodes.item(i);
            String id = source.getElementsByTagName("id").item(0).getTextContent();
            String url = source.getElementsByTagName("url").item(0).getTextContent();
            sources.add(new Source(id, url));
        }

        return sources;
    }
}
