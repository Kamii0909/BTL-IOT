package hust.iot.kien.restapi;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import hust.iot.kien.restapi.model.SensorData;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    @Query("from SensorData s order by s.timeAdded desc")
    List<SensorData> fetch(Pageable pageable);
}
