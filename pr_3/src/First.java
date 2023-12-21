import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

class SensorData {
    private int value;
    private String sensorName;

    public SensorData(int value, String sensorName) {
        this.value = value;
        this.sensorName = sensorName;
    }

    public int getValue() {
        return value;
    }

    public String getSensorName() {
        return sensorName;
    }
}

class TemperatureSensor extends Observable<SensorData> {
    private Random random = new Random();
    private String sensorName = "TemperatureSensor";

    @Override
    protected void subscribeActual(Observer<? super SensorData> observer) {
        Disposable disposable = new Disposable() {
            private boolean disposed = false;

            @Override
            public void dispose() {
                disposed = true;
            }

            @Override
            public boolean isDisposed() {
                return disposed;
            }
        };

        observer.onSubscribe(disposable);

        new Thread(() -> {
            while (!disposable.isDisposed()) {
                int temperature = random.nextInt(16) + 15;
                observer.onNext(new SensorData(temperature, sensorName));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

class CO2Sensor extends Observable<SensorData> {
    private Random random = new Random();
    private String sensorName = "CO2Sensor";

    @Override
    protected void subscribeActual(Observer<? super SensorData> observer) {
        Disposable disposable = new Disposable() {
            private boolean disposed = false;

            @Override
            public void dispose() {
                disposed = true;
            }

            @Override
            public boolean isDisposed() {
                return disposed;
            }
        };

        observer.onSubscribe(disposable);

        new Thread(() -> {
            while (!disposable.isDisposed()) {
                int co2Level = random.nextInt(71) + 30;
                observer.onNext(new SensorData(co2Level, sensorName));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

class Alarm implements Observer<SensorData> {
    private PublishSubject<String> alarmSubject = PublishSubject.create();
    private int temperatureThreshold = 25;
    private int co2Threshold = 70;
    private Map<String, Integer> lastSensorValues = new HashMap<>();

    public Observable<String> getAlarmObservable() {
        return alarmSubject;
    }

    @Override
    public void onSubscribe(Disposable d) {
        // Ничего не делаем
    }

    @Override
    public void onNext(SensorData data) {
        lastSensorValues.put(data.getSensorName(), data.getValue());
        System.out.println("CO2 Level: " + lastSensorValues.get("CO2Sensor"));
        System.out.println("Temperature Level: " + lastSensorValues.get("TemperatureSensor"));
        if (lastSensorValues.containsKey("TemperatureSensor") && lastSensorValues.containsKey("CO2Sensor")) {
            int temperature = lastSensorValues.get("TemperatureSensor");
            int co2Level = lastSensorValues.get("CO2Sensor");
            
            if (temperature > temperatureThreshold && co2Level > co2Threshold) {
                alarmSubject.onNext("ALARM!!!");
            } else if (temperature > temperatureThreshold || co2Level > co2Threshold) {
                alarmSubject.onNext("Warning: Temperature or CO2 level is above the threshold!");
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        // Ничего не делаем
    }
}

public class First {
    public static void main(String[] args) {
        TemperatureSensor temperatureSensor = new TemperatureSensor();
        CO2Sensor co2Sensor = new CO2Sensor();
        Alarm alarm = new Alarm();

        temperatureSensor.subscribeOn(Schedulers.newThread()).subscribe(alarm);
        co2Sensor.subscribeOn(Schedulers.newThread()).subscribe(alarm);

        alarm.getAlarmObservable().subscribe(alert -> {
            System.out.println("Alert: " + alert);
        });

        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}