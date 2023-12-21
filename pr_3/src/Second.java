import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.Random;

public class Second {
    public static void main(String[] args) {
        // Генерируем поток случайных чисел от 0 до 1000
        Observable<Integer> randomNumbers = Observable
                .range(0, 1000)
                .map(i -> new Random().nextInt(1001)) // Генерируем случайное число от 0 до 1000
                .subscribeOn(Schedulers.computation()); // Выполнение на вычислительном потоке

        // Применяем оператор map для вычисления квадратов чисел
        Observable<Integer> squares = randomNumbers.map(num -> num * num);

        // Подписываемся на поток квадратов и выводим результаты
        squares.subscribe(
                square -> System.out.println("Квадрат числа: " + square),
                throwable -> System.err.println("Ошибка: " + throwable),
                () -> System.out.println("Завершено")
        );

        // При этом, основной поток не должен завершиться раньше, чем поток квадратов.
        try {
            Thread.sleep(2000); // Подождем 2 секунды, чтобы убедиться, что обработка успела завершиться
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}