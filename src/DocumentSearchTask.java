import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class DocumentSearchTask extends RecursiveTask<List<String>> {

    private final Map<String, String> documents;
    private final List<String> keywords;

    public DocumentSearchTask(Map<String, String> documents, List<String> keywords) {
        this.documents = documents;
        this.keywords = keywords;
    }

    @Override
    protected List<String> compute() {
        List<String> results = new ArrayList<>();
        if (documents.size() <= 10) {
            for (Map.Entry<String, String> document : documents.entrySet()) {
                if (isDocumentMatchesKeywords(document.getValue(), keywords)) {
                    results.add(document.getKey());
                }
            }
        } else {
            int mid = documents.size() / 2;
            DocumentSearchTask leftTask =
                    new DocumentSearchTask(documents.entrySet()
                            .stream().skip(0).limit(mid)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), keywords);
            DocumentSearchTask rightTask =
                    new DocumentSearchTask(documents.entrySet()
                            .stream().skip(mid).limit(documents.size())
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), keywords);
            invokeAll(leftTask, rightTask);
            results.addAll(leftTask.join());
            results.addAll(rightTask.join());
        }
        return results;
    }

    private boolean isDocumentMatchesKeywords(String document, List<String> keywords) {
        for (String keyword : keywords) {
            if (document.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
