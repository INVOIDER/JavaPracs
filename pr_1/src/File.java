import java.util.concurrent.*;

public class File {
    private final String fileType;
    private final int fileSize;

    public File(String fileType, int fileSize) {
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public int getFileSize() {
        return fileSize;
    }
}

class FileGenerator implements Runnable {
    private final BlockingQueue<File> queue;
    private final int minDelay;
    private final int maxDelay;

    public FileGenerator(BlockingQueue<File> queue, int minDelay, int maxDelay) {
        this.queue = queue;
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int delay = ThreadLocalRandom.current().nextInt(minDelay, maxDelay + 1);
                Thread.sleep(delay);

                File file = generateFile();
                queue.put(file);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private File generateFile() {
        String[] fileTypes = {"XML", "JSON", "XLS"};
        String fileType = fileTypes[ThreadLocalRandom.current().nextInt(0, fileTypes.length)];
        int fileSize = ThreadLocalRandom.current().nextInt(10, 101);

        return new File(fileType, fileSize);
    }
}

class FileProcessor implements Runnable {
    private final BlockingQueue<File> queue;
    private final String requiredFileType;

    public FileProcessor(BlockingQueue<File> queue, String requiredFileType) {
        this.queue = queue;
        this.requiredFileType = requiredFileType;
    }

    @Override
    public void run() {
        try {
            while (true) {
                File file = queue.take();

                if (file.getFileType().equals(requiredFileType)) {
                    System.out.println("Processing file: " + file.getFileType() + ", Size: " + file.getFileSize());
                    int processingTime = file.getFileSize() * 7;
                    Thread.sleep(processingTime);
                    System.out.println("File processed: " + file.getFileType() + ", Size: " + file.getFileSize());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MultiThreadedFileProcessingSystem {
    public static void main(String[] args) {
        BlockingQueue<File> queue = new ArrayBlockingQueue<>(5);

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.submit(new FileGenerator(queue, 100, 1000));

        for (String fileType : new String[]{"XML", "JSON", "XLS"}) {
            executorService.submit(new FileProcessor(queue, fileType));
        }

        executorService.shutdown();
    }
}