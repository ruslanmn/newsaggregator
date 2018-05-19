package documentpreparing;

import org.apache.commons.collections4.MultiSet;
import org.apache.commons.collections4.multiset.HashMultiSet;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DocumentPreparer {
    private static final Set<String> forbiddenFilter = new HashSet<>(
            Arrays.asList("nbsp"));

    /*public static boolean verifyChar(char c) {
        return ((c >= 'a') && (c <= 'z')) ||
                ((c >= 'а') && (c <= 'я'));
    }*/


    public static String prepare(String str) {
        str = str.replaceAll("[^a-zA-Zа-яА-Я0-9]", " ");
        str = str.replaceAll("( )+", " ");
        return str;
    }


    public static String prepareWord(String word) {
        /*for(int i = 1; i < word.length(); i++) {
            char c = word.charAt(i);
            if((('A' <= c) && (c <= 'Z')) ||
                    (('А' <= c) && (c <= 'Я')) ||
                    (('0' <= c) && (c <= '9'))) {
                return word;
            }
        }*/

        int i;
        for(i = word.length() - 1; (i > 1) && checkSkipChar(word.charAt(i)); i--);
        return word.substring(0, i + 1).toLowerCase();
    }

    private static boolean checkSkipChar(char c) {
        String charToSkip = "йуеыаоэяию";
        return charToSkip.indexOf(c) != -1;
    }


    public static MultiSet<String> parseContent(String content) {
        MultiSet<String> wordsOccurrences = new HashMultiSet<>();

        content = prepare(Jsoup.clean(content, Whitelist.none()));
        for(String word : content.split("[^a-zA-Zа-яА-Я0-9]")) {
            if(!word.isEmpty()) {
                word = prepareWord(word);
                if(!forbiddenFilter.contains(word)) {
                    wordsOccurrences.add(word);
                }
            }
        }

        return wordsOccurrences;
    }

}
