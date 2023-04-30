import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Java Statistical Text Analysis");

        String[] wordList = Files.lines(Path.of("res/big.txt"))
                .flatMap(line -> Stream.of(line.split("\\W+")))
                .filter(word -> !word.isBlank())
                .toArray(String[]::new);

        System.out.println("Word count: " + wordList.length);

//        for (String word : wordList) {
//            System.out.println(word);
//        }

        try (ForkJoinPool commonPool = ForkJoinPool.commonPool()) {
            int[] result = commonPool.invoke(new WordLengthCountTask(wordList));

            double average = Arrays.stream(result).mapToDouble(i -> i).average().orElse(0.0);
            double variance = Arrays.stream(result).mapToDouble(i -> Math.pow(i - average, 2)).average().orElse(0.0);
            double standardDeviation = Math.sqrt(variance);

            System.out.println("Average word length: " + Math.round(average * 100.0) / 100.0);
            System.out.println("Variance: " + Math.round(variance * 100.0) / 100.0);
            System.out.println("Standard deviation: " + Math.round(standardDeviation * 100.0) / 100.0);
        }
    }

}