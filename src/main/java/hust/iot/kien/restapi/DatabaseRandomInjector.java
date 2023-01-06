package hust.iot.kien.restapi;

import java.time.LocalDateTime;
import java.util.Random;
import org.springframework.boot.CommandLineRunner;
import hust.iot.kien.restapi.model.SensorData;
import hust.iot.kien.restapi.repository.SensorDataRepository;
// import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @Component
public class DatabaseRandomInjector implements CommandLineRunner {

    private final SensorDataRepository sensorDataRepository;

    private final Random random;

    public DatabaseRandomInjector(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
        this.random = new Random();
    }

    @Override
    public void run(String... args) throws Exception {
        int i = 25;
        while (i-- > 0) {
            SensorData sensorData = new SensorData();
            sensorData.setTemperature(random.nextDouble(15, 40));
            sensorData.setHumidity(random.nextDouble(20, 90));
            sensorData.setTimeAdded(LocalDateTime.now().minusMinutes(5L * i));

            log.info("Saved entity: {} ", sensorDataRepository.save(sensorData));
        }

    }
}
