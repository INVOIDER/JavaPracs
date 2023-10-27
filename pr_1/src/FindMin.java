import java.util.Arrays;
import java.util.concurrent.*;

public class FindMin {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int[] arr = new int[100000];

        long startTime = System.nanoTime();
        ArrGeneration(arr);
        long endTime = System.nanoTime();
        System.out.println("Время генерации массива: " + (endTime - startTime) / 1000000 + " мс\n");
        System.out.println("Массив: " + Arrays.toString(Arrays.copyOfRange(arr, 0, 10)) + " ... " +
                Arrays.toString(Arrays.copyOfRange(arr, arr.length - 10, arr.length)));


        startTime = System.nanoTime();
        System.out.println("Однопоточный вывод: " + FindMin(arr));
        endTime = System.nanoTime();
        System.out.println("Время выполнения: " + (endTime - startTime) / 1000000 + " мс\n");


        startTime = System.nanoTime();
        System.out.println("Многопоточный вывод: " + ThreadMin(arr));
        endTime = System.nanoTime();
        System.out.println("Время выполнения: " + (endTime - startTime) / 1000000 + " мс\n");

        startTime = System.nanoTime();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkMinTask task = new ForkMinTask(arr, 0, arr.length);
        int result = forkJoinPool.invoke(task);
        forkJoinPool.shutdown();
        System.out.println("Многопоточный вывод с помощью Fork/Join: " + result);
        endTime = System.nanoTime();
        System.out.println("Время выполнения: " + (endTime - startTime) / 1000000 + " мс\n");
    }

    //Функция генерации массива
    public static void ArrGeneration(int[] arr) throws InterruptedException {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * 10000);
            Thread.sleep(1);
        }
    }
    //Функция поиска минимального числа
    public static int FindMin(int[] arr) throws InterruptedException {
        int min = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
                Thread.sleep(1);
            }
        }
        return min;
    }

    public static int ThreadMin(int[] arr) throws ExecutionException, InterruptedException {
        Callable<Integer> task1 = () -> {
            int[] fst_half = Arrays.copyOfRange(arr, 0, arr.length / 2);
            System.out.println("1я половина массива: " + Arrays.toString(Arrays.copyOfRange(fst_half, 0, 10)) + " ... " +
                    Arrays.toString(Arrays.copyOfRange(fst_half, fst_half.length - 10, fst_half.length)));
            return FindMin(fst_half);
        };

        Callable<Integer> task2 = () -> {
            int[] scnd_half = Arrays.copyOfRange(arr, arr.length / 2, arr.length);
            System.out.println("2я половина массива: " + Arrays.toString(Arrays.copyOfRange(scnd_half, 0, 10)) + " ... " +
                    Arrays.toString(Arrays.copyOfRange(scnd_half, scnd_half.length - 10, scnd_half.length)));
            return FindMin(scnd_half);
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<Integer> future1 = executor.submit(task1);
        Future<Integer> future2 = executor.submit(task2);

        int min1 = future1.get();
        int min2 = future2.get();

        executor.shutdown();

        return Math.min(min1, min2);
    }

    public static class ForkMinTask extends RecursiveTask<Integer> {
        private final int[] arr;
        private final int start;
        private final int end;

        public ForkMinTask(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= 10) {
                try {
                    return FindMin(arr);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                int middle = start + (end - start) / 2;

                ForkMinTask leftTask = new ForkMinTask(arr, start, middle);
                ForkMinTask rightTask = new ForkMinTask(arr, middle, end);

                leftTask.fork();
                int rightResult = rightTask.compute();
                int leftResult = leftTask.join();

                return Math.min(leftResult, rightResult);
            }
            return null;
        }
    }
}