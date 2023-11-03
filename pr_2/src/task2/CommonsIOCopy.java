package task2;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CommonsIOCopy {
    public static void main(String[] args) {
        File sourceFile = new File("src/task2/100MB.txt");
        File destinationFile = new File("src/task2/CommonsIOCopy.txt");

        long startTime = System.nanoTime();

        try {
            FileUtils.copyFile(sourceFile, destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long endTime = System.nanoTime();

        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Copying is completed. \n Time spent: " + (endTime - startTime) / 1000000 + " мс \nMemory spent: "+usedMemory);
    }
}