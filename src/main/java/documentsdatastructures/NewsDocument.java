package documentsdatastructures;

import documentpreparing.DocumentPreparer;
import lombok.Getter;
import org.apache.commons.collections4.MultiSet;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import storage.sourceparsers.SourceParser;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class NewsDocument {

    String title;
    MultiSet<String> wordsOccurrences;

    @Getter
    String content;

    double vectorLength;

    public NewsDocument(String title, String url) throws IOException {
        this.title = title;
        Document doc = Jsoup.connect(url).get();
    }

    public NewsDocument(File newsFile, SourceParser sourceParser) throws IOException {
        String newsStr = FileUtils.readFileToString(newsFile);
        int newLineInd = newsStr.indexOf('\n');
        String title = newsStr.substring(0, newLineInd);
        String content = newsStr.substring(newLineInd + 1);
        loadNewsDocument(title, Jsoup.parse(content), sourceParser);
    }

    void loadNewsDocument(String title, Document doc, SourceParser sourceParser) {
        this.title = title;
        doc.select("a").remove();
        this.content = sourceParser.getContent(doc);
        wordsOccurrences = DocumentPreparer.parseContent(content + "\n" + title);

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
