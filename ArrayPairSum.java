import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class ArrayPairSum {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Введення даних від користувача
        System.out.println("Введіть кількість елементів у масиві:");
        int n = scanner.nextInt();
        System.out.println("Введіть мінімальне значення:");
        int min = scanner.nextInt();
        System.out.println("Введіть максимальне значення:");
        int max = scanner.nextInt();

        // Генерація випадкового масиву
        int[] array = generateRandomArray(n, min, max);
        System.out.println("Згенерований масив: " + Arrays.toString(array));

        // Використання Fork/Join Framework з підходом Work Stealing
        long startTimeStealing = System.nanoTime();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        PairSumTask task = new PairSumTask(array, 0, array.length);
        int resultStealing = forkJoinPool.invoke(task);
        long endTimeStealing = System.nanoTime();

        // Використання ExecutorService для підходу Work Dealing
        long startTimeDealing = System.nanoTime();
        int resultDealing = calculateWithWorkDealing(array);
        long endTimeDealing = System.nanoTime();

        // Виведення результатів
        System.out.println("Результат роботи Work Stealing: " + resultStealing);
        System.out.println("Час роботи (Work Stealing): " + (endTimeStealing - startTimeStealing) / 1_000_000 + " ms");

        System.out.println("Результат роботи Work Dealing: " + resultDealing);
        System.out.println("Час роботи (Work Dealing): " + (endTimeDealing - startTimeDealing) / 1_000_000 + " ms");
    }


    private static int[] generateRandomArray(int n, int min, int max) {
        Random random = new Random();
        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = random.nextInt(max - min + 1) + min;
        }
        return array;
    }

    // Задача Fork/Join
    static class PairSumTask extends RecursiveTask<Integer> {
        private final int[] array;
        private final int start;
        private final int end;
        private static final int THRESHOLD = 100;

        PairSumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= THRESHOLD) {
                return calculatePairSum(array, start, end);
            } else {
                int mid = (start + end) / 2;
                PairSumTask leftTask = new PairSumTask(array, start, mid);
                PairSumTask rightTask = new PairSumTask(array, mid, end);

                leftTask.fork();
                int rightResult = rightTask.compute();
                int leftResult = leftTask.join();

                return leftResult + rightResult;
            }
        }
    }

    private static int calculatePairSum(int[] array, int start, int end) {
        int sum = 0;
        for (int i = start; i < end; i++) {
            sum += array[i];
        }
        return sum;
    }

    // Реалізація Work Dealing за допомогою ExecutorService
    private static int calculateWithWorkDealing(int[] array) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int chunkSize = (int) Math.ceil((double) array.length / numThreads);

        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < array.length; i += chunkSize) {
            int start = i;
            int end = Math.min(i + chunkSize, array.length);
            futures.add(executor.submit(() -> calculatePairSum(array, start, end)));
        }

        int totalSum = 0;
        for (Future<Integer> future : futures) {
            try {
                totalSum += future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        return totalSum;
    }
}
