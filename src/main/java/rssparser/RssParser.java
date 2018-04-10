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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

public class RssParser {

    private static RssParser rssParser = new RssParser();

    private final DocumentBuilderFactory dbf;

    public RssParser() {
        dbf = DocumentBuilderFactory.newInstance();
    }

    public static RssParser getInstance() {
        return rssParser;
    }


    public List<RawNewsDocument> parseRawDocumentsByUrl(String rssUrl) throws IOException, ParserConfigurationException, SAXException, ParseException {
        List<RawNewsDocument> rawNewsDocuments = rssParser.parseRawDocuments(rssUrl);
        return rawNewsDocuments;
    }


    public List<RawNewsDocument> parseRawDocuments(String rssFeedUrl) throws IOException, SAXException, ParserConfigurationException, ParseException {
        Document doc = Jsoup.connect(rssFeedUrl).get();

        Elements items = doc.select("item");
        List<RawNewsDocument> rawNewsDocuments = new LinkedList<>();

        for(Element item : items) {
            try {
                String title = item.getElementsByTag("title").first().text();
                String link = item.getElementsByTag("link").first().text();
                String date = convertDate(item.getElementsByTag("pubDate").first().text());
                rawNewsDocuments.add(new RawNewsDocument(title, link, date));
            } catch (NullPointerException e) {
                System.out.println(item.toString());
            }
        }

        return rawNewsDocuments;
    }

    private String convertDate(String dateStr) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        Date date = formatter.parse(dateStr);
        DateFormat outFormat = new SimpleDateFormat("dd MM yyyy HH:mm");
        outFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return outFormat.format(date);
    }

}
