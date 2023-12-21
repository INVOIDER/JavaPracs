import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserFriend {
    private int userId;
    private int friendId;

    public UserFriend(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public int getUserId() {
        return userId;
    }

    public int getFriendId() {
        return friendId;
    }

    public static Observable<UserFriend> getFriends(int userId) {
        // В этом примере, мы просто создадим случайные пары userId и friendId.
        List<UserFriend> friends = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 10; i++) { // Добавим 10 случайных друзей
            int friendId = random.nextInt(100); // Предположим, что userId и friendId варьируются от 0 до 99
            friends.add(new UserFriend(userId, friendId));
        }

        return Observable.fromIterable(friends).subscribeOn(Schedulers.io());
    }

    public static void main(String[] args) {
        Random random = new Random();
        Integer[] userIds = new Integer[5]; // Допустим, у нас есть 5 случайных userId

        for (int i = 0; i < 5; i++) {
            userIds[i] = random.nextInt(100);
        }

        Observable.fromArray(userIds)
                .flatMap(userId -> getFriends(userId)) // Используем явный тип данных Integer
                .subscribe(userFriend -> {
                    System.out.println("User ID: " + userFriend.getUserId() +
                            ", Friend ID: " + userFriend.getFriendId());
                });
        try {
            Thread.sleep(2000); // Подождем 2 секунды, чтобы убедиться, что обработка успела завершиться
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    
}