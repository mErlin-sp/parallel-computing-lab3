import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class WordLengthCountTask extends RecursiveTask<int[]> {
    private final String[] wordList;

    private static final int THRESHOLD = 20;

    public WordLengthCountTask(String[] wordList) {
        this.wordList = wordList;
    }

    @Override
    protected int[] compute() {
        if (wordList.length > THRESHOLD) {
            return ForkJoinTask.invokeAll(createSubtasks())
                    .stream()
                    .map(ForkJoinTask::join)
                    .flatMapToInt(Arrays::stream)
                    .toArray();
        } else {
            return processing(wordList);
        }
    }

    private Collection<WordLengthCountTask> createSubtasks() {
        List<WordLengthCountTask> dividedTasks = new ArrayList<>();
        dividedTasks.add(new WordLengthCountTask(
                Arrays.copyOfRange(wordList, 0, wordList.length / 2)));
        dividedTasks.add(new WordLengthCountTask(
                Arrays.copyOfRange(wordList, wordList.length / 2, wordList.length)));
        return dividedTasks;
    }

    private int[] processing(String[] wordList) {
        return Arrays.stream(wordList).mapToInt(String::length).toArray();
    }
}