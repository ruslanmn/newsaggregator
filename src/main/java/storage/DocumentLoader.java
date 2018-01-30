package storage;

import documentsdatastructures.NewsDocument;
import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;
import rssparser.RssParser;
import storage.sourceparsers.SourceParser;
import storage.sourceparsers.SourceParserFactory;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class DocumentLoader {

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        long start = System.nanoTime();
        RssParser rssParser = RssParser.getInstance();
        List<Source> sources = ConfigLoader.loadUrls();
        for(Source source : sources) {
            try {
                String dir = ConfigLoader.DOCUMENTS_DIR_PATH + "/" + source.getId();
                Set<String> titles = loadExistedTitles(dir);
                List<String> newTitles = new LinkedList<>();

                System.out.println(dir);
                Files.createDirectories(Paths.get(dir));

                rssParser.parseRawDocumentsByUrl(source.getUrl()).parallelStream().forEach(new Consumer<RawNewsDocument>() {
                    int startedName = titles.size();

                    @Override
                    public void accept(RawNewsDocument doc) {
                        try {
                            if (!titles.contains(doc.getTitle())) {
                                startedName++;
                                doc.loadAndStore(dir, Integer.toString(startedName));
                                newTitles.add(doc.getTitle());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                FileUtils.writeLines(new File(dir + "/" + ConfigLoader.NEWS_LIST_FILENAME), newTitles, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Set<String> loadExistedTitles(String dir) throws IOException {
        File f = new File(dir);
        if(!f.exists())
            f.mkdir();
        f = new File(dir + "/" + ConfigLoader.NEWS_LIST_FILENAME);
        if(!f.exists())
            f.createNewFile();

        return new HashSet<>(FileUtils.readLines(f));
    }


    public static List<NewsDocument> loadNewsDocuments() throws IOException, ParserConfigurationException, SAXException {
        File[] sources = new File(ConfigLoader.DOCUMENTS_DIR_PATH).listFiles(File::isDirectory);

        List<NewsDocument> newsDocuments = new LinkedList<>();

        for(Source source : ConfigLoader.loadUrls()) {
            SourceParser sourceParser = SourceParserFactory.get(source);
            File sourceFile = new File(ConfigLoader.DOCUMENTS_DIR_PATH + "/" + source.getId());
            for(File newsFile : sourceFile.listFiles(File::isFile)) {
                if(!ConfigLoader.NEWS_LIST_FILENAME.equals(newsFile.getName()))
                    newsDocuments.add(new NewsDocument(newsFile, sourceParser));
            }
        }

        return newsDocuments;
    }
}
