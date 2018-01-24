package rssparser;

import documentsdatastructures.NewsDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import storage.RawNewsDocument;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

    public List<NewsDocument> parseNewsDocumentsByUrl(String rssUrl) throws IOException, ParserConfigurationException, SAXException {
        URL url = new URL(rssUrl);
        InputStream rssFeed = url.openStream();
        List<NewsDocument> newsDocuments = rssParser.parse(rssFeed);
        rssFeed.close();
        return newsDocuments;
    }

    public List<RawNewsDocument> parseRawDocumentsByUrl(String rssUrl) throws IOException, ParserConfigurationException, SAXException {
        URL url = new URL(rssUrl);
        InputStream rssFeed = url.openStream();
        List<RawNewsDocument> rawNewsDocuments = rssParser.parseRawDocuments(rssFeed);
        rssFeed.close();
        return rawNewsDocuments;
    }


    public List<NewsDocument> parse(InputStream rssFeed) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document doc = documentBuilder.parse(rssFeed);
        Element rssRoot = (Element) doc.getElementsByTagName("rss").item(0);
        Element channelRoot = (Element) rssRoot.getElementsByTagName("channel").item(0);
        NodeList newsList = channelRoot.getElementsByTagName("item");

        List<NewsDocument> newsDocuments = new LinkedList<>();
        for(int i = 0; i < newsList.getLength(); i++) {
            Element news = (Element) newsList.item(i);
            String title = news.getElementsByTagName("title").item(0).getTextContent();
            String link = news.getElementsByTagName("link").item(0).getTextContent();
            newsDocuments.add(new NewsDocument(title, link));
        }

        return newsDocuments;
    }

    public List<RawNewsDocument> parseRawDocuments(InputStream rssFeed) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document doc = documentBuilder.parse(rssFeed);
        Element rssRoot = (Element) doc.getElementsByTagName("rss").item(0);
        Element channelRoot = (Element) rssRoot.getElementsByTagName("channel").item(0);
        NodeList newsList = channelRoot.getElementsByTagName("item");

        List<RawNewsDocument> rawNewsDocuments = new LinkedList<>();
        for(int i = 0; i < newsList.getLength(); i++) {
            Element news = (Element) newsList.item(i);
            String title = news.getElementsByTagName("title").item(0).getTextContent();
            String link = news.getElementsByTagName("link").item(0).getTextContent();
            rawNewsDocuments.add(new RawNewsDocument(title, link));
        }

        return rawNewsDocuments;
    }

}
