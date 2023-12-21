import io.reactivex.rxjava3.core.Observable;

public class Second2 {
    public static void main(String[] args) {
        int count = 1000; // Количество элементов

        // Создаем два потока с указанным количеством элементов
        Observable<Character> lettersStream = generateRandomLetters(count);
        Observable<Integer> numbersStream = generateRandomNumbers(count);

        // Объединяем элементы из обоих потоков
        Observable<String> combinedStream = Observable.zip(
                lettersStream,
                numbersStream,
                (letter, number) -> letter + number.toString() // Преобразуем число в строку
        );

        // Подписываемся на объединенный поток и выводим элементы
        combinedStream.subscribe(
                item -> System.out.print(item + " "),
                Throwable::printStackTrace,
                () -> System.out.println("\nОбъединение завершено")
        );
    }

    // Генерирует случайные буквы A, B, C
    private static Observable<Character> generateRandomLetters(int count) {
        return Observable.range(0, count)
                .map(i -> (char) ('A' + Math.random() * 3));
    }

    // Генерирует случайные цифры 1, 2, 3
    private static Observable<Integer> generateRandomNumbers(int count) {
        return Observable.range(0, count)
                .map(i -> (int) (1 + Math.random() * 3));
    }
}