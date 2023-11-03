import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FIleRead {
    public static void main(String[] args) {
        String filePath = "./file1.txt";

        // Создание файла и запись текста в него
        try {
            Path file = Paths.get(filePath);
            System.out.println("Имя созданного файла: " + file.getFileName());

            Scanner sc = new Scanner(System.in);
            List<String> lines = new ArrayList<>();
            for (int i=1;i<4;i++){
                System.out.println("Введите "+i+ "ю строчку");
                lines.add(sc.nextLine());
            }
            Files.write(file, lines);
            System.out.println("Файл успешно создан!");
        } catch (IOException e) {
            System.out.println("Ошибка при создании файла: " + e.getMessage());
        }

        // Чтение файла и вывод содержимого
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            System.out.println("Содержимое файла:");
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }
}
