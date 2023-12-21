import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.observables.ConnectableObservable;
import java.util.Random;


class File {
    private String type;
    private int size;

    public File(String type, int size) {
        this.type = type;
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }
}


class FileGenerator {
    private PublishSubject<File> fileSubject = PublishSubject.create();
    private String[] fileTypes = {"json", "xml", "xls"};
    private ConnectableObservable<File> fileObservable;

    public FileGenerator() {
        fileObservable = fileSubject.publish();
    }

    public ConnectableObservable<File> generateFiles() {
        return fileObservable;
    }

    public void startGeneratingFiles(int numberOfFiles) {
        Random random = new Random();
        for (int i = 0; i < numberOfFiles; i++) {
            String type = fileTypes[random.nextInt(fileTypes.length)];
            int size = random.nextInt(91) + 10;
            fileSubject.onNext(new File(type, size));
            try {
                Thread.sleep(random.nextInt(901) + 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        fileSubject.onComplete();
    }

    public void connect() {
        fileObservable.connect();
    }
}


class FileHandler implements Observer<File> {
    private String fileType;

    public FileHandler(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(File file) {
        if (file.getType().equals(fileType)) {
            int processingTime = file.getSize() * 7;
            try {
                Thread.sleep(processingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Обработчик для файла типа " + fileType + " обработал файл за " + processingTime + " мс.");
        }
        else {
            System.out.println("Обработчик файлов типа " + fileType + " получил файл " + file.getType() + ". Обработка невозможна.");
        }
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onComplete() {
        System.out.println("Обработчик для файла типа " + fileType + " завершил работу.");
    }
}

public class FileProcessingSystem {
    public static void main(String[] args) {
        FileGenerator fileGenerator = new FileGenerator();

        FileHandler jsonHandler = new FileHandler("json");
        FileHandler xmlHandler = new FileHandler("xml");
        FileHandler xlsHandler = new FileHandler("xls");

        fileGenerator.generateFiles().subscribe(jsonHandler);
        fileGenerator.generateFiles().subscribe(xmlHandler);
        fileGenerator.generateFiles().subscribe(xlsHandler);

        fileGenerator.connect();

        fileGenerator.startGeneratingFiles(20);

        

        
    }
}