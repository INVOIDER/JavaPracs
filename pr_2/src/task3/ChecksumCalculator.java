package task3;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ChecksumCalculator {
    public static short calculateChecksum(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             FileChannel channel = fis.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate(2);
            short checksum = 0;

            while (channel.read(buffer) != -1) {
                buffer.flip();

                while (buffer.remaining() >= 2) { // Проверяем, что в буфере достаточно данных
                    checksum ^= buffer.getShort(); // Применяем XOR операцию
                }

                buffer.compact(); // Переносим оставшиеся данные в начало буфера
            }

            return checksum;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static void main(String[] args) {
        String filePath = "src/task3/file3.txt";
        short checksum = calculateChecksum(filePath);
        System.out.println("Контрольная сумма: " + checksum);
    }
}