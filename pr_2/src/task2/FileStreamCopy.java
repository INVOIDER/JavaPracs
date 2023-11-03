package task2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileStreamCopy {
    public static void main(String[] args) {
        try (FileInputStream inputStream = new FileInputStream("src/task2/100MB.txt");
             FileOutputStream outputStream = new FileOutputStream("src/task2/FileStreamCOpy.txt")) {

            long startTime = System.nanoTime();

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            long endTime = System.nanoTime();

            long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            System.out.println("Copying is completed. \n Time spent: " + (endTime - startTime) / 1000000 + " мс \nMemory spent: "+usedMemory);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}