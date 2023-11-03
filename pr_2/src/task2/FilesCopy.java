package task2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FilesCopy {
    public static void main(String[] args) {
        Path sourcePath = Path.of("src/task2/100MB.txt");
        Path destinationPath = Path.of("src/task2/FilesCopy.txt");

        long startTime = System.nanoTime();

        try {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();

        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Copying is completed. \n Time spent: " + (endTime - startTime) / 1000000 + " мс \nMemory spent: "+usedMemory);
    }
}