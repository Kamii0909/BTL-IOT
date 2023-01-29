package hust.iot.kien.restapi.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
public class DeviceInfo {
    
    private String name;

    // more info? 
}
