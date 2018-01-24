package storage;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ConfigLoader {

    public static final String APP_DIR = "news_app_resources";
    public static final String DOCUMENTS_DIR_PATH = APP_DIR + "/documents";
    public static final String URLS_FILE_PATH = APP_DIR + "/urls.list";

    private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    public static List<String> loadUrls() throws IOException {
        return Files.readAllLines(Paths.get(URLS_FILE_PATH));
    }
}
