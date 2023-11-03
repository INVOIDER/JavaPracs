package task4;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            Path directory = Paths.get("src/task4/observed");
            DirectoryWatcher directoryWatcher = new DirectoryWatcher(directory);
            Thread watcherThread = new Thread(directoryWatcher);
            watcherThread.start();
        } catch (IOException e) {
            System.err.println("Ошибка при создании WatchService");
            e.printStackTrace();
        }
    }

    public static class DirectoryWatcher implements Runnable {
        private final Path directory;
        private final WatchService watchService;
        private final Map<WatchKey, Path> keys;

        public DirectoryWatcher(Path directory) throws IOException {
            this.directory = directory;
            this.watchService = FileSystems.getDefault().newWatchService();
            this.keys = new HashMap<>();

            // Регистрируем каталог для наблюдения за событиями
            try {
                registerDirectory(directory);
            } catch (IOException e) {
                System.err.println("Ошибка при регистрации каталога для наблюдения");
                e.printStackTrace();
            }
        }

        private void registerDirectory(Path directory) throws IOException {
            WatchKey key = directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
            keys.put(key, directory);
        }

        @Override
        public void run() {
            try {
                // Главный цикл, в котором получаем события от WatchService
                while (true) {
                    WatchKey key = watchService.take();
                    Path dir = keys.get(key);

                    // Обрабатываем события для текущего каталога
                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();

                        // Если создан новый файл
                        if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
                            Path file = dir.resolve((Path) event.context());
                            System.out.println("Новый файл создан: " + file.getFileName());
                        }
                        // Если изменен существующий файл
                        else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                            Path file = dir.resolve((Path) event.context());
                            System.out.println("Файл изменен: " + file.getFileName());
                        }
                        // Если удален файл
                        else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                            Path file = dir.resolve((Path) event.context());
                            try {
                                printFileDetails(file);
                            } catch (IOException | NoSuchAlgorithmException e) {
                                System.err.println("Ошибка при выводе деталей удаленного файла");
                                e.printStackTrace();
                            }
                        }
                    }

                    // Сбрасываем ключ и проверяем наличие следующего события
                    boolean isKeyValid = key.reset();
                    if (!isKeyValid) {
                        keys.remove(key);
                        if (keys.isEmpty()) {
                            // Если больше нет зарегистрированных каталогов для наблюдения, завершаем выполнение
                            break;
                        }
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Получено прерывание, завершение наблюдения за каталогом");
            }
        }

        private void printFileDetails(Path file) throws IOException, NoSuchAlgorithmException {
            BasicFileAttributes attributes = Files.readAttributes(file, BasicFileAttributes.class);
            System.out.println("Удален файл: " + file.getFileName());
            System.out.println("   Размер: " + attributes.size() + " байт");
            System.out.println("   Контрольная сумма: " + calculateChecksum(file));
        }

        private String calculateChecksum(Path file) throws IOException, NoSuchAlgorithmException {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] fileData = Files.readAllBytes(file);
            byte[] digestBytes = md.digest(fileData);
            StringBuilder hexString = new StringBuilder();

            for (byte b : digestBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        }
    }
}
