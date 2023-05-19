import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class CommonWordsFinder {
    private static final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    public static void main(String[] args) throws IOException {
        System.out.println("Java Common Words Finder");

        List<String> documents = new ArrayList<>();

        Scanner sc = new Scanner(System.in);
        do {
            System.out.print("Enter file path: ");

            Path path = Path.of(sc.nextLine());
            if (path.toFile().exists()) {
                documents.add(Files.lines(path).reduce((s, s1) -> s + s1).orElseThrow());
            } else {
                System.out.println("This file does not exist!");
            }

            System.out.print("Do you want to add one more file (Y / N) ? ");
        } while (sc.nextLine().equals("Y"));

        if (documents.size() < 2) {
            System.out.println("You entered less than 2 files.");
            return;
        }

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
