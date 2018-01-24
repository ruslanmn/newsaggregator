package documentprocessing;

import documentsdatastructures.NewsDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@AllArgsConstructor
public class SearchResultItem {
    @Getter
    NewsDocument newsDocument;
    @Getter
    Collection<String> keywords;

    public String getKeywordsString(String delimiter) {
        return String.join(delimiter, keywords);
    }
}
