package storage;

import org.apache.commons.io.FileUtils;
import utils.URLLoader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class RawNewsDocument {
    String title;
    String link;
    String date;

    public RawNewsDocument(String title, String link, String date) throws IOException {
        this.title = title.replaceAll("\n", " ");
        this.date = date;
        this.link = link;
    }

    public void loadAndStore(String dir, String name) throws IOException {
        String docFilePath = dir + "/" + name + ".txt";

        if(! new File(docFilePath).isFile()) {
            String content = URLLoader.loadContentByUrl(link);
            FileUtils.writeStringToFile(new File(docFilePath), link + '\n' +
                    title + '\n' +
                    date + '\n' +
                    content, Charset.defaultCharset());
            System.out.println(title + " loaded and saved");
        }
    }

    public String getTitle() {
        return title;
    }
}
