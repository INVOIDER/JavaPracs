import java.util.Scanner;
import java.util.concurrent.*;

public class SquareGenerator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExecutorService executor = Executors.newSingleThreadExecutor();

        while (true) {
            System.out.print("Введите число (или 'exit' для выхода): ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                int number = Integer.parseInt(input);
                Future<Integer> futureResult = executor.submit(() -> performCalculation(number));

                // Пока выполняется задача, пользователь может отправить новый запрос
                while (!futureResult.isDone()) {
                    System.out.println("Выполняется обработка запроса. Вы можете отправить новый запрос.");
//                    TimeUnit.SECONDS.sleep(1);
                    int delay = ThreadLocalRandom.current().nextInt(1000, 5000);
                    Thread.sleep(delay);
                }

                // Получение результата и вывод на экран
                int result = futureResult.get();
                System.out.println("Результат: " + result);
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод. Попробуйте снова.");
            } catch (Exception e) {
                System.out.println("Произошла ошибка при выполнении запроса: " + e.getMessage());
            }
        }

        executor.shutdown();
        scanner.close();
    }

    private static int performCalculation(int number) throws InterruptedException {
        System.out.println("Запрос принят. Выполняется обработка запроса...");

        // Имитация обработки запроса с задержкой от 1 до 5 секунд
        int delay = (int) (Math.random() * 5) + 1;
        TimeUnit.SECONDS.sleep(delay);

        int result = number * number;
        System.out.println("Обработка запроса завершена.");

        return result;
    }
}