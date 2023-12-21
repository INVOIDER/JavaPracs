import io.reactivex.rxjava3.core.Observable;

public class Second3 {
    public static void main(String[] args) {
        // Создаем поток из 10 случайных чисел
        Observable<Integer> randomNumbersStream = generateRandomNumbers(10);

        // Создаем новый поток, пропуская первые три элемента
        Observable<Integer> filteredStream = randomNumbersStream.skip(3);

        // Подписываемся на новый поток и выводим элементы
        filteredStream.subscribe(
                item -> System.out.print(item + " "),
                Throwable::printStackTrace,
                () -> System.out.println("\nОбработка завершена")
        );
    }

    // Генерирует поток из случайных чисел
    private static Observable<Integer> generateRandomNumbers(int count) {
        return Observable.range(1, count)
                .map(i -> (int) (Math.random() * 100)); // Генерирует случайное число от 0 до 99
    }
}