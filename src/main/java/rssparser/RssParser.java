package rssparser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;
import storage.RawNewsDocument;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class RssParser {

    private static RssParser rssParser = new RssParser();

    private final DocumentBuilderFactory dbf;

    public RssParser() {
        dbf = DocumentBuilderFactory.newInstance();
    }

    public static RssParser getInstance() {
        return rssParser;
    }


    public List<RawNewsDocument> parseRawDocumentsByUrl(String rssUrl) throws IOException, ParserConfigurationException, SAXException {
        List<RawNewsDocument> rawNewsDocuments = rssParser.parseRawDocuments(rssUrl);
        return rawNewsDocuments;
    }


    public List<RawNewsDocument> parseRawDocuments(String rssFeedUrl) throws IOException, SAXException, ParserConfigurationException {
        Document doc = Jsoup.connect(rssFeedUrl).get();

        Elements items = doc.select("item");
        List<RawNewsDocument> rawNewsDocuments = new LinkedList<>();

        for(Element item : items) {
            String title = item.getElementsByTag("title").first().text();
            String link = item.getElementsByTag("link").first().text();
            rawNewsDocuments.add(new RawNewsDocument(title, link));
        }

        return rawNewsDocuments;
    }

}
