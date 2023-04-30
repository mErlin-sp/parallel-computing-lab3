import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ForkJoinPool;

public class DocumentSearcher {
    public static void main(String[] args) {
        System.out.println("Java Document Searcher");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter search path: ");
        String searchPath = scanner.nextLine();

        List<String> keywords = new ArrayList<>();
        do {
            System.out.print("Enter keyword to search: ");
            keywords.add(scanner.nextLine());

            System.out.print("Do you wanna add more keywords (Y / N) ? ");
        } while (scanner.nextLine().trim().equals("Y"));

        System.out.println("Start searching documents in path: " + searchPath + " with keywords: " + keywords);

        Map<String, String> documents = loadDocumentsFromDirectory(searchPath);
        try (ForkJoinPool pool = ForkJoinPool.commonPool()) {
            DocumentSearchTask searchTask = new DocumentSearchTask(documents, keywords);
            List<String> results = pool.invoke(searchTask);
            System.out.println("Founded documents: " + results);
        }
    }

    private static Map<String, String> loadDocumentsFromDirectory(String directoryPath) {
        Map<String, String> documents = new HashMap<>();

        try {
            Files.walk(Paths.get(directoryPath)).filter(Files::isRegularFile).filter(path -> path.toString().endsWith(".txt")).forEach(path -> {
                try {
                    String content = Files.readString(path);
                    documents.put(String.valueOf(path), content);
                } catch (IOException e) {
                    System.err.println("Error reading file: " + path);
                }
            });
        } catch (IOException e) {
            System.err.println("Error walking directory: " + directoryPath);
        }

        return documents;
    }

}
