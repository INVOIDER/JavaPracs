package task2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileChannelCopy {
    public static void main(String[] args) {
        try (FileInputStream inputStream = new FileInputStream("src/task2/100MB.txt");
             FileOutputStream outputStream = new FileOutputStream("src/task2/FileChannelCopy.txt")) {

            long startTime = System.nanoTime();

            FileChannel sourceChannel = inputStream.getChannel();
            FileChannel destinationChannel = outputStream.getChannel();
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

            long endTime = System.nanoTime();

            long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            System.out.println("Copying is completed. \n Time spent: " + (endTime - startTime) / 1000000 + " мс \nMemory spent: "+usedMemory);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}