import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class Main {
    private static final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    public static void main(String[] args) throws IOException {
        System.out.println("Common Words Finder");

        List<String> documents = Arrays.asList(
                Files.lines(Path.of("res/bible.txt")).reduce((s, s1) -> s + s1)
                        .orElse("The quick brown fox jumps over the lazy dog."),
                Files.lines(Path.of("res/world192.txt")).reduce((s, s1) -> s + s1)
                        .orElse("The quick brown cat jumps over the lazy dog.")
        );
        Set<String> commonWords = findCommonWords(documents);
        System.out.println("Founded common words: " + commonWords);
    }

    public static Set<String> findCommonWords(List<String> documents) {
        Map<String, Integer> wordCounts = forkJoinPool.invoke(new WordCountTask(documents, 0, documents.size()));
        Set<String> commonWords = new HashSet<>();
        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            if (entry.getValue() == documents.size()) {
                commonWords.add(entry.getKey());
            }
        }
        return commonWords;
    }

}
