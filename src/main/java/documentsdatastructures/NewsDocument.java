package documentsdatastructures;

import documentpreparing.DocumentPreparer;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class NewsDocument {

    String title;
    MultiSet<String> wordsOccurrences;

    double vectorLength;

    public NewsDocument(String title, String url) throws IOException {
        this.title = title;
        Document doc = Jsoup.connect(url).get();
    }

    public NewsDocument(File newsFile) throws IOException {
        this(FilenameUtils.getBaseName(newsFile.getName()), Jsoup.parse(FileUtils.readFileToString(newsFile)));
    }

    public NewsDocument(String title, Document doc) {
        this.title = title;
        doc.select("a").remove();
        wordsOccurrences = DocumentPreparer.parseContent(doc.toString() + " " + title);

        long n = 0;

        for(MultiSet.Entry<String> termCount : wordsOccurrences.entrySet()) {
            n += termCount.getCount() * termCount.getCount();
        }

        vectorLength = Math.sqrt(n);

        if(wordsOccurrences.contains("CVf0W")) {
            System.out.println("Something is wrong");
        }
    }


    public Set<String> getWords() {
        return wordsOccurrences.uniqueSet();
    }

    public MultiSet<String> getWordsOccurrences() {
        return wordsOccurrences;
    }


    public String getTitle() {
        return title;
    }

    public int getTermFrequency(String term) {
        return wordsOccurrences.getCount(term);
    }

    public double getNormolizedTermWeight(String term) {
        return getTermFrequency(term) / vectorLength;
    }
}
