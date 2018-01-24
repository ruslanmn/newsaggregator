package storage;

import org.apache.commons.io.FileUtils;
import utils.URLLoader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class RawNewsDocument {
    String title;
    String link;

    public RawNewsDocument(String title, String link) throws IOException {
        this.title = title;
        this.link = link;
    }

    public void loadAndStore(String dir) throws IOException {
        String docFilePath = dir + "/" + title + ".txt";

        if(! new File(docFilePath).isFile()) {
            String content = URLLoader.loadContentByUrl(link);
            FileUtils.writeStringToFile(new File(docFilePath), content, Charset.defaultCharset());
            System.out.println(title + " loaded and saved");
        }
    }
}
