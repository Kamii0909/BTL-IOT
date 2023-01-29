package hust.iot.kien.restapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Device {
    
    @GeneratedValue
    @Id
    private Long id;

    private DeviceInfo info;

    @ManyToOne
    private IotUser user;
}
