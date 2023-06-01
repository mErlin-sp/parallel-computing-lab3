import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Stream;

public class TextAnalyser {
    public static void main(String[] args) throws IOException {
        System.out.println("Java Text Analyser");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter text file path: ");
        Path filePath = Path.of(sc.nextLine());
        if (!filePath.toFile().exists()) {
            System.out.println("File does not exist!");
            return;
        }

        long timer = System.currentTimeMillis();

        String[] wordList = Files.lines(filePath)
                .flatMap(line -> Stream.of(line.split("\\W+")))
                .filter(word -> !word.isBlank())
                .toArray(String[]::new);

        System.out.println("Word count: " + wordList.length);

        double averageLength = calculateAverageWordLength(wordList);
        double variance = calculateVariance(wordList, averageLength);
        double standardDeviation = Math.sqrt(variance);

        System.out.println("Execution time: " + (System.currentTimeMillis() - timer) + "\n");

        System.out.println("Average word length: " + averageLength);
        System.out.println("Variance: " + variance);
        System.out.println("Standard deviation: " + standardDeviation);
    }

    public static double calculateAverageWordLength(String[] wordList) {
        int totalLength = 0;
        for (String word : wordList) {
            totalLength += word.length();
        }
        return (double) totalLength / wordList.length;
    }

    public static double calculateVariance(String[] wordList, double averageLength) {
        double sumOfSquaredDifferences = 0;
        for (String word : wordList) {
            double difference = word.length() - averageLength;
            sumOfSquaredDifferences += difference * difference;
        }
        return sumOfSquaredDifferences / wordList.length;
    }

}