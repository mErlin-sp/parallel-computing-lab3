import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class WordCountTask extends RecursiveTask<Map<String, Integer>> {
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