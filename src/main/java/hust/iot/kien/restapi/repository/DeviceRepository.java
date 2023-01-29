package hust.iot.kien.restapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import hust.iot.kien.restapi.model.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    
}
