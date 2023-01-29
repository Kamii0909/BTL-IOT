package hust.iot.kien.restapi;

import java.time.LocalDateTime;
import java.util.Random;
import org.springframework.boot.CommandLineRunner;
import hust.iot.kien.restapi.model.Device;
import hust.iot.kien.restapi.model.DeviceInfo;
import hust.iot.kien.restapi.model.IotUser;
import hust.iot.kien.restapi.model.SensorData;
import hust.iot.kien.restapi.repository.DeviceRepository;
import hust.iot.kien.restapi.repository.IotUserRepository;
import hust.iot.kien.restapi.repository.SensorDataRepository;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DatabaseRandomInjector implements CommandLineRunner {

    private final SensorDataRepository sensorDataRepository;

    private final IotUserRepository userRepository;

    private final DeviceRepository deviceRepository;

    private final Random random;

    public DatabaseRandomInjector(SensorDataRepository sensorDataRepository,
        IotUserRepository userRepository, DeviceRepository deviceRepository) {
        this.sensorDataRepository = sensorDataRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.random = new Random();
    }

    @Override
    public void run(String... args) throws Exception {
        int i = 25;
        while (i-- > 0) {
            IotUser user = new IotUser();
            user.setUsername("test user " + i);

            userRepository.save(user);
            
            Device device = new Device();
            device.setUser(user);
            DeviceInfo info = new DeviceInfo();
            info.setName("device " + i + " of user " + user.getUsername());
            device.setInfo(info);

            deviceRepository.save(device);


            SensorData sensorData = new SensorData();
            sensorData.setTemperature(random.nextDouble(15, 40));
            sensorData.setHumidity(random.nextDouble(20, 90));
            sensorData.setTimeAdded(LocalDateTime.now().minusMinutes(5L * i));
            sensorData.setDevice(device);

            log.info("Saved entity: {} ", sensorDataRepository.save(sensorData));
        }

    }
}
