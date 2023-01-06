package hust.iot.kien.restapi.controller;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import hust.iot.kien.restapi.model.SensorData;
import hust.iot.kien.restapi.repository.SensorDataRepository;

@RestController
@RequestMapping("/api")
public class MyRestController {

    private final SensorDataRepository sensorDataRepository;

    public MyRestController(final SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }

    @GetMapping("/{fetchAmount}")
    public ResponseEntity<List<SensorData>> fetch(@PathVariable("fetchAmount") int fetchAmount) {
        return ResponseEntity.ok().body(sensorDataRepository.fetch(PageRequest.of(0, fetchAmount)));
    }
}
