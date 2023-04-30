import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    private static final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    public static void main(String[] args) throws IOException {
        List<String> documents = Arrays.asList(
                Files.lines(Path.of("res/bible.txt")).reduce((s, s1) -> s + s1)
                        .orElse("The quick brown fox jumps over the lazy dog."),
                Files.lines(Path.of("res/world192.txt")).reduce((s, s1) -> s + s1)
                        .orElse("The quick brown cat jumps over the lazy dog.")
        );
        Set<String> commonWords = findCommonWords(documents);
        System.out.println(commonWords);
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

    private static class WordCountTask extends RecursiveTask<Map<String, Integer>> {
        private final List<String> documents;
        private final int start;
        private final int end;

        public WordCountTask(List<String> documents, int start, int end) {
            this.documents = documents;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Map<String, Integer> compute() {
            if (end - start <= 1) {
                return countWords(documents.get(start));
            } else {
                int middle = start + (end - start) / 2;
                WordCountTask leftTask = new WordCountTask(documents, start, middle);
                WordCountTask rightTask = new WordCountTask(documents, middle, end);
                leftTask.fork();
                Map<String, Integer> rightResult = rightTask.compute();
                Map<String, Integer> leftResult = leftTask.join();
                return mergeResults(leftResult, rightResult);
            }
        }

        private Map<String, Integer> countWords(String document) {
            Map<String, Integer> wordCounts = new HashMap<>();
            String[] words = document.split("\\W+");
            for (String word : words) {
                wordCounts.merge(word.toLowerCase(), 1, Integer::sum);
            }
            return wordCounts;
        }

        private Map<String, Integer> mergeResults(Map<String, Integer> leftResult, Map<String, Integer> rightResult) {
            Map<String, Integer> mergedResult = new HashMap<>(leftResult);
            rightResult.forEach((key, value) -> mergedResult.merge(key, value, Integer::sum));
            return mergedResult;
        }
    }
}
