package hust.iot.kien.restapi;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.springframework.data.domain.PageRequest;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient.Mqtt5Publishes;
import hust.iot.kien.restapi.model.SensorData;
import hust.iot.kien.restapi.repository.SensorDataRepository;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataFetcher {

    private final SensorDataRepository sensorDataRepository;

    private static final String TOPIC = "hust/iot/test";

    private final ObjectMapper objectMapper;

    public DataFetcher(SensorDataRepository sensorDataRepository, ObjectMapper objectMapper) {
        this.sensorDataRepository = sensorDataRepository;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    @Retryable(retryFor = RuntimeException.class,
        maxAttempts = 2,
        recover = "logRetryAttempt",
        backoff = @Backoff(delay = 10000))
    public void fetchData() {

        if (RetrySynchronizationManager.getContext().getRetryCount() > 0) {
            log.warn("Retrying: {}", RetrySynchronizationManager.getContext().getRetryCount());
        }

        Mqtt5BlockingClient client = Mqtt5Client.builder()
            .serverHost("test.mosquitto.org")
            .serverPort(1883)
            .buildBlocking();

        client.connectWith()
            .cleanStart(true)
            .keepAlive(10)
            .send();

        Mqtt5Publishes publishes = client.publishes(MqttGlobalPublishFilter.SUBSCRIBED);

        client.subscribeWith().topicFilter(TOPIC).send();

        try {
            byte[] payload = publishes.receive(3, TimeUnit.SECONDS)
                .orElseThrow(() -> new RuntimeException(new TimeoutException()))
                .getPayloadAsBytes();

            SensorData sensorData = objectMapper.readValue(payload, SensorData.class);
            SensorData latest = sensorDataRepository.fetch(PageRequest.ofSize(1)).get(0);

            LocalDateTime newTime = sensorData.getTimeAdded();
            LocalDateTime oldTime = latest.getTimeAdded();

            int i = newTime.compareTo(oldTime);

            if (i > 0) {
                log.info("Saved data: {}", sensorDataRepository.save(sensorData));
            } else if (i == 0) {
                log.info("Skipping duplicate message at {}...", newTime);
            } else {
                log.warn("""
                    Possible corrupt data:
                    \t Fetched time: {}
                    \t Latest local time: {}
                        """, newTime, oldTime);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.disconnect();
        }
    }

    @Recover
    public void logRetryAttempt(RuntimeException e) {
        log.info("Failed to fetch new data from broker with error {}",
            e.getCause().getClass().getSimpleName());
    }
}
