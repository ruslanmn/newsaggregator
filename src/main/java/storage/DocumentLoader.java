package storage;

import documentsdatastructures.NewsDocument;
import org.xml.sax.SAXException;
import rssparser.RssParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class DocumentLoader {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        long start = System.nanoTime();
        RssParser rssParser = RssParser.getInstance();
        List<String> urls = ConfigLoader.loadUrls();
        int dirOrder = 0;
        for(String url : urls) {
            try {
                dirOrder++;
                String dir = ConfigLoader.DOCUMENTS_DIR_PATH + "/" + dirOrder;
                System.out.println(dir);
                Files.createDirectories(Paths.get(dir));
                rssParser.parseRawDocumentsByUrl(url).parallelStream().forEach(doc -> {
                    try {
                        if(true)
                            doc.loadAndStore(dir);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println(System.nanoTime() - start);
    }


    public static List<NewsDocument> loadNewsDocuments() throws IOException {
        File[] sources = new File(ConfigLoader.DOCUMENTS_DIR_PATH).listFiles(File::isDirectory);

        List<NewsDocument> newsDocuments = new LinkedList<>();

        for(File source : sources) {
            for(File newsFile : source.listFiles(File::isFile)) {
                newsDocuments.add(new NewsDocument(newsFile));
            }
        }

        return newsDocuments;
    }
}
